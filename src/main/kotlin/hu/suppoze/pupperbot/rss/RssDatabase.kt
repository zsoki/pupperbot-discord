package hu.suppoze.pupperbot.rss

import com.rometools.rome.feed.synd.SyndEntry
import com.rometools.rome.feed.synd.SyndFeed
import hu.suppoze.pupperbot.rss.model.*
import io.reactivex.Observable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.reactivestreams.Subscriber

class RssDatabase {

    fun persistFeed(syndFeed: SyndFeed, feedUrl: String): Observable<RssFeedDao> = Observable.fromPublisher<RssFeedDao> {
        checkedTransaction(it) {
            val dbFeed = RssFeedDao.new {
                this.feedUrl = feedUrl
                author = syndFeed.author ?: null
                description = syndFeed.description ?: null
                imgUrl = syndFeed.image?.url
                link = syndFeed.link
                title = syndFeed.title
            }

            for (entry in syndFeed.entries) {
                RssEntryDao.new {
                    author = entry.author ?: null
                    description = entry.description.value
                    link = entry.link
                    title = entry.title
                    isPosted = true
                    saveTime = DateTime.now().millis
                    feed = dbFeed
                }
            }
            it.onNext(dbFeed)
        }
    }

    fun persistSubscription(guildId: Long, channelId: Long, dbFeedDao: RssFeedDao): Observable<RssSubscriptionDao> = Observable.fromPublisher<RssSubscriptionDao> {
        checkedTransaction(it) {
            val subscription = RssSubscriptionDao.new {
                this.guildId = guildId
                this.channelId = channelId
                this.feed = dbFeedDao
            }
            it.onNext(subscription)
        }
    }

    fun persistEntriesFor(feedDao: RssFeedDao, entries: List<SyndEntry>): Observable<List<RssEntryDao>> = Observable.fromPublisher<List<RssEntryDao>> {
        checkedTransaction(it) {
            it.onNext(
                    entries
                            .filterNot { feedDao.entries.any { dbEntry -> dbEntry.title == it.title } }
                            .map {
                                RssEntryDao.new {
                                    author = it.author ?: null
                                    description = it.description.value
                                    link = it.link
                                    title = it.title
                                    isPosted = true
                                    saveTime = DateTime.now().millis
                                    feed = feedDao
                                }
                            }
                            .toList())
        }
    }

    fun deleteEntriesOlderThan(now: Long): Observable<List<RssEntryDao>> = Observable.fromPublisher {
        checkedTransaction(it) {
            RssEntryTable.deleteWhere { RssEntryTable.saveTime less now }
            it.onNext(RssEntryDao.all().toList())
        }
    }

    fun getFeeds(): Observable<RssFeedDao> = Observable.fromPublisher {
        checkedTransaction(it) {
            RssFeedDao.all().forEach { feedDao -> it.onNext(feedDao) }
        }
    }

    fun getFeedByUrl(feedUrl: String): Observable<RssFeedDao> = Observable.fromPublisher {
        checkedTransaction(it) {
            val feedDao = RssFeedDao.find { RssFeedTable.feedUrl eq feedUrl }.singleOrNull()
            if (feedDao != null) it.onNext(feedDao)
        }
    }

    // TODO: extract this little helper method somewhere
    private fun <T : Any?> checkedTransaction(subscriber: Subscriber<T>, action: () -> Unit) {
        try {
            transaction {
                action()
            }
        } catch (e: Exception) {
            subscriber.onError(e)
            e.printStackTrace()
        } finally {
            subscriber.onComplete()
        }
    }

    fun removeSubscription(feedUrl: String, guildId: Long, channelId: Long): Observable<RssFeedDao> = Observable.fromPublisher {
        checkedTransaction(it) {

            val exception = Exception("Subscription to $feedUrl does not exist on this channel.")

            val feed = RssFeedDao.find { RssFeedTable.feedUrl eq feedUrl }.singleOrNull()
                    ?: throw exception
            val sub = feed.subscriptions.singleOrNull { sub -> sub.channelId == channelId && sub.guildId == guildId }
                    ?: throw exception

            sub.delete()

            it.onNext(feed)
        }
    }

    fun removeFeedIfNoSubs(feedDao: RssFeedDao): Observable<String>  = Observable.fromPublisher {
        checkedTransaction(it) {
            val feedName = feedDao.title

            if (feedDao.subscriptions.empty()) {
                feedDao.entries.forEach { it.delete() }
                feedDao.delete()
            }

            it.onNext(feedName)
        }
    }
}
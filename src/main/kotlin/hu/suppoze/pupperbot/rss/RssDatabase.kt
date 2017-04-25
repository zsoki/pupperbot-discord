package hu.suppoze.pupperbot.rss

import com.rometools.rome.feed.synd.SyndEntry
import com.rometools.rome.feed.synd.SyndFeed
import hu.suppoze.pupperbot.rss.model.RssEntries
import hu.suppoze.pupperbot.rss.model.RssEntry
import hu.suppoze.pupperbot.rss.model.RssFeed
import hu.suppoze.pupperbot.rss.model.RssSubscription
import io.reactivex.Observable
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.reactivestreams.Subscriber

class RssDatabase {

    fun persistFeed(syndFeed: SyndFeed, feedUrl: String): Observable<RssFeed> = Observable.fromPublisher<RssFeed> {
        checkedAction(it) {
            transaction {
                val dbFeed = RssFeed.new {
                    this.feedUrl = feedUrl
                    author = syndFeed.author ?: null
                    description = syndFeed.description ?: null
                    imgUrl = syndFeed.image?.url
                    link = syndFeed.link
                    title = syndFeed.title
                }

                for (entry in syndFeed.entries) {
                    RssEntry.new {
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
    }

    fun persistSubscription(guildId: Long, channelId: Long, dbFeed: RssFeed): Observable<RssSubscription> = Observable.fromPublisher<RssSubscription> {
        checkedAction(it) {
            transaction {
                val subscription = RssSubscription.new {
                    this.guildId = guildId
                    this.channelId = channelId
                    this.feed = dbFeed
                }
                it.onNext(subscription)
            }
        }
    }

    fun persistEntriesFor(subscription: RssSubscription, entries: List<SyndEntry>): Observable<List<RssEntry>> = Observable.fromPublisher<List<RssEntry>> {
        checkedAction(it) {
            transaction {
                it.onNext(
                        entries
                            .filterNot { subscription.feed.entries.any { dbEntry -> dbEntry.title == it.title } }
                            .map {
                                RssEntry.new {
                                    author = it.author ?: null
                                    description = it.description.value
                                    link = it.link
                                    title = it.title
                                    isPosted = true
                                    saveTime = DateTime.now().millis
                                    feed = subscription.feed
                                }
                            }
                            .toList())
            }
        }
    }

    fun deleteEntriesOlderThan(now: Long): Observable<RssEntry> = Observable.fromPublisher {
        checkedAction(it) {
            transaction {
                RssEntries.deleteWhere { RssEntries.saveTime less now }
                RssEntry.all().forEach { dbEntry -> it.onNext(dbEntry) }
            }
        }
    }

    // TODO: extract this little helper method somewhere
    private fun <T : Any?> checkedAction(subscriber: Subscriber<T>, action: () -> Unit) {
        try {
            action()
        } catch (e: Exception) {
            subscriber.onError(e)
        } finally {
            subscriber.onComplete()
        }
    }
}
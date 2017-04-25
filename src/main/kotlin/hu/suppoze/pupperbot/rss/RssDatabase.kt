package hu.suppoze.pupperbot.rss

import com.rometools.rome.feed.synd.SyndFeed
import hu.suppoze.pupperbot.rss.model.RssEntry
import hu.suppoze.pupperbot.rss.model.RssFeed
import hu.suppoze.pupperbot.rss.model.RssSubscription
import io.reactivex.Observable
import org.jetbrains.exposed.sql.transactions.transaction

class RssDatabase {

    fun persistFeed(syndFeed: SyndFeed, feedUrl: String): Observable<RssFeed> = Observable.fromPublisher<RssFeed> {
        try {
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
                        feed = dbFeed
                    }
                }

                it.onNext(dbFeed)
            }
        } catch(e: Exception) {
            it.onError(e)
        } finally {
            it.onComplete()
        }
    }

    fun persistSubscription(guildId: Long, channelId: Long, dbFeed: RssFeed): Observable<RssSubscription> = Observable.fromPublisher<RssSubscription> {
        try {
            transaction {
                val subscription = RssSubscription.new {
                    this.guildId = guildId
                    this.channelId = channelId
                    this.feed = dbFeed
                }
                it.onNext(subscription)
            }
        } catch(e: Exception) {
            it.onError(e)
        } finally {
            it.onComplete()
        }
    }
}
package hu.suppoze.pupperbot.rss

import com.rometools.rome.feed.synd.SyndFeed
import hu.suppoze.pupperbot.rss.model.RssFeed
import io.reactivex.Observable
import org.jetbrains.exposed.sql.transactions.transaction

class RssDatabase {

    fun persistFeed(feed: SyndFeed) = Observable.fromPublisher<RssFeed> {
        transaction {
            val feed = RssFeed.new {
                feedUrl = feed.uri
                author = feed.author
                description = feed.description
                imgUrl = feed.image.url
                link = feed.link
                title = feed.title
            }

            // TODO: save entries in database

        }
    }

}
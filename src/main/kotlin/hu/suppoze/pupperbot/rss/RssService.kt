package hu.suppoze.pupperbot.rss

import hu.suppoze.pupperbot.rss.model.RssEntry
import hu.suppoze.pupperbot.rss.model.RssSubscription
import kotlinx.coroutines.experimental.*
import org.joda.time.DateTime
import java.util.concurrent.TimeUnit

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
class RssService(private val rssServer: RssServer, private val rssDatabase: RssDatabase) {

    private val subscriptionJobs = hashMapOf<Int, Job>()

    fun subscribe(subscription: RssSubscription,
                  onNewEntry: (RssEntry) -> Unit,
                  onFeedError: (Throwable) -> Unit) {

        val subscriptionJob = launch(CommonPool) {
            rssUpdateJob(subscription, onNewEntry, onFeedError)
        }

        subscriptionJobs.putIfAbsent(subscription.hashCode(), subscriptionJob)
    }

    fun unSubscribe(subscription: RssSubscription) {
        subscriptionJobs[subscription.hashCode()]?.cancel()
        subscriptionJobs.remove(subscription.hashCode())
    }

    suspend private fun rssUpdateJob(subscription: RssSubscription, onNewEntry: (RssEntry) -> Unit, onFeedError: (Throwable) -> Unit) {
        while(true) {
            rssServer.getFeed(subscription.feed.feedUrl)
                    .map { syndFeed -> syndFeed.entries }
                    .flatMap { entries -> rssDatabase.persistEntriesFor(subscription, entries) }
                    .flatMap { _ -> rssDatabase.deleteEntriesOlderThan(DateTime.now().millis) }
                    .subscribe(onNewEntry, onFeedError)
            delay(5, TimeUnit.MINUTES)
        }
    }
}

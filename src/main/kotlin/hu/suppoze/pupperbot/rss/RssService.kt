package hu.suppoze.pupperbot.rss

import com.github.salomonbrys.kodein.instance
import hu.suppoze.pupperbot.common.PupperBot
import hu.suppoze.pupperbot.di.kodein
import hu.suppoze.pupperbot.rss.model.RssEntryDao
import hu.suppoze.pupperbot.rss.model.RssFeedDao
import hu.suppoze.pupperbot.rss.model.RssSubscriptionDao
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import kotlinx.coroutines.experimental.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.util.concurrent.TimeUnit

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
class RssService(private val rssServer: RssServer, private val rssDatabase: RssDatabase) {

    private val pupperBot: PupperBot by kodein.instance()

    private val rssFeedUpdateJobs = hashMapOf<String, Job>()

    private val postToChannel: (SubToEntries) -> Unit = {
        pupperBot.client.channels.find { channel -> channel.longID == it.sub.channelId }
            ?.sendMessage("New post on that shitty RSS feed you subscribed to.")
    }

    private val onUpdateJobError: (Throwable) -> Unit ={
        it.printStackTrace()
    }

    private var updateFrequency: Long = 5

    fun startService(updateFrequencyInMinutes: Long = updateFrequency) {

        updateFrequency = updateFrequencyInMinutes

        rssDatabase.getFeeds()
                .map {
                    val subscriptions = transaction { it.subscriptions }.toList()
                    startNewRssFeedUpdateJob(it, subscriptions)
                }.subscribe()
    }

    private fun startNewRssFeedUpdateJob(feed: RssFeedDao, subscriptions: List<RssSubscriptionDao>) {

        val feedUpdateJob = launch(CommonPool) {
            rssFeedUpdateJob(feed, subscriptions)
        }

        rssFeedUpdateJobs.putIfAbsent(feed.feedUrl, feedUpdateJob)
    }

    suspend private fun rssFeedUpdateJob(feed: RssFeedDao, subscriptions: List<RssSubscriptionDao>) {

        val feedUrl = feed.feedUrl

        while (true) {
            Observable.zip(
                    rssServer.getFeed(feedUrl)
                            .map { syndFeed -> syndFeed.entries }
                            .map { entries -> rssDatabase.persistEntriesFor(feed, entries) }
                            .flatMap {
                                rssDatabase.deleteEntriesOlderThan(DateTime.now().millis)
                                        .repeat(subscriptions.count().toLong())
                            },
                    Observable.fromIterable(subscriptions),
                    BiFunction<List<RssEntryDao>, RssSubscriptionDao, SubToEntries> {
                        t1, t2 ->
                        SubToEntries(t1, t2)
                    }
            ).subscribe(postToChannel, onUpdateJobError)

            delay(updateFrequency, TimeUnit.MINUTES)
        }
    }

    fun notifyFeedsChanged() {

//            rssFeedUpdateJobs[subscriptionDao.hashCode()]?.cancel()
//            rssFeedUpdateJobs.remove(subscriptionDao.hashCode())

    }

    private data class SubToEntries(val entries: List<RssEntryDao>,
                                    val sub: RssSubscriptionDao)
}

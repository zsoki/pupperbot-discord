package hu.suppoze.pupperbot.rss

import com.github.salomonbrys.kodein.instance
import hu.suppoze.pupperbot.common.PupperBot
import hu.suppoze.pupperbot.di.kodein
import hu.suppoze.pupperbot.rss.model.RssEntryDao
import hu.suppoze.pupperbot.rss.model.RssFeedDao
import hu.suppoze.pupperbot.rss.model.RssSubscriptionDao
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import org.jetbrains.exposed.sql.transactions.transaction
import sx.blah.discord.util.EmbedBuilder
import java.util.concurrent.TimeUnit

class RssService(private val rssServer: RssServer, private val rssDatabase: RssDatabase) {

    private val pupperBot: PupperBot by kodein.instance()

    private lateinit var observables: MutableMap<RssFeedDao, Observable<List<RssEntryDao>>>
    private lateinit var subscriptions: MutableMap<RssSubscriptionDao, Disposable>

    private fun postToChannel(sub: RssSubscriptionDao, entryList: List<RssEntryDao>) {
        val subbedChannel = pupperBot.client.channels.singleOrNull { channel -> channel.longID == sub.channelId }

        if (subbedChannel == null) {
            rssDatabase.removeSubscription(sub.feed.feedUrl, sub.guildId, sub.channelId)
            return
        }

        entryList.forEach {
            val embed = EmbedBuilder()
                    .withTitle(it.title)
                    .withAuthorName(it.author)
                    .withDesc(it.description)
                    .withUrl(it.link)
            pupperBot.client.channels.singleOrNull { it.longID == sub.channelId }?.sendMessage("", embed.build(), false)
        }
    }

    private val onUpdateJobError: (Throwable) -> Unit = {
        it.printStackTrace()
    }

    fun startService() {
        val feeds = rssDatabase.getFeeds().blockingIterable()

        observables = feeds.associateBy({ feedDao -> feedDao }) { feedDao ->
            createHotFeedObservable(feedDao)
        }.toMutableMap()

        feeds.forEach { feedDao -> subscribeAllChannelToFeed(feedDao) }
    }

    private fun createHotFeedObservable(feedDao: RssFeedDao): Observable<List<RssEntryDao>> {
        return Observable.interval(5, TimeUnit.MINUTES)
                .flatMap { rssServer.getFeed(feedDao.feedUrl) }
                .map { syndFeed -> syndFeed.entries }
                .flatMap { entries -> rssDatabase.saveNewEntries(feedDao, entries) }
                .publish()
                .refCount()
    }

    private fun subscribeAllChannelToFeed(feedDao: RssFeedDao) {
        transaction { feedDao.subscriptions }
                .forEach { sub -> subscribeChannel(sub) }
    }

    private fun subscribeChannel(sub: RssSubscriptionDao) {
        val feed = sub.feed

        val disposable = observables[feed]!!
                .subscribe(
                        { entryList -> postToChannel(sub, entryList) },
                        onUpdateJobError
                )

        subscriptions.putIfAbsent(sub, disposable)
    }

    fun subscriptionAdded(sub: RssSubscriptionDao) {
        val feed = sub.feed

        if (!observables.containsKey(feed)) {
            observables.put(feed, createHotFeedObservable(feed))
            subscribeAllChannelToFeed(feed)
        } else {
            subscribeChannel(sub)
        }
    }

    fun subscriptionRemoved(sub: RssSubscriptionDao) {
        subscriptions[sub]?.dispose()
        subscriptions.remove(sub)

        val feed = sub.feed
        if (observables.contains(feed) && subscriptions.keys.none { it.feed == feed }) {
            observables.remove(feed)
        }

    }
}
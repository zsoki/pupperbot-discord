package hu.suppoze.pupperbot.rss.model

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class RssFeed(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RssFeed>(RssFeeds)

    var feedUrl by RssFeeds.feedUrl
    var author by RssFeeds.author
    var description by RssFeeds.description
    var imgUrl by RssFeeds.imgUrl
    var link by RssFeeds.link
    var title by RssFeeds.title

    val subscriptions by RssSubscription referrersOn RssSubscriptions.feed
    val entries by RssEntry referrersOn RssEntries.feed
}

class RssEntry(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RssEntry>(RssEntries)

    var author by RssEntries.author
    var description by RssEntries.description
    var link by RssEntries.link
    var title by RssEntries.title
    var isPosted by RssEntries.isPosted

    var feed by RssFeed referencedOn RssEntries.feed
}

class RssSubscription(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RssSubscription>(RssSubscriptions)

    var guildId by RssSubscriptions.guildId
    var channelId by RssSubscriptions.channelId

    var feed by RssFeed referencedOn RssSubscriptions.feed
}
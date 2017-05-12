package hu.suppoze.pupperbot.rss.model

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class RssFeedDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RssFeedDao>(RssFeedTable)

    var feedUrl by RssFeedTable.feedUrl
    var author by RssFeedTable.author
    var description by RssFeedTable.description
    var imgUrl by RssFeedTable.imgUrl
    var link by RssFeedTable.link
    var title by RssFeedTable.title

    val subscriptions by RssSubscriptionDao referrersOn RssSubscriptionTable.feed
    val entries by RssEntryDao referrersOn RssEntryTable.feed
}

class RssEntryDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RssEntryDao>(RssEntryTable)

    var author by RssEntryTable.author
    var description by RssEntryTable.description
    var link by RssEntryTable.link
    var title by RssEntryTable.title
    var isPosted by RssEntryTable.isPosted
    var saveTime by RssEntryTable.saveTime

    var feed by RssFeedDao referencedOn RssEntryTable.feed
}

class RssSubscriptionDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RssSubscriptionDao>(RssSubscriptionTable)

    var guildId by RssSubscriptionTable.guildId
    var channelId by RssSubscriptionTable.channelId

    var feed by RssFeedDao referencedOn RssSubscriptionTable.feed
}
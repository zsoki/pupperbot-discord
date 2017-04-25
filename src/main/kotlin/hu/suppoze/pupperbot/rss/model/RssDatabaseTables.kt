package hu.suppoze.pupperbot.rss.model

import org.jetbrains.exposed.dao.IntIdTable

object RssFeeds : IntIdTable() {
    val feedUrl = varchar("feedUrl", 512)
    val author = varchar("author", 128).nullable()
    val description = varchar("description", 512).nullable()
    val imgUrl = varchar("imgUrl", 512).nullable()
    val link = varchar("link", 512)
    val title = varchar("title", 128)
}

object RssEntries : IntIdTable() {
    val author = varchar("author", 128).nullable()
    val description = varchar("description", 512)
    val link = varchar("link", 512)
    val title = varchar("title", 128)
    val isPosted = bool("isPosted").default(false)
    val saveTime = long("saveTime")

    val feed = reference("feed", RssFeeds)

}

object RssSubscriptions : IntIdTable() {
    val guildId = long("guildId")
    val channelId = long("channelId")

    val feed = reference("feed", RssFeeds)
}
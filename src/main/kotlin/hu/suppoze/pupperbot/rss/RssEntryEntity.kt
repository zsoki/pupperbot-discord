package hu.suppoze.pupperbot.rss

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object RssEntries : IntIdTable() {
    val title = varchar("title", 128)
    val author = varchar("author", 128)
    val authorUrl = varchar("authorUrl", 256)
    val description = varchar("description", 512)
    val link = varchar("link", 256)
}

class RssEntry(id: EntityID<Int>) : IntEntity(id) {
    companion object: IntEntityClass<RssEntry>(RssEntries)

    var title by RssEntries.title
    var author by RssEntries.author
    var authorUrl by RssEntries.authorUrl
    var description by RssEntries.description
    var link by RssEntries.link
}
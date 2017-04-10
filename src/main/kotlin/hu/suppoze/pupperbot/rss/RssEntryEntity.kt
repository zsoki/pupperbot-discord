package hu.suppoze.pupperbot.rss

import io.requery.Entity
import io.requery.Generated
import io.requery.Key
import io.requery.Persistable

@Entity
interface RssEntryEntity : Persistable {
    @get:Key
    @get:Generated
    val id: Int

    var title: String
    var author: String
    var authorUrl: String
    var description: String
    var link: String
}

class RssEntry(override var title: String, override var author: String, override var authorUrl: String, override var description: String, override var link: String) : RssEntryEntity {

    override val id: Int
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

}
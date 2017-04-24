package hu.suppoze.pupperbot.rss

import com.github.salomonbrys.kodein.instance
import hu.suppoze.pupperbot.common.UseCase
import hu.suppoze.pupperbot.common.CommandParser
import hu.suppoze.pupperbot.di.kodein
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.transaction
import sx.blah.discord.util.EmbedBuilder

class RssCommand(val rawCommand: CommandParser.RawCommand) : UseCase<RssEntry> {

    private val rssDatabaseDatasource: RssDatabase by kodein.instance()

    override val onNext: (RssEntry) -> Unit = {
//        val entry = it.entries.first()!!
        val embed = EmbedBuilder()
                .withTitle(it.title)
                .withAuthorName(it.author)
                .withAuthorUrl(it.authorUrl)
                .withDesc(it.description)
                .withUrl(it.link)
//        if (it.image != null) embed.withImage(it.image.url)
        rawCommand.event.message.channel.sendMessage("", embed.build(), false)
    }

    override val onError: (Throwable) -> Unit = {
        rawCommand.event.message.author.orCreatePMChannel.sendMessage("Error during RSS request: ${it.message}")
        it.printStackTrace()
    }

    override fun execute() {

        transaction {
            create (RssEntries)
            RssEntry.new {
                title = "title"
                author = "author"
                authorUrl = "https://google.com"
                link = "https://google.com"
                description = "description"
            }

            rssDatabaseDatasource.getTestEntry()
                    .subscribe(onNext, onError)
        }

//        RssService().getFeed()
//                .flatMap { feed -> feed.toEntities() }
//                .flatMap { feed -> rssDatabaseDatasource.persistFeedAndGetNew(feed) }
//                .subscribe(onNext, onError)
    }
}
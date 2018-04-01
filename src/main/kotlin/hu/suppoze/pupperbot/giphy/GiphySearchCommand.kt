package hu.suppoze.pupperbot.giphy

import hu.suppoze.pupperbot.common.AvailableCommands
import hu.suppoze.pupperbot.common.ChatCommand
import hu.suppoze.pupperbot.common.UseCase
import hu.suppoze.pupperbot.di.kodein
import org.kodein.di.generic.instance
import java.net.URLEncoder
import java.util.concurrent.ThreadLocalRandom

@ChatCommand(type = AvailableCommands.GIPHY_SEARCH)
class GiphySearchCommand : UseCase() {

    private val giphyServer: GiphyServer by kodein.instance()

    override fun onExecute() {
        val phrase = commandContext.rawArgs ?: throw IllegalStateException("Param string was null.")

        val urlEncodedPhrase = URLEncoder.encode(phrase, Charsets.UTF_8.name())
        val limit = 10
        val giphyRandomResponse = giphyServer.searchGiphyBy(urlEncodedPhrase, limit)

        val upperRange = minOf(limit, giphyRandomResponse.pagination.count)
        if (upperRange == 0) throw IndexOutOfBoundsException("No results found.")

        val url =
            giphyRandomResponse.data[ThreadLocalRandom.current().nextInt(upperRange)].url // TODO: outsource randomizer
        commandContext.event.textChannel.sendMessage("${commandContext.rawArgs} $url").queue()
    }
}
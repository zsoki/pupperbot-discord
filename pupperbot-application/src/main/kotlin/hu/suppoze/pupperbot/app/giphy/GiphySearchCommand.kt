package hu.suppoze.pupperbot.app.giphy

import hu.suppoze.pupperbot.app.common.AvailableCommands
import hu.suppoze.pupperbot.app.common.ChatCommand
import hu.suppoze.pupperbot.app.common.UseCase
import hu.suppoze.pupperbot.app.di.kodein
import hu.suppoze.pupperbot.app.giphy.api.GiphyClient
import org.kodein.di.generic.instance
import java.net.URLEncoder
import java.util.concurrent.ThreadLocalRandom

@ChatCommand(type = AvailableCommands.GIPHY_SEARCH)
class GiphySearchCommand : UseCase() {

    private val giphyClient: GiphyClient by kodein.instance()

    override fun onExecute() {
        val phrase = commandContext.rawArgs ?: throw IllegalStateException("Param string was null.")

        val urlEncodedPhrase = URLEncoder.encode(phrase, Charsets.UTF_8.name())
        val limit = 10
        val giphyRandomResponse = giphyClient.searchGiphyBy(urlEncodedPhrase, limit)

        val upperRange = minOf(limit, giphyRandomResponse.pagination.count)
        if (upperRange == 0) throw IndexOutOfBoundsException("No results found.")

        val url =
            giphyRandomResponse.data[ThreadLocalRandom.current().nextInt(upperRange)].url // TODO: outsource randomizer
        commandContext.event.textChannel.sendMessage("${commandContext.rawArgs} $url").queue()
    }
}
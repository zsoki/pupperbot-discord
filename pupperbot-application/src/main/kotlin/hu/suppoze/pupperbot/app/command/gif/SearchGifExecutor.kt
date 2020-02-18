package hu.suppoze.pupperbot.app.command.gif

import hu.suppoze.pupperbot.app.command.CommandContext
import hu.suppoze.pupperbot.app.command.common.CommandExecutor
import hu.suppoze.pupperbot.app.command.gif.api.PupperGifClient
import java.net.URLEncoder
import java.util.concurrent.ThreadLocalRandom

class SearchGifExecutor(private val gifClient: PupperGifClient) : CommandExecutor {

    override fun execute(commandContext: CommandContext) {
        val phrase = commandContext.rawArgs ?: throw IllegalStateException("Param string was null.")

        val urlEncodedPhrase = URLEncoder.encode(phrase, Charsets.UTF_8.name())
        val limit = 10
        val giphyRandomResponse = gifClient.searchGifBy(urlEncodedPhrase, limit)

        val upperRange = minOf(limit, giphyRandomResponse.pagination.count)
        if (upperRange == 0) throw IndexOutOfBoundsException("No results found.")

        val url =
            giphyRandomResponse.data[ThreadLocalRandom.current().nextInt(upperRange)].url // TODO: outsource randomizer
        commandContext.event.textChannel.sendMessage("${commandContext.rawArgs} $url").queue()
    }
}
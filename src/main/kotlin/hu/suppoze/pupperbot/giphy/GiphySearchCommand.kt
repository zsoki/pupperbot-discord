package hu.suppoze.pupperbot.giphy

import com.github.salomonbrys.kodein.instance
import hu.suppoze.pupperbot.common.AvailableCommands
import hu.suppoze.pupperbot.common.ChatCommand
import hu.suppoze.pupperbot.common.UseCase
import hu.suppoze.pupperbot.di.kodein
import java.net.URLEncoder
import java.util.concurrent.ThreadLocalRandom

@ChatCommand(type = AvailableCommands.GIPHY_SEARCH)
class GiphySearchCommand : UseCase() {

    private val giphyServer: GiphyServer by kodein.instance()

    override fun onExecute() {
        val phrase = parameterizedCommand.paramString ?: throw IllegalStateException("Param string was null.")

        val urlEncodedPhrase = URLEncoder.encode(phrase, Charsets.UTF_8.name())
        val limit = 10

        giphyServer.searchGiphyBy(urlEncodedPhrase, limit)
                .map {
                    val upperRange = minOf(limit, it.pagination.count)
                    if (upperRange == 0) throw Throwable("No results found.") // TODO: Custom exception
                    it.data[ThreadLocalRandom.current().nextInt(upperRange)].url // TODO: outsource randomizer
                }
                .subscribe({
                    parameterizedCommand.event.textChannel.sendMessage("${parameterizedCommand.paramString} $it").queue()
                }, {

                })
    }
}
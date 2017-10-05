package hu.suppoze.pupperbot.giphy

import com.github.salomonbrys.kodein.instance
import hu.suppoze.pupperbot.common.*
import hu.suppoze.pupperbot.di.kodein
import mu.KLogging
import java.net.URLEncoder
import java.util.concurrent.ThreadLocalRandom

@ChatCommand(type = AvailableCommands.GIPHY_SEARCH)
class GiphySearchCommand : UseCase<String> {
    companion object : KLogging()

    private val giphyServer: GiphyServer by kodein.instance()

    private lateinit var parameterizedCommand: ParameterizedCommand

    override val onNext: (String) -> Unit = {
        parameterizedCommand.event.message.channel.sendMessage("${parameterizedCommand.paramString} $it")
    }

    override val onError: (Throwable) -> Unit = {
        logger.error { it.stackTrace }
        parameterizedCommand.event.message.channel.sendMessage("Error during giphy request: ${it.message}")
    }

    override fun execute(parameterizedCommand: ParameterizedCommand) {
        this.parameterizedCommand = parameterizedCommand

        val phrase = parameterizedCommand.paramString

        if (phrase == null) {
            onError(Throwable("Phrase was null."))
            return
        }

        val urlEncodedPhrase = URLEncoder.encode(phrase, Charsets.UTF_8.name())
        val limit = 10

        giphyServer.searchGiphyBy(urlEncodedPhrase, limit)
                .map {
                    val upperRange = minOf(limit, it.pagination.count)
                    if (upperRange == 0) throw Throwable("No results found.") // TODO: Custom exception
                    it.data[ThreadLocalRandom.current().nextInt(upperRange)].url // TODO: outsource randomizer
                }
                .subscribe(onNext, onError)
    }
}
package hu.suppoze.pupperbot.giphy

import com.github.salomonbrys.kodein.instance
import hu.suppoze.pupperbot.common.*
import hu.suppoze.pupperbot.di.kodein
import java.net.URLEncoder
import java.util.concurrent.ThreadLocalRandom

@ChatCommand(type = AvailableCommands.GIPHY_SEARCH)
class GiphySearchCommand : UseCase<String> {

    private val giphyServer: GiphyServer by kodein.instance()

    private lateinit var rawCommand: RawCommand

    override val onNext: (String) -> Unit = {
        rawCommand.event.message.channel.sendMessage("${rawCommand.rawParams} $it")
    }

    override val onError: (Throwable) -> Unit = {
        rawCommand.event.message.channel.sendMessage("Error during giphy request: ${it.message}")
        it.printStackTrace()
    }

    override fun execute(rawCommand: RawCommand) {
        this.rawCommand = rawCommand

        val phrase = rawCommand.rawParams

        if (phrase == null) {
            onError(Throwable("Phrase was null."))
            return
        }

        val urlEncodedPhrase = URLEncoder.encode(phrase, Charsets.UTF_8.name())
        val limit = 10

        giphyServer.searchGiphyBy(urlEncodedPhrase, limit)
                .map {
                    val range = maxOf(limit, it.pagination.count)
                    if (range == 0) throw Throwable("No results found.") // TODO: Custom exception
                    it.data[ThreadLocalRandom.current().nextInt(range)].url // TODO: outsource randomizer
                }
                .subscribe(onNext, onError)
    }
}
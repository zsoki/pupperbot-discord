package hu.suppoze.pupperbot.pron

import hu.suppoze.pupperbot.common.AvailableCommands
import hu.suppoze.pupperbot.common.ChatCommand
import hu.suppoze.pupperbot.common.ParameterizedCommand
import hu.suppoze.pupperbot.common.UseCase
import mu.KLogging
import org.jsoup.Jsoup

@ChatCommand(type = AvailableCommands.GIFLAND_NSFW)
class PronCommand : UseCase<String> {

    companion object : KLogging()

    private lateinit var parameterizedCommand: ParameterizedCommand

    override val onNext: (String) -> Unit = {
        parameterizedCommand.event.textChannel
                .sendMessage(it)
                .queue()
    }

    override val onError: (Throwable) -> Unit = {
        logger.error(it) { it.message }
    }

    // TODO better implementation
    override fun execute(parameterizedCommand: ParameterizedCommand) {
        this.parameterizedCommand = parameterizedCommand

        if (!parameterizedCommand.event.textChannel.isNSFW)
            onError(Throwable("You naughty boy, this channel isn't NSFW!"))

        var imgUrl = ""

        try {
            val document = Jsoup.connect("http://porn.gifland.us/").get()
            imgUrl = document.select("main.container a img").first().attr("src")
        } catch (ex: Exception) {
            onError(ex)
        }

        onNext(imgUrl)
    }
}
package hu.suppoze.pupperbot.app.command.pron

import hu.suppoze.pupperbot.app.command.AvailableCommands
import hu.suppoze.pupperbot.app.command.ChatCommand
import hu.suppoze.pupperbot.app.command.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

@ChatCommand(type = AvailableCommands.GIFLAND_NSFW)
class PronCommand : UseCase() {

    override suspend fun onExecute() {
        if (!commandContext.event.textChannel.isNSFW) {
            commandContext.event.textChannel.sendMessage("You naughty boy, this channel is SFW!").queue()
            return
        }

        val document = withContext(Dispatchers.IO) { Jsoup.connect("http://porn.gifland.club/").get() }
        val selector = "body > main > div.gf__body.gf__body--image.gf__body--big > a > img"
        val imgUrl = "https:${document.select(selector).first().attr("src")}"

        commandContext.event.textChannel.sendMessage(imgUrl).queue()
    }

}
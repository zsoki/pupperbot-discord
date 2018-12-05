package hu.suppoze.pupperbot.app.command.pron

import hu.suppoze.pupperbot.app.command.AvailableCommands
import hu.suppoze.pupperbot.app.command.ChatCommand
import hu.suppoze.pupperbot.app.command.UseCase
import org.jsoup.Jsoup

@ChatCommand(type = AvailableCommands.GIFLAND_NSFW)
class PronCommand : UseCase() {

    override suspend fun onExecute() {
        if (!commandContext.event.textChannel.isNSFW) {
            commandContext.event.textChannel.sendMessage("You naughty boy, this channel is SFW!").queue()
            return
        }

        val document = Jsoup.connect("http://porn.gifland.us/").get()
        val imgUrl = document.select("main.container a img").first().attr("src")

        commandContext.event.textChannel.sendMessage(imgUrl).queue()
    }

}
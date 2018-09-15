package hu.suppoze.pupperbot.app.pron

import hu.suppoze.pupperbot.app.common.command.AvailableCommands
import hu.suppoze.pupperbot.app.common.command.ChatCommand
import hu.suppoze.pupperbot.app.common.command.UseCase
import org.jsoup.Jsoup

@ChatCommand(type = AvailableCommands.GIFLAND_NSFW)
class PronCommand : UseCase() {

    override fun onExecute() {
        if (!commandContext.event.textChannel.isNSFW) {
            commandContext.event.textChannel.sendMessage("You naughty boy, this channel is SFW!").queue()
            return
        }

        val document = Jsoup.connect("http://porn.gifland.us/").get()
        val imgUrl = document.select("main.container a img").first().attr("src")

        commandContext.event.textChannel.sendMessage(imgUrl).queue()
    }

}
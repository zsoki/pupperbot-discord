package hu.suppoze.pupperbot.pron

import hu.suppoze.pupperbot.common.AvailableCommands
import hu.suppoze.pupperbot.common.ChatCommand
import hu.suppoze.pupperbot.common.UseCase
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
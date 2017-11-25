package hu.suppoze.pupperbot.pron

import hu.suppoze.pupperbot.common.*
import mu.KLogging
import org.jsoup.Jsoup

@ChatCommand(type = AvailableCommands.GIFLAND_NSFW)
class PronCommand : UseCase2<String>() {

    override fun onExecute() {
        if (!parameterizedCommand.event.textChannel.isNSFW) {
            parameterizedCommand.event.textChannel.sendMessage("You naughty boy, this channel isn't NSFW!").queue()
            return
        }

        val document = Jsoup.connect("http://porn.gifland.us/").get()
        val imgUrl = document.select("main.container a img").first().attr("src")

        parameterizedCommand.event.textChannel.sendMessage(imgUrl).queue()
    }

}
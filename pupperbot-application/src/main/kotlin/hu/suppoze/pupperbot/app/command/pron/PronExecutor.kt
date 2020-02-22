package hu.suppoze.pupperbot.app.command.pron

import hu.suppoze.pupperbot.app.command.CommandContext
import hu.suppoze.pupperbot.app.command.common.CommandExecutor
import org.jsoup.Jsoup

class PronExecutor : CommandExecutor {

    override fun execute(commandContext: CommandContext) {
//        if (!commandContext.event.textChannel.isNSFW) { TODO
//            commandContext.event.textChannel.sendMessage("You naughty boy, this channel is SFW!").queue()
//            return
//        }

        // TODO test if it hangs the thread. Probably.
        val document = Jsoup.connect("http://porn.gifland.club/").get()
        val selector = "body > main > div.gf__body.gf__body--image.gf__body--big > a > img"
        val imgUrl = "https:${document.select(selector).first().attr("src")}"

//        commandContext.event.textChannel.sendMessage(imgUrl).queue()
    }
}
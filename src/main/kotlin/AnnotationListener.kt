import common.Command
import common.CommandError
import giphy.GiphyCommand
import help.HelpCommand
import sx.blah.discord.api.events.EventSubscriber
import sx.blah.discord.handle.impl.events.MessageReceivedEvent
import sx.blah.discord.handle.impl.events.ReadyEvent
import sx.blah.discord.handle.obj.Status

class AnnotationListener {

    @EventSubscriber
    fun onReadyEvent(event: ReadyEvent) {
        Main.pupperBot.changeStatus(Status.game(";help for commands"))
        Main.listenForCommand()
    }

    @EventSubscriber
    fun onMessageReceiedEvent(event: MessageReceivedEvent) {
        if (event.message.content.startsWith(';')) {
            CommandFactory(event).build().perform()
        }
    }
}

class CommandFactory(val event: MessageReceivedEvent) {

    val commandLine: String = event.message.content.trimStart(';')

    fun  build(): Command {
        val words = commandLine.split(' ')
        return determineAndCreateCommand(words[0], words.drop(1))
    }

    private fun determineAndCreateCommand(command: String, params: List<String>): Command {
        when (command) {
            "giphy" -> return GiphyCommand(event, params.reduce { s1, s2 -> "$s1+$s2" })
            "help" -> return HelpCommand(event)
            else -> return CommandError(event)
        }
    }
}

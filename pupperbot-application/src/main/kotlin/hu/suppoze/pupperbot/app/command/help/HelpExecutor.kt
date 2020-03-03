package hu.suppoze.pupperbot.app.command.help

import hu.suppoze.pupperbot.app.command.CommandContext
import hu.suppoze.pupperbot.app.command.CommandLibrary
import hu.suppoze.pupperbot.app.command.common.CommandExecutor

class HelpExecutor(private val commandLibrary: CommandLibrary) : CommandExecutor {

    override fun execute(commandContext: CommandContext) {
        val commandList = commandLibrary.commands.map { command ->
            "${command.example} - ${command.description}\n"
        }

        val inviteUrl = "TODO"

        val helpString =
            """
                To invite PupperBot to your server, use this link: <${inviteUrl}>
                
                $commandList
            """.trimIndent()

//        commandContext.event.textChannel.sendMessage(helpString).queue() TODO
    }
}
package hu.suppoze.pupperbot.app.command.say

import hu.suppoze.pupperbot.app.command.CommandContext
import hu.suppoze.pupperbot.app.command.common.CommandExecutor

class SayCommandExecutor : CommandExecutor {

    override fun execute(commandContext: CommandContext) {
        if (commandContext.rawArgs == null || commandContext.rawArgs.isEmpty()) {
            throw IllegalArgumentException("Argument string is null or empty")
        }

//        commandContext.event.message.delete().queue() TODO
//        commandContext.event.textChannel.sendMessage(commandContext.rawArgs).queue()
    }
}
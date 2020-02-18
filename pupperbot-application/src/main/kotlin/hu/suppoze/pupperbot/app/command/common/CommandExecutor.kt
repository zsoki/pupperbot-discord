package hu.suppoze.pupperbot.app.command.common

import hu.suppoze.pupperbot.app.command.CommandContext

interface CommandExecutor {
    fun execute(commandContext: CommandContext)
}

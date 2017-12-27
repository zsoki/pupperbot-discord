package hu.suppoze.pupperbot.common

import net.dv8tion.jda.core.events.message.MessageReceivedEvent

data class CommandContext(
        val event: MessageReceivedEvent,
        val rawCommand: String,
        val rawArgs: String? = null,
        val argList: List<String>? = null
)
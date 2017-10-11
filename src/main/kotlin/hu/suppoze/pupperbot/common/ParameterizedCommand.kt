package hu.suppoze.pupperbot.common

import net.dv8tion.jda.core.events.message.MessageReceivedEvent

data class ParameterizedCommand(
        val event: MessageReceivedEvent,
        val command: String,
        val paramString: String? = null,
        val params: List<String>? = null
)
package hu.suppoze.pupperbot.common

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent

data class ParameterizedCommand(
        val event: MessageReceivedEvent,
        val command: String,
        val paramString: String? = null,
        val params: List<String>? = null
)
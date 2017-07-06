package hu.suppoze.pupperbot.common

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent

data class RawCommand(
        val event: MessageReceivedEvent,
        val command: String,
        val rawParams: String? = null,
        val parameters: List<String>? = null
)
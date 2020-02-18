package hu.suppoze.pupperbot.app.command

import net.dv8tion.jda.api.events.message.MessageReceivedEvent

data class CommandContext(
    val event: MessageReceivedEvent,
    val keyword: String,
    val rawArgs: String? = null,
    val argList: List<String>? = null
)
package hu.suppoze.pupperbot.app.command

import com.jessecorbett.diskord.api.model.Message

data class CommandContext(
    val event: Message,
    val keyword: String,
    val rawArgs: String? = null,
    val argList: List<String>? = null
)

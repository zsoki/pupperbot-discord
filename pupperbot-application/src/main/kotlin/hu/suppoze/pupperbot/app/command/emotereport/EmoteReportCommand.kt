package hu.suppoze.pupperbot.app.command.emotereport

import hu.suppoze.pupperbot.app.command.AvailableCommands
import hu.suppoze.pupperbot.app.command.ChatCommand
import hu.suppoze.pupperbot.app.command.UseCase
import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.entities.Message
import java.time.LocalDateTime

@ChatCommand(type = AvailableCommands.EMOTE_REPORT)
class EmoteReportCommand : UseCase() {

    override suspend fun onExecute() {
        val server = commandContext.event.guild
        val reportMap = mutableMapOf<String, Int>()

        val months = try { commandContext.argList?.firstOrNull()?.toLong() ?: 1L } catch (ex: Exception) { 1L }

        server.textChannels.forEach { channel ->
            channel.iterableHistory.asSequence()
                .takeWhile { message -> messageCreatedSince(message, months) }
                .filter { message -> message.emotes.isNotEmpty() || message.reactions.any { it.reactionEmote.isEmote } }
                .flatMap { message ->
                    message.emotes.asSequence().map { it.asMention }.plus(
                        message.reactions.asSequence().filter { it.reactionEmote.isEmote }.map { it.reactionEmote.emote.asMention })
                }
                .filterNotNull()
                .forEach { increaseEmoteCount(it, reportMap) }
        }
        val sortedReport = reportMap.toList().sortedByDescending { it.second }
        buildMessagesFrom(sortedReport).forEach { commandContext.event.channel.sendMessage(it.build()).complete() }
    }

    private fun buildMessagesFrom(sortedReport: List<Pair<String, Int>>): List<MessageBuilder> {
        var returnString = ""
        sortedReport.forEachIndexed { rank, emoteToCount ->
            returnString += emoteToCount.first + " - #$rank with ${emoteToCount.second} uses\n"
        }
        return returnString.lineSequence()
            .chunked(10) { it.reduceRight { a, b -> "$a\n$b" } }
            .map { MessageBuilder().append(it) }
            .toList()
    }

    private fun increaseEmoteCount(
        emoteAsMention: String,
        reportMap: MutableMap<String, Int>
    ) {
        reportMap.merge(emoteAsMention, 1, Int::plus)
    }

    private fun messageCreatedSince(message: Message, months: Long) =
        message.creationTime.toLocalDateTime().isAfter(LocalDateTime.now().minusMonths(months))

}
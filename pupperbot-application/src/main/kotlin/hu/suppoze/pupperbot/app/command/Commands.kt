package hu.suppoze.pupperbot.app.command

import hu.suppoze.pupperbot.app.command.common.CommandExecutor
import hu.suppoze.pupperbot.app.command.gif.RandomGifExecutor
import hu.suppoze.pupperbot.app.command.gif.SearchGifExecutor
import hu.suppoze.pupperbot.app.command.gif.api.PupperGifClient
import hu.suppoze.pupperbot.app.command.help.HelpExecutor
import hu.suppoze.pupperbot.app.command.pron.PronExecutor
import hu.suppoze.pupperbot.app.command.say.SayCommandExecutor

sealed class Command {
    abstract val executor: CommandExecutor

    fun execute(commandContext: CommandContext) {
        executor.execute(commandContext)
    }
}

abstract class PublicCommand : Command() {
    abstract val keyword: String
    abstract val description: String
    abstract val example: String
}

class HelpCommand(commandLibrary: CommandLibrary) : PublicCommand() {
    override val executor = HelpExecutor(commandLibrary)
    override val keyword = "help"
    override val description = "You are using this right now, woof!"
    override val example = "`;$keyword`"
}

class SayCommand : PublicCommand() {
    override val executor = SayCommandExecutor()
    override val keyword = "say"
    override val description = "Make Pupper say something. Your message will be deleted."
    override val example = "`;$keyword I'm a good booy!`"
}


class SearchGifCommand(gifClient: PupperGifClient) : PublicCommand() {
    override val executor = SearchGifExecutor(gifClient)
    override val keyword = "gif"
    override val description = "Returns a random gif restricted by tags (R rated!)"
    override val example = "`;$keyword wet pussy cat kittens <add any more tags>`"
}

class RandomGifCommand(gifClient: PupperGifClient) : PublicCommand() {
    override val executor = RandomGifExecutor(gifClient)
    override val keyword = "random"
    override val description = "Searches for a related gif by a phrase (R rated!)"
    override val example = "`;$keyword just do it!`"
}

class PronCommand : PublicCommand() {
    override val executor = PronExecutor()
    override val keyword = "pron"
    override val description = "Fetches a random NSFW gif from <http://porn.gifland.club>"
    override val example = "`;$keyword`"
}

class NoopCommand : Command() {
    override val executor = object : CommandExecutor {
        // TODO do something with this?
        override fun execute(commandContext: CommandContext) { }
    }
}

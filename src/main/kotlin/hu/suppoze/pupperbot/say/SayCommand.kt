package hu.suppoze.pupperbot.say

import hu.suppoze.pupperbot.common.UseCase
import hu.suppoze.pupperbot.common.CommandParser

class SayCommand(val rawCommand: CommandParser.RawCommand) : UseCase<Any> {

    override val onNext: (Any) -> Unit
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val onError: (Throwable) -> Unit
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun execute() {
        if (rawCommand.rawParams == null || rawCommand.rawParams.isEmpty())
            return

        rawCommand.event.message.delete()
        rawCommand.event.message.channel.sendMessage(rawCommand.rawParams)
    }
}
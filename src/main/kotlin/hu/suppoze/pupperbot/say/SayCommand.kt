package hu.suppoze.pupperbot.say

import hu.suppoze.pupperbot.common.Command
import hu.suppoze.pupperbot.common.CommandParser
import io.reactivex.Observable

class SayCommand(val rawCommand: CommandParser.RawCommand) : Command<Any> {

    override val onNext: (Any) -> Unit
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val onError: (Throwable) -> Unit
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun perform() {
        if (rawCommand.rawParams == null || rawCommand.rawParams.isEmpty())
            return

        rawCommand.event.message.delete()
        rawCommand.event.message.channel.sendMessage(rawCommand.rawParams)
    }
}
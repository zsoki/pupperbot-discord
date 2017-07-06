package hu.suppoze.pupperbot.say

import hu.suppoze.pupperbot.common.*

@ChatCommand(type = AvailableCommands.SAY)
class SayCommand : UseCase<Any> {

    override val onNext: (Any) -> Unit
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override val onError: (Throwable) -> Unit
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun execute(rawCommand : RawCommand) {
        if (rawCommand.rawParams == null || rawCommand.rawParams.isEmpty())
            return

        rawCommand.event.message.delete()
        rawCommand.event.message.channel.sendMessage(rawCommand.rawParams)
    }
}
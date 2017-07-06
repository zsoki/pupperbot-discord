package hu.suppoze.pupperbot.help

import hu.suppoze.pupperbot.common.*

@ChatCommand(type = AvailableCommands.HELP)
class HelpCommand : UseCase<Any> {

    override val onNext: (Any) -> Unit
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override val onError: (Throwable) -> Unit
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun execute(rawCommand: RawCommand) {

        var helpString = ""

        for (item in AvailableCommands.values()) {
            helpString += "${item.usage} - ${item.description}\n"
        }

        rawCommand.event.message.channel.sendMessage(helpString)
    }
}
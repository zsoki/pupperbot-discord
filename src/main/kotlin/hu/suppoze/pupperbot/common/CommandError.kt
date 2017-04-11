package hu.suppoze.pupperbot.common

import sx.blah.discord.handle.impl.events.MessageReceivedEvent

class CommandError(val event: MessageReceivedEvent) : Command<Any> {

    override val onNext: (Any) -> Unit
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override val onError: (Throwable) -> Unit
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun perform() {
        // Nothing to do
    }
}
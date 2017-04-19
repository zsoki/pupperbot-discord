package hu.suppoze.pupperbot.error

import hu.suppoze.pupperbot.common.Command
import hu.suppoze.pupperbot.common.CommandParser
import io.reactivex.Observable

class CommandError(val rawCommand: CommandParser.RawCommand, val errorMessage: String) : Command<String> {

    override val onNext: (String) -> Unit = {
        rawCommand.event.message.author.orCreatePMChannel.sendMessage(it)
    }

    override val onError: (Throwable) -> Unit = {
        rawCommand.event.message.author.orCreatePMChannel.sendMessage("Error during sending you the error message: ${it.message}")
        it.printStackTrace()
    }

    override fun perform() {
        Observable.fromCallable { errorMessage }
                .subscribe(onNext, onError)
    }
}
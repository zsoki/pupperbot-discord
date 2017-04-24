package hu.suppoze.pupperbot.error

import hu.suppoze.pupperbot.common.UseCase
import hu.suppoze.pupperbot.common.CommandParser
import io.reactivex.Observable

class UseCaseError(val rawCommand: CommandParser.RawCommand, val errorMessage: String) : UseCase<String> {

    override val onNext: (String) -> Unit = {
        rawCommand.event.message.author.orCreatePMChannel.sendMessage(it)
    }

    override val onError: (Throwable) -> Unit = {
        rawCommand.event.message.author.orCreatePMChannel.sendMessage("Error during sending you the error message: ${it.message}")
        it.printStackTrace()
    }

    override fun execute() {
        Observable.fromCallable { errorMessage }
                .subscribe(onNext, onError)
    }
}
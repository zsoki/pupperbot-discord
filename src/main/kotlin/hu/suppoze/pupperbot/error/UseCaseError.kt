package hu.suppoze.pupperbot.error

import hu.suppoze.pupperbot.common.UseCase
import hu.suppoze.pupperbot.common.CommandParser
import hu.suppoze.pupperbot.common.RawCommand
import io.reactivex.Observable

class UseCaseError(val errorMessage: String) : UseCase<String> {

    private lateinit var rawCommand: RawCommand

    override val onNext: (String) -> Unit = {
        rawCommand.event.message.author.orCreatePMChannel.sendMessage(it)
    }

    override val onError: (Throwable) -> Unit = {
        rawCommand.event.message.author.orCreatePMChannel.sendMessage("Error during sending you the error message: ${it.message}")
        it.printStackTrace()
    }

    override fun execute(rawCommand: RawCommand) {
        this.rawCommand = rawCommand

        Observable.fromCallable { errorMessage }
                .subscribe(onNext, onError)
    }
}
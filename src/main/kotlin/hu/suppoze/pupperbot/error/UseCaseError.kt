package hu.suppoze.pupperbot.error

import hu.suppoze.pupperbot.common.UseCase
import hu.suppoze.pupperbot.common.ParameterizedCommand
import io.reactivex.Observable

class UseCaseError(val errorMessage: String) : UseCase<String> {

    private lateinit var parameterizedCommand: ParameterizedCommand

    override val onNext: (String) -> Unit = {
        parameterizedCommand.event.message.author.orCreatePMChannel.sendMessage(it)
    }

    override val onError: (Throwable) -> Unit = {
        parameterizedCommand.event.message.author.orCreatePMChannel.sendMessage("Error during sending you the error message: ${it.message}")
        it.printStackTrace()
    }

    override fun execute(parameterizedCommand: ParameterizedCommand) {
        this.parameterizedCommand = parameterizedCommand

        Observable.fromCallable { errorMessage }
                .subscribe(onNext, onError)
    }
}
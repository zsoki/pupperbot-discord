package hu.suppoze.pupperbot.say

import hu.suppoze.pupperbot.common.*
import mu.KLogging

@ChatCommand(type = AvailableCommands.SAY)
class SayCommand : UseCase<ParameterizedCommand> {
    companion object : KLogging()

    override val onNext: (ParameterizedCommand) -> Unit = {
        it.event.message.delete()
        it.event.message.channel.sendMessage(it.paramString)
    }

    override val onError: (Throwable) -> Unit = {
        logger.error { it.message }
    }

    override fun execute(parameterizedCommand: ParameterizedCommand) {
        if (parameterizedCommand.paramString == null || parameterizedCommand.paramString.isEmpty()) {
            onError(IllegalArgumentException("Parameter string is null or empty"))
            return
        }

        onNext(parameterizedCommand)
    }
}
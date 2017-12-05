package hu.suppoze.pupperbot.common

import mu.KLogging

abstract class UseCase {

    companion object : KLogging()

    protected lateinit var parameterizedCommand: ParameterizedCommand

    private val onNext: () -> Unit = {
        logger.info { "${parameterizedCommand.command} executed with params \"${parameterizedCommand.paramString}\"" }
    }

    private val onError: (Throwable) -> Unit = {
        logger.error(it) { it.message }
        parameterizedCommand.event.textChannel
                .sendMessage("Error during ${parameterizedCommand.command} command: ${it.message}")
                .queue()
    }

    fun execute(parameterizedCommand: ParameterizedCommand) {
        this.parameterizedCommand = parameterizedCommand
        try {
            onExecute()
            onNext()
        } catch (ex: Exception) {
            onError(ex)
            return
        }
    }

    protected abstract fun onExecute()

}
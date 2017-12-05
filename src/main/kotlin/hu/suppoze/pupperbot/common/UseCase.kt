package hu.suppoze.pupperbot.common

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import mu.KLogging

abstract class UseCase {

    companion object : KLogging()

    protected lateinit var parameterizedCommand: ParameterizedCommand

    private val onComplete: () -> Unit = {
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
        launch(CommonPool) {
            try {
                onExecute()
                onComplete()
            } catch (ex: Exception) {
                onError(ex)
            }
        }
    }

    protected abstract fun onExecute()

}
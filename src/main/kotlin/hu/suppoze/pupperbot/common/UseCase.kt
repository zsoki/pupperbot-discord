package hu.suppoze.pupperbot.common

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import mu.KLogging

abstract class UseCase {

    companion object : KLogging()

    protected lateinit var commandContext: CommandContext

    private val onComplete: () -> Unit = {
        logger.info { "${commandContext.rawCommand} executed with arguments \"${commandContext.rawArgs}\"" }
    }

    private val onError: (Throwable) -> Unit = {
        logger.error(it) { it.message }
        commandContext.event.textChannel
            .sendMessage("Error during ${commandContext.rawCommand} rawCommand: ${it.message}")
            .queue()
    }

    fun executeAsync(commandContext: CommandContext) {
        this.commandContext = commandContext
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
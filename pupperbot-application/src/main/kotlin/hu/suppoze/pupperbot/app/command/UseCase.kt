package hu.suppoze.pupperbot.app.command

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import mu.KLogging
import kotlin.coroutines.CoroutineContext

abstract class UseCase : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + SupervisorJob()

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
        launch {
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
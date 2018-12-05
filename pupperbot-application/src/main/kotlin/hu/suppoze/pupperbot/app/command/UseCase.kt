package hu.suppoze.pupperbot.app.command

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

    suspend fun executeAsync(commandContext: CommandContext) {
        this.commandContext = commandContext
        try {
            onExecute()
            onComplete()
        } catch (ex: Exception) {
            onError(ex)
        }
    }

    protected abstract suspend fun onExecute()

}
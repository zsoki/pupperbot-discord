package hu.suppoze.pupperbot.common

import mu.KotlinLogging
import org.reflections.Reflections

class CommandProvider {

    companion object {

        private val logger = KotlinLogging.logger {}
        private val commandMap = mutableMapOf<String, UseCase<*>>()
        private val packageString = "hu.suppoze.pupperbot"

        init {
            val reflections = Reflections(packageString)
            val chatCommandClasses = reflections.getTypesAnnotatedWith(ChatCommand::class.java)

            for (clazz in chatCommandClasses) {

                try {

                    val useCase = clazz.newInstance() as UseCase<*>
                    val chatCommand = clazz.getAnnotation(ChatCommand::class.java) as ChatCommand

                    if (!commandMap.containsKey(chatCommand.type.commandString)) {
                        commandMap.put(chatCommand.type.commandString, useCase)
                    } else {
                        logger.warn { "Duplicated commands! ${chatCommand.type} already defined!" }
                    }

                } catch (ex: Exception) {
                    when (ex) {
                        is InstantiationException,
                        is IllegalAccessException -> logger.error(ex) { "Cannot make class instance" }
                        else -> logger.error(ex) { "Unknown error: ${ex.message}" }
                    }
                }
            }
        }

        fun get(commandString: String): UseCase<*>? = commandMap[commandString]
    }
}
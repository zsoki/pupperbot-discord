package hu.suppoze.pupperbot.common

import mu.KotlinLogging
import org.reflections.Reflections

class CommandProvider {

    companion object {

        private val logger = KotlinLogging.logger {}
        private val chatCommandClassMap = mutableMapOf<String, Class<*>>()
        private val packageString = "hu.suppoze.pupperbot"

        init {
            val reflections = Reflections(packageString)
            val chatCommandClasses = reflections.getTypesAnnotatedWith(ChatCommand::class.java)

            for (clazz in chatCommandClasses) {

                try {

                    val chatCommand = clazz.getAnnotation(ChatCommand::class.java) as ChatCommand

                    if (!chatCommandClassMap.containsKey(chatCommand.type.commandString)) {
                        chatCommandClassMap.put(chatCommand.type.commandString, clazz)
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

        fun getUseCaseFor(commandString: String) = chatCommandClassMap[commandString]?.newInstance() as? UseCase

        fun getCommandStrings() = chatCommandClassMap.keys
    }
}
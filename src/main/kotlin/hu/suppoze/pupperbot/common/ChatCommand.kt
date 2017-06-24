package hu.suppoze.pupperbot.common

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ChatCommand(val name: AvailableCommands)
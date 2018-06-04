package hu.suppoze.pupperbot.app.common

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ChatCommand(val type: AvailableCommands)
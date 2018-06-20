package hu.suppoze.pupperbot.app.common.command

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ChatCommand(val type: AvailableCommands)
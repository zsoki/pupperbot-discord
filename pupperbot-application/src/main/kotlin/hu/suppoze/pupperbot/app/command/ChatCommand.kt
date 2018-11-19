package hu.suppoze.pupperbot.app.command

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ChatCommand(val type: AvailableCommands)
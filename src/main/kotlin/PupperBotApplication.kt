import com.github.salomonbrys.kodein.instance
import common.PupperBot
import common.TokenProvider
import di.kodein
import listeners.AnnotationListener
import sx.blah.discord.handle.obj.Permissions
import sx.blah.discord.handle.obj.Status
import java.util.*

object PupperBotApplication {

    private val pupperBot: PupperBot by kodein.instance()

    @JvmStatic fun main(args: Array<String>) {

        if (args.count() < 1) throw RuntimeException("Need at least 1 argument! (Token)")

        TokenProvider.token = args[0]

        pupperBot.client.dispatcher.registerListener(AnnotationListener())

        createInviteLink()

        listenForCommand()
    }

    fun listenForCommand() {

        while (true) {

            val input = readLine()?.split(' ') ?: continue
            val command = input[0]

            when (command) {
                "login" -> {
                    pupperBot.client.login()
                    return
                }
                "logout" -> {
                    pupperBot.client.logout()
                    return
                }
                "playing" -> {
                    if (input.size > 1) {
                        pupperBot.client.changeStatus(Status.game(input.takeLast(input.size - 1).reduce { s1, s2 -> "$s1 $s2" }))
                    }
                }
                else -> println("Unrecognized command.")
            }
        }
    }

    private fun createInviteLink() {
        val permissions = Permissions.generatePermissionsNumber(EnumSet.copyOf(mutableListOf(Permissions.SEND_MESSAGES, Permissions.READ_MESSAGES)))
        print("Invite link: https://discordapp.com/api/oauth2/authorize?client_id=${pupperBot.client.applicationClientID}&scope=bot&permissions=$permissions\n")
    }
}
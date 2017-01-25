import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.handle.obj.Permissions
import sx.blah.discord.handle.obj.Status
import java.util.*

object Main {

    lateinit var pupperBot: IDiscordClient

    @JvmStatic fun main(args: Array<String>) {

        if (args.count() < 1) throw RuntimeException("Need at least 1 argument! (Token)")

        val client = PupperBot().createClient(args[0], false) ?: throw RuntimeException("Failed to create client.")

        pupperBot = client

        val dispatcher = pupperBot.dispatcher
        dispatcher.registerListener(AnnotationListener())

        createInviteLink()

        listenForCommand()
    }

    fun listenForCommand() {

        while (true) {

            print("₍ᐢ•ﻌ•ᐢ₎ > ")

            val input = readLine()?.split(' ') ?: continue
            val command = input[0]

            when (command) {
                "login" -> {
                    pupperBot.login()
                    return
                }
                "logout" -> {
                    pupperBot.logout()
                    return
                }
                "playing" -> {
                    if (input.size > 1) {
                        pupperBot.changeStatus(Status.game(input.takeLast(input.size - 1).reduce { s1, s2 -> "$s1 $s2" }))
                    }
                }
                else -> println("Bork? - Unrecognized command.")
            }
        }
    }

    private fun createInviteLink() {
        val permissions = Permissions.generatePermissionsNumber(EnumSet.copyOf(mutableListOf(Permissions.SEND_MESSAGES, Permissions.READ_MESSAGES)))
        print("Invite link: https://discordapp.com/api/oauth2/authorize?client_id=${pupperBot.applicationClientID}&scope=bot&permissions=$permissions\n")
    }
}
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.handle.obj.Permissions
import java.util.*

object Main {

    lateinit var pupperBot : IDiscordClient

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

        print("₍ᐢ•ﻌ•ᐢ₎ > ")
        val command = readLine()

        if (command == "login") {
            pupperBot.login()
            return
        } else if (command == "logout") {
            pupperBot.logout()
            return
        } else {
            println("Bork? - Unrecognized command.")
        }

        listenForCommand()
    }

    private fun createInviteLink() {
        val permissions = Permissions.generatePermissionsNumber(EnumSet.copyOf(mutableListOf(Permissions.SEND_MESSAGES, Permissions.READ_MESSAGES)))
        print("Invite link: https://discordapp.com/api/oauth2/authorize?client_id=${pupperBot.applicationClientID}&scope=bot&permissions=$permissions\n")
    }
}




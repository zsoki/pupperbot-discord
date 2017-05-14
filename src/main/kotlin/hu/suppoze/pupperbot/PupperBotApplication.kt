package hu.suppoze.pupperbot

import com.github.salomonbrys.kodein.instance
import hu.suppoze.pupperbot.common.PupperBot
import hu.suppoze.pupperbot.common.TokenProvider
import hu.suppoze.pupperbot.di.kodein
import hu.suppoze.pupperbot.listeners.AnnotationListener
import sx.blah.discord.handle.obj.Permissions
import java.io.FileReader
import java.util.*

object PupperBotApplication {

    private val pupperBot: PupperBot by kodein.instance()

    @JvmStatic fun main(args: Array<String>) {

        val reader = FileReader("./token.txt")
        val token = reader.readText()

        TokenProvider.token = token

        pupperBot.client.dispatcher.registerListener(AnnotationListener())

        createInviteLink()
    }

    fun listenForCommand() {

        while (true) {

            val input = readLine()?.split(' ') ?: continue
            val command = input[0]

            when (command) {
                "logout" -> {
                    pupperBot.client.logout()
                    System.exit(ExitCodes.EXIT_CODE_NORMAL)
                    return
                }
                "restart" -> {
                    pupperBot.client.logout()
                    System.exit(ExitCodes.EXIT_CODE_RESTART)
                }
                "update" -> {
                    pupperBot.client.logout()
                    System.exit(ExitCodes.EXIT_CODE_UPDATE)
                }
                else -> println("Unrecognized command.")
            }
        }
    }

    private fun createInviteLink() {
        val permissions = Permissions.generatePermissionsNumber(
                EnumSet.copyOf(
                        mutableListOf(
                                Permissions.SEND_MESSAGES,
                                Permissions.READ_MESSAGES,
                                Permissions.MANAGE_MESSAGES)))
        print("Invite link: https://discordapp.com/api/oauth2/authorize?client_id=${pupperBot.client.applicationClientID}&scope=bot&permissions=$permissions\n")
    }

    private object ExitCodes {
        const val EXIT_CODE_NORMAL: Int = 0
        const val EXIT_CODE_UPDATE: Int = 20
        const val EXIT_CODE_RESTART: Int = 21
    }
}
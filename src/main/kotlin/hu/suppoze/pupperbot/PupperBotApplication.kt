package hu.suppoze.pupperbot

import com.github.salomonbrys.kodein.instance
import hu.suppoze.pupperbot.common.TokenProvider
import hu.suppoze.pupperbot.di.kodein
import hu.suppoze.pupperbot.listeners.AnnotationListener
import mu.KotlinLogging
import sx.blah.discord.handle.obj.Permissions
import java.io.FileReader
import java.util.*
import kotlin.concurrent.thread

private val logger = KotlinLogging.logger {}

object PupperBotApplication {

    private val pupperBot: PupperBot by kodein.instance()
    private val consoleScanner: Scanner by lazy { Scanner(System.`in`) }

    @JvmStatic fun main(args: Array<String>) {
        val reader = FileReader("./token.txt")
        val token = reader.readText()

        TokenProvider.token = token

        pupperBot.client.dispatcher.registerListener(AnnotationListener())

        createInviteLink()
    }

    fun listenForCommand() = thread(start = true, name = "ConsoleInput") {
        while (true) {
            logger.info { "Listening for user input on System.in" }
            val command = consoleScanner.next() ?: continue

            when (command) {
                "logout" -> logoutAndShutdown(command, ExitCodes.EXIT_CODE_NORMAL)
                "restart" -> logoutAndShutdown(command, ExitCodes.EXIT_CODE_RESTART)
                "update" -> logoutAndShutdown(command, ExitCodes.EXIT_CODE_UPDATE)
                else -> println { "Unrecognized command." }
            }
        }
    }

    private fun logoutAndShutdown(command: String, exitCode: Int) {
        logger.info { "command=$command msg='Shutting down with ExitCode: $exitCode'" }
        pupperBot.client.logout()
        System.exit(exitCode)
    }

    private fun createInviteLink() {
        val permissions = Permissions.generatePermissionsNumber(
                EnumSet.copyOf(
                        mutableListOf(
                                Permissions.SEND_MESSAGES,
                                Permissions.READ_MESSAGES,
                                Permissions.MANAGE_MESSAGES)))
        println { "Invite link: https://discordapp.com/api/oauth2/authorize?client_id=" +
                "${pupperBot.client.applicationClientID}&scope=bot&permissions=$permissions" }
    }

    private object ExitCodes {
        const val EXIT_CODE_NORMAL: Int = 0
        const val EXIT_CODE_UPDATE: Int = 20
        const val EXIT_CODE_RESTART: Int = 21
    }
}
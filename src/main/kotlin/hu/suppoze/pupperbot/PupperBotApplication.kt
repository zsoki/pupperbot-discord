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
                "logout" -> {
                    logger.info { "command=$command msg='Shutting down with ExitCode: ${ExitCodes.EXIT_CODE_NORMAL}'" }
                    pupperBot.client.logout()
                    System.exit(ExitCodes.EXIT_CODE_NORMAL)
                }
                "restart" -> {
                    logger.info { "command=$command msg='Shutting down with ExitCode: ${ExitCodes.EXIT_CODE_RESTART}'" }
                    pupperBot.client.logout()
                    System.exit(ExitCodes.EXIT_CODE_RESTART)
                }
                "update" -> {
                    logger.info { "command=$command msg='Shutting down with ExitCode: ${ExitCodes.EXIT_CODE_UPDATE}'" }
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
        logger.info { "Invite link: https://discordapp.com/api/oauth2/authorize?client_id=${pupperBot.client.applicationClientID}&scope=bot&permissions=$permissions" }
    }

    private object ExitCodes {
        const val EXIT_CODE_NORMAL: Int = 0
        const val EXIT_CODE_UPDATE: Int = 20
        const val EXIT_CODE_RESTART: Int = 21
    }
}
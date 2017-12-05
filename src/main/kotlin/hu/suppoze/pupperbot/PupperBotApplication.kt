package hu.suppoze.pupperbot

import com.github.salomonbrys.kodein.instance
import hu.suppoze.pupperbot.common.TokenProvider
import hu.suppoze.pupperbot.di.kodein
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import mu.KotlinLogging
import net.dv8tion.jda.core.Permission
import java.io.FileReader
import java.util.*

private val logger = KotlinLogging.logger {}

object PupperBotApplication {

    private val pupperBot: PupperBot by kodein.instance()
    private val consoleScanner: Scanner by lazy { Scanner(System.`in`) }

    @JvmStatic
    fun main(args: Array<String>) {
        val reader = FileReader("./token.txt")
        val token = reader.readText()

        TokenProvider.token = token

        pupperBot.init()

        logInviteLink()
    }

    fun listenForCommand() = launch(CommonPool) {
        while (isActive) {
            logger.info { "Listening for user input on System.in" }
            val command = consoleScanner.next() ?: continue

            when (command) {
                "logout" -> logoutAndShutdown(command, ExitCodes.EXIT_CODE_NORMAL)
                "restart" -> logoutAndShutdown(command, ExitCodes.EXIT_CODE_RESTART)
                "update" -> logoutAndShutdown(command, ExitCodes.EXIT_CODE_UPDATE)
                else -> logger.trace { "Unrecognized command." }
            }
        }
    }

    private fun logoutAndShutdown(command: String, exitCode: Int) {
        logger.info { "command=$command msg='Shutting down with ExitCode: $exitCode'" }
        pupperBot.api.shutdownNow()
        System.exit(exitCode)
    }

    private fun logInviteLink() {
        val inviteUrl = pupperBot.api.asBot().getInviteUrl(
                listOf(
                        Permission.MESSAGE_WRITE,
                        Permission.MESSAGE_READ,
                        Permission.MESSAGE_MANAGE))
        logger.info { "Invite link: $inviteUrl" }
    }

    private object ExitCodes {
        const val EXIT_CODE_NORMAL: Int = 0
        const val EXIT_CODE_UPDATE: Int = 20
        const val EXIT_CODE_RESTART: Int = 21
    }
}
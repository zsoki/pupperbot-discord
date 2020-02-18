package hu.suppoze.pupperbot.app

import hu.suppoze.pupperbot.app.command.CommandLibrary
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import mu.KotlinLogging
import net.dv8tion.jda.api.Permission

import java.util.*
import java.util.concurrent.Executors
import kotlin.system.exitProcess

private val logger = KotlinLogging.logger {}

object PupperBotApplication {

    private val consoleScanner: Scanner = Scanner(System.`in`)

    lateinit var inviteUrl: String

    private lateinit var pupperBot: PupperBot

    @JvmStatic
    fun main(args: Array<String>) {
        val commandLibrary = CommandLibrary(pupperConfiguration)
        pupperBot = PupperBot(commandLibrary, pupperConfiguration.reactionCallbackCache)
        pupperBot.init()

        inviteUrl = logInviteLink()
    }

    fun listenForCommand() {
        GlobalScope.launch(Executors.newSingleThreadExecutor().asCoroutineDispatcher()) {
            while (isActive) {
                try {
                    logger.info { "Listening for user input on System.in" }
                    val command = consoleScanner.next() ?: continue

                    when (command) {
                        "logout" -> logoutAndShutdown(
                            command,
                            ExitCodes.EXIT_CODE_NORMAL
                        )
                        "restart" -> logoutAndShutdown(
                            command,
                            ExitCodes.EXIT_CODE_RESTART
                        )
                        "update" -> logoutAndShutdown(
                            command,
                            ExitCodes.EXIT_CODE_UPDATE
                        )
                        else -> logger.trace { "Unrecognized command." }
                    }
                } catch (e: Exception) {
                    println("Exception during console command: ${e.message}")
                }
            }
        }
    }

    private fun logoutAndShutdown(command: String, exitCode: Int) {
        logger.info { "command=$command msg='Shutting down with ExitCode: $exitCode'" }
        pupperBot.api.shutdownNow()
        exitProcess(exitCode)
    }

    private fun logInviteLink(): String {
        val inviteUrl = pupperBot.api.getInviteUrl(
            listOf(
                Permission.MESSAGE_WRITE,
                Permission.MESSAGE_READ,
                Permission.MESSAGE_MANAGE,
                Permission.MESSAGE_HISTORY
            )
        )
        logger.info { "Invite link created: $inviteUrl" }
        return inviteUrl
    }

    private object ExitCodes {
        const val EXIT_CODE_NORMAL: Int = 0
        const val EXIT_CODE_UPDATE: Int = 20
        const val EXIT_CODE_RESTART: Int = 21
    }
}
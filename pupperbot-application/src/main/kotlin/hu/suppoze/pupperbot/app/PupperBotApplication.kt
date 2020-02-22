package hu.suppoze.pupperbot.app

import com.jessecorbett.diskord.dsl.bot
import com.jessecorbett.diskord.dsl.command
import com.jessecorbett.diskord.dsl.commands
import kotlinx.coroutines.*
import mu.KotlinLogging

import java.util.*
import kotlin.system.exitProcess

private val logger = KotlinLogging.logger {}

fun main() {
    runBlocking {
        launch {
            bot(pupperConfiguration.token) {
                commands(";") {
                    command("help") {
                        reply("Work in progress.")
                    }
                }
            }
        }

        launch {
            listenForCommand()
        }
    }
}

private fun logoutAndShutdown(command: String, exitCode: Int) {
    logger.info { "command=$command msg='Shutting down with ExitCode: $exitCode'" }
//        pupperBot.api.shutdownNow()
    exitProcess(exitCode)
}

private val consoleScanner: Scanner = Scanner(System.`in`)

const val EXIT_CODE_NORMAL: Int = 0
const val EXIT_CODE_UPDATE: Int = 20
const val EXIT_CODE_RESTART: Int = 21

suspend fun listenForCommand() {
        while (true) {
            try {
                delay(2000)
                if (!consoleScanner.hasNext()) continue
                val command = consoleScanner.next() ?: continue

                when (command) {
                    "logout" -> logoutAndShutdown(
                        command,
                        EXIT_CODE_NORMAL
                    )
                    "restart" -> logoutAndShutdown(
                        command,
                        EXIT_CODE_RESTART
                    )
                    "update" -> logoutAndShutdown(
                        command,
                        EXIT_CODE_UPDATE
                    )
                    else -> logger.trace { "Unrecognized command." }
                }
            } catch (e: Exception) {
                println("Exception during console command: ${e.message}")
            }
        }

}

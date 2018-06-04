/*
 * MIT License
 *
 * Copyright (c) 2017 Frederik Ar. Mikkelsen
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package hu.suppoze.pupperbot.bootloader

import com.google.gson.Gson
import java.io.FileReader
import java.io.File

object BootLoader {

    private var config: BootLoaderConfig? = null
    private var lastBoot: Long = 0
    private var recentBoots: Int = 0

    @JvmStatic fun main(args: Array<String>) {

        OUTER@ while (true) {

            val reader = FileReader("bootloader.json")
            config = Gson().fromJson(reader.readText(), BootLoaderConfig::class.java)
            reader.close()

            val process = boot()
            process.waitFor()
            println("[BOOTLOADER] Bot exited with code " + process.exitValue())

            when (process.exitValue()) {

                ExitCodes.EXIT_CODE_UPDATE -> {
                    println("[BOOTLOADER] Now updating...")
                    update()
                }

                130, ExitCodes.EXIT_CODE_NORMAL -> {
                    println("[BOOTLOADER] Now shutting down...")
                    break@OUTER
                }

                // SIGINT received or clean exit
                else -> println("[BOOTLOADER] Now restarting..")
            }

        }
    }

    private fun boot(): Process {

        // Check that we are not booting too quick (we could be stuck in a login loop)
        if (System.currentTimeMillis() - lastBoot > 3000 * 1000) {
            recentBoots = 0
        }

        recentBoots++
        lastBoot = System.currentTimeMillis()

        if (recentBoots >= 4) {
            println("[BOOTLOADER] Failed to restart 3 times, probably due to login errors. Exiting...")
            System.exit(-1)
        }

        // ProcessBuilder pb = new ProcessBuilder(System.getProperty("java.home") + "/bin/java -jar "+new File("FredBoat-1.0.jar").getAbsolutePath())
        val pb = ProcessBuilder().inheritIO()
        pb.command(config?.command)

        return pb.start()
    }

    private fun update() {

        // The main program has already prepared the shaded jar. We just need to replace the jars.
        val oldJar = File("./" + config?.jarName)
        if (oldJar.exists()) oldJar.delete()

        val newJar = File("./update/target/" + config?.jarName)
        newJar.renameTo(oldJar)

        // Now clean up the workspace
        val deleted = File("./update").delete()
        println("[BOOTLOADER] Updated. Update dir deleted: " + deleted)
    }

    private object ExitCodes {
        const val EXIT_CODE_NORMAL: Int = 0
        const val EXIT_CODE_UPDATE: Int = 20
        const val EXIT_CODE_RESTART: Int = 21
    }

    private data class BootLoaderConfig(val command: MutableList<String>, val jarName: String)
}
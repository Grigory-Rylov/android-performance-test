package com.github.grishberg.performance.command

import com.github.grishberg.tests.ConnectedDeviceWrapper
import com.github.grishberg.tests.common.RunnerLogger

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.file.Path
import java.nio.file.Paths

class AssembleCommand(private val logger: RunnerLogger) : LauncherCommand {

    override fun execute(device: ConnectedDeviceWrapper) {
        val processBuilder = ProcessBuilder()

        // -- Linux --

        // Run a shell command
        //processBuilder.command("bash", "-c", "sh assemble.sh");

        val currentRelativePath = Paths.get("").toAbsolutePath()
        // Run a shell script
        processBuilder.command(currentRelativePath.toString() + "/env/assemble.sh")

        // -- Windows --

        // Run a command
        //processBuilder.command("cmd.exe", "/c", "dir C:\\Users\\mkyong");

        // Run a bat file
        //processBuilder.command("C:\\Users\\mkyong\\hello.bat");

        try {
            val process = processBuilder.start()
            val reader = BufferedReader(
                    InputStreamReader(process.inputStream))

            var line: String?
            while (true) {
                line = reader.readLine()
                if (line == null) {
                    break
                }
                logger.i(TAG, "\tgradle: $line")
            }

            val exitVal = process.waitFor()
            if (exitVal == 0) {
                logger.i(TAG, "Success!")
            } else {
                logger.e(TAG, "Error: read log.")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

    companion object {
        private val TAG = AssembleCommand::class.java.simpleName
    }
}

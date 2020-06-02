package com.github.grishberg.performance.command

import com.github.grishberg.performance.launcher.DeviceFacade
import com.github.grishberg.tests.ConnectedDeviceWrapper
import com.github.grishberg.tests.commands.CommandExecutionException
import com.github.grishberg.tests.common.RunnerLogger
import java.io.File
import java.util.Locale

private const val TAG = "InstallApkCommand"

class InstallApkCommand(
        private val logger: RunnerLogger,
        private val apkFile: File
) : LauncherCommand {

    override fun execute(device: DeviceFacade) {
        var lastException: Throwable? = null
        for (i in 0..2) {
            try {
                val extraArgument = ""
                logger.i(TAG, "InstallApkCommand: install file {}", apkFile.getName())
                device.installPackage(apkFile.absolutePath, true, extraArgument)
                break
            } catch (e: Throwable) {
                logger.e(TAG, "InstallApkCommand: ", e)
                lastException = e
            }

        }
        if (lastException != null) {
            throw CommandExecutionException(String.format(Locale.US,
                    "Exception while install app apk on device [%s]",
                    device.serialNumber), lastException)
        }
    }
}

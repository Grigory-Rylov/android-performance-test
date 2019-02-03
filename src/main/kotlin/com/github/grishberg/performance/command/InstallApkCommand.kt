package com.github.grishberg.performance.command

import com.github.grishberg.tests.ConnectedDeviceWrapper
import com.github.grishberg.tests.commands.DeviceCommandResult
import com.github.grishberg.tests.commands.ExecuteCommandException
import com.github.grishberg.tests.common.RunnerLogger
import java.io.File
import java.util.*

private const val TAG = "InstallApkCommand"
private const val PATH_TO_APK = "env/Performeter/app/build/outputs/apk/release/app-release.apk"

class InstallApkCommand(
        private val logger: RunnerLogger
) : LauncherCommand {
    private val apkFile = File(PATH_TO_APK)

    override fun execute(device: ConnectedDeviceWrapper) {
        var lastException: Exception? = null
        for (i in 0..2) {
            try {
                val extraArgument = ""
                logger.i(TAG, "InstallApkCommand: install file {}", apkFile.getName())
                device.installPackage(apkFile.absolutePath, true, extraArgument)
                break
            } catch (e: Exception) {
                logger.e(TAG, "InstallApkCommand: ", e)
                lastException = e
            }

        }
        if (lastException != null) {
            throw ExecuteCommandException(String.format(Locale.US,
                    "Exception while install app apk on device [%s]",
                    device.serialNumber), lastException)
        }
    }
}
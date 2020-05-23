package com.github.grishberg.performance.command

import com.github.grishberg.tests.ConnectedDeviceWrapper
import com.github.grishberg.tests.commands.CommandExecutionException
import com.github.grishberg.tests.common.RunnerLogger
import java.io.File
import java.util.Locale

private const val TAG = "InstallApkCommand"
private const val PATH_TO_APK = "env/android-performeter-sample/app/build/outputs/apk/release/app-release.apk"

class InstallApkCommand(
        private val logger: RunnerLogger,
        apkPath: String = PATH_TO_APK
) : LauncherCommand {
    private val apkFile = File(apkPath)

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
            throw CommandExecutionException(String.format(Locale.US,
                    "Exception while install app apk on device [%s]",
                    device.serialNumber), lastException)
        }
    }
}

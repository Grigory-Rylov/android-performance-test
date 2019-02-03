package com.github.grishberg.performance.command

import com.github.grishberg.tests.ConnectedDeviceWrapper

private const val START_APK_COMMAND = "am start -n com.grishberg.performeter/com.grishberg.performeter.MainActivity --es \"mode\" \"%s\""

/**
 * Starts main activity.
 */
class StartActivityCommand(
        private val mode: String
) : LauncherCommand {
    override fun execute(device: ConnectedDeviceWrapper) {
        val mode = ""
        device.executeShellCommand(String.format(START_APK_COMMAND, mode))

    }
}
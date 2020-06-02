package com.github.grishberg.performance.command

import com.github.grishberg.performance.launcher.DeviceFacade

private const val START_APK_COMMAND = "am start -n com.grishberg.performeter/com.grishberg.performeter.MainActivity " +
        "--es \"mode\" \"%s\" --ei \"number\" %d --ei \"iterations\" %d"

/**
 * Starts main activity.
 */
class StartActivityWithParametersCommand(
        private val expNumber: Int,
        private val mode: String,
        private val iterations: Int
) : LauncherCommand {
    override fun execute(device: DeviceFacade) {
        device.executeShellCommand(String.format(START_APK_COMMAND, mode, expNumber, iterations))
    }
}

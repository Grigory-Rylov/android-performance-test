package com.github.grishberg.performance.command

import com.github.grishberg.tests.ConnectedDeviceWrapper
import com.github.grishberg.tests.common.RunnerLogger

private const val ADB_COMMAND = "am force-stop com.grishberg.performeter"
private const val TAG = "KillAppCommand"

class KillAppCommand(
        private val logger: RunnerLogger
) : LauncherCommand {
    override fun execute(device: ConnectedDeviceWrapper) {
        logger.i(TAG, "stop app")
        device.executeShellCommand(ADB_COMMAND)
    }
}
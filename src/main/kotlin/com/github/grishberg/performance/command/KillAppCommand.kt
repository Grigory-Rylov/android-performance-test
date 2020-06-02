package com.github.grishberg.performance.command

import com.github.grishberg.performance.launcher.DeviceFacade
import com.github.grishberg.tests.common.RunnerLogger

private const val ADB_COMMAND = "am force-stop "
private const val TAG = "KillAppCommand"

class KillAppCommand(
        private val logger: RunnerLogger,
        private val appId: String
) : LauncherCommand {
    override fun execute(device: DeviceFacade) {
        logger.i(TAG, "stop $appId")
        device.executeShellCommand(ADB_COMMAND + appId)
    }

    override fun toString(): String {
        return "KillAppCommand[$appId]"
    }
}
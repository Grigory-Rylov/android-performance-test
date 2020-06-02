package com.github.grishberg.performance.command

import com.github.grishberg.performance.launcher.DeviceFacade
import com.github.grishberg.tests.ConnectedDeviceWrapper
import com.github.grishberg.tests.common.RunnerLogger

private const val TAG = "ClearLogcatCommand"
private const val CLEAR_LOGCAT_COMMAND = "logcat -c"

class ClearLogcatCommand(
        private val logger: RunnerLogger
) : LauncherCommand {
    override fun execute(device: DeviceFacade) {
        //logger.i(TAG, "clear logcat")
        //device.executeShellCommand(CLEAR_LOGCAT_COMMAND)
        //Thread.sleep(200)
        //device.executeShellCommand(CLEAR_LOGCAT_COMMAND)
        //Thread.sleep(200)
    }
}

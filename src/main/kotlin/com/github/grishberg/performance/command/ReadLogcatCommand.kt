package com.github.grishberg.performance.command

import com.github.grishberg.performance.command.logcat.LogcatParser
import com.github.grishberg.performance.launcher.DeviceFacade
import com.github.grishberg.tests.ConnectedDeviceWrapper
import com.github.grishberg.tests.common.RunnerLogger

private const val READ_LOGCAT_COMMAND = "logcat -d -s "
private const val CLEAR_LOGCAT_COMMAND = "logcat -c"
private const val TAG = "ReadLogcatCommand"

/**
 * Reads logcat and parses duration result.
 */
class ReadLogcatCommand(
        private val logger: RunnerLogger,
        private val logcatParser: LogcatParser,
        private val logcatTagFilter: String,
        private val timeoutIntSeconds: Int
) : LauncherCommand {

    override fun execute(device: DeviceFacade) {
        logger.i(TAG, "read logcat..")
        for (i in 0 until timeoutIntSeconds) {
            val logcatOutput = device.executeShellCommandAndReturnOutput(READ_LOGCAT_COMMAND + logcatTagFilter)
            if (logcatParser.processLogcatLine(logcatOutput, device)) {
                break
            }

            Thread.sleep(1000)
        }
        logger.i(TAG, "clear logcat")
        device.executeShellCommand(CLEAR_LOGCAT_COMMAND)
    }
}

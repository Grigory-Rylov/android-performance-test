package com.github.grishberg.performance.command

import com.github.grishberg.performance.ResultsPrinter
import com.github.grishberg.tests.ConnectedDeviceWrapper
import com.github.grishberg.tests.common.RunnerLogger

private const val READ_LOGCAT_COMMAND = "logcat -v threadtime -d"
private const val CLEAR_LOGCAT_COMMAND = "logcat -c"
private const val TAG = "ReadLogcatCommand"

/**
 * Reads logcat and parses duration result.
 */
class ReadLogcatCommand(
        private val expNumber: Int,
        private val resultsPrinter: ResultsPrinter,
        private val logger: RunnerLogger
) : LauncherCommand {

    override fun execute(device: ConnectedDeviceWrapper) {
        logger.i(TAG, "read logcat..")
        val regex = "\\[PERF_$expNumber\\]\\:\\s\\(td=(\\d+), md=(\\d+)\\)".toRegex()
        for (i in 0 until 60 * 15) {
            val logcatOutput = device.executeShellCommandAndReturnOutput(READ_LOGCAT_COMMAND)
            val results = regex.find(logcatOutput)
            if (results != null) {
                val threadDuration = results.groupValues[1].toLong()
                val microDuration = results.groupValues[2].toLong()

                logger.i(TAG, "found result: run number = $expNumber [$device]:  $threadDuration $microDuration")
                resultsPrinter.populateResult(expNumber, device, threadDuration, microDuration)
                break
            }
            Thread.sleep(1000)
        }
        logger.i(TAG, "clear logcat")
        device.executeShellCommand(CLEAR_LOGCAT_COMMAND)
    }
}
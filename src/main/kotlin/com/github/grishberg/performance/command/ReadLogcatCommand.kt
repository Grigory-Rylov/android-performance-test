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
        private val resultsPrinter: ResultsPrinter,
        private val logger: RunnerLogger
) : LauncherCommand {

    override fun execute(device: ConnectedDeviceWrapper) {
        logger.i(TAG, "read logcat..")
        val regex = "\\[PERF\\]\\s+\\:\\s\\(d1=(\\d+), d2=(\\d+), td1=(\\d+), td2=(\\d+)\\)".toRegex()
        for (i in 0 until 100) {
            val logcatOutput = device.executeShellCommandAndReturnOutput(READ_LOGCAT_COMMAND)
            val results = regex.find(logcatOutput)
            if (results != null) {
                val duration1 = results.groupValues[1].toLong()
                val duration2 = results.groupValues[2].toLong()
                val threadDuration1 = results.groupValues[3].toLong()
                val threadDuration2 = results.groupValues[4].toLong()
                logger.i(TAG, "found result: [$device]:  $duration1 vs $duration2" +
                        " ($threadDuration1 vs $threadDuration2)")
                resultsPrinter.populateResult(device, duration1, threadDuration1, duration2, threadDuration2)
                break
            }
            Thread.sleep(1000)
        }
        logger.i(TAG, "clear logcat")
        device.executeShellCommand(CLEAR_LOGCAT_COMMAND)
    }
}
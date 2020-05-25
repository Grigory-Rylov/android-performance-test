package com.github.grishberg.performance.command.logcat

import com.github.grishberg.performance.ResultsPrinter
import com.github.grishberg.tests.ConnectedDeviceWrapper
import com.github.grishberg.tests.common.RunnerLogger

private const val TAG = "PerfLogcatReader"

class PerfLogcatReader(
        private val logger: RunnerLogger,
        private val expNumber: Int,
        private val resultsPrinter: ResultsPrinter

) : LogcatParser {
    private val regex = "\\[PERF_$expNumber\\]\\:\\s\\(td=(\\d+), md=(\\d+)\\)".toRegex()

    override fun processLogcatLine(logcatOutput: String, device: ConnectedDeviceWrapper): Boolean {
        val results = regex.find(logcatOutput)
        if (results != null) {
            val threadDuration = results.groupValues[1].toLong()
            val microDuration = results.groupValues[2].toLong()

            logger.i(TAG, "found result: run number = $expNumber [$device]:  $threadDuration $microDuration")
            resultsPrinter.populateResult(expNumber, device, threadDuration, microDuration)
            return true
        }
        return false
    }
}

package com.github.grishberg.performance.command.logcat

import com.github.grishberg.performance.aggregation.MeasurementAggregator
import com.github.grishberg.performance.data.MeasurementData
import com.github.grishberg.tests.ConnectedDeviceWrapper
import com.github.grishberg.tests.common.RunnerLogger

private const val TAG = "StartupTimeLogcatParser"

class StartupTimeLogcatParser(
        private val logger: RunnerLogger,
        private val resultsAggregator: MeasurementAggregator,
        logcatValuesRegexPattern: String
) : LogcatParser {
    private val regex = "(\\d+-\\d+\\s\\d+:\\d+:\\d+\\.\\d+)\\s+\\d+\\s+\\d+\\s+$logcatValuesRegexPattern".toRegex()
    private val data = mutableMapOf<String, MeasurementData>()
    private val processedTime = mutableSetOf<String>()

    override fun processLogcatLine(logcatOutput: String, device: ConnectedDeviceWrapper): Boolean {
        logger.d(TAG, logcatOutput)
        val lines = logcatOutput.split("\n")
        for (line in lines) {
            val results = regex.find(line)
            if (results != null) {
                val timeString = results.groupValues[1]
                val name = results.groupValues[2]
                val key = timeString + name
                if (processedTime.contains(key)) {
                    continue
                }
                processedTime.add(key)
                val threadDuration = results.groupValues[3].toLong()
                val nanoDuration = results.groupValues[4].toLong()
                data[name] = MeasurementData(threadDuration, nanoDuration)
                if (name == "MainActivity.onPreDraw") {
                    logger.d(TAG, "found $name, finish parsing.")
                    resultsAggregator.addResult(data)
                    return true
                }
            }
        }
        return false
    }
}

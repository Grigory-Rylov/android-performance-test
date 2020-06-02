package com.github.grishberg.performance.command.logcat

import com.github.grishberg.performance.aggregation.MeasurementAggregator
import com.github.grishberg.performance.data.MeasurementData
import com.github.grishberg.performance.launcher.DeviceFacade
import com.github.grishberg.tests.common.RunnerLogger

private const val TAG = "StartupTimeLogcatParser"

/**
 * [logcatValuesRegexPattern] used to parse metric name and metric value from logcat.
 * [firstStartStopWordParameterName] used to stop from first dry run.
 * [stopWordParameterName] used to find metric in session and stop listening logcat.
 */
class StartupTimeLogcatParser(
        private val logger: RunnerLogger,
        private val resultsAggregator: MeasurementAggregator,
        private val logcatValuesRegexPattern: String,
        private val stopWordParameterName: String,
        private val firstStartStopWordParameterName: String
) : LogcatParser {
    private val regex = "(\\d+-\\d+\\s\\d+:\\d+:\\d+\\.\\d+)\\s+\\d+\\s+\\d+\\s+$logcatValuesRegexPattern".toRegex()
    private val data = mutableMapOf<String, MeasurementData>()

    override fun processLogcatLine(lines: Array<out String>): Boolean {
        for (line in lines) {
            val results = regex.find(line)
            if (results != null) {
                val timeString = results.groupValues[1]
                val name = results.groupValues[2]
                val key = timeString + name
                if (name == firstStartStopWordParameterName) {
                    // first run
                    logger.d(TAG, "Exit from first run after install")
                    return true
                }
                val threadDuration = results.groupValues[3].toLong()
                val nanoDuration = if (results.groups.size > 4) {
                    results.groupValues[4].toLong()
                } else {
                    0
                }
                data[name] = MeasurementData(threadDuration, nanoDuration)
                if (name == stopWordParameterName) {
                    logger.d(TAG, "found $name, finish parsing.")
                    resultsAggregator.addResult(data)
                    return true
                }
            }
        }
        return false
    }

    override fun processLogcatLine(logcatOutput: String, device: DeviceFacade): Boolean {
        return false
    }
}

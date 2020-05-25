package com.github.grishberg.performance.aggregation

import com.github.grishberg.performance.data.MeasurementData

private const val APP_STARTUP = "App.onCreate"
private const val ACTIVITY_STARTUP = "MainActivity.onCreate"
private const val ACTIVITY_SET_CONTENT_VIEW = "MainActivity.setContentView"
private const val ACTIVITY_ON_RESUME = "MainActivity.onResume"
private const val ACTIVITY_ON_PRE_DRAW = "MainActivity.onPreDraw"

class AverageAggregator(
        override val measurementName: String,
        private val measurementCount: Int
) : MeasurementAggregator {
    private var count = 0
    private var values = mutableMapOf<String, MeasurementData>()
    override val average: Map<String, MeasurementData>
        get() = averageValues()

    private fun averageValues(): Map<String, MeasurementData> {
        var result = mutableMapOf<String, MeasurementData>()
        for (data in values) {
            val currentData = values.getOrDefault(data.key, MeasurementData(0, 0))
            val countAsDouble = count.toDouble()
            result[data.key] = MeasurementData(currentData.threadTime / countAsDouble, currentData.nanoDuration / countAsDouble)
        }
        return result
    }

    override fun addResult(result: Map<String, MeasurementData>) {
        if (count >= measurementCount) {
            throw IllegalStateException("Current measurement count $count is more than required $measurementCount") as Throwable

        }
        for (data in result) {
            val currentBucket = values.getOrDefault(data.key, MeasurementData(0, 0))
            values[data.key] = currentBucket.add(data.value)
        }
        count++
    }
}

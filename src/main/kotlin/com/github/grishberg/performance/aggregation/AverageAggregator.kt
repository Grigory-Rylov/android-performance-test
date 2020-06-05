package com.github.grishberg.performance.aggregation

import com.github.grishberg.performance.data.MeasurementData
import com.github.grishberg.tests.common.RunnerLogger

private const val TAG = "AverageAggregator"

class AverageAggregator(
        private val logger: RunnerLogger,
        override val measurementName: String,
        private val measurementCount: Int
) : MeasurementAggregator {
    private var count = 0
    private val _values = mutableMapOf<String, MutableList<MeasurementData>>()

    private val _average = mutableMapOf<String, MeasurementData>()

    override val values: Map<String, List<MeasurementData>>
        get() = _values

    override val average: Map<String, MeasurementData>
        get() = averageValues()

    private fun averageValues(): Map<String, MeasurementData> {
        if (_average.isNotEmpty()) {
            return _average
        }

        for (currentValue in _values) {
            val valuesForKey = currentValue.value

            var averageValueForKey = valuesForKey.first()
            for (i in 1 until valuesForKey.size) {
                averageValueForKey = averageValueForKey.sum(valuesForKey[i])
            }

            _average[currentValue.key] = averageValueForKey.average(valuesForKey.size)
        }

        return _average
    }

    override fun addResult(result: Map<String, MeasurementData>) {
        _average.clear()
        if (count >= measurementCount) {
            throw IllegalStateException("Current measurement count $count is more than required $measurementCount")
        }
        for (entry in result) {
            val valuesForKey = _values.getOrPut(entry.key) { mutableListOf() }
            valuesForKey.add(entry.value)
        }
        count++
    }
}

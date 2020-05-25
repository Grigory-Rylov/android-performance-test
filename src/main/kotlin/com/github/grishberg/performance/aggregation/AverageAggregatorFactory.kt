package com.github.grishberg.performance.aggregation

class AverageAggregatorFactory(
        private val measurementCount: Int
) : AggregatorFactory {
    override fun createAggregator(measurementName: String): MeasurementAggregator {
        return AverageAggregator(measurementName, measurementCount)
    }
}

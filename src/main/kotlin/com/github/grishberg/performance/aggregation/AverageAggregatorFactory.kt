package com.github.grishberg.performance.aggregation

import com.github.grishberg.tests.common.RunnerLogger

class AverageAggregatorFactory(
        private val logger: RunnerLogger,
        private val measurementCount: Int
) : AggregatorFactory {
    override fun createAggregator(measurementName: String): MeasurementAggregator {
        return AverageAggregator(logger, measurementName, measurementCount)
    }
}

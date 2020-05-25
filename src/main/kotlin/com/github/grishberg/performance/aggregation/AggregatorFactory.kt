package com.github.grishberg.performance.aggregation

interface AggregatorFactory {
    fun createAggregator(measurementName: String) : MeasurementAggregator
}

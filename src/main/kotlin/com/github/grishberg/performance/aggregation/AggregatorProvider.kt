package com.github.grishberg.performance.aggregation

class AggregatorProvider(
        measurementName1: String,
        measurementName2: String,

        aggregatorFactory: AggregatorFactory
) {
    val aggregator1: MeasurementAggregator by lazy { aggregatorFactory.createAggregator(measurementName1) }
    val aggregator2: MeasurementAggregator by lazy { aggregatorFactory.createAggregator(measurementName2) }
}

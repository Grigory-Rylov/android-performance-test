package com.github.grishberg.performance.aggregation

import com.github.grishberg.performance.data.MeasurementData

/**
 * Aggregates data from measures.
 */
interface MeasurementAggregator {
    val measurementName: String
    val average: Map<String, MeasurementData>
    val values: Map<String, List<MeasurementData>>
    fun addResult(result: Map<String, MeasurementData>)
}

object EmptyMeasurementAggregator : MeasurementAggregator {
    override fun addResult(result: Map<String, MeasurementData>) = Unit
    override val measurementName: String
        get() = ""
    override val average: Map<String, MeasurementData>
        get() = emptyMap()

    override val values: Map<String, List<MeasurementData>>
        get() = emptyMap()
}

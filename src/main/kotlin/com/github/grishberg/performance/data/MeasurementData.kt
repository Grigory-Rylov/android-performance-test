package com.github.grishberg.performance.data

/**
 * Holds measurement of one event.
 * It can holds wall time, thread time, time from start, or time from previous event,
 * but for the one event.
 */
interface MeasurementData {
    /**
     * List of values.
     */
    val values: List<Double>
    /**
     * Description of [values], size must be equals to size of [values]
     */
    val valuesDescription: Array<String>

    fun sum(newData: MeasurementData): MeasurementData

    /**
     * Calculates average for
     */
    fun average(iterationsCount: Int): MeasurementData
}

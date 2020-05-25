package com.github.grishberg.performance.data

data class MeasurementData(val threadTime: Double,
                           val nanoDuration: Double) {
    constructor(threadTime: Long,
                nanoDuration: Long) : this(threadTime.toDouble(), nanoDuration.toDouble())

    fun add(newData: MeasurementData): MeasurementData {
        return MeasurementData(threadTime + newData.threadTime,
                nanoDuration + newData.nanoDuration)
    }
}

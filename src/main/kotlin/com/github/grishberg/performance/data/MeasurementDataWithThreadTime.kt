package com.github.grishberg.performance.data

class MeasurementDataWithThreadTime(val threadTime: Double,
                                    val nanoDuration: Double) : MeasurementData {
    constructor(threadTime: Long,
                nanoDuration: Long) : this(threadTime.toDouble(), nanoDuration.toDouble())

    override val values: List<Double> = listOf(threadTime, nanoDuration)

    override val valuesDescription: Array<String>
        get() = DESCRITPTIONS

    override fun sum(newData: MeasurementData): MeasurementData {
        if (newData.values.size != values.size) {
            throw IllegalStateException("Trying to sum wrong instance $newData")
        }

        return MeasurementDataWithThreadTime(threadTime + newData.values[0],
                nanoDuration + newData.values[1])
    }

    override fun average(iterationsCount: Int): MeasurementData {
        return MeasurementDataWithThreadTime(threadTime / iterationsCount.toDouble(),
                nanoDuration / iterationsCount.toDouble())
    }

    companion object {
        val DESCRITPTIONS = arrayOf("thread time", "wall time")
    }
}
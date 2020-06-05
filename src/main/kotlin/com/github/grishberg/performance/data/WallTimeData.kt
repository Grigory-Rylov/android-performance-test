package com.github.grishberg.performance.data

data class WallTimeData(val wallTimeInMs: Double) : MeasurementData {
    constructor(wallTimeInMs: Long) : this(wallTimeInMs.toDouble())

    override val values: List<Double> = listOf(wallTimeInMs)

    override val valuesDescription: Array<String>
        get() = DESCRIPTIONS


    override fun sum(newData: MeasurementData): MeasurementData {
        if (newData.values.size != values.size) {
            throw IllegalStateException("Trying to sum wrong instance $newData")
        }
        return WallTimeData(wallTimeInMs + newData.values[0])
    }

    override fun average(iterationsCount: Int): MeasurementData {
        return WallTimeData(wallTimeInMs / iterationsCount.toDouble())
    }

    companion object {
        private val DESCRIPTIONS = arrayOf("wall time")
    }
}
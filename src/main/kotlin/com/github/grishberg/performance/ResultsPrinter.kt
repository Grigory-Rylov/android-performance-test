package com.github.grishberg.performance

import com.github.grishberg.performance.data.MeasurementData
import com.github.grishberg.performance.launcher.DeviceFacade
import com.github.grishberg.tests.ConnectedDeviceWrapper

/**
 * Prints results.
 */
interface ResultsPrinter {
    fun populateResult(experimentNumber: Int,
                       device: DeviceFacade,
                       threadDuration: Long,
                       microDuration: Long) = Unit

    fun populateResult(device: ConnectedDeviceWrapper,
                       result: Map<String, MeasurementData>) = Unit

    fun results(): String
}

class ConsoleResultPrinter : ResultsPrinter {
    private var resultStrings = ""
    override fun populateResult(experimentNumber: Int,
                                device: DeviceFacade,
                                threadDuration: Long,
                                microDuration: Long) {
        resultStrings = "run $experimentNumber: device=${device.name} : thread: $threadDuration ," +
                " micro benchmark: $microDuration"
        println(resultStrings)
    }

    override fun results(): String = resultStrings
}

object EmptyResultsPrinter : ResultsPrinter {
    override fun results(): String = ""
}

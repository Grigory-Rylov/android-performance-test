package com.github.grishberg.performance

import com.github.grishberg.tests.ConnectedDeviceWrapper

/**
 * Prints results.
 */
interface ResultsPrinter {
    fun populateResult(experimentNumber: Int,
                       device: ConnectedDeviceWrapper,
                       threadDuration: Long,
                       microDuration: Long)

    fun results(): String
}

class ConsoleResultPrinter : ResultsPrinter {
    private var resultStrings = ""
    override fun populateResult(experimentNumber: Int,
                                device: ConnectedDeviceWrapper,
                                threadDuration: Long,
                                microDuration: Long) {
        resultStrings = "run $experimentNumber: device=${device.name} : thread: $threadDuration ," +
                " micro benchmark: $microDuration"
        println(resultStrings)
    }

    override fun results(): String = resultStrings
}
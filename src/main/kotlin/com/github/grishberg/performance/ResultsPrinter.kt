package com.github.grishberg.performance

import com.github.grishberg.tests.ConnectedDeviceWrapper

/**
 * Prints results.
 */
interface ResultsPrinter {
    fun populateResult(device: ConnectedDeviceWrapper,
                       duration1: Long,
                       threadDuration1: Long,
                       duration2: Long,
                       threadDuration2: Long)

    fun results(): String
}

class ConsoleResultPrinter : ResultsPrinter {
    private var resultStrings = ""
    override fun populateResult(device: ConnectedDeviceWrapper,
                                duration1: Long, threadDuration1: Long,
                                duration2: Long, threadDuration2: Long) {
        resultStrings = "${device.name} : $duration1 vs $duration2" +
                " (thread: $threadDuration1 vs $threadDuration2)"
        println(resultStrings)
    }

    override fun results(): String = resultStrings
}
package com.github.grishberg.performance.command.logcat

import com.github.grishberg.performance.aggregation.MeasurementAggregator
import com.github.grishberg.performance.data.MeasurementData
import com.github.grishberg.tests.ConnectedDeviceWrapper
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

internal class StartupTimeLogcatParserTest {

    private val device = mock<ConnectedDeviceWrapper>()
    private val resultsCaptor = argumentCaptor<Map<String, MeasurementData>>()
    private val measurementAggregator = mock<MeasurementAggregator>()
    private val underTest = StartupTimeLogcatParser(mock(), measurementAggregator, "E PERF\\s+\\:\\s\\{name='(\\S+)', td=(\\d+), md=(\\d+)\\}")

    @Test
    fun `correctly parse all data`() {
        val result = underTest.processLogcatLine(TEST_OUTPUT, device)
        assertTrue(result)
    }

    @Test
    fun `correctly parse all data with previous values`() {
        underTest.processLogcatLine(TEST_OUTPUT, device)
        reset(measurementAggregator)

        underTest.processLogcatLine(TEST_OUTPUT_WITH_WRONG, device)
        verify(measurementAggregator).addResult(resultsCaptor.capture())

        assertEquals(73018.0, resultsCaptor.firstValue.getValue("App.onCreate").nanoDuration, 0.001)
        assertEquals(9.0, resultsCaptor.firstValue.getValue("MainActivity.onCreate").threadTime, 0.001)
        assertEquals(20.0, resultsCaptor.firstValue.getValue("MainActivity.setContentView").threadTime, 0.001)
        assertEquals(2.0, resultsCaptor.firstValue.getValue("MainActivity.onResume").threadTime, 0.001)
        assertEquals(10.0, resultsCaptor.firstValue.getValue("MainActivity.onPreDraw").threadTime, 0.001)
    }

    companion object {
        const val TEST_OUTPUT =
                "05-23 18:40:46.707 10432 10432 E PERF    : {name='App.onCreate', td=0, md=242000}\n" +
                        "05-23 18:40:46.727 10432 10432 E PERF    : {name='MainActivity.onCreate', td=13, md=20177000}\n" +
                        "05-23 18:40:46.771 10432 10432 E PERF    : {name='MainActivity.setContentView', td=33, md=44350000}\n" +
                        "05-23 18:40:46.776 10432 10432 E PERF    : {name='MainActivity.onResume', td=3, md=4815000}\n" +
                        "05-23 18:40:46.865 10432 10432 E PERF    : {name='MainActivity.onPreDraw', td=13, md=89073000}"

        const val TEST_OUTPUT_WITH_WRONG = TEST_OUTPUT + "\n" +
                "05-23 22:52:35.551  5593  5593 E PERF    : {name='App.onCreate', td=0, md=73018}\n" +
                "05-23 22:52:35.566  5593  5593 E PERF    : {name='MainActivity.onCreate', td=9, md=14347225}\n" +
                "05-23 22:52:35.593  5593  5593 E PERF    : {name='MainActivity.setContentView', td=20, md=27829049}\n" +
                "05-23 22:52:35.596  5593  5593 E PERF    : {name='MainActivity.onResume', td=2, md=2658244}\n" +
                "05-23 22:52:35.648  5593  5593 E PERF    : {name='MainActivity.onPreDraw', td=10, md=52105946}"
    }
}

package com.github.grishberg.performance.report

import com.github.grishberg.performance.aggregation.MeasurementAggregator
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.InputStreamReader
import java.util.stream.Collectors


class ChartsReportBuilder(
        private val deviceName: String,
        private val results1: MeasurementAggregator,
        private val results2: MeasurementAggregator
) {

    private val reportTemplate = getResourceFileAsString("html_template.html")
    private val chartTemplate = getResourceFileAsString("chart_template.html")

    fun build(namePrefix: String, tableResult: String) {
        val charts = StringBuilder()
        var chartIndex = 0
        for (data in results1.values) {
            val values1 = data.value.map { it.values[0] }.joinToString()

            val xAxis = createXAxis(data.value.size)

            var chartBlock = chartTemplate.replace("%NAME%", data.key)
            chartBlock = chartBlock.replace("%XAXIS%", xAxis)
            chartBlock = chartBlock.replace("%CHARTINDEX%", "$chartIndex")
            chartBlock = chartBlock.replace("%SERIES1%", values1)
            chartBlock = chartBlock.replace("%MEASURENAME1%", results1.measurementName)
            chartBlock = chartBlock.replace("%MEASURENAME2%", results2.measurementName)

            val values2 = results2.values[data.key]
            if (values2 != null) {
                chartBlock = chartBlock.replace("%SERIES2%", values2.map{it.values[0]}.joinToString())
            } else {
                chartBlock = chartBlock.replace("%SERIES2%", "0")
            }

            charts.append(chartBlock)
            charts.append("\n")
            chartIndex++
        }
        val withTable = reportTemplate.replace("%TABLE%", tableResult)
        val reportAsString = withTable.replace("%CHARTS%", charts.toString())
        writeToFile(reportAsString, namePrefix)
    }

    private fun createXAxis(size: Int): String {
        val sb = StringBuilder()
        for (i in 0 until size) {
            sb.append("${(i + 1)}")
            if (i < size - 1) {
                sb.append(", ")
            }
        }
        return sb.toString()
    }

    private fun writeToFile(str: String, namePrefix: String) {
        val dir = File("reports")
        if (!dir.exists()) {
            dir.mkdir()
        }
        val fn = "charts_${namePrefix}_$deviceName.html"
        val writer = BufferedWriter(FileWriter(File(dir, fn)))
        writer.write(str)
        writer.close()
    }

    @Throws(IOException::class)
    private fun getResourceFileAsString(fileName: String): String {
        val classLoader = ClassLoader.getSystemClassLoader()
        classLoader.getResourceAsStream(fileName).use { stream ->
            if (stream == null) return ""
            InputStreamReader(stream).use { isr -> BufferedReader(isr).use { reader -> return reader.lines().collect(Collectors.joining(System.lineSeparator())) } }
        }
    }
}
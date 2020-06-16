package com.github.grishberg.performance.report

import com.github.grishberg.performance.aggregation.MeasurementAggregator
import com.github.grishberg.tests.common.RunnerLogger
import org.apache.commons.math3.stat.inference.MannWhitneyUTest
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

private const val TAG = "CompareHtmlReport"

class CompareHtmlReport(
        private val logger: RunnerLogger,
        private val deviceName: String,
        private val measurementCount: Int,
        private val results1: MeasurementAggregator,
        private val results2: MeasurementAggregator
) : Reporter {
    private val chartsBuilder = ChartsReportBuilder(deviceName, results1, results2)

    override fun buildReport() {
        val sb = StringBuilder()
        sb.append("device: $deviceName <br/>")
        sb.append("<table cellpadding=\"4\">")

        buildHeader(sb)

        val average1 = results1.average
        val average2 = results2.average
        for (data in average1) {
            // TODO make for-cycle for each data.values
            var valueIndex = 0
            val time = data.value.values[valueIndex]
            val values1 = results1.values.getValue(data.key).map { it.values[valueIndex] }

            val values2: List<Double>
            val time2: Double
            val measurementData2 = average2[data.key]
            if (measurementData2 == null) {
                logger.e(TAG, "No data for second run key=${data.key}")
                // create dump data
                values2 = emptyList()
                time2 = 0.0
            } else {
                values2 = results2.values.getValue(data.key).map { it.values[valueIndex] }
                time2 = measurementData2.values[valueIndex]
            }

            val pValue = if (values1.isNotEmpty() && values2.isNotEmpty()) {
                MannWhitneyUTest().mannWhitneyUTest(values1.toDoubleArray(), values2.toDoubleArray())
            } else {
                1.0
            }

            val timeDiff = abs(time - time2)
            val timeDiffInPercent = (abs(time - time2) / max(time, time2) * 100.0).roundToInt()

            val timeColor1 = "black"
            val timeColor2 = color(time, time2)

            sb.append("<tr>")

            sb.append("<td align=\"left\" style=\"background-color:#AAAAAA;color:black;\">")
            sb.append(data.key)
            sb.append("</td>")

            sb.append("<td style=\"color:$timeColor1;\">")
            sb.append("%.2f".format(time))
            sb.append("</td>")

            sb.append("<td style=\"color:$timeColor2;\">")
            sb.append("%.2f".format(time2))
            sb.append("</td>")

            sb.append("<td style=\"color:$timeColor2;\">")
            sb.append("%.2f".format(timeDiff))
            sb.append("</td>")

            sb.append("<td style=\"color:$timeColor2;\">")
            sb.append("$timeDiffInPercent")
            sb.append("</td>")

            sb.append("<td style=\"color:${getPValueColor(pValue)};\">")
            sb.append("%.8f".format(pValue))
            sb.append("</td>")

            sb.append("</tr>\n")
        }
        sb.append("</table>")
        writeToFile(sb.toString())
    }

    private fun color(v1: Double, v2: Double): String {
        if (v2 == v1) {
            return "black"
        }
        return if (v2 < v1) {
            "green"
        } else {
            "red"
        }
    }

    private fun getPValueColor(pValue: Double): String {
        if (pValue < 0.0001) {
            return "green"
        }
        if (pValue < 0.02) {
            return "brown"
        }
        return "red"
    }

    private fun buildHeader(sb: StringBuilder) {
        sb.append("<tr style=\"background-color:yellowgreen;color:white;\">")
        sb.append("<td>Metric name, mesurements count: $measurementCount</td>")
        sb.append("<td>${results1.measurementName}</td>")
        sb.append("<td>${results2.measurementName}</td>")
        sb.append("<td>diff</td>")
        sb.append("<td>diff in percents</td>")
        sb.append("<td>p-value</td>")
        sb.append("</tr>")
    }

    private fun writeToFile(table: String) {
        val dir = File("reports")
        if (!dir.exists()) {
            dir.mkdir()
        }
        val date = Date()
        val formattedDate = SimpleDateFormat("yyyyMMdd_HH-mm-ss").format(date)
        chartsBuilder.build(formattedDate, table)
    }
}

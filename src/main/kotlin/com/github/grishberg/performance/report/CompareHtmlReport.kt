package com.github.grishberg.performance.report

import com.github.grishberg.performance.aggregation.MeasurementAggregator
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.lang.Math.abs
import java.lang.Math.max
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.roundToInt

class CompareHtmlReport(
        private val deviceName: String,
        private val measurementCount: Int,
        private val results1: MeasurementAggregator,
        private val results2: MeasurementAggregator
) : Reporter {
    override fun buildReport() {
        val sb = StringBuilder()
        sb.append("<html>")
        sb.append("device: $deviceName <br/>")
        sb.append("<table cellpadding=\"4\">")

        buildHeader(sb)

        val average1 = results1.average
        val average2 = results2.average
        for (data in average1) {

            val threadTime1 = data.value.threadTime

            val threadTime2 = average2.getValue(data.key).threadTime
            val threadDiff = abs(threadTime1 - threadTime2)
            val threadDiffInPercent = (abs(threadTime1 - threadTime2) / max(threadTime1, threadTime2) * 100.0).roundToInt()

            val threadTimeColor1 = "black"
            val threadTimeColor2 = color(threadTime1, threadTime2)

            val nanoDuration1 = data.value.nanoDuration / 1000.0
            val nanoDuration2 = average2.getValue(data.key).nanoDuration / 1000.0
            val globalDiff = abs(nanoDuration1 - nanoDuration2)
            val globalDiffInPercent = (abs(nanoDuration1 - nanoDuration2) / max(nanoDuration1, nanoDuration2) * 100.0).roundToInt()

            val globalTimeColor1 = "black"
            val globalTimeColor2 = color(nanoDuration1, nanoDuration2)

            sb.append("<tr>")

            sb.append("<td align=\"left\" style=\"background-color:#AAAAAA;color:black;\">")
            sb.append("${data.key} thread")
            sb.append("</td>")

            sb.append("<td style=\"color:$threadTimeColor1;\">")
            sb.append("%.2f".format(threadTime1))
            sb.append("</td>")

            sb.append("<td style=\"color:$threadTimeColor2;\">")
            sb.append("%.2f".format(threadTime2))
            sb.append("</td>")

            sb.append("<td style=\"color:$threadTimeColor2;\">")
            sb.append("%.2f".format(threadDiff))
            sb.append("</td>")

            sb.append("<td style=\"color:$threadTimeColor2;\">")
            sb.append("$threadDiffInPercent")
            sb.append("</td>")

            sb.append("</tr>\n")


            sb.append("<tr>")
            sb.append("<td align=\"left\" style=\"background-color:#AAAAAA;color:black;\">")
            sb.append("${data.key} global")
            sb.append("</td>")

            sb.append("<td style=\"color:$globalTimeColor1;\">")
            sb.append("%.2f".format(nanoDuration1))
            sb.append("</td>")

            sb.append("<td style=\"color:$globalTimeColor2;\">")
            sb.append("%.2f".format(nanoDuration2))

            sb.append("</td>")

            sb.append("<td style=\"color:$globalTimeColor2;\">")
            sb.append("%.2f".format(globalDiff))

            sb.append("</td>")

            sb.append("<td style=\"color:$globalTimeColor2;\">")
            sb.append("$globalDiffInPercent")
            sb.append("</td>")
            sb.append("</tr>\n")
        }
        sb.append("</table>")
        sb.append("</html>")

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

    private fun buildHeader(sb: StringBuilder) {
        sb.append("<tr style=\"background-color:yellowgreen;color:white;\">")
        sb.append("<td>Metric name, mesurements count: $measurementCount</td>")
        sb.append("<td>${results1.measurementName}</td>")
        sb.append("<td>${results2.measurementName}</td>")
        sb.append("<td>diff</td>")
        sb.append("<td>diff in percents</td>")
        sb.append("</tr>")
    }

    private fun writeToFile(str: String) {
        val dir = File("reports")
        if (!dir.exists()) {
            dir.mkdir()
        }
        val date = Date()
        val formattedDate = SimpleDateFormat("yyyyMMdd_HH-mm-ss").format(date)
        val fn = "results_${formattedDate}_$deviceName.html"
        val writer = BufferedWriter(FileWriter(File(dir, fn)))
        writer.write(str)
        writer.close()
    }

}

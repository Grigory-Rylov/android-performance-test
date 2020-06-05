package com.github.grishberg.performance.environments

import com.google.gson.GsonBuilder
import com.google.gson.stream.JsonReader
import java.io.BufferedWriter
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.FileReader
import java.io.IOException
import java.io.OutputStreamWriter

private const val CONFIGURATION_FILE = "config.json"

class Settings {
    private val gson = GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create()
    val configuration: Configuration

    init {
        try {
            val fileReader = FileReader(CONFIGURATION_FILE)
            val reader = JsonReader(fileReader)
            configuration = gson.fromJson(reader, Configuration::class.java)
        } catch (e: FileNotFoundException) {
            createEmptyFile()
            throw e
        }
    }

    private fun createEmptyFile() {
        var outputStream: FileOutputStream? = null
        val configuration = Configuration("com.github.grishberg.perf",
                "com.github.grishberg.perf.MainActivity",
                10,
                "base",
                "improvements",
                "apk1.apk",
                "apk2.apk",
                "\\S+\\s\\S+\\s\\S+\\s(\\S+)\\ [time:]*\\s*([-\\d]+)",
                "DryRunIsEnded",
                "LastParameterName",
                listOf(
                        "android.permission.ACCESS_FINE_LOCATION",
                        "android.permission.RECORD_AUDIO",
                        "android.permission.ACCESS_COARSE_LOCATION")
        )

        try {
            outputStream = FileOutputStream(CONFIGURATION_FILE)
            val bufferedWriter: BufferedWriter
            bufferedWriter = BufferedWriter(OutputStreamWriter(outputStream, "UTF-8"))
            gson.toJson(configuration, bufferedWriter)
            bufferedWriter.close()
            println("Created dump file $CONFIGURATION_FILE")
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush()
                    outputStream.close()
                } catch (e: IOException) {
                }
            }
        }

    }
}
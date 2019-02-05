package com.github.grishberg.performance

import com.github.grishberg.tests.common.RunnerLogger
import java.io.*
import java.nio.file.Files
import java.nio.file.Paths
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream


private const val TAG = "SourceFileSystem"
private const val SOURCE_DIR = "env/android-performeter-sample/app/src/main/java/com/grishberg/performeter/samples"

class SourceFileSystem(
        private val logger: RunnerLogger
) {
    private val classLoader: ClassLoader = javaClass.classLoader

    fun prepareTmpDirAndFiles() {
        File("tmp").mkdir()

        copyAppSrcFromResources()

        makeExecutable()
    }

    private fun copyAppSrcFromResources() {
        logger.i(TAG, "copy sources from resources...")

        val sourceStream = classLoader.getResourceAsStream("env.zip")
        unzip(sourceStream, "env")
        //FileUtils.copyDirectory(sourceStream, destinationFile)

        logger.i(TAG, "sources copied")
    }


    private fun unzip(zipFileStream: InputStream, destDir: String) {
        val dir = File(destDir)
        // create output directory if it doesn't exist
        if (!dir.exists()) dir.mkdirs()
        //buffer for read and write data to file
        val buffer = ByteArray(1024)

        val zis = ZipInputStream(zipFileStream)
        var ze: ZipEntry? = zis.nextEntry
        while (ze != null) {
            val fileName = ze.name
            val newFile = File(destDir + File.separator + fileName)
            //create directories for sub directories in zip
            File(newFile.parent).mkdirs()
            if (ze.isDirectory) {
                newFile.mkdirs()
                zis.closeEntry()
                ze = zis.nextEntry
                continue
            }
            val fos = FileOutputStream(newFile)
            var len: Int
            while (true) {
                len = zis.read(buffer)
                if (len <= 0) {
                    break
                }
                fos.write(buffer, 0, len)
            }
            fos.close()
            //close this ZipEntry
            zis.closeEntry()
            ze = zis.nextEntry
        }
        //close last ZipEntry
        zis.closeEntry()
        zis.close()
        zipFileStream.close()
    }

    private fun makeExecutable() {
        Runtime.getRuntime().exec("chmod u+x " + "env/assemble.sh")
        Runtime.getRuntime().exec("chmod u+x " + "env/android-performeter-sample/gradlew")
    }

    /**
     * Reads file from templates.
     */
    fun readTemplateFile(fileName: String): String {
        try {
            return String(Files.readAllBytes(Paths.get("$SOURCE_DIR/$fileName")))
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * Writes source to file.
     */
    fun writeSourceFile(fileName: String, source: String) {
        BufferedWriter(OutputStreamWriter(
                FileOutputStream("$SOURCE_DIR/$fileName"), "utf-8"))
                .use { writer -> writer.write(source) }
    }
}
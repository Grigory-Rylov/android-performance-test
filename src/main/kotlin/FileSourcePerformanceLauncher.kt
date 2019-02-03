import com.github.grishberg.performance.PerformanceLauncher
import java.nio.file.Files
import java.nio.file.Paths

private const val SRC_DIR = "in"

class FileSourcePerformanceLauncher(
        private val performanceLauncher: PerformanceLauncher
) {
    /**
     * reads sources from "in" folder and start performance test.
     */
    fun readSourcesAndLaunch() {
        val import1 = readSourceFile("imports1.kt")
        val import2 = readSourceFile("imports2.kt")
        val src1 = readSourceFile("source1.kt")
        val src2 = readSourceFile("source2.kt")

        performanceLauncher.measurePerformance(false, import1, src1,
                false, import2, src2)
    }

    private fun readSourceFile(fileName: String): String {
        return String(Files.readAllBytes(Paths.get("$SRC_DIR/$fileName")))
    }
}
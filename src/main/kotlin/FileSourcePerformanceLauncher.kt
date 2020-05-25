
import com.github.grishberg.performance.Language
import com.github.grishberg.performance.RunConfiguration
import com.github.grishberg.performance.launcher.PerformanceLauncher
import com.github.grishberg.performance.launcher.SourceCodeInfo
import com.github.grishberg.tests.common.RunnerLogger
import java.nio.file.Files
import java.nio.file.Paths

private const val SRC_DIR = "in"

class FileSourcePerformanceLauncher(
        logger: RunnerLogger,
        private val performanceLauncher: PerformanceLauncher
) {
    private val configuration = RunConfiguration(logger)
    /**
     * reads sources from "in" folder and start performance test.
     */
    fun readSourcesAndLaunch() {
        val import1 = readSourceFile("imports1.kt")
        val import2 = readSourceFile("imports2.kt")
        val src1 = readSourceFile("source1.kt")
        val src2 = readSourceFile("source2.kt")
        val souce1 = SourceCodeInfo(Language.KOTLIN, import1, "// fields", src1, "// init")
        val souce2 = SourceCodeInfo(Language.KOTLIN, import2, "// fields", src2, "// init")

    }

    private fun readSourceFile(fileName: String): String {
        return String(Files.readAllBytes(Paths.get("$SRC_DIR/$fileName")))
    }
}

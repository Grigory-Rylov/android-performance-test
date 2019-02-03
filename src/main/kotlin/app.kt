import com.github.grishberg.performance.ConsoleResultPrinter
import com.github.grishberg.performance.Log4JLogger
import com.github.grishberg.performance.PerformanceLauncher

fun main(args: Array<String>) {
    val resultsPrinter = ConsoleResultPrinter()
    val logger = Log4JLogger()
    val perfLauncher = PerformanceLauncher(resultsPrinter, logger)
    val launcher = FileSourcePerformanceLauncher(perfLauncher)
    launcher.readSourcesAndLaunch()
    System.exit(0)
    println("Done")
}
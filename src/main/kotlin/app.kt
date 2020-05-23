import com.github.grishberg.performance.ConsoleResultPrinter
import com.github.grishberg.performance.Log4JLogger
import com.github.grishberg.performance.launcher.ParallelPerformanceLauncher

fun main(args: Array<String>) {
    val resultsPrinter = ConsoleResultPrinter()
    val logger = Log4JLogger()

    val perfLauncher = ParallelPerformanceLauncher(resultsPrinter, logger)

    val launcher = FileSourcePerformanceLauncher(logger, perfLauncher)
    launcher.readSourcesAndLaunch()

    System.exit(0)
    println("Done")
}

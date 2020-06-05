import com.github.grishberg.performance.Log4JLogger
import com.github.grishberg.performance.RunConfiguration
import com.github.grishberg.performance.environments.CompareApkEnvironmentsFactory
import com.github.grishberg.performance.environments.Settings
import com.github.grishberg.performance.launcher.ParallelPerformanceLauncher


fun main(args: Array<String>) {
    val logger = Log4JLogger()
    compareCompiledApk(logger)

    System.exit(0)
    println("Done")
}

private fun compareCompiledApk(logger: Log4JLogger) {
    val settings = Settings()
    val envFactory = CompareApkEnvironmentsFactory(logger, settings.configuration)

    val launcher = ParallelPerformanceLauncher(logger, envFactory, "AppPerformanceStats")

    val configuration = RunConfiguration(logger)
    launcher.launchPerformance(configuration.adb)
}


import com.github.grishberg.performance.Log4JLogger
import com.github.grishberg.performance.RunConfiguration
import com.github.grishberg.performance.environments.CompareApkEnvironmentsFactory
import com.github.grishberg.performance.environments.EnvironmentData
import com.github.grishberg.performance.launcher.ParallelPerformanceLauncher
import java.io.File


fun main(args: Array<String>) {
    val logger = Log4JLogger()
    compareCompiledApk(logger)

    System.exit(0)
    println("Done")
}

private fun compareCompiledApk(logger: Log4JLogger) {
    val measurementCount = 10

    val envFactory = CompareApkEnvironmentsFactory(logger, measurementCount,
            "com.github.grishberg.performance",
            "com.github.grishberg.performance.MainActivity",
            EnvironmentData("base", File("<path_to_apk1>.apk")),
            EnvironmentData("improvements", File("<path_to_apk2>.apk")),
            "\\S+\\s\\S+\\s\\S+\\s(\\S+)\\ [time:]*\\s*([-\\d]+)",
            "lastParameterName",
            "dryRunLastParameterName"
    )

    val launcher = ParallelPerformanceLauncher(logger, envFactory, "AppPerformanceStats")

    val configuration = RunConfiguration(logger)
    launcher.launchPerformance(configuration.adb)
}


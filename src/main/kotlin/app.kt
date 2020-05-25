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
    val measurementCount = 50

    val envFactory = CompareApkEnvironmentsFactory(logger, measurementCount,
            "com.grishberg.performeter",
            "com.grishberg.performeter.MainActivity",
            EnvironmentData("debuggable release", File("in/app-release-debuggable.apk")),
            EnvironmentData("release", File("in/app-release-original.apk")),
            "E PERF\\s+\\:\\s\\{name='(\\S+)', td=(\\d+), md=(\\d+)\\}"
    )

    val launcher = ParallelPerformanceLauncher(logger, envFactory)

    val configuration = RunConfiguration(logger)
    launcher.launchPerformance(configuration.adb)
}


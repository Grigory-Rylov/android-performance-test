package com.github.grishberg.performance

import com.github.grishberg.performance.command.ClearLogcatCommand
import com.github.grishberg.performance.command.InstallApkCommand
import com.github.grishberg.performance.command.KillAppCommand
import com.github.grishberg.performance.command.LauncherCommand
import com.github.grishberg.performance.command.ReadLogcatCommand
import com.github.grishberg.performance.command.StartActivityWithParametersCommand
import com.github.grishberg.performance.command.logcat.PerfLogcatReader
import com.github.grishberg.performance.launcher.DeviceFacade
import com.github.grishberg.performance.launcher.SourceCodeInfo
import com.github.grishberg.tests.common.RunnerLogger
import java.io.File

private const val TAG = "CompareFromSourcesCommandsFactory"
private const val PATH_TO_APK = "env/android-performeter-sample/app/buildReport/outputs/apk/release/app-release.apk"

class CompareFromSourcesCommandsFactory(
        private val logger: RunnerLogger,
        private val resultsPrinter: ResultsPrinter,
        private val source1: SourceCodeInfo,
        private val source2: SourceCodeInfo,
        private val launchesCount: Int,
        private val iterationsPerLaunch: Int
) : Commands {

    override fun execute(device: DeviceFacade, logcatReader: LogcatReader) {
        val commands = ArrayList<LauncherCommand>()
        commands.add(InstallApkCommand(logger, File(PATH_TO_APK)))
        for (i in 0 until launchesCount) {
            commands.add(ClearLogcatCommand(logger))
            commands.add(StartActivityWithParametersCommand(0, generateModeForFirstRun(), iterationsPerLaunch))
            commands.add(ReadLogcatCommand(logger, PerfLogcatReader(logger, 0, resultsPrinter), "PERF", 15 * 60))
            commands.add(KillAppCommand(logger, "com.grishberg.performeter"))
            commands.add(StartActivityWithParametersCommand(1, generateModeForSecondRun(), iterationsPerLaunch))
            commands.add(ReadLogcatCommand(logger, PerfLogcatReader(logger, 1, resultsPrinter), "PERF", 15 * 60))
            commands.add(KillAppCommand(logger, "com.grishberg.performeter"))
        }
        commands.forEach {
            it.execute(device)
        }
    }

    private fun generateModeForFirstRun(): String = if (source1.language == Language.JAVA) "j1" else "k1"

    private fun generateModeForSecondRun(): String = if (source2.language == Language.JAVA) "j2" else "k2"
}

package com.github.grishberg.performance

import com.github.grishberg.performance.aggregation.AggregatorProvider
import com.github.grishberg.performance.aggregation.EmptyMeasurementAggregator
import com.github.grishberg.performance.command.ClearLogcatCommand
import com.github.grishberg.performance.command.CompositeCommand
import com.github.grishberg.performance.command.DeleteApkCommand
import com.github.grishberg.performance.command.InstallApkCommand
import com.github.grishberg.performance.command.KillAppCommand
import com.github.grishberg.performance.command.LauncherCommand
import com.github.grishberg.performance.command.ReadLogcatCommand
import com.github.grishberg.performance.command.SleepCommand
import com.github.grishberg.performance.command.StartActivityCommand
import com.github.grishberg.performance.command.logcat.LogcatParser
import com.github.grishberg.performance.command.logcat.StartupTimeLogcatParser
import com.github.grishberg.tests.ConnectedDeviceWrapper
import com.github.grishberg.tests.common.RunnerLogger
import java.io.File

/**
 * Deletes apk with [appId] if installed.
 * Installs apk placed in [firstApk].
 * Executes [measuresCount] times:
 *      starts activity [startActivityName]
 *      reads logs
 *      kills app.
 *
 * Installs apk placed in [secondApk].
 * Executes [measuresCount] times:
 *      starts activity [startActivityName]
 *      reads logs
 *      kills app.
 */
class CompareFromApkCommands(
        private val appId: String,
        private val startActivityName: String,
        private val firstApk: File,
        private val secondApk: File,
        private val logger: RunnerLogger,
        private val measuresCount: Int,
        private val aggregatorProvider: AggregatorProvider,
        private val logcatValuesPattern: String
) : Commands {
    override fun execute(device: ConnectedDeviceWrapper) {
        val commands = buildCommands()

        commands.forEach {
            it.execute(device)
        }
    }

    private fun buildCommands(): ArrayList<LauncherCommand> {
        val commands = ArrayList<LauncherCommand>()
        commands.add(ClearLogcatCommand(logger))
        commands.add(DeleteApkCommand(appId))
        commands.add(InstallApkCommand(logger, firstApk))
        commands.add(StartActivityCommand(appId, startActivityName))
        commands.add(ReadLogcatCommand(logger, StartupTimeLogcatParser(logger, EmptyMeasurementAggregator, logcatValuesPattern)))
        commands.add(SleepCommand(1))
        commands.add(ClearLogcatCommand(logger))
        commands.add(KillAppCommand(logger))

        val logcatParser = StartupTimeLogcatParser(logger, aggregatorProvider.aggregator1, logcatValuesPattern)
        for (i in 0 until measuresCount) {
            commands.add(firstAppMeasurementLoop(logcatParser))
        }

        commands.add(ClearLogcatCommand(logger))
        commands.add(DeleteApkCommand(appId))
        commands.add(InstallApkCommand(logger, secondApk))
        commands.add(StartActivityCommand(appId, startActivityName))
        commands.add(ReadLogcatCommand(logger, StartupTimeLogcatParser(logger, EmptyMeasurementAggregator, logcatValuesPattern)))
        commands.add(SleepCommand(1))
        commands.add(ClearLogcatCommand(logger))
        commands.add(KillAppCommand(logger))

        val logcatParser2 = StartupTimeLogcatParser(logger, aggregatorProvider.aggregator2, logcatValuesPattern)
        for (i in 0 until measuresCount) {
            commands.add(secondAppMeasurementLoop(logcatParser2))
        }
        return commands
    }

    private fun firstAppMeasurementLoop(logcatParser: LogcatParser): CompositeCommand {
        return CompositeCommand(listOf(
                ClearLogcatCommand(logger),
                StartActivityCommand(appId, startActivityName),
                ReadLogcatCommand(logger, logcatParser),
                SleepCommand(1),
                KillAppCommand(logger)
        ))
    }

    private fun secondAppMeasurementLoop(logcatParser: LogcatParser): CompositeCommand {
        return CompositeCommand(listOf(
                ClearLogcatCommand(logger),
                StartActivityCommand(appId, startActivityName),
                ReadLogcatCommand(logger, logcatParser),
                SleepCommand(1),
                KillAppCommand(logger)
        ))
    }
}

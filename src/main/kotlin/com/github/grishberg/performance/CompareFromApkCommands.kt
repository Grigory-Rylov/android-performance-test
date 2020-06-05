package com.github.grishberg.performance

import com.github.grishberg.performance.aggregation.AggregatorProvider
import com.github.grishberg.performance.aggregation.EmptyMeasurementAggregator
import com.github.grishberg.performance.command.CompositeCommand
import com.github.grishberg.performance.command.DeleteApkCommand
import com.github.grishberg.performance.command.GrantPermissionsCommand
import com.github.grishberg.performance.command.InstallApkCommand
import com.github.grishberg.performance.command.KillAppCommand
import com.github.grishberg.performance.command.LauncherCommand
import com.github.grishberg.performance.command.ReadLogcatFromReader
import com.github.grishberg.performance.command.SleepCommand
import com.github.grishberg.performance.command.StartActivityCommand
import com.github.grishberg.performance.command.logcat.LogcatParser
import com.github.grishberg.performance.command.logcat.StartupTimeLogcatParser
import com.github.grishberg.performance.launcher.DeviceFacade
import com.github.grishberg.tests.ConnectedDeviceWrapper
import com.github.grishberg.tests.common.RunnerLogger
import java.io.File

private const val TAG = "CFAC"

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
        private val logcatValuesPattern: String,
        private val stopWordParameterName: String,
        private val dryRunStopWordParameterName: String,
        private val permissions: List<String>
) : Commands {
    override fun execute(device: DeviceFacade, logcatReader: LogcatReader) {
        val commands = buildCommands(logcatReader)

        logger.d(TAG, "execute commands count: ${commands.size}")
        commands.forEach {
            logger.d(TAG, "execute $it")
            it.execute(device)
        }
    }

    private fun buildCommands(logcatReader: LogcatReader): ArrayList<LauncherCommand> {
        val commands = ArrayList<LauncherCommand>()
        commands.add(DeleteApkCommand(appId, logger))
        commands.add(InstallApkCommand(logger, firstApk))
        if (permissions.isNotEmpty()) {
            commands.add(GrantPermissionsCommand(appId, permissions))
        }
        commands.add(StartActivityCommand(appId, startActivityName))

        val firstRunlogcatParser1 = StartupTimeLogcatParser(logger,
                EmptyMeasurementAggregator,
                logcatValuesPattern,
                stopWordParameterName,
                dryRunStopWordParameterName)

        commands.add(ReadLogcatFromReader(logger, firstRunlogcatParser1, logcatReader))

        commands.add(SleepCommand(1))
        commands.add(KillAppCommand(logger, appId))

        val logcatParser = StartupTimeLogcatParser(logger,
                aggregatorProvider.aggregator1,
                logcatValuesPattern,
                stopWordParameterName,
                dryRunStopWordParameterName)

        for (i in 0 until measuresCount) {
            commands.add(firstAppMeasurementLoop(i, logcatParser, logcatReader))
        }

        commands.add(DeleteApkCommand(appId, logger))
        commands.add(InstallApkCommand(logger, secondApk))
        if (permissions.isNotEmpty()) {
            commands.add(GrantPermissionsCommand(appId, permissions))
        }
        commands.add(StartActivityCommand(appId, startActivityName))

        val firstStartLogcatParser2 = StartupTimeLogcatParser(logger,
                EmptyMeasurementAggregator,
                logcatValuesPattern,
                stopWordParameterName,
                dryRunStopWordParameterName)

        commands.add(ReadLogcatFromReader(logger, firstStartLogcatParser2, logcatReader))

        commands.add(SleepCommand(1))
        commands.add(KillAppCommand(logger, appId))

        val logcatParser3 = StartupTimeLogcatParser(logger,
                aggregatorProvider.aggregator2,
                logcatValuesPattern,
                stopWordParameterName,
                dryRunStopWordParameterName)

        for (i in 0 until measuresCount) {
            commands.add(secondAppMeasurementLoop(i, logcatParser3, logcatReader))
        }
        return commands
    }

    private fun firstAppMeasurementLoop(iteration: Int, logcatParser: LogcatParser, logcatReader: LogcatReader): CompositeCommand {
        return CompositeCommand(iteration, logger, listOf(
                StartActivityCommand(appId, startActivityName),
                ReadLogcatFromReader(logger, logcatParser, logcatReader),
                SleepCommand(1),
                KillAppCommand(logger, appId)
        ))
    }

    private fun secondAppMeasurementLoop(iteration: Int, logcatParser: LogcatParser, logcatReader: LogcatReader): CompositeCommand {
        return CompositeCommand(iteration, logger, listOf(
                StartActivityCommand(appId, startActivityName),
                ReadLogcatFromReader(logger, logcatParser, logcatReader),
                SleepCommand(1),
                KillAppCommand(logger, appId)
        ))
    }
}

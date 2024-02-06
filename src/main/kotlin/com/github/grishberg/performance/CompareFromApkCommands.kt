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
import com.github.grishberg.performance.command.StartActivityCustomAdbShellCommand
import com.github.grishberg.performance.command.logcat.LogcatParser
import com.github.grishberg.performance.command.logcat.StartupTimeLogcatParser
import com.github.grishberg.performance.launcher.DeviceFacade
import com.github.grishberg.tests.common.RunnerLogger
import java.io.File

private const val TAG = "CFAC"
private const val SLEEP_BEFORE_KILL_SECONDS = 1
private const val SLEEP_AFTER_KILL_SECONDS = 1

/**
 * Deletes apk with [appId] if installed.
 * Installs apk placed in [firstApk].
 * Executes [measurementCount] times:
 *      starts activity [startActivityName]
 *      reads logs
 *      kills app.
 *
 * Installs apk placed in [secondApk].
 * Executes [measurementCount] times:
 *      starts activity [startActivityName]
 *      reads logs
 *      kills app.
 */
class CompareFromApkCommands(
    private val appId: String,
    private val startActivityName: String,
    private val startActivityAdbShellCommand: String,
    private val shouldDeleteBeforeInstall: Boolean,
    private val firstApk: File,
    private val secondApk: File,
    private val logger: RunnerLogger,
    private val measurementCount: Int,
    private val aggregatorProvider: AggregatorProvider,
    private val logcatValuesPattern: String,
    private val stopWordParameterName: String,
    private val dryRunStopWordParameterName: String,
    private val permissions: List<String>
) : Commands {

    private val dryRunLogcatParser = StartupTimeLogcatParser(
        logger, EmptyMeasurementAggregator, logcatValuesPattern, stopWordParameterName, dryRunStopWordParameterName
    )

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

        // install first apk
        commands.addAll(installAndPrepareApk(firstApk, logcatReader))

        val logcatParser1 = StartupTimeLogcatParser(
            logger,
            aggregatorProvider.aggregator1,
            logcatValuesPattern,
            stopWordParameterName,
            dryRunStopWordParameterName
        )

        for (i in 0 until measurementCount) {
            commands.add(firstAppMeasurementLoop(i, logcatParser1, logcatReader))
        }

        commands.add(SleepCommand(60))
        // install second apk
        commands.addAll(installAndPrepareApk(secondApk, logcatReader))

        val logcatParser2 = StartupTimeLogcatParser(
            logger,
            aggregatorProvider.aggregator2,
            logcatValuesPattern,
            stopWordParameterName,
            dryRunStopWordParameterName
        )

        for (i in 0 until measurementCount) {
            commands.add(secondAppMeasurementLoop(i, logcatParser2, logcatReader))
        }
        return commands
    }

    private fun installAndPrepareApk(apk: File, logcatReader: LogcatReader): List<LauncherCommand> {
        val commands = mutableListOf<LauncherCommand>()

        if (shouldDeleteBeforeInstall) {
            commands.add(DeleteApkCommand(appId, logger))
        }
        commands.add(InstallApkCommand(logger, apk))
        if (permissions.isNotEmpty()) {
            commands.add(GrantPermissionsCommand(appId, permissions))
        }
        commands.add(startApkCommand())
        commands.add(ReadLogcatFromReader(logger, dryRunLogcatParser, logcatReader))
        commands.add(SleepCommand(SLEEP_BEFORE_KILL_SECONDS))
        commands.add(KillAppCommand(logger, appId))
        commands.add(SleepCommand(SLEEP_AFTER_KILL_SECONDS))

        return commands
    }

    private fun firstAppMeasurementLoop(
        iteration: Int,
        logcatParser: LogcatParser,
        logcatReader: LogcatReader
    ): CompositeCommand {
        return CompositeCommand(
            iteration, logger, listOf(
                startApkCommand(),
                ReadLogcatFromReader(logger, logcatParser, logcatReader),
                SleepCommand(SLEEP_BEFORE_KILL_SECONDS),
                KillAppCommand(logger, appId),
                SleepCommand(SLEEP_AFTER_KILL_SECONDS)
            )
        )
    }

    private fun secondAppMeasurementLoop(
        iteration: Int,
        logcatParser: LogcatParser,
        logcatReader: LogcatReader
    ): CompositeCommand {
        return CompositeCommand(
            iteration, logger, listOf(
                startApkCommand(),
                ReadLogcatFromReader(logger, logcatParser, logcatReader),
                SleepCommand(SLEEP_BEFORE_KILL_SECONDS),
                KillAppCommand(logger, appId),
                SleepCommand(SLEEP_AFTER_KILL_SECONDS)
            )
        )
    }

    private fun startApkCommand() = if (startActivityAdbShellCommand.isNotEmpty()) {
        StartActivityCustomAdbShellCommand(startActivityAdbShellCommand)
    } else {
        StartActivityCommand(appId, startActivityName)
    }
}

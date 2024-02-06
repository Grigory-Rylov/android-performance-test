package com.github.grishberg.performance.environments

import com.github.grishberg.performance.Commands
import com.github.grishberg.performance.CompareFromApkCommands
import com.github.grishberg.performance.aggregation.AggregatorProvider
import com.github.grishberg.performance.aggregation.AverageAggregatorFactory
import com.github.grishberg.performance.launcher.DeviceFacade
import com.github.grishberg.performance.report.CompareHtmlReport
import com.github.grishberg.performance.report.Reporter
import com.github.grishberg.tests.common.RunnerLogger
import java.io.File

/**
 * Creates environments for compare two apk.
 */
class CompareTwoApkEnvironments(
    private val logger: RunnerLogger, private val configuration: Configuration, private val device: DeviceFacade
) : MeasurementEnvironments {

    private val aggregatorProvider = AggregatorProvider(
        configuration.measurementName1,
        configuration.measurementName2,
        AverageAggregatorFactory(logger, configuration.measurementCount)
    )

    override fun createCommands(): Commands {
        return CompareFromApkCommands(
            appId = configuration.appId,
            startActivityName = configuration.startActivityName,
            startActivityAdbShellCommand = configuration.startActivityAdbShellCommand,
            shouldDeleteBeforeInstall = configuration.shouldDeleteBeforeInstall,
            firstApk = File(configuration.apkPath1),
            secondApk = File(configuration.apkPath2),
            logger = logger,
            measurementCount = configuration.measurementCount,
            aggregatorProvider = aggregatorProvider,
            logcatValuesPattern = configuration.logcatValuesRegexPattern,
            stopWordParameterName = configuration.lastParameterName,
            dryRunStopWordParameterName = configuration.stopDryRunParameterName,
            permissions = configuration.permissions
        )
    }

    override fun createReporterForDevice(): Reporter {
        return CompareHtmlReport(
            logger,
            device.name,
            configuration.measurementCount,
            aggregatorProvider.aggregator1,
            aggregatorProvider.aggregator2
        )
    }
}

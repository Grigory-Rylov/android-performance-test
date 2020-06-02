package com.github.grishberg.performance.environments

import com.github.grishberg.performance.Commands
import com.github.grishberg.performance.CompareFromApkCommands
import com.github.grishberg.performance.aggregation.AggregatorProvider
import com.github.grishberg.performance.aggregation.AverageAggregatorFactory
import com.github.grishberg.performance.launcher.DeviceFacade
import com.github.grishberg.performance.report.CompareHtmlReport
import com.github.grishberg.performance.report.Reporter
import com.github.grishberg.tests.common.RunnerLogger

/**
 * Creates environments for compare two apk.
 */
class CompareTwoApkEnvironments(
        private val logger: RunnerLogger,
        private val measurementCount: Int,
        private val appId: String,
        private val startActivityName: String,
        private val envData1: EnvironmentData,
        private val envData2: EnvironmentData,
        private val device: DeviceFacade,
        private val logcatTagAndValuesPattern: String,
        private val stopWordParameterName: String,
        private val dryRunStopWordParameterName: String
) : MeasurementEnvironments {
    private val aggregatorProvider = AggregatorProvider(
            envData1.measurementName,
            envData2.measurementName,
            AverageAggregatorFactory(measurementCount))

    override fun createCommands(): Commands {
        return CompareFromApkCommands(
                appId,
                startActivityName,
                envData1.apkPath,
                envData2.apkPath,
                logger,
                measurementCount,
                aggregatorProvider,
                logcatTagAndValuesPattern,
                stopWordParameterName,
                dryRunStopWordParameterName
        )
    }

    override fun createReporterForDevice(): Reporter {
        return CompareHtmlReport(device.name,
                measurementCount,
                aggregatorProvider.aggregator1,
                aggregatorProvider.aggregator2)
    }
}

package com.github.grishberg.performance.environments

import com.github.grishberg.tests.ConnectedDeviceWrapper
import com.github.grishberg.tests.common.RunnerLogger

class CompareApkEnvironmentsFactory(
        private val logger: RunnerLogger,
        private val measurementCount: Int,
        private val appId: String,
        private val startActivityName: String,
        private val envData1: EnvironmentData,
        private val envData2: EnvironmentData,
        private val logcatValuesPattern: String
) : EnvironmentsFactory {
    override fun create(device: ConnectedDeviceWrapper): MeasurementEnvironments {
        return CompareTwoApkEnvironments(logger,
                measurementCount,
                appId,
                startActivityName,
                envData1,
                envData2,
                device,
                logcatValuesPattern)
    }
}

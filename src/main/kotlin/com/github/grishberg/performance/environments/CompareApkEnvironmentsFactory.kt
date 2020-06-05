package com.github.grishberg.performance.environments

import com.github.grishberg.performance.launcher.DeviceFacade
import com.github.grishberg.tests.common.RunnerLogger

class CompareApkEnvironmentsFactory(
        private val logger: RunnerLogger,
        private val configuration: Configuration
) : EnvironmentsFactory {
    override fun create(device: DeviceFacade): MeasurementEnvironments {
        return CompareTwoApkEnvironments(logger, configuration, device)
    }
}

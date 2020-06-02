package com.github.grishberg.performance.environments

import com.github.grishberg.performance.launcher.DeviceFacade

interface EnvironmentsFactory {
    fun create(device: DeviceFacade): MeasurementEnvironments
}

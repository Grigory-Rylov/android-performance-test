package com.github.grishberg.performance.environments

import com.github.grishberg.tests.ConnectedDeviceWrapper

interface EnvironmentsFactory {
    fun create(device: ConnectedDeviceWrapper): MeasurementEnvironments
}

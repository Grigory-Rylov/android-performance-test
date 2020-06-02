package com.github.grishberg.performance

import com.github.grishberg.performance.launcher.DeviceFacade
import com.github.grishberg.tests.ConnectedDeviceWrapper

/**
 * Command for execution on [ConnectedDeviceWrapper]
 */
interface Commands {
    fun execute(device: DeviceFacade, logcatReader: LogcatReader)
}

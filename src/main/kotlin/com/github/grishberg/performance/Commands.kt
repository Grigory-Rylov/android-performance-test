package com.github.grishberg.performance

import com.github.grishberg.tests.ConnectedDeviceWrapper

/**
 * Command for execution on [ConnectedDeviceWrapper]
 */
interface Commands {
    fun execute(device: ConnectedDeviceWrapper)
}

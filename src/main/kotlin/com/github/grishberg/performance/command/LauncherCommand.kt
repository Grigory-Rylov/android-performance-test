package com.github.grishberg.performance.command

import com.github.grishberg.tests.ConnectedDeviceWrapper

/**
 * launches commands on device.
 */
interface LauncherCommand {
    fun execute(device: ConnectedDeviceWrapper)
}
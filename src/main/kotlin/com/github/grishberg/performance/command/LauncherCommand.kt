package com.github.grishberg.performance.command

import com.github.grishberg.performance.launcher.DeviceFacade

/**
 * launches commands on device.
 */
interface LauncherCommand {
    fun execute(device: DeviceFacade)
}
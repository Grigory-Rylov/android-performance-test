package com.github.grishberg.performance.command

import com.github.grishberg.performance.launcher.DeviceFacade

class SleepCommand(private val seconds: Int) : LauncherCommand {
    override fun execute(device: DeviceFacade) {
        Thread.sleep((1000 * seconds).toLong())
    }

    override fun toString(): String {
        return "SleepCommand[$seconds]"
    }
}

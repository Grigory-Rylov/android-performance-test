package com.github.grishberg.performance.command

import com.github.grishberg.tests.ConnectedDeviceWrapper

class SleepCommand(private val seconds: Int) : LauncherCommand {
    override fun execute(device: ConnectedDeviceWrapper) {
        Thread.sleep((1000 * seconds).toLong())
    }
}

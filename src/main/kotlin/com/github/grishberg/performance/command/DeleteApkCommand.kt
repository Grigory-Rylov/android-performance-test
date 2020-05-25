package com.github.grishberg.performance.command

import com.github.grishberg.tests.ConnectedDeviceWrapper

class DeleteApkCommand(
        private val appId: String
) : LauncherCommand {
    override fun execute(device: ConnectedDeviceWrapper) {
        device.executeShellCommand("pm uninstall $appId")
    }
}

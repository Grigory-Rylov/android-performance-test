package com.github.grishberg.performance.command

import com.github.grishberg.tests.ConnectedDeviceWrapper

class StartActivityCommand(
        private val appId: String,
        private val activityName: String
) : LauncherCommand {
    override fun execute(device: ConnectedDeviceWrapper) {
        device.executeShellCommand("am start -n \"$appId/$activityName\" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER")
    }
}

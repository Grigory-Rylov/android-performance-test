package com.github.grishberg.performance.command

import com.github.grishberg.performance.launcher.DeviceFacade

class StartActivityCommand(
        private val appId: String,
        private val activityName: String
) : LauncherCommand {
    override fun execute(device: DeviceFacade) {
        device.executeShellCommand("am start -n \"$appId/$activityName\" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER")
    }

    override fun toString(): String {
        return "StartActivityCommand[$appId, $activityName]"
    }
}

package com.github.grishberg.performance.command

import com.github.grishberg.performance.launcher.DeviceFacade

class StartActivityCustomAdbShellCommand(
    private val adbShellCommand: String
) : LauncherCommand {

    override fun execute(device: DeviceFacade) {
        device.executeShellCommand(adbShellCommand)
    }

    override fun toString(): String {
        return "StartActivityCustomAdbShellCommand[$adbShellCommand]"
    }
}

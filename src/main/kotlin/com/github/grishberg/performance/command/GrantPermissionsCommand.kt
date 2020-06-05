package com.github.grishberg.performance.command

import com.github.grishberg.performance.launcher.DeviceFacade

class GrantPermissionsCommand(
        private val appId: String,
        private val permissions: List<String>
) : LauncherCommand {
    override fun execute(device: DeviceFacade) {
        permissions.forEach {
            device.executeShellCommand("pm grant $appId $it")
        }
    }
}
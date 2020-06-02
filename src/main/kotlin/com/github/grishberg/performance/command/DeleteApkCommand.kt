package com.github.grishberg.performance.command

import com.android.ddmlib.CollectingOutputReceiver
import com.github.grishberg.performance.launcher.DeviceFacade
import com.github.grishberg.tests.ConnectedDeviceWrapper
import com.github.grishberg.tests.common.RunnerLogger

class DeleteApkCommand(
        private val appId: String,
        private val log: RunnerLogger
) : LauncherCommand {
    override fun execute(device: DeviceFacade) {
        log.d("DeleteApk", "delete $appId")
        device.executeShellCommand("pm uninstall $appId")
    }
}

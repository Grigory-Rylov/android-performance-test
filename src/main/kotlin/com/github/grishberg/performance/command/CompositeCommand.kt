package com.github.grishberg.performance.command

import com.github.grishberg.tests.ConnectedDeviceWrapper

class CompositeCommand(
        private val commands: List<LauncherCommand>
) : LauncherCommand {
    override fun execute(device: ConnectedDeviceWrapper) {
        for (command in commands) {
            command.execute(device)
        }
    }
}

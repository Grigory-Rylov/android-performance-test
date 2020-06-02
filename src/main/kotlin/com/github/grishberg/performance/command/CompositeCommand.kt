package com.github.grishberg.performance.command

import com.github.grishberg.performance.launcher.DeviceFacade
import com.github.grishberg.tests.common.RunnerLogger

class CompositeCommand(
        private val iteration: Int,
        private val logger: RunnerLogger,
        private val commands: List<LauncherCommand>
) : LauncherCommand {
    override fun execute(device: DeviceFacade) {
        for (command in commands) {
            logger.d("CompositeCommand", "iteration : ${iteration + 1},  execute $command")
            command.execute(device)
        }
    }
}

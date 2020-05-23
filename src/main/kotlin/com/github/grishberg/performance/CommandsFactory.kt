package com.github.grishberg.performance

import com.github.grishberg.performance.command.LauncherCommand

interface CommandsFactory {
    fun provideCommands(): List<LauncherCommand>
}

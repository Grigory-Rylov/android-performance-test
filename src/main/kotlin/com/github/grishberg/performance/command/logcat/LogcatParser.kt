package com.github.grishberg.performance.command.logcat

import com.github.grishberg.performance.launcher.DeviceFacade

interface LogcatParser {
    /**
     * Return true when should stop.
     */
    fun processLogcatLine(logcatOutput: String, device: DeviceFacade): Boolean = false

    fun processLogcatLine(lines: Array<out String>): Boolean = false
}

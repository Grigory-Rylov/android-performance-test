package com.github.grishberg.performance.command.logcat

import com.github.grishberg.tests.ConnectedDeviceWrapper

interface LogcatParser {
    /**
     * Return true when should stop.
     */
    fun processLogcatLine(logcatOutput: String, device: ConnectedDeviceWrapper): Boolean
}

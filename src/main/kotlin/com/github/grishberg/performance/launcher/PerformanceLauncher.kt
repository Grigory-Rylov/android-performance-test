package com.github.grishberg.performance.launcher

import com.github.grishberg.tests.adb.AdbWrapper

interface PerformanceLauncher {
    fun launchPerformance(adb: AdbWrapper)
}

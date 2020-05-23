package com.github.grishberg.performance.launcher

import com.github.grishberg.tests.adb.AdbWrapper

interface PerformanceLauncher {
    /**
     * Launches performance test.
     */
    fun measurePerformance(
            adb: AdbWrapper,
            firstSourceInfo: SourceCodeInfo,
            secondSourceInfo: SourceCodeInfo,
            launchesCount: Int,
            iterationsPerLaunch: Int)
}

package com.github.grishberg.performance

import com.android.ddmlib.AndroidDebugBridge
import com.github.grishberg.tests.InstrumentalExtension
import com.github.grishberg.tests.adb.AdbWrapper
import com.github.grishberg.tests.common.RunnerLogger

private const val TAG = "ParallelPerformanceLauncher"

class RunConfiguration(
        logger: RunnerLogger
) {
    val adb: AdbWrapper

    init {
        adb = initAdbConnection(logger)
    }

    private fun initAdbConnection(logger: RunnerLogger): AdbWrapper {
        val adb = AdbWrapper()
        val instrumentalExtension = InstrumentalExtension()
        var androidSdkPath: String? = instrumentalExtension.androidSdkPath
        if (androidSdkPath == null) {
            logger.i(TAG, "androidSdkPath is empty, get path from env ANDROID_HOME")
            androidSdkPath = System.getenv("ANDROID_HOME")
            logger.i(TAG, "androidSdkPath = {}", androidSdkPath)
        }
        AndroidDebugBridge.initIfNeeded(false)
        adb.init(androidSdkPath!!, logger)
        adb.waitForAdb()
        return adb
    }

}

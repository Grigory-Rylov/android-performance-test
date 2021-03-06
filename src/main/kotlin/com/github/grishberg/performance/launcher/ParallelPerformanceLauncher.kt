package com.github.grishberg.performance.launcher

import com.github.grishberg.performance.LogcatReader
import com.github.grishberg.performance.environments.EnvironmentsFactory
import com.github.grishberg.tests.adb.AdbWrapper
import com.github.grishberg.tests.common.RunnerLogger
import java.util.concurrent.CountDownLatch

private const val TAG = "PerformanceLauncher"

/**
 * Launches pref test on available devices.
 */
class ParallelPerformanceLauncher(
        private val logger: RunnerLogger,
        private val environmentsFactory: EnvironmentsFactory,
        private val logcatFilter: String,
        private val initializer: Runnable = Runnable { }
) : PerformanceLauncher {

    override fun launchPerformance(adb: AdbWrapper) {
        val deviceList = adb.provideDevices()

        initializer.run()

        if (deviceList.isEmpty()) {
            throw IllegalStateException("No devices found")
        }
        val deviceCounter = CountDownLatch(deviceList.size)
        deviceList.forEach { deviceWrapper ->
            Thread {
                try {
                    val device = DeviceFacade(deviceWrapper)
                    val logcatReader = LogcatReader(device, logger, logcatFilter)
                    logcatReader.start()

                    val measurementEnvironments = environmentsFactory.create(device)

                    measurementEnvironments.createCommands().execute(device, logcatReader)
                    measurementEnvironments.createReporterForDevice().buildReport()

                    logcatReader.close()

                } catch (e: Exception) {
                    logger.e(TAG, "Execute command exception:", e)
                } finally {
                    deviceCounter.countDown()
                }
            }.start()
        }
        deviceCounter.await()
        logger.i(TAG, "Done")
    }
}

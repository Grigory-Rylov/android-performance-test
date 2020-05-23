package com.github.grishberg.performance.launcher

import com.github.grishberg.performance.CompareFromSourcesCommandsFactory
import com.github.grishberg.performance.ResultsPrinter
import com.github.grishberg.performance.command.LauncherCommand
import com.github.grishberg.performance.environments.SourceFileSystem
import com.github.grishberg.tests.ConnectedDeviceWrapper
import com.github.grishberg.tests.adb.AdbWrapper
import com.github.grishberg.tests.common.RunnerLogger
import java.util.concurrent.CountDownLatch

private const val TAG = "PerformanceLauncher"

/**
 * Launches pref test on available devices.
 */
class ParallelPerformanceLauncher(
        private val resultsPrinter: ResultsPrinter,
        private val logger: RunnerLogger,
        private val sourceFileSystem: SourceFileSystem = SourceFileSystem(logger)
) : PerformanceLauncher {

    init {
        sourceFileSystem.prepareTmpDirAndFiles()
    }

    /**
     * Start performance tests.
     */
    override fun measurePerformance(
            adb: AdbWrapper,
            firstSourceInfo: SourceCodeInfo,
            secondSourceInfo: SourceCodeInfo,
            launchesCount: Int,
            iterationsPerLaunch: Int) {

        val commandsFactory = CompareFromSourcesCommandsFactory(sourceFileSystem, logger, resultsPrinter,
                firstSourceInfo,
                secondSourceInfo,
                launchesCount,
                iterationsPerLaunch)

        launchPerformance(adb, commandsFactory)
    }

    private fun launchPerformance(adb: AdbWrapper, commandsFactory: CompareFromSourcesCommandsFactory) {
        val deviceList = adb.provideDevices()

        commandsFactory.buildReplaceCommentCommand().execute()
        commandsFactory.provideAssembleCommand().execute()

        val deviceCounter = CountDownLatch(deviceList.size)
        deviceList.forEach { device ->
            Thread {
                try {
                    executeCommands(device, commandsFactory.provideCommands())
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

    private fun executeCommands(firstDevice: ConnectedDeviceWrapper, provideCommands: List<LauncherCommand>) {
        provideCommands.forEach {
            it.execute(firstDevice)
        }
    }
}

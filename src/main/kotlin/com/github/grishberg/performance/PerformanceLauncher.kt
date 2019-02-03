package com.github.grishberg.performance

import com.android.ddmlib.AndroidDebugBridge
import com.github.grishberg.performance.command.LauncherCommand
import com.github.grishberg.tests.ConnectedDeviceWrapper
import com.github.grishberg.tests.InstrumentalExtension
import com.github.grishberg.tests.adb.AdbWrapper
import com.github.grishberg.tests.adb.AdbWrapperImpl
import com.github.grishberg.tests.common.RunnerLogger
import java.util.concurrent.CountDownLatch

private const val TAG = "PerformanceLauncher"

/**
 * Launches pref test on available devices.
 */
class PerformanceLauncher(
        private val resultsPrinter: ResultsPrinter,
        private val logger: RunnerLogger,
        private val sourceFileSystem: SourceFileSystem = SourceFileSystem(logger)
) {

    private val adb: AdbWrapper

    init {
        adb = initAdbConnection(logger)
        sourceFileSystem.prepareTmpDirAndFiles()
    }

    /**
     * Start performance tests.
     */
    fun measurePerformance(
            firstSourceJava: Boolean,
            firstSourceImport: String,
            firstSourceCode: String,
            secondSourceJava: Boolean,
            secondSourceImport: String,
            secondSourceCode: String) {

        val commandsFabric = CommandsFabric(adb, sourceFileSystem, logger, resultsPrinter,
                firstSourceJava, firstSourceImport, firstSourceCode,
                secondSourceJava, secondSourceImport, secondSourceCode)

        launchPerformance(commandsFabric)
    }

    private fun launchPerformance(commandsFabric: CommandsFabric) {
        val deviceList = adb.provideDevices()

        val replaceCommentCommand = commandsFabric.buildReplaceCommentCommand()
        replaceCommentCommand.execute()

        val deviceCounter = CountDownLatch(deviceList.size)
        deviceList.forEach { device ->
            Thread {
                try {
                    executeCommands(device, commandsFabric.provideCommands())
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

    private fun initAdbConnection(logger: RunnerLogger): AdbWrapper {
        val adb = AdbWrapperImpl()
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
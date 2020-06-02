package com.github.grishberg.performance.command

import com.github.grishberg.performance.LogcatReader
import com.github.grishberg.performance.command.logcat.LogcatParser
import com.github.grishberg.performance.launcher.DeviceFacade
import com.github.grishberg.tests.common.RunnerLogger
import java.util.concurrent.CountDownLatch

private const val TAG = "ReadLogcatFromReader"

class ReadLogcatFromReader(
        private val logger: RunnerLogger,
        private val logcatParser: LogcatParser,
        private val logcat: LogcatReader
) : LauncherCommand {
    private val countDownLatch = CountDownLatch(1)

    override fun execute(device: DeviceFacade) {
        logger.d(TAG, "parse logcat..")
        val listener = Listener()
        logcat.addListener(listener)
        countDownLatch.await()
        logger.d(TAG, "ended parsing logcat")
    }

    inner class Listener : LogcatReader.LogcatInputListener {
        override fun processLinesAndUnsubscribeIfNeeded(lines: Array<out String>): Boolean {
            if (logcatParser.processLogcatLine(lines)) {
                countDownLatch.countDown()
                logger.d(TAG, "lines are processed.")
                return true
            }
            return false
        }
    }

    override fun toString(): String {
        return "ReadLogcatFromReader"
    }
}

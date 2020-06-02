package com.github.grishberg.performance

import com.android.ddmlib.MultiLineReceiver
import com.github.grishberg.performance.launcher.DeviceFacade
import com.github.grishberg.tests.common.RunnerLogger
import java.io.Closeable
import java.util.concurrent.FutureTask
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

private const val LOGCAT_COMMAND = "logcat -s "
private const val MAX_WAIT_TO_STOP_LOGGER_MS = 10000L

/**
 * Helper to stream logcat from a device to the specified file.
 */
class LogcatReader(private val device: DeviceFacade,
                   private val logger: RunnerLogger,
                   private val logcatFilter: String) : Closeable {
    private val logTag = "LogcatReader-${device.serialNumber}"
    private val monitor = Any()

    @Volatile
    private var running = false

    private val captureTask = FutureTask(this::captureLogOnWorkerThread, null)
    private val workerThread = Thread(captureTask).apply {
        name = "LogcatReader-${device.serialNumber}"
        // Don't keep this thread around if the whole test runner is stopping.
        isDaemon = true
    }

    private val listeners = mutableListOf<LogcatInputListener>()

    private fun captureLogOnWorkerThread() {
        device.executeShellCommand(
                LOGCAT_COMMAND + logcatFilter,
                ShellCommandWriter(), 0, 0,
                TimeUnit.MILLISECONDS)
    }

    /**
     * Starts logcat streaming.
     */
    fun start() {
        if (running) {
            return
        }
        running = true
        workerThread.start()
        logger.d(logTag, "started")
    }

    fun addListener(listener: LogcatInputListener) {
        synchronized(monitor) {
            listeners.add(listener)
        }
    }

    fun removeListener(listener: LogcatInputListener) {
        synchronized(monitor) {
            listeners.remove(listener)
        }
    }

    override fun close() {
        if (!running) {
            return
        }

        logger.d(logTag, "closing")
        running = false
        try {
            // Normally setting |running| to false should be enough for reader to stop quickly
            // (source code of ddmlib suggests that it checks the flag at every successful read and
            // sleeps between reads no more than 25ms if there is no logcat data available.
            // However it would be disappointing to lose logs if something goes wrong here.
            captureTask.get(MAX_WAIT_TO_STOP_LOGGER_MS, TimeUnit.MILLISECONDS)

            // There is no need in special recovery if captureLogOnWorkerThread throws: get() will
            // throw ExecutionException and test runner will handle it (by marking run as fail).
            // Timing out is worse though.
        } catch (e: TimeoutException) {
            logger.e(logTag, "Reader thread failed to cooperate, terminating forcefully")
            // WorkerThread can be stuck in socket read for some reason or ddms polling timeout may
            // increase. Interrupt can help to unstuck it (causing executeShellCommand to throw and
            // flush log file).
            workerThread.interrupt()

            // There is no happy end here: either thread proceeds and get() throws
            // ExecutionException or it is still stuck and TimeoutException is thrown. Something
            // fishy is going on anyway so it is ok to propagate exception and mark test run as
            // a failure.
            captureTask.get(MAX_WAIT_TO_STOP_LOGGER_MS, TimeUnit.MILLISECONDS)
        }
    }


    private inner class ShellCommandWriter : MultiLineReceiver() {
        override fun processNewLines(lines: Array<out String>) {
            if (running) {
                try {
                    processLogLines(lines)
                } catch (e: Exception) {
                    logger.e(logTag, "processNewLine", e)
                }
            }
        }

        override fun isCancelled(): Boolean {
            return !running
        }
    }

    private fun processLogLines(lines: Array<out String>) {
        for (line in lines) {
            logger.d("Logcat>>", line)
        }
        val iterator = getListeners()
        while (iterator.hasNext()) {
            val listener = iterator.next()
            val shouldRemove = listener.processLinesAndUnsubscribeIfNeeded(lines)
            if (shouldRemove) {
                iterator.remove()
            }
        }
    }

    private fun getListeners(): MutableIterator<LogcatInputListener> {
        synchronized(monitor) {
            return listeners.iterator()
        }
    }


    interface LogcatInputListener {
        /**
         * If returned true - should remove listener.
         */
        fun processLinesAndUnsubscribeIfNeeded(lines: Array<out String>): Boolean
    }
}


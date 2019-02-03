package com.grishberg.performeter

import android.util.Log

/**
 * Result reporter interface
 */
interface ResultReporter {
    fun reportPerfResults(
        duration1: Long,
        duration2: Long,
        threadDuration1: Long,
        threadDuration2: Long
    )
}

/**
 * Log reporter.
 */
class LogReporter : ResultReporter {
    override fun reportPerfResults(
        duration1: Long,
        duration2: Long,
        threadDuration1: Long,
        threadDuration2: Long
    ) {
        Log.e(
            "[PERF]", "(d1=$duration1, d2=$duration2, " +
                    "td1=$threadDuration1, td2=$threadDuration2)"
        )
    }
}

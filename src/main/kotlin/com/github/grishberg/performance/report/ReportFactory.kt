package com.github.grishberg.performance.report

interface ReportFactory {
    fun createReporterForDevice(): Reporter
}

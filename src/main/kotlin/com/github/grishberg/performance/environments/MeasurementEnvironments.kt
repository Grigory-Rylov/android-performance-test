package com.github.grishberg.performance.environments

import com.github.grishberg.performance.Commands
import com.github.grishberg.performance.report.ReportFactory

interface MeasurementEnvironments : ReportFactory {
    fun createCommands(): Commands
}

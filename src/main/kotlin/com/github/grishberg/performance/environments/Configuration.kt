package com.github.grishberg.performance.environments

data class Configuration(
        val appId: String,
        val startActivityName: String,
        val measurementCount: Int,
        val measurementName1: String,
        val measurementName2: String,
        val apkPath1: String,
        val apkPath2: String,
        val logcatValuesRegexPattern: String,
        val stopDryRunParameterName: String,
        val lastParameterName: String,
        val permissions: List<String>
)
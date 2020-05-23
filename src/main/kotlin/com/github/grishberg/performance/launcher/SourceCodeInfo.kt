package com.github.grishberg.performance.launcher

import com.github.grishberg.performance.Language

data class SourceCodeInfo(
        val language: Language,
        val imports: String,
        val fields: String,
        val sourceCode: String,
        val initialization: String
)

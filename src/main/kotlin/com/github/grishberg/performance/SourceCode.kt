package com.github.grishberg.performance

import com.github.grishberg.performance.launcher.SourceCodeInfo

interface SourceCode {
    fun replaceWithCode(fileSystem: SourceFiles)
    fun createAlternativeEmptySource(fileSystem: SourceFiles)
}

class KotlinSource(
        private val firstIndex: Boolean,
        private val sourceInfo: SourceCodeInfo
) : SourceCode {

    override fun replaceWithCode(fileSystem: SourceFiles) {
        val index = if (firstIndex) 1 else 2
        fileSystem.moveTemplateAndReplaceSource("KotlinSample$index.kt",
                sourceInfo.imports, sourceInfo.fields, sourceInfo.sourceCode, sourceInfo.initialization)
    }

    override fun createAlternativeEmptySource(fileSystem: SourceFiles) {
        val index = if (firstIndex) 1 else 2
        fileSystem.moveTemplateAndReplaceSource("JavaSample$index.java", "", "", "", "")
    }
}

class JavaSource(
        private val firstIndex: Boolean,
        private val sourceInfo: SourceCodeInfo
) : SourceCode {

    override fun replaceWithCode(fileSystem: SourceFiles) {
        val index = if (firstIndex) 1 else 2
        fileSystem.moveTemplateAndReplaceSource("JavaSample$index.java",
                sourceInfo.imports, sourceInfo.fields, sourceInfo.sourceCode, sourceInfo.initialization)
    }

    override fun createAlternativeEmptySource(fileSystem: SourceFiles) {
        val index = if (firstIndex) 1 else 2
        fileSystem.moveTemplateAndReplaceSource("KotlinSample$index.kt", "", "", "", "")
    }
}

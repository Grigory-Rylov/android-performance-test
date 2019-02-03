package com.github.grishberg.performance

interface SourceCode {
    fun replaceWithCode(fileSystem: SourceFiles)
    fun createAlternativeEmptySource(fileSystem: SourceFiles)
}

class KotlinSource(
        private val firstIndex: Boolean,
        private val importCode: String = "",
        private val sourceCode: String = ""
) : SourceCode {

    override fun replaceWithCode(fileSystem: SourceFiles) {
        val index = if (firstIndex) 1 else 2
        fileSystem.moveTemplateAndReplaceSource("KotlinSample$index.kt", importCode, sourceCode)
    }

    override fun createAlternativeEmptySource(fileSystem: SourceFiles) {
        val index = if (firstIndex) 1 else 2
        fileSystem.moveTemplateAndReplaceSource("JavaSample$index.java", "", "")
    }
}

class JavaSource(
        private val firstIndex: Boolean,
        private val importCode: String = "",
        private val sourceCode: String = ""
) : SourceCode {

    override fun replaceWithCode(fileSystem: SourceFiles) {
        val index = if (firstIndex) 1 else 2
        fileSystem.moveTemplateAndReplaceSource("JavaSample$index.java", importCode, sourceCode)
    }

    override fun createAlternativeEmptySource(fileSystem: SourceFiles) {
        val index = if (firstIndex) 1 else 2
        fileSystem.moveTemplateAndReplaceSource("KotlinSample$index.kt", "", "")
    }
}
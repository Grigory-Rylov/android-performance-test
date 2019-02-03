package com.github.grishberg.performance.command

import com.github.grishberg.performance.SourceCode
import com.github.grishberg.performance.SourceFiles

class ReplaceCommentCommand(
        private val sourceFiles: SourceFiles,
        private val source1: SourceCode,
        private val source2: SourceCode

) {
    fun execute() {
        source1.replaceWithCode(sourceFiles)
        source2.replaceWithCode(sourceFiles)
        source1.createAlternativeEmptySource(sourceFiles)
        source2.createAlternativeEmptySource(sourceFiles)
    }
}
package com.github.grishberg.performance

import com.github.grishberg.performance.command.AssembleCommand
import com.github.grishberg.performance.command.ReplaceCommentCommand
import com.github.grishberg.performance.environments.SourceFileSystem
import com.github.grishberg.performance.launcher.SourceCodeInfo
import com.github.grishberg.tests.common.RunnerLogger

class ApkBuilder(
        private val logger: RunnerLogger,
        private val source1: SourceCodeInfo,
        private val source2: SourceCodeInfo,
        sourceFileSystem: SourceFileSystem
) : Runnable {
    private val sourceFiles = SourceFiles(sourceFileSystem)

    override fun run() {
        buildReplaceCommentCommand().execute()
        provideAssembleCommand().execute()
    }

    fun buildReplaceCommentCommand(): ReplaceCommentCommand {
        val sourceCode1 = if (source1.language == Language.JAVA)
            JavaSource(true, source1)
        else
            KotlinSource(true, source1)


        val sourceCode2 = if (source2.language == Language.JAVA)
            JavaSource(false, source2)
        else
            KotlinSource(false, source2)
        return ReplaceCommentCommand(sourceFiles, sourceCode1, sourceCode2)
    }

    fun provideAssembleCommand() = AssembleCommand(logger)
}

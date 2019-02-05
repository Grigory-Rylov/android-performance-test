package com.github.grishberg.performance

import com.github.grishberg.performance.command.*
import com.github.grishberg.tests.common.RunnerLogger

private const val TAG = "CommandsFabric"

class CommandsFabric(
        sourceFileSystem: SourceFileSystem,
        private val logger: RunnerLogger,
        private val resultsPrinter: ResultsPrinter,
        private val firstSourceJava: Boolean,
        private val firstSourceImport: String,
        private val firstSourceField: String,
        private val firstSourceCode: String,
        private val secondSourceJava: Boolean,
        private val secondSourceImport: String,
        private val secondSourceField: String,
        private val secondSourceCode: String
) {
    private val sourceFiles = SourceFiles(sourceFileSystem)

    fun provideCommands(): List<LauncherCommand> {
        val commands = ArrayList<LauncherCommand>()
        commands.add(AssembleCommand(logger))
        commands.add(InstallApkCommand(logger))
        commands.add(StartActivityCommand(generateMode()))
        commands.add(ReadLogcatCommand(resultsPrinter, logger))
        return commands
    }

    private fun generateMode(): String {
        return if (firstSourceJava && secondSourceJava) {
            "j vs j"
        } else if (firstSourceJava && !secondSourceJava) {
            "j vs k"
        } else if (!firstSourceJava && secondSourceJava) {
            "k vs j"
        } else "k vs k"
    }

    fun buildReplaceCommentCommand(): ReplaceCommentCommand {
        val sourceCode1 = if (firstSourceJava)
            JavaSource(true, firstSourceImport, firstSourceField, firstSourceCode)
        else
            KotlinSource(true, firstSourceImport, firstSourceField, firstSourceCode)

        val sourceCode2 = if (secondSourceJava)
            JavaSource(false, secondSourceImport, secondSourceField, secondSourceCode)
        else
            KotlinSource(false, secondSourceImport, secondSourceField, secondSourceCode)
        val replaceSourceCommand = ReplaceCommentCommand(sourceFiles, sourceCode1, sourceCode2)
        return replaceSourceCommand
    }
}
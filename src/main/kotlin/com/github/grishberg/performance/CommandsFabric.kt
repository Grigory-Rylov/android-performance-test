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
        private val firstInitCode: String,
        private val secondSourceJava: Boolean,
        private val secondSourceImport: String,
        private val secondSourceField: String,
        private val secondSourceCode: String,
        private val secondInitCode: String,
        private val cyclesCount: Int
) {
    private val sourceFiles = SourceFiles(sourceFileSystem)

    fun provideCommands(iterationsCount: Int = 50000): List<LauncherCommand> {
        val commands = ArrayList<LauncherCommand>()
        commands.add(InstallApkCommand(logger))
        for (i in 0 until cyclesCount) {
            commands.add(ClearLogcatCommand(logger))
            commands.add(StartActivityCommand(0, generateModeForFirstRun(), iterationsCount))
            commands.add(ReadLogcatCommand(0, resultsPrinter, logger))
            commands.add(KillAppCommand(logger))
            commands.add(StartActivityCommand(1, generateModeForSecondRun(), iterationsCount))
            commands.add(ReadLogcatCommand(1, resultsPrinter, logger))
            commands.add(KillAppCommand(logger))
        }
        return commands
    }

    fun provideAssembleCommand() = AssembleCommand(logger)

    private fun generateModeForFirstRun(): String = if (firstSourceJava) "j1" else "k1"

    private fun generateModeForSecondRun(): String = if (secondSourceJava) "j2" else "k2"

    fun buildReplaceCommentCommand(): ReplaceCommentCommand {
        val sourceCode1 = if (firstSourceJava)
            JavaSource(true, firstSourceImport, firstSourceField, firstSourceCode, firstInitCode)
        else
            KotlinSource(true, firstSourceImport, firstSourceField, firstSourceCode, firstInitCode)

        val sourceCode2 = if (secondSourceJava)
            JavaSource(false, secondSourceImport, secondSourceField, secondSourceCode, secondInitCode)
        else
            KotlinSource(false, secondSourceImport, secondSourceField, secondSourceCode, secondInitCode)
        val replaceSourceCommand = ReplaceCommentCommand(sourceFiles, sourceCode1, sourceCode2)
        return replaceSourceCommand
    }
}
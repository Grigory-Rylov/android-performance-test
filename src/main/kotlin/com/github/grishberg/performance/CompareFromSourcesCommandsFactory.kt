package com.github.grishberg.performance

import com.github.grishberg.performance.command.AssembleCommand
import com.github.grishberg.performance.command.ClearLogcatCommand
import com.github.grishberg.performance.command.InstallApkCommand
import com.github.grishberg.performance.command.KillAppCommand
import com.github.grishberg.performance.command.LauncherCommand
import com.github.grishberg.performance.command.ReadLogcatCommand
import com.github.grishberg.performance.command.ReplaceCommentCommand
import com.github.grishberg.performance.command.StartActivityCommand
import com.github.grishberg.performance.environments.SourceFileSystem
import com.github.grishberg.performance.launcher.SourceCodeInfo
import com.github.grishberg.tests.common.RunnerLogger

private const val TAG = "CompareFromSourcesCommandsFactory"

class CompareFromSourcesCommandsFactory(
        sourceFileSystem: SourceFileSystem,
        private val logger: RunnerLogger,
        private val resultsPrinter: ResultsPrinter,
        private val source1: SourceCodeInfo,
        private val source2: SourceCodeInfo,
        private val launchesCount: Int,
        private val iterationsPerLaunch: Int
) : CommandsFactory {
    private val sourceFiles = SourceFiles(sourceFileSystem)

    override fun provideCommands(): List<LauncherCommand> {
        val commands = ArrayList<LauncherCommand>()
        commands.add(InstallApkCommand(logger))
        for (i in 0 until launchesCount) {
            commands.add(ClearLogcatCommand(logger))
            commands.add(StartActivityCommand(0, generateModeForFirstRun(), iterationsPerLaunch))
            commands.add(ReadLogcatCommand(0, resultsPrinter, logger))
            commands.add(KillAppCommand(logger))
            commands.add(StartActivityCommand(1, generateModeForSecondRun(), iterationsPerLaunch))
            commands.add(ReadLogcatCommand(1, resultsPrinter, logger))
            commands.add(KillAppCommand(logger))
        }
        return commands
    }

    fun provideAssembleCommand() = AssembleCommand(logger)

    private fun generateModeForFirstRun(): String = if (source1.language == Language.JAVA) "j1" else "k1"

    private fun generateModeForSecondRun(): String = if (source2.language == Language.JAVA) "j2" else "k2"

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
}

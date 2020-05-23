package com.github.grishberg.performance

import com.github.grishberg.performance.environments.SourceFileSystem

private const val IMPORT_TEMPLATE = "/* place your includes there */"
private const val FIELD_TEMPLATE = "/* place your fields and methods there */"
private const val CODE_TEMPLATE = "/* place your code there */"
private const val INIT_CODE_TEMPLATE = "/* place your init code there */"

class SourceFiles(
        private val fileSystem: SourceFileSystem
) {
    /**
     * Changes source and replaces files with template.
     */
    fun moveTemplateAndReplaceSource(fileName: String,
                                     importCode: String,
                                     fieldCode: String,
                                     sourceCode: String,
                                     initCode: String) {
        var content = fileSystem.readTemplateFile(fileName)
                .replace(CODE_TEMPLATE, sourceCode)
        if (fieldCode.isNotEmpty()) {
            content = content.replace(FIELD_TEMPLATE, fieldCode)
        }
        if (importCode.isNotEmpty()) {
            content = content.replace(IMPORT_TEMPLATE, importCode)
        }
        if (initCode.isNotEmpty()) {
            content = content.replace(INIT_CODE_TEMPLATE, initCode)
        }
        fileSystem.writeSourceFile(fileName, content)
    }
}

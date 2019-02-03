package com.github.grishberg.performance

private const val IMPORT_TEMPLATE = "/* place your includes there */"
private const val CODE_TEMPLATE = "/* place your code there */"

class SourceFiles(
        private val fileSystem: SourceFileSystem
) {

    /**
     * Changes source and replaces files with template.
     */
    fun moveTemplateAndReplaceSource(fileName: String,
                                     importCode: String,
                                     sourceCode: String) {
        var content = fileSystem.readTemplateFile(fileName)
                .replace(CODE_TEMPLATE, sourceCode)
        if (importCode.isNotEmpty()) {
            content = content.replace(IMPORT_TEMPLATE, importCode)
        }

        fileSystem.writeSourceFile(fileName, content)
    }
}
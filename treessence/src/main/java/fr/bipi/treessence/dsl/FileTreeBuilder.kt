package fr.bipi.treessence.dsl

import fr.bipi.treessence.common.filters.mergeFilters
import fr.bipi.treessence.common.formatter.LogcatFormatter
import fr.bipi.treessence.file.FileLoggerTree

typealias FileTreeDeclaration = FileTreeScope.() -> Unit

class FileTreeScope : TreeScope() {
    var fileName = "log"
    var dir = ""
    var sizeLimit = FileLoggerTree.Builder.SIZE_LIMIT
    var fileLimit = FileLoggerTree.Builder.NB_FILE_LIMIT
    var appendToFile = true
}

/**
 * Builder for [FileLoggerTree]
 */
object FileTreeBuilder {
    fun build(data: FileTreeScope) = FileLoggerTree.Builder().apply {
        with(data) {
            withDirName(dir)
            withFileName(fileName)
            withMinPriority(level)
            withSizeLimit(sizeLimit)
            withFileLimit(fileLimit)
            withFilter(filters.mergeFilters())
            withFormatter(formatter ?: LogcatFormatter.INSTANCE)
            appendToFile(appendToFile)
        }
    }.build()
}

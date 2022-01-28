package fr.bipi.treessence.dsl

import fr.bipi.treessence.common.filters.mergeFilters
import fr.bipi.treessence.common.formatter.LogcatFormatter
import fr.bipi.treessence.console.SystemLogTree

/**
 * Builder for [SystemLogTree]
 */
object SystemTreeBuilder {
    fun build(data: TreeScope) = with(data) {
        SystemLogTree(
            priority = level,
            filter = filters.mergeFilters(),
            formatter = formatter ?: LogcatFormatter.INSTANCE
        )
    }
}

package fr.bipi.tressence.dsl

import fr.bipi.tressence.common.filters.mergeFilters
import fr.bipi.tressence.common.formatter.LogcatFormatter
import fr.bipi.tressence.console.SystemLogTree

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

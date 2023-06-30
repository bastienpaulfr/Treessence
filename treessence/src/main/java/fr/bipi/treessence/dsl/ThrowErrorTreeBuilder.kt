package fr.bipi.treessence.dsl

import fr.bipi.treessence.common.filters.mergeFilters
import fr.bipi.treessence.console.ThrowErrorTree

/**
 * Builder for [ThrowErrorTree].
 */
object ThrowErrorTreeBuilder {
    fun build(data: TreeScope) = with(data) {
        ThrowErrorTree(
            priority = level,
            filter = filters.mergeFilters(),
        )
    }
}

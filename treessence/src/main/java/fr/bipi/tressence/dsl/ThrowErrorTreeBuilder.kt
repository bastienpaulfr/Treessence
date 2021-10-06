package fr.bipi.tressence.dsl

import fr.bipi.tressence.common.filters.mergeFilters
import fr.bipi.tressence.console.ThrowErrorTree

/**
 * Builder for [ThrowErrorTree].
 */
object ThrowErrorTreeBuilder {
    fun build(data: TreeScope) = with(data) {
        ThrowErrorTree(
            priority = level,
            filter = filters.mergeFilters()
        )
    }
}

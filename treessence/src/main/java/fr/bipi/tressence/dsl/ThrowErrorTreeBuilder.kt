package fr.bipi.tressence.dsl

import fr.bipi.tressence.common.filters.mergeFilters
import fr.bipi.tressence.console.ThrowErrorTree
import timber.log.Timber

/**
 * Builder for [ThrowErrorTree].
 */
object ThrowErrorTreeBuilder {
    fun build(data: TreeScope): Timber.Tree {
        return with(data) {
            ThrowErrorTree(
                priority = level,
                filter = filters.mergeFilters()
            )
        }
    }
}

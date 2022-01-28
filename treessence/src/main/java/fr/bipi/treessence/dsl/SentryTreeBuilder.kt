package fr.bipi.treessence.dsl

import fr.bipi.treessence.common.filters.mergeFilters
import fr.bipi.treessence.common.formatter.LogcatFormatter
import fr.bipi.treessence.sentry.SentryBreadcrumbTree
import fr.bipi.treessence.sentry.SentryEventTree

/**
 * Builder for [SentryEventTree] and [SentryBreadcrumbTree]
 */
object SentryTreeBuilder {
    fun buildBreadCrumbTree(data: TreeScope) = with(data) {
        SentryBreadcrumbTree(
            priority = level,
            filter = filters.mergeFilters(),
            formatter = formatter ?: LogcatFormatter.INSTANCE
        )
    }

    fun buildEventTree(data: TreeScope) = with(data) {
        SentryEventTree(
            priority = level,
            filter = filters.mergeFilters()
        )
    }
}

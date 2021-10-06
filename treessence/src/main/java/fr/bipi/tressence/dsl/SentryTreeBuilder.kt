package fr.bipi.tressence.dsl

import fr.bipi.tressence.common.filters.mergeFilters
import fr.bipi.tressence.common.formatter.LogcatFormatter
import fr.bipi.tressence.sentry.SentryBreadcrumbTree
import fr.bipi.tressence.sentry.SentryEventTree

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

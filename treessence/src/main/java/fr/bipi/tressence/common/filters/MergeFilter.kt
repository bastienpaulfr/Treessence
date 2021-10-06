package fr.bipi.tressence.common.filters

/**
 * Merge several filters into one.
 *
 * - If one of filters wants to skip log, then this merge filter is skipping log.
 * - If one of filter tells that it is not loggable, then this merge filter is not loggable as well.
 */
class MergeFilter(
    filters: List<Filter> = emptyList()
) : Filter {
    val filters = mutableListOf<Filter>().apply {
        addAll(filters)
    }

    override fun skipLog(priority: Int, tag: String?, message: String, t: Throwable?): Boolean {
        filters.forEach {
            if (it.skipLog(priority, tag, message, t)) {
                return true
            }
        }
        return false
    }

    override fun isLoggable(priority: Int, tag: String?): Boolean {
        filters.forEach {
            if (!it.isLoggable(priority, tag)) {
                return false
            }
        }
        return true
    }
}

fun List<Filter>.mergeFilters() = when {
    this.isEmpty() -> NoFilter.INSTANCE
    this.size == 1 -> this[0]
    else -> MergeFilter().apply {
        this.filters.addAll(this@mergeFilters)
    }
}

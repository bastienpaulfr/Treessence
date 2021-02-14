package fr.bipi.tressence.common.filters

class MergeFilter : Filter {
    val filters = mutableListOf<Filter>()

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
        this.filters.addAll(filters)
    }
}

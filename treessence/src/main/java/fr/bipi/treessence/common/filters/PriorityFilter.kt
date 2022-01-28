package fr.bipi.treessence.common.filters

class PriorityFilter(val minPriority: Int) : Filter {
    override fun skipLog(priority: Int, tag: String?, message: String, t: Throwable?): Boolean {
        return priority < minPriority
    }

    override fun isLoggable(priority: Int, tag: String?): Boolean {
        return priority >= minPriority
    }
}

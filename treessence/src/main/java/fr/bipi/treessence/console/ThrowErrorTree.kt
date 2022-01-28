package fr.bipi.treessence.console

import android.util.Log
import fr.bipi.treessence.base.PriorityTree
import fr.bipi.treessence.common.filters.Filter
import fr.bipi.treessence.common.filters.NoFilter

/**
 * An implementation of [Timber.Tree] which throws [java.lang.Error] when priority of
 * log is exceeded the limit. Useful for development or test environment.
 *
 * @param priority Minimum log priority to throw error. Expects one of constants defined in [Log].
 */
class ThrowErrorTree @JvmOverloads constructor(
    priority: Int = Log.ERROR,
    filter: Filter = NoFilter.INSTANCE
) : PriorityTree(priority, filter) {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (skipLog(priority, tag, message, t)) {
            return
        }
        if (t != null) {
            throw LogPriorityExceededError(priority, priorityFilter.minPriority, t)
        } else {
            throw LogPriorityExceededError(priority, priorityFilter.minPriority)
        }
    }
}

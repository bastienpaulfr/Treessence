package fr.bipi.treessence.base

import fr.bipi.treessence.common.filters.Filter
import fr.bipi.treessence.common.filters.NoFilter
import fr.bipi.treessence.common.formatter.Formatter
import fr.bipi.treessence.common.formatter.NoTagFormatter

/**
 * Base class to filter logs by priority
 */
open class FormatterPriorityTree @JvmOverloads constructor(
    priority: Int,
    filter: Filter = NoFilter.INSTANCE,
    formatter: Formatter = NoTagFormatter.INSTANCE
) : PriorityTree(priority, filter) {

    var formatter = formatter
        private set

    @Deprecated("Method for retro compatibility")
    @Synchronized
    fun withFormatter(newFormatter: Formatter): FormatterPriorityTree {
        formatter = newFormatter
        return this
    }

    /**
     * Use its formatter to format log
     *
     * @param priority Priority
     * @param tag      Tag
     * @param message  Message
     * @return Formatted log
     */
    protected fun format(priority: Int, tag: String?, message: String): String {
        return formatter.format(priority, tag, message)
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, tag, format(priority, tag, message), t)
    }
}

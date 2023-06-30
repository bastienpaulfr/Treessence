package fr.bipi.treessence.console

import android.util.Log
import fr.bipi.treessence.base.FormatterPriorityTree
import fr.bipi.treessence.common.filters.Filter
import fr.bipi.treessence.common.filters.NoFilter
import fr.bipi.treessence.common.formatter.Formatter
import fr.bipi.treessence.common.formatter.LogcatFormatter
import timber.log.Timber

/**
 * An implementation of [Timber.Tree] which log using [System.out] print() method.
 */
class SystemLogTree @JvmOverloads constructor(
    priority: Int = Log.VERBOSE,
    filter: Filter = NoFilter.INSTANCE,
    formatter: Formatter = LogcatFormatter.INSTANCE,
) : FormatterPriorityTree(priority, filter, formatter) {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (skipLog(priority, tag, message, t)) {
            return
        }
        print(format(priority, tag, message))
        t?.printStackTrace()
    }
}

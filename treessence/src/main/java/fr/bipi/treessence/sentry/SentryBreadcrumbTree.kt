package fr.bipi.treessence.sentry

import android.util.Log
import fr.bipi.treessence.base.FormatterPriorityTree
import fr.bipi.treessence.common.filters.Filter
import fr.bipi.treessence.common.filters.NoFilter
import fr.bipi.treessence.common.formatter.DefaultLogFormatter
import fr.bipi.treessence.common.formatter.Formatter
import io.sentry.Sentry
import io.sentry.event.Breadcrumb
import io.sentry.event.BreadcrumbBuilder
import java.util.Date

/**
 * Logger that will store a Breadcrumb. Throwable are ignored.
 *
 * @param priority priority from witch log will be logged
 */
class SentryBreadcrumbTree @JvmOverloads constructor(
    priority: Int,
    filter: Filter = NoFilter.INSTANCE,
    formatter: Formatter = DefaultLogFormatter.INSTANCE
) : FormatterPriorityTree(priority, filter, formatter) {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (skipLog(priority, tag, message, t)) {
            return
        }
        val bb = BreadcrumbBuilder()
        bb.setMessage(format(priority, tag, message))
            .setTimestamp(Date())
            .setCategory("log")
            .setType(Breadcrumb.Type.DEFAULT)
            .setLevel(fromAndroidLogPriorityToSentryLevel(priority))
        Sentry.getContext().recordBreadcrumb(bb.build())
    }

    private fun fromAndroidLogPriorityToSentryLevel(priority: Int): Breadcrumb.Level {
        return when (priority) {
            Log.INFO -> Breadcrumb.Level.INFO
            Log.WARN -> Breadcrumb.Level.WARNING
            Log.ERROR -> Breadcrumb.Level.ERROR
            Log.ASSERT -> Breadcrumb.Level.CRITICAL
            Log.DEBUG, Log.VERBOSE -> Breadcrumb.Level.DEBUG
            else -> Breadcrumb.Level.DEBUG
        }
    }
}

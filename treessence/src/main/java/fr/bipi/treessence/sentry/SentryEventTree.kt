package fr.bipi.treessence.sentry

import android.util.Log
import fr.bipi.treessence.base.PriorityTree
import fr.bipi.treessence.common.filters.Filter
import fr.bipi.treessence.common.filters.NoFilter
import io.sentry.Sentry
import io.sentry.event.Event
import io.sentry.event.EventBuilder
import java.util.concurrent.Executors

/**
 * Logger that will log to sentry server
 *
 * @param priority priority from witch log will be logged
 */
class SentryEventTree @JvmOverloads constructor(
    priority: Int,
    filter: Filter = NoFilter.INSTANCE
) : PriorityTree(priority, filter) {
    /**
     * Threads from where sentry events will be sent.
     *
     *
     * Threads are needed because if connectivity is bad, then [Sentry.capture] can take a long time to
     * complete
     */
    private val executorService = Executors.newFixedThreadPool(1)
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (skipLog(priority, tag, message, t)) {
            return
        }
        executorService.submit {
            val eb = EventBuilder()
            eb.withTag(KEY_TAG, tag)
                .withLevel(fromAndroidLogPriorityToSentryLevel(priority))
                .withMessage(message)
            Sentry.capture(eb)
            if (t != null) {
                Sentry.capture(t)
            }
        }
    }

    private fun fromAndroidLogPriorityToSentryLevel(priority: Int): Event.Level {
        return when (priority) {
            Log.INFO -> Event.Level.INFO
            Log.WARN -> Event.Level.WARNING
            Log.ERROR -> Event.Level.ERROR
            Log.ASSERT -> Event.Level.FATAL
            Log.DEBUG, Log.VERBOSE -> Event.Level.DEBUG
            else -> Event.Level.DEBUG
        }
    }

    companion object {
        private const val KEY_TAG = "tag"
    }
}

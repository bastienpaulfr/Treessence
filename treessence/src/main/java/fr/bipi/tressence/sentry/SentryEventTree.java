package fr.bipi.tressence.sentry;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import fr.bipi.tressence.base.PriorityTree;
import io.sentry.Sentry;
import io.sentry.event.Event;
import io.sentry.event.EventBuilder;

/**
 * Logger that will log to sentry server
 */
public class SentryEventTree extends PriorityTree {

    private static final String KEY_TAG = "tag";
    private static final String DEFAULT_PLATFORM = "Android";

    /**
     * @param priority priority from witch log will be logged
     */
    public SentryEventTree(int priority) {
        super(priority);
    }

    @Override
    protected void log(int priority, String tag, @NotNull String message, Throwable t) {
        if (skipLog(priority, tag, message, t)) {
            return;
        }

        EventBuilder eb = new EventBuilder();
        eb.withTag(KEY_TAG, tag)
            .withLevel(fromAndroidLogPriorityToSentryLevel(priority))
            .withMessage(message);

        Sentry.capture(eb);

        if (t != null) {
            Sentry.capture(t);
        }
    }

    private Event.Level fromAndroidLogPriorityToSentryLevel(int priority) {
        switch (priority) {
            case Log.INFO:
                return Event.Level.INFO;
            case Log.WARN:
                return Event.Level.WARNING;
            case Log.ERROR:
                return Event.Level.ERROR;
            case Log.ASSERT:
                return Event.Level.FATAL;
            case Log.DEBUG:
            case Log.VERBOSE:
            default:
                return Event.Level.DEBUG;
        }
    }
}

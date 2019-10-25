package fr.bipi.tressence.sentry;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
     * Threads from where sentry events will be sent.
     * <p>
     * Threads are needed because if connectivity is bad, then {@link Sentry#capture(Event)} can take a long time to
     * complete
     */
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    /**
     * @param priority priority from witch log will be logged
     */
    public SentryEventTree(int priority) {
        super(priority);
    }

    @Override
    protected void log(final int priority, final String tag, @NotNull final String message, final Throwable t) {
        if (skipLog(priority, tag, message, t)) {
            return;
        }

        executorService.submit(new Runnable() {
            @Override
            public void run() {

                EventBuilder eb = new EventBuilder();
                eb.withTag(KEY_TAG, tag)
                    .withLevel(fromAndroidLogPriorityToSentryLevel(priority))
                    .withMessage(message);

                Sentry.capture(eb);

                if (t != null) {
                    Sentry.capture(t);
                }
            }
        });
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

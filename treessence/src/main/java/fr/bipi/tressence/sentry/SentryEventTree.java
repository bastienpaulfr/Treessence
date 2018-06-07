package fr.bipi.tressence.sentry;

import android.os.Build;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

import fr.bipi.tressence.base.PriorityTree;
import io.sentry.Sentry;
import io.sentry.event.Event;
import io.sentry.event.EventBuilder;
import io.sentry.event.interfaces.SentryInterface;

/**
 * Logger that will log to sentry server
 */
public class SentryEventTree extends PriorityTree {

    private static final String KEY_TAG = "tag";
    private static final String DEFAULT_PLATFORM = "Android";
    @NotNull
    private Event template;

    /**
     * @param priority priority from witch log will be logged
     */
    public SentryEventTree(int priority) {
        super(priority);
        template = makeDefaultEventTemplate();
    }

    public void setTemplate(@NotNull Event template) {
        this.template = template;
    }

    public EventBuilder getEventBuilderFromLocalTemplate() {
        EventBuilder eb = new EventBuilder()
            .withDist(template.getDist())
            .withBreadcrumbs(template.getBreadcrumbs())
            .withChecksum(template.getChecksum())
            .withContexts(template.getContexts())
            .withEnvironment(template.getEnvironment())
            .withFingerprint(template.getFingerprint())
            .withLevel(template.getLevel())
            .withLogger(template.getLogger())
            .withMessage(template.getMessage())
            .withPlatform(template.getPlatform())
            .withServerName(template.getServerName())
            .withRelease(template.getRelease())
            .withTimestamp(template.getTimestamp())
            .withTransaction(template.getTransaction());
        for (Map.Entry<String, Object> entry : template.getExtra().entrySet()) {
            eb.withExtra(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<String, SentryInterface> entry : template.getSentryInterfaces().entrySet()) {
            eb.withSentryInterface(entry.getValue());
        }

        for (Map.Entry<String, String> entry : template.getTags().entrySet()) {
            eb.withTag(entry.getKey(), entry.getValue());
        }

        return eb;
    }

    @Override
    protected void log(int priority, String tag, @NotNull String message, Throwable t) {
        if (skipLog(priority, tag, message, t)) {
            return;
        }

        EventBuilder eb = getEventBuilderFromLocalTemplate();
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

    private Event makeDefaultEventTemplate() {
        return new EventBuilder()
            .withLevel(Event.Level.DEBUG)
            .withDist(Build.DISPLAY)
            .withPlatform(DEFAULT_PLATFORM)
            .withLogger(this.getClass().getCanonicalName())
            .build();
    }
}

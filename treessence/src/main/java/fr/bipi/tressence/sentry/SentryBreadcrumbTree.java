package fr.bipi.tressence.sentry;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

import fr.bipi.tressence.base.FormatterPriorityTree;
import fr.bipi.tressence.common.formatter.DefaultLogFormatter;
import fr.bipi.tressence.common.formatter.Formatter;
import io.sentry.Sentry;
import io.sentry.event.Breadcrumb;
import io.sentry.event.BreadcrumbBuilder;

/**
 * Logger that will store a Breadcrumb. Throwable are ignored.
 */
public class SentryBreadcrumbTree extends FormatterPriorityTree {

    private static final String KEY_TAG = "tag";

    /**
     * @param priority priority from witch log will be logged
     */
    public SentryBreadcrumbTree(int priority) {
        super(priority);
    }

    @Override
    protected Formatter getDefaultFormatter() {
        return DefaultLogFormatter.INSTANCE;
    }

    @Override
    protected void log(int priority, String tag, @NotNull String message, Throwable t) {
        if (skipLog(priority, tag, message, t)) {
            return;
        }

        BreadcrumbBuilder bb = new BreadcrumbBuilder();
        bb.setMessage(format(priority, tag, message))
            .setTimestamp(new Date())
            .setCategory("log")
            .setType(Breadcrumb.Type.DEFAULT)
            .setLevel(fromAndroidLogPriorityToSentryLevel(priority));

        Sentry.getContext().recordBreadcrumb(bb.build());
    }

    private Breadcrumb.Level fromAndroidLogPriorityToSentryLevel(int priority) {
        switch (priority) {
            case Log.INFO:
                return Breadcrumb.Level.INFO;
            case Log.WARN:
                return Breadcrumb.Level.WARNING;
            case Log.ERROR:
                return Breadcrumb.Level.ERROR;
            case Log.ASSERT:
                return Breadcrumb.Level.CRITICAL;
            case Log.DEBUG:
            case Log.VERBOSE:
            default:
                return Breadcrumb.Level.DEBUG;
        }
    }
}

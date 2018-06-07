package fr.bipi.tressence.sentry;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fr.bipi.tressence.base.PriorityTree;
import io.sentry.Sentry;
import io.sentry.event.Breadcrumb;
import io.sentry.event.BreadcrumbBuilder;

/**
 * Logger that will store a Breadcrumb. Throwable are ignored.
 */
public class SentryBreadcrumbTree extends PriorityTree {

    private static final String KEY_TAG = "tag";
    @NotNull
    private Breadcrumb template;

    /**
     * @param priority priority from witch log will be logged
     */
    public SentryBreadcrumbTree(int priority) {
        super(priority);
        setTemplate(makeDefaultEventTemplate());
    }

    public void setTemplate(@NotNull Breadcrumb template) {
        this.template = template;
    }

    public BreadcrumbBuilder getBreadcrumbBuilderFromLocalTemplate() {
        BreadcrumbBuilder bb = new BreadcrumbBuilder();
        bb.setType(template.getType())
            .setData(template.getData())
            .setLevel(template.getLevel())
            .setMessage(template.getMessage())
            .setCategory(template.getCategory());
        return bb;
    }

    @Override
    protected void log(int priority, String tag, @NotNull String message, Throwable t) {
        if (skipLog(priority, tag, message, t)) {
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put(KEY_TAG, tag);

        BreadcrumbBuilder bb = getBreadcrumbBuilderFromLocalTemplate();
        bb.setMessage(message)
            .setTimestamp(new Date())
            .setData(map)
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

    private Breadcrumb makeDefaultEventTemplate() {
        return new BreadcrumbBuilder()
            .setCategory("")
            .setMessage("")
            .setLevel(Breadcrumb.Level.DEBUG)
            .setData(null)
            .setType(Breadcrumb.Type.DEFAULT)
            .build();
    }
}

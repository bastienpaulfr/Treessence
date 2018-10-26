package fr.bipi.tressence.console;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import fr.bipi.tressence.base.FormatterPriorityTree;
import fr.bipi.tressence.common.formatter.Formatter;
import fr.bipi.tressence.common.formatter.LogcatFormatter;
import timber.log.Timber;


/**
 * An implementation of {@link Timber.Tree} which log using {@link System#out} print() method.
 */
public class SystemLogTree extends FormatterPriorityTree {

    public SystemLogTree() {
        this(Log.VERBOSE);
    }

    public SystemLogTree(int priority) {
        super(priority);
    }

    @Override
    protected Formatter getDefaultFormatter() {
        return LogcatFormatter.INSTANCE;
    }

    @Override
    protected void log(int priority, String tag, @NotNull String message, Throwable t) {
        if (skipLog(priority, tag, message, t)) {
            return;
        }
        System.out.print(format(priority, tag, message));
        if (t != null) {
            t.printStackTrace();
        }
    }
}

package fr.bipi.tressence.ui;

import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

import fr.bipi.tressence.base.FormatterPriorityTree;
import fr.bipi.tressence.formatter.Formatter;
import fr.bipi.tressence.formatter.NoTagFormatter;

/**
 * An implementation of `Timber.Tree` which sends log into a text view
 */
public class TextViewTree extends FormatterPriorityTree {

    private final UiHandler handler = new UiHandler();
    private WeakReference<TextView> textViewWeakReference = new WeakReference<>(null);

    public TextViewTree(int priority) {
        super(priority);
    }

    public void setTextView(TextView tv) {
        textViewWeakReference = new WeakReference<>(tv);
    }

    @Override
    protected Formatter getDefaultFormatter() {
        return NoTagFormatter.INSTANCE;
    }

    @Override
    protected void log(final int priority, final String tag, @NotNull final String message, Throwable t) {
        if (skipLog(priority, tag, message, t)) {
            return;
        }

        handler.post(new Runnable() {
            @Override
            public void run() {
                TextView tv = textViewWeakReference.get();
                if (tv != null) {
                    tv.setText(format(priority, tag, message));
                }
            }
        });
    }

    private static class UiHandler extends Handler {
        UiHandler() {
            super(Looper.getMainLooper());
        }
    }
}

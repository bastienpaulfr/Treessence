package fr.bipi.tressence.ui

import android.os.Handler
import android.os.Looper
import android.widget.TextView
import fr.bipi.tressence.base.FormatterPriorityTree
import fr.bipi.tressence.common.filters.Filter
import fr.bipi.tressence.common.filters.NoFilter
import fr.bipi.tressence.common.formatter.Formatter
import fr.bipi.tressence.common.formatter.NoTagFormatter
import java.lang.ref.WeakReference


/**
 * An implementation of `Timber.Tree` which sends log into a text view
 */
class TextViewTree @JvmOverloads constructor(
    priority: Int,
    filter: Filter = NoFilter.INSTANCE,
    formatter: Formatter = NoTagFormatter.INSTANCE,
    private val append: Boolean = true
) : FormatterPriorityTree(
    priority, filter,
    formatter
) {
    private val handler = UiHandler()
    private var textViewWeakReference = WeakReference<TextView?>(null)

    fun setTextView(tv: TextView?) {
        textViewWeakReference = WeakReference(tv)
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (skipLog(priority, tag, message, t)) {
            return
        }
        handler.post {
            if (append) {
                textViewWeakReference.get()?.text = textViewWeakReference.get()?.text?.toString() ?: "" + format(priority, tag, message)
            } else {
                textViewWeakReference.get()?.text = format(priority, tag, message)
            }
        }
    }

    private class UiHandler : Handler(Looper.getMainLooper())
}

package fr.bipi.tressence.dsl

import android.widget.TextView
import fr.bipi.tressence.common.filters.mergeFilters
import fr.bipi.tressence.common.formatter.LogcatFormatter
import fr.bipi.tressence.ui.TextViewTree
import timber.log.Timber

typealias TextViewTreeDeclaration = TextViewTreeScope.() -> Unit

class TextViewTreeScope : TreeScope() {
    var textView: TextView? = null
    var append: Boolean = true
}

/**
 * Builder for [TextViewTree]
 */
object TextViewTreeBuilder {
    fun build(data: TextViewTreeScope): Timber.Tree {
        return with(data) {
            TextViewTree(
                priority = level,
                filter = filters.mergeFilters(),
                formatter = formatter ?: LogcatFormatter.INSTANCE,
                append
            ).apply {
                setTextView(textView)
            }
        }
    }
}

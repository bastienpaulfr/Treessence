package fr.bipi.treessence.dsl

import android.widget.TextView
import fr.bipi.treessence.common.filters.mergeFilters
import fr.bipi.treessence.common.formatter.LogcatFormatter
import fr.bipi.treessence.ui.TextViewTree

typealias TextViewTreeDeclaration = TextViewTreeScope.() -> Unit

class TextViewTreeScope : TreeScope() {
    var textView: TextView? = null
    var append: Boolean = true
}

/**
 * Builder for [TextViewTree]
 */
object TextViewTreeBuilder {
    fun build(data: TextViewTreeScope) = with(data) {
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

package fr.bipi.tressence.dsl

import android.util.Log
import fr.bipi.tressence.base.FormatterPriorityTree
import fr.bipi.tressence.base.PriorityTree
import fr.bipi.tressence.common.filters.Filter
import fr.bipi.tressence.common.filters.mergeFilters
import fr.bipi.tressence.common.formatter.Formatter
import fr.bipi.tressence.common.formatter.SimpleFormatter
import timber.log.Timber


typealias TreeDeclaration = TreeScope.() -> Unit

typealias FilterDeclaration = (priority: Int, tag: String?, message: String, t: Throwable?) -> Boolean

typealias FormatterDeclaration = (priority: Int, tag: String?, message: String) -> String

/**
 * Scope for Timber DSL
 */
open class TreeScope {
    internal val filters = mutableListOf<Filter>()
    internal var formatter: Formatter? = null

    var level: Int = Log.VERBOSE

    fun filter(vararg filter: Filter) {
        filters.addAll(filter)
    }

    fun filter(lambda: FilterDeclaration) {
        filters.add(object : Filter {
            override fun isLoggable(priority: Int, tag: String?) = true

            override fun skipLog(priority: Int, tag: String?, message: String, t: Throwable?) = lambda(priority, tag, message, t)
        })
    }

    fun formatter(formatter: Formatter) {
        this.formatter = formatter
    }

    fun formatter(lambda: FormatterDeclaration) {
        this.formatter = object : Formatter {
            override fun format(priority: Int, tag: String?, message: String) = lambda(priority, tag, message)
        }
    }
}

object TreeBuilder {
    /**
     * Build a [Timber.Tree] to log into logcat
     */
    fun buildLogcat(data: TreeScope): Timber.Tree {
        val fo = data.formatter

        return when {
            fo != null -> {
                FormatterPriorityTree(data.level, data.filters.mergeFilters(), fo)
            }
            else -> {
                PriorityTree(data.level, data.filters.mergeFilters())
            }
        }
    }

    /**
     * Build regular [Timber.Tree]
     */
    fun buildTree(
        writer: Writer,
        data: TreeScope
    ): Timber.Tree {
        return object : Timber.Tree() {
            val filter = data.filters.mergeFilters()
            val form = data.formatter ?: SimpleFormatter.INSTANCE

            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                with(data) {
                    if (priority >= level
                        && filter.isLoggable(priority, tag)
                        && !filter.skipLog(priority, tag, message, t)
                    ) {
                        writer(form.format(priority, tag, message), t)
                    }
                }
            }
        }
    }
}

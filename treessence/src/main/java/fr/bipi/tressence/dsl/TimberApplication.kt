package fr.bipi.tressence.dsl

import android.util.Log
import fr.bipi.tressence.base.FormatterPriorityTree
import fr.bipi.tressence.base.PriorityTree
import fr.bipi.tressence.common.filters.Filter
import fr.bipi.tressence.common.formatter.Formatter
import timber.log.Timber

typealias TimberDeclaration = TimberApplication.() -> Unit

// Lambda that writes string somewhere
typealias Writer = (s: String, t: Throwable?) -> Unit

/**
 * Object that describes Timber DSL
 */
object TimberApplication {
    fun debugTree(): Timber.DebugTree {
        return Timber.DebugTree().also {
            Timber.plant(it)
        }
    }

    fun releaseTree(): PriorityTree {
        return PriorityTree(Log.INFO).also {
            Timber.plant(it)
        }
    }

    fun priorityTree(level: Int) {
        Timber.plant(PriorityTree(level))
    }

    fun filterTree(level: Int, filter: (priority: Int, tag: String?, message: String, t: Throwable?) -> Boolean) {
        Timber.plant(PriorityTree(level, object : Filter {
            override fun isLoggable(priority: Int, tag: String?) = true

            override fun skipLog(priority: Int, tag: String?, message: String, t: Throwable?) = filter(priority, tag, message, t)
        }))
    }

    fun formatterTree(level: Int, formatter: (priority: Int, tag: String?, message: String) -> String) {
        Timber.plant(FormatterPriorityTree(level, formatter = object : Formatter {
            override fun format(priority: Int, tag: String?, message: String) = formatter(priority, tag, message)
        }))
    }

    fun filterAndFormatterTree(
        level: Int,
        filter: (priority: Int, tag: String?, message: String, t: Throwable?) -> Boolean,
        formatter: (priority: Int, tag: String?, message: String) -> String
    ) {
        Timber.plant(
            FormatterPriorityTree(
                level,
                filter = object : Filter {
                    override fun isLoggable(priority: Int, tag: String?) = true

                    override fun skipLog(priority: Int, tag: String?, message: String, t: Throwable?) = filter(priority, tag, message, t)
                },
                formatter = object : Formatter {
                    override fun format(priority: Int, tag: String?, message: String) = formatter(priority, tag, message)
                }
            )
        )
    }

    fun tree(
        writer: Writer,
        declaration: TreeDeclaration
    ): Timber.Tree {
        val data = TreeScope()
        declaration(data)
        return TreeBuilder.buildTree(writer, data).also {
            Timber.plant(it)
        }
    }

    fun logcatTree(declaration: TreeDeclaration) {
        val data = TreeScope()
        declaration(data)
        Timber.plant(TreeBuilder.buildLogcat(data))
    }

    fun fileTree(declaration: FileTreeDeclaration) {
        val data = FileTreeScope()
        declaration(data)
        Timber.plant(FileTreeBuilder.build(data))
    }

    fun systemTree(declaration: TreeDeclaration) {
        val data = TreeScope()
        declaration(data)
        Timber.plant(SystemTreeBuilder.build(data))
    }

    fun throwErrorTree(declaration: TreeDeclaration) {
        val data = TreeScope()
        declaration(data)
        Timber.plant(ThrowErrorTreeBuilder.build(data))
    }

    fun sentryBreadCrumbTree(declaration: TreeDeclaration) {
        val data = TreeScope()
        declaration(data)
        Timber.plant(SentryTreeBuilder.buildBreadCrumbTree(data))
    }

    fun sentryEventTree(declaration: TreeDeclaration) {
        val data = TreeScope()
        declaration(data)
        Timber.plant(SentryTreeBuilder.buildEventTree(data))
    }

    fun textViewTree(declaration: TextViewTreeDeclaration) {
        val data = TextViewTreeScope()
        declaration(data)
        Timber.plant(TextViewTreeBuilder.build(data))
    }
}

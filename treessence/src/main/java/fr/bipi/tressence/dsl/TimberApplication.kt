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

    fun priorityTree(level: Int): PriorityTree = PriorityTree(level).also {
        Timber.plant(it)
    }

    fun filterTree(level: Int, filter: (priority: Int, tag: String?, message: String, t: Throwable?) -> Boolean) =
        PriorityTree(level, object : Filter {
            override fun isLoggable(priority: Int, tag: String?) = true

            override fun skipLog(priority: Int, tag: String?, message: String, t: Throwable?) = filter(priority, tag, message, t)
        }).also {
            Timber.plant(it)
        }

    fun formatterTree(level: Int, formatter: (priority: Int, tag: String?, message: String) -> String) =
        FormatterPriorityTree(level, formatter = object : Formatter {
            override fun format(priority: Int, tag: String?, message: String) = formatter(priority, tag, message)
        }).also {
            Timber.plant(it)
        }

    fun filterAndFormatterTree(
        level: Int,
        filter: (priority: Int, tag: String?, message: String, t: Throwable?) -> Boolean,
        formatter: (priority: Int, tag: String?, message: String) -> String
    ) = FormatterPriorityTree(
        level,
        filter = object : Filter {
            override fun isLoggable(priority: Int, tag: String?) = true

            override fun skipLog(priority: Int, tag: String?, message: String, t: Throwable?) = filter(priority, tag, message, t)
        },
        formatter = object : Formatter {
            override fun format(priority: Int, tag: String?, message: String) = formatter(priority, tag, message)
        }
    ).also {
        Timber.plant(it)
    }

    fun tree(
        writer: Writer,
        declaration: TreeDeclaration
    ) = with(declaration) {
        val data = TreeScope()
        this(data)
        TreeBuilder.buildTree(writer, data).also {
            Timber.plant(it)
        }
    }

    fun logcatTree(declaration: TreeDeclaration) = with(declaration) {
        val data = TreeScope()
        this(data)
        TreeBuilder.buildLogcat(data).also {
            Timber.plant(it)
        }
    }

    fun fileTree(declaration: FileTreeDeclaration) = with(declaration) {
        val data = FileTreeScope()
        this(data)
        FileTreeBuilder.build(data).also {
            Timber.plant(it)
        }
    }

    fun systemTree(declaration: TreeDeclaration) = with(declaration) {
        val data = TreeScope()
        this(data)
        SystemTreeBuilder.build(data).also {
            Timber.plant(it)
        }
    }

    fun throwErrorTree(declaration: TreeDeclaration) = with(declaration) {
        val data = TreeScope()
        this(data)
        ThrowErrorTreeBuilder.build(data).also {
            Timber.plant(it)
        }
    }

    fun sentryBreadCrumbTree(declaration: TreeDeclaration) = with(declaration) {
        val data = TreeScope()
        this(data)
        SentryTreeBuilder.buildBreadCrumbTree(data).also {
            Timber.plant(it)
        }
    }

    fun sentryEventTree(declaration: TreeDeclaration) = with(declaration) {
        val data = TreeScope()
        this(data)
        SentryTreeBuilder.buildEventTree(data).also {
            Timber.plant(it)
        }
    }

    fun textViewTree(declaration: TextViewTreeDeclaration) = with(declaration) {
        val data = TextViewTreeScope()
        this(data)
        TextViewTreeBuilder.build(data).also {
            Timber.plant(it)
        }
    }
}

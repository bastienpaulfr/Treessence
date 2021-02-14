package fr.bipi.tressence.dsl

import android.util.Log
import fr.bipi.tressence.common.filters.NoFilter
import fr.bipi.tressence.context.GlobalContext.startTimber
import fr.bipi.tressence.context.GlobalContext.stopTimber
import org.amshove.kluent.`should be equal to`
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import timber.log.Timber


class DslTest {

    @Rule
    @JvmField
    val temporaryFolder = TemporaryFolder()

    @Test
    fun startTimberDsl() {
        startTimber {
            debugTree()

            releaseTree()

            tree(
                { s: String, t: Throwable? ->

                }
            ) {
                level = Log.INFO

                filter(NoFilter())

                filter { prio, tag, m, t ->
                    false
                }

                formatter { prio, tag, message ->
                    ""
                }
            }

            logcatTree {
                level = Log.INFO

                filter(NoFilter())

                filter { prio, tag, m, t ->
                    false
                }

                formatter { prio, tag, message ->
                    ""
                }
            }

            fileTree {
                level = Log.INFO
                fileName = "myfile"
                dir = temporaryFolder.newFolder().absolutePath
                sizeLimit = 10
                fileLimit = 1
                appendToFile = true

                filter { prio, tag, m, t ->
                    false
                }

                formatter { prio, tag, message ->
                    ""
                }
            }

            systemTree {
                level = Log.INFO

                filter(NoFilter())

                filter { prio, tag, m, t ->
                    false
                }

                formatter { prio, tag, message ->
                    ""
                }
            }

            throwErrorTree {
                level = Log.INFO

                filter(NoFilter())

                filter { prio, tag, m, t ->
                    false
                }

                formatter { prio, tag, message ->
                    ""
                }
            }

            sentryBreadCrumbTree {
                level = Log.INFO

                filter(NoFilter())

                filter { prio, tag, m, t ->
                    false
                }

                formatter { prio, tag, message ->
                    ""
                }
            }

            sentryEventTree {
                level = Log.INFO

                filter(NoFilter())

                filter { prio, tag, m, t ->
                    false
                }

                formatter { prio, tag, message ->
                    ""
                }
            }

            textViewTree {
                level = Log.INFO
                append = true

                filter(NoFilter())

                filter { prio, tag, m, t ->
                    false
                }

                formatter { prio, tag, message ->
                    ""
                }
            }
        }

        Timber.treeCount().`should be equal to`(10)
    }

    @Test
    fun debugTree() {
        var tree: Timber.Tree? = null
        startTimber {
            debugTree().also {
                tree = it
            }
        }

        Timber.treeCount().`should be equal to`(1)
        Timber.forest()[0].`should be equal to`(tree)

        stopTimber()

        Timber.treeCount().`should be equal to`(0)
    }

    @Test
    fun releaseTree() {
        var tree: Timber.Tree? = null
        startTimber {
            releaseTree().also {
                tree = it
            }
        }

        Timber.treeCount().`should be equal to`(1)
        Timber.forest()[0].`should be equal to`(tree)

        stopTimber()

        Timber.treeCount().`should be equal to`(0)
    }

    @Test
    fun tree() {
        var tree: Timber.Tree? = null
        val sb = StringBuilder()
        startTimber {
            tree(
                { s: String, t: Throwable? ->
                    sb.append("$s:$t\n")
                }
            ) {
                level = Log.INFO

                filter(NoFilter())

                filter { prio, tag, m, t ->
                    false
                }

                formatter { prio, tag, message ->
                    "$prio:$tag:$message"
                }
            }.also {
                tree = it
            }
        }

        Timber.treeCount().`should be equal to`(1)
        Timber.forest()[0].`should be equal to`(tree)

        Timber.tag("tag").i("message")

        sb.toString().`should be equal to`("4:tag:message:null\n")

        stopTimber()

        Timber.treeCount().`should be equal to`(0)
    }

}

package fr.bipi.tressence.common.filters

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MergeFilterTest {

    private lateinit var filters: MutableList<Filter>
    private val accept = object : Filter {
        override fun skipLog(priority: Int, tag: String?, message: String, t: Throwable?): Boolean = false
        override fun isLoggable(priority: Int, tag: String?): Boolean = true
    }
    private val deny = object : Filter {
        override fun skipLog(priority: Int, tag: String?, message: String, t: Throwable?): Boolean = true
        override fun isLoggable(priority: Int, tag: String?): Boolean = false
    }

    @Before
    fun setUp() {

        filters = mutableListOf(accept, accept, accept, deny)
    }

    @Test
    fun oneDeny() {
        val filter = MergeFilter().apply {
            filters.addAll(mutableListOf(accept, accept, accept, deny))
        }
        assertTrue(filter.skipLog(0, "", "", null))
        assertFalse(filter.isLoggable(0, ""))
    }

    @Test
    fun accept() {
        val filter = MergeFilter().apply {
            filters.addAll(mutableListOf(accept, accept, accept))
        }
        assertFalse(filter.skipLog(0, "", "", null))
        assertTrue(filter.isLoggable(0, ""))
    }
}

package fr.bipi.tressence.common.filters

import org.amshove.kluent.`should be instance of`
import org.amshove.kluent.`should be`
import org.amshove.kluent.`should contain same`
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

    @Test
    fun `empty list gives no filters`() {
        emptyList<Filter>().mergeFilters().`should be instance of`(NoFilter::class)
    }

    @Test
    fun `single list return its own filter`() {
        val filter = PriorityFilter(1)
        listOf(filter).mergeFilters().`should be`(filter)
    }

    @Test
    fun `multi filter list return a merge filter containing all filters`() {
        val prio = PriorityFilter(1)
        val tag = TagFilter("tag")
        val merge = listOf(prio, tag).mergeFilters() as MergeFilter
        merge.filters.`should contain same`(listOf(prio, tag))
    }
}

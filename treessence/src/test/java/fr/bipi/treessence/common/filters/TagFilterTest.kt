package fr.bipi.treessence.common.filters

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.Test
import java.util.regex.Pattern

class TagFilterTest {

    @Test
    fun skipLog() {
        val f1 = TagFilter("Filter.*")
        assertThat(f1.skipLog(0, "prout", "pouet", null), Matchers.`is`(true))
        assertThat(f1.skipLog(0, "Filter", "pouet", null), Matchers.`is`(false))
        assertThat(f1.skipLog(0, "FilterProut", "pouet", null), Matchers.`is`(false))
        val f2 = TagFilter(Pattern.compile("Filter.*"))
        assertThat(f2.skipLog(0, "prout", "pouet", null), Matchers.`is`(true))
        assertThat(f2.skipLog(0, "Filter", "pouet", null), Matchers.`is`(false))
        assertThat(f2.skipLog(0, "FilterProut", "pouet", null), Matchers.`is`(false))
    }

    @Test
    fun isLoggable() {
        val f1 = TagFilter("Filter.*")
        assertThat(f1.isLoggable(0, "prout"), Matchers.`is`(false))
        assertThat(f1.isLoggable(0, "Filter"), Matchers.`is`(true))
        assertThat(f1.isLoggable(0, "FilterProut"), Matchers.`is`(true))
        val f2 = TagFilter(Pattern.compile("Filter.*"))
        assertThat(f2.isLoggable(0, "prout"), Matchers.`is`(false))
        assertThat(f2.isLoggable(0, "Filter"), Matchers.`is`(true))
        assertThat(f2.isLoggable(0, "FilterProut"), Matchers.`is`(true))
    }

    @Test
    fun tagRegex() {
        assertThat(TagFilter("Filter.*").tagRegex, Matchers.`is`("Filter.*"))
        assertThat(TagFilter(Pattern.compile("Filter.*")).tagRegex, Matchers.`is`("Filter.*"))
    }
}

package fr.bipi.tressence.common.filters;

import org.junit.Test;

import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TagFilterTest {

    private TagFilter filter;

    @Test
    public void skipLog() {
        filter = new TagFilter("Filter.*");
        assertThat(filter.skipLog(0, "prout", "pouet", null), is(true));
        assertThat(filter.skipLog(0, "Filter", "pouet", null), is(false));
        assertThat(filter.skipLog(0, "FilterProut", "pouet", null), is(false));

        filter = new TagFilter(Pattern.compile("Filter.*"));
        assertThat(filter.skipLog(0, "prout", "pouet", null), is(true));
        assertThat(filter.skipLog(0, "Filter", "pouet", null), is(false));
        assertThat(filter.skipLog(0, "FilterProut", "pouet", null), is(false));
    }

    @Test
    public void isLoggable() {
        filter = new TagFilter("Filter.*");
        assertThat(filter.isLoggable(0, "prout"), is(false));
        assertThat(filter.isLoggable(0, "Filter"), is(true));
        assertThat(filter.isLoggable(0, "FilterProut"), is(true));

        filter = new TagFilter(Pattern.compile("Filter.*"));
        assertThat(filter.isLoggable(0, "prout"), is(false));
        assertThat(filter.isLoggable(0, "Filter"), is(true));
        assertThat(filter.isLoggable(0, "FilterProut"), is(true));
    }

    @Test
    public void getTagRegex() {
        filter = new TagFilter("Filter.*");
        assertThat(filter.getTagRegex(), is("Filter.*"));

        filter = new TagFilter(Pattern.compile("Filter.*"));
        assertThat(filter.getTagRegex(), is("Filter.*"));
    }
}

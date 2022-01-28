package fr.bipi.treessence.common.filters;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class PriorityFilterTest {

    @Test
    public void test() {
        PriorityFilter filter = new PriorityFilter(3);
        assertThat(filter.getMinPriority(), is(3));
        assertThat(filter.isLoggable(3, ""), is(true));
        assertThat(filter.isLoggable(4, ""), is(true));
        assertThat(filter.isLoggable(2, ""), is(false));

        assertThat(filter.skipLog(3, "", "", null), is(false));
        assertThat(filter.skipLog(4, "", "", null), is(false));
        assertThat(filter.skipLog(2, "", "", null), is(true));
    }
}

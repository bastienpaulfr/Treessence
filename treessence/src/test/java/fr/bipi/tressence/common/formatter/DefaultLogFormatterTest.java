package fr.bipi.tressence.common.formatter;

import org.junit.Before;
import org.junit.Test;

import fr.bipi.tressence.robolectric.RobolectricTest;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


public class DefaultLogFormatterTest extends RobolectricTest {

    private DefaultLogFormatter defaultLogFormatter;

    @Before
    public void before() {
        defaultLogFormatter = DefaultLogFormatter.Companion.getINSTANCE();
    }

    @Test
    public void test() {
        assertThat(defaultLogFormatter.format(1, "tag", "message"),
                   is("tag : message\n"));
        assertThat(defaultLogFormatter.format(2, "tag", "message"),
                   is("V/tag : message\n"));
        assertThat(defaultLogFormatter.format(3, "tag", "message"),
                   is("D/tag : message\n"));
        assertThat(defaultLogFormatter.format(4, "tag", "message"),
                   is("I/tag : message\n"));
        assertThat(defaultLogFormatter.format(5, "tag", "message"),
                   is("W/tag : message\n"));
        assertThat(defaultLogFormatter.format(6, "tag", "message"),
                   is("E/tag : message\n"));
        assertThat(defaultLogFormatter.format(7, "tag", "message"),
                   is("WTF/tag : message\n"));
        assertThat(defaultLogFormatter.format(8, "tag", "message"),
                   is("tag : message\n"));
    }
}

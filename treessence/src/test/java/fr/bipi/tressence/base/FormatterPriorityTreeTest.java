package fr.bipi.tressence.base;

import android.util.Log;

import org.junit.Test;

import fr.bipi.tressence.common.formatter.Formatter;
import fr.bipi.tressence.robolectric.RobolectricTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class FormatterPriorityTreeTest extends RobolectricTest {

    private FormatterPriorityTree tree;

    @Test
    public void test() {
        tree = new FormatterPriorityTree(Log.DEBUG).withFormatter(new Formatter() {
            @Override
            public String format(int priority, String tag, String message) {
                return "" + priority + ", " + tag + ", " + message;
            }
        });

        assertThat(tree.format(1, "tag", "message"), is("1, tag, message"));
        tree.d("message");
        assertLog().hasDebugMessage("NativeMethodAccessorImp", "3, NativeMethodAccessorImp, message")
            .hasNoMoreMessages();
    }
}
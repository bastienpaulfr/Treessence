package fr.bipi.treessence.base;

import android.util.Log;

import org.junit.Test;

import fr.bipi.treessence.common.filters.NoFilter;
import fr.bipi.treessence.robolectric.RobolectricTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class FormatterPriorityTreeTest extends RobolectricTest {

    private FormatterPriorityTree tree;

    @Test
    public void test() {
        tree = new FormatterPriorityTree(Log.DEBUG,
                                         NoFilter.Companion.getINSTANCE(),
                                         (priority, tag, message) -> "" + priority + ", " + tag + ", " + message);

        assertThat(tree.format(1, "tag", "message"), is("1, tag, message"));
        tree.d("message");
        assertLog().hasDebugMessage("FormatterPriorityTreeTest", "3, FormatterPriorityTreeTest, message")
            .hasNoMoreMessages();
    }
}

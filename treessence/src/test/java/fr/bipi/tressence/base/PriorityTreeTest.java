package fr.bipi.tressence.base;

import android.util.Log;

import org.junit.Test;

import fr.bipi.tressence.robolectric.RobolectricTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PriorityTreeTest extends RobolectricTest {

    private PriorityTree tree;

    @Test
    public void testVerbose() {
        tree = new PriorityTree(Log.VERBOSE);
        assertThat(tree.isLoggable("", Log.VERBOSE), is(true));
        assertThat(tree.isLoggable("", Log.DEBUG), is(true));
        assertThat(tree.isLoggable("", Log.INFO), is(true));
        assertThat(tree.isLoggable("", Log.WARN), is(true));
        assertThat(tree.isLoggable("", Log.ERROR), is(true));
        assertThat(tree.isLoggable("", Log.ASSERT), is(true));
    }

    @Test
    public void testAssert() {
        tree = new PriorityTree(Log.ASSERT);
        assertThat(tree.isLoggable("", Log.VERBOSE), is(false));
        assertThat(tree.isLoggable("", Log.DEBUG), is(false));
        assertThat(tree.isLoggable("", Log.INFO), is(false));
        assertThat(tree.isLoggable("", Log.WARN), is(false));
        assertThat(tree.isLoggable("", Log.ERROR), is(false));
        assertThat(tree.isLoggable("", Log.ASSERT), is(true));
    }

    @Test
    public void log() {
        tree = new PriorityTree(Log.VERBOSE);
        tree.v("log");
        assertLog()
            .hasVerboseMessage("NativeMethodAccessorImp", "log")
            .hasNoMoreMessages();
    }

    @Test
    public void noLog() {
        tree = new PriorityTree(Log.DEBUG);
        tree.v("log");
        assertLog().hasNoMoreMessages();
    }
}
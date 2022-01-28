package fr.bipi.treessence.console;

import android.util.Log;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ThrowErrorTreeTest {
    private static final String TAG = "ThrowErrorTreeTest";

    @Test
    public void testIsLoggable() throws Exception {
        ThrowErrorTree tree = new ThrowErrorTree();
        assertTrue(tree.isLoggable(TAG, Log.ERROR));
        assertFalse(tree.isLoggable(TAG, Log.WARN));

        tree = new ThrowErrorTree(Log.INFO);
        assertTrue(tree.isLoggable(TAG, Log.INFO));
        assertFalse(tree.isLoggable(TAG, Log.DEBUG));
    }

    @Test
    public void testLog() throws Exception {
        ThrowErrorTree tree = new ThrowErrorTree();
        Throwable testThrowable = new Throwable("test");
        try {
            tree.log(Log.INFO, "tag", "message", testThrowable);
            fail("Expected LogPriorityExceededError");
        } catch (LogPriorityExceededError e) {
            assertEquals(testThrowable, e.getCause());
        }
    }
}

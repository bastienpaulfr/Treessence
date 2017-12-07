package fr.bipi.tressence.crash;

import android.util.Log;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CrashlyticsLogExceptionTreeTest {
    @Test
    public void testIsLoggable() throws Exception {
        CrashlyticsLogExceptionTree tree = new CrashlyticsLogExceptionTree();
        assertTrue(tree.isLoggable("tag", Log.ERROR));
        assertFalse(tree.isLoggable("tag", Log.WARN));

        tree = new CrashlyticsLogExceptionTree(Log.INFO);
        assertTrue(tree.isLoggable("tag", Log.INFO));
        assertFalse(tree.isLoggable("tag", Log.DEBUG));
    }
}
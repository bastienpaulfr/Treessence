package fr.bipi.tressence.crash;

import android.util.Log;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CrashlyticsLogTreeTest {

    private static final String TAG = "CrashlyticsLogTreeTest";

    @Test
    public void testIsLoggable() throws Exception {
        CrashlyticsLogTree tree = new CrashlyticsLogTree();
        assertTrue(tree.isLoggable(TAG, Log.WARN));
        assertFalse(tree.isLoggable(TAG, Log.INFO));

        tree = new CrashlyticsLogTree(Log.INFO);
        assertTrue(tree.isLoggable(TAG, Log.INFO));
        assertFalse(tree.isLoggable(TAG, Log.DEBUG));
    }
}
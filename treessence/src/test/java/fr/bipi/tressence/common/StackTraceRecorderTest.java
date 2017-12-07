package fr.bipi.tressence.common;

import org.junit.After;
import org.junit.Test;

import timber.log.Timber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StackTraceRecorderTest {
    private TestRecorderTree mTestRecorderTree;

    @After
    public void tearDown() throws Exception {
        if (mTestRecorderTree != null) {
            Timber.uproot(mTestRecorderTree);
        }
    }

    @Test
    public void testFillInStackTrace() throws Exception {
        final StackTraceRecorder[] stackTraceRecorderArray = new StackTraceRecorder[1];
        mTestRecorderTree = new TestRecorderTree(stackTraceRecorderArray);
        Timber.plant(mTestRecorderTree);
        Timber.e("hoge");
        StackTraceRecorder stackTraceRecorder = stackTraceRecorderArray[0];
        assertNotNull(stackTraceRecorder);
        assertEquals("fr.bipi.tressence.common.StackTraceRecorderTest",
                     stackTraceRecorder.getStackTrace()[0].getClassName());
        assertEquals("testFillInStackTrace", stackTraceRecorder.getStackTrace()[0].getMethodName());
    }

    private static class TestRecorderTree extends Timber.Tree {
        private final StackTraceRecorder[] stackTraceRecorderArray;

        public TestRecorderTree(StackTraceRecorder[] stackTraceRecorderArray) {
            this.stackTraceRecorderArray = stackTraceRecorderArray;
        }

        @Override
        protected void log(int i, String s, String s1, Throwable throwable) {
            stackTraceRecorderArray[0] = new StackTraceRecorder("hoge");
        }
    }
}
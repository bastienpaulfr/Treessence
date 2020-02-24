package fr.bipi.tressence.robolectric;

import android.os.Build;
import android.util.Log;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


/**
 * Base class extended by every Robolectric test in this project.
 * <p>
 * Robolectric tests are done in a single thread !
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P)
public abstract class RobolectricTest {

    private AtomicBoolean unblock = new AtomicBoolean(false);

    @BeforeClass
    public static void beforeClass() {
        //Configure robolectric
        ShadowLog.stream = System.out;
    }

    @Before
    public void roboSetup() {
        ShadowLog.clear();
    }

    public static LogAssert assertLog() {
        return new LogAssert(ShadowLog.getLogs());
    }

    public void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void unblock() {
        unblock.set(true);
    }

    public void block() {
        await().untilTrue(unblock);
        unblock.set(false);
    }

    public static final class LogAssert {
        private final List<ShadowLog.LogItem> items;
        private int index = 0;

        private LogAssert(List<ShadowLog.LogItem> items) {
            this.items = items;
        }

        public LogAssert hasVerboseMessage(String tag, String message) {
            return hasMessage(Log.VERBOSE, tag, message);
        }

        public LogAssert hasDebugMessage(String tag, String message) {
            return hasMessage(Log.DEBUG, tag, message);
        }

        public LogAssert hasInfoMessage(String tag, String message) {
            return hasMessage(Log.INFO, tag, message);
        }

        public LogAssert hasWarnMessage(String tag, String message) {
            return hasMessage(Log.WARN, tag, message);
        }

        public LogAssert hasErrorMessage(String tag, String message) {
            return hasMessage(Log.ERROR, tag, message);
        }

        public LogAssert hasAssertMessage(String tag, String message) {
            return hasMessage(Log.ASSERT, tag, message);
        }

        private LogAssert hasMessage(int priority, String tag, String message) {
            ShadowLog.LogItem item = items.get(index++);
            assertThat(item.type, is(priority));
            assertThat(item.tag, is(tag));
            assertThat(item.msg, is(message));
            return this;
        }

        public void hasNoMoreMessages() {
            assertThat(items.size(), is(index));
        }
    }
}

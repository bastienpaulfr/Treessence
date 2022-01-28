package fr.bipi.treessence.console;

import android.util.Log;

import org.junit.Test;

import java.util.TimeZone;

import fr.bipi.treessence.common.filters.NoFilter;
import fr.bipi.treessence.common.formatter.LogcatFormatter;
import fr.bipi.treessence.common.time.TimeStamper;

public class SystemLogTreeTest {

    @Test
    public void log() {
        SystemLogTree tree = new SystemLogTree(Log.VERBOSE,
                                               NoFilter.Companion.getINSTANCE(),
                                               LogcatFormatter.Companion.getINSTANCE());

        LogcatFormatter.Companion.getINSTANCE().setTimeStamper(new TimeStamper("MM-dd HH:mm:ss:SSS",
                                                                               TimeZone.getTimeZone("GMT+2")));
    }
}

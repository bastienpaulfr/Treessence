package fr.bipi.tressence.console;

import org.junit.Test;

import java.util.TimeZone;

import fr.bipi.tressence.common.formatter.LogcatFormatter;
import fr.bipi.tressence.common.time.TimeStamper;

public class SystemLogTreeTest {

    @Test
    public void log() {
        SystemLogTree tree = new SystemLogTree();
        tree.setFormatter(LogcatFormatter.INSTANCE);

        LogcatFormatter.INSTANCE.setTimeStamper(new TimeStamper("MM-dd HH:mm:ss:SSS", TimeZone.getTimeZone("GMT+2")));
    }
}

package fr.bipi.treessence.common.utils;

import org.junit.Test;

import java.util.TimeZone;

import fr.bipi.treessence.common.time.TimeStamper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TimeStamperTest {

    @Test
    public void tzUTC() {
        TimeStamper timeStamper = new TimeStamper("yyyyMMddHHmmss", TimeZone.getTimeZone("UTC"));
        assertThat(timeStamper.getCurrentTimeStamp(1571991195000L), is("20191025081315"));
    }

    @Test
    public void tzGMT2() {
        TimeStamper timeStamper = new TimeStamper("yyyyMMddHHmmss", TimeZone.getTimeZone("GMT+2"));
        assertThat(timeStamper.getCurrentTimeStamp(1571991195000L), is("20191025101315"));
    }
}

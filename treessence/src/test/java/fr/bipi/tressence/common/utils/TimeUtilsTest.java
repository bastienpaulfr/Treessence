package fr.bipi.tressence.common.utils;

import org.junit.Test;

import java.util.TimeZone;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TimeUtilsTest {

    @Test
    public void timestampToDate() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        String date = TimeUtils.timestampToDateString(1571991195000L, "yyyyMMddHHmmss");
        assertThat(date, is("20191025081315"));
    }
}

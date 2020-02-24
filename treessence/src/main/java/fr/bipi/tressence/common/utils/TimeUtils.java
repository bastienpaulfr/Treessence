package fr.bipi.tressence.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Deprecated
public final class TimeUtils {

    private TimeUtils() {
    }

    /**
     * Get the String date from timestamp (nb millis after 1970)
     *
     * @param milli  nb millis after 1970
     * @param format Date format
     * @return Date string
     */
    @Deprecated
    public static String timestampToDateString(long milli, String format) {
        Date date = new Date(milli);
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(date);
    }

}

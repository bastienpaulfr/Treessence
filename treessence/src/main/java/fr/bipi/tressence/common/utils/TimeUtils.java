package fr.bipi.tressence.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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
    public static String timestampToDate(long milli, String format) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milli);
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        Date date = calendar.getTime();
        return sdf.format(date);
    }

}

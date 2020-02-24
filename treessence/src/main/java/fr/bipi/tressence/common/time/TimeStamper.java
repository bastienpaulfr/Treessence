package fr.bipi.tressence.common.time;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@SuppressWarnings("WeakerAccess")
public final class TimeStamper {

    private final SimpleDateFormat dateFormat;

    /**
     * Create a {@link TimeStamper} with a {@link SimpleDateFormat} created from format provided.
     *
     * @param format Format given to {@link SimpleDateFormat}
     */
    public TimeStamper(String format) {
        dateFormat = new SimpleDateFormat(format, Locale.US);
        dateFormat.setTimeZone(getDefaultTimeZone());
    }

    /**
     * Create a {@link TimeStamper} with a {@link SimpleDateFormat} created from format provided.
     *
     * @param format format Format given to {@link SimpleDateFormat}
     * @param tz     TimeZone data to add to {@link SimpleDateFormat}
     */
    public TimeStamper(String format, TimeZone tz) {
        this.dateFormat = new SimpleDateFormat(format, Locale.US);
        dateFormat.setTimeZone(tz);
    }

    /**
     * Create a {@link TimeStamper} with {@link SimpleDateFormat} used as is.
     *
     * @param format Format of time
     */
    public TimeStamper(SimpleDateFormat format) {
        dateFormat = format;
    }

    /**
     * Get default {@link TimeZone} object
     *
     * @return default TimeZone
     */
    public TimeZone getDefaultTimeZone() {
        // Bug in some Android platform
        TimeZone.setDefault(null);
        return TimeZone.getDefault();
    }

    /**
     * Get current time according to format provided
     *
     * @param milli Timestamp in milliseconds (nb of milliseconds after 1970)
     * @return current time String
     */
    public String getCurrentTimeStamp(long milli) {
        return dateFormat.format(new Date(milli));
    }
}

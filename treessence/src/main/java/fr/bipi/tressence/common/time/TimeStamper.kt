package fr.bipi.tressence.common.time

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class TimeStamper {
    private val dateFormat: SimpleDateFormat

    /**
     * Create a [TimeStamper] with a [SimpleDateFormat] created from format provided.
     *
     * @param format Format given to [SimpleDateFormat]
     */
    constructor(format: String) {
        dateFormat = SimpleDateFormat(format, Locale.US)
        dateFormat.timeZone = defaultTimeZone
    }

    /**
     * Create a [TimeStamper] with a [SimpleDateFormat] created from format provided.
     *
     * @param format format Format given to [SimpleDateFormat]
     * @param tz     TimeZone data to add to [SimpleDateFormat]
     */
    constructor(format: String, tz: TimeZone) {
        dateFormat = SimpleDateFormat(format, Locale.US)
        dateFormat.timeZone = tz
    }

    /**
     * Create a [TimeStamper] with [SimpleDateFormat] used as is.
     *
     * @param format Format of time
     */
    constructor(format: SimpleDateFormat) {
        dateFormat = format
    }

    /**
     * Get default [TimeZone] object
     *
     * @return default TimeZone
     */
    val defaultTimeZone: TimeZone
        get() {
            // Bug in some Android platform
            TimeZone.setDefault(null)
            return TimeZone.getDefault()
        }

    /**
     * Get current time according to format provided
     *
     * @param milli Timestamp in milliseconds (nb of milliseconds after 1970)
     * @return current time String
     */
    fun getCurrentTimeStamp(milli: Long): String {
        return dateFormat.format(Date(milli))
    }
}

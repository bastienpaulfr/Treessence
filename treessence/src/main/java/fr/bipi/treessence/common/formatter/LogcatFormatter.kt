package fr.bipi.treessence.common.formatter

import android.util.Log
import fr.bipi.treessence.common.os.OsInfoProvider
import fr.bipi.treessence.common.os.OsInfoProviderDefault
import fr.bipi.treessence.common.time.TimeStamper

class LogcatFormatter : Formatter {
    private val priorities = mapOf(
        Log.VERBOSE to "V/",
        Log.DEBUG to "D/",
        Log.INFO to "I/",
        Log.WARN to "W/",
        Log.ERROR to "E/",
        Log.ASSERT to "WTF/"
    )
    var timeStamper = TimeStamper("MM-dd HH:mm:ss:SSS")
    var osInfoProvider: OsInfoProvider = OsInfoProviderDefault()

    override fun format(priority: Int, tag: String?, message: String): String {
        return "${timeStamper.getCurrentTimeStamp(osInfoProvider.currentTimeMillis)} ${priorities[priority] ?: ""}${tag ?: ""}(${osInfoProvider.currentThreadId}) : ${message}\n"
    }

    companion object {
        val INSTANCE = LogcatFormatter()
    }
}

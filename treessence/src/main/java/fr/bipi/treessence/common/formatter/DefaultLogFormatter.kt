package fr.bipi.treessence.common.formatter

import android.util.Log

class DefaultLogFormatter private constructor() : Formatter {
    private val priorities = mapOf(
        Log.VERBOSE to "V/",
        Log.DEBUG to "D/",
        Log.INFO to "I/",
        Log.WARN to "W/",
        Log.ERROR to "E/",
        Log.ASSERT to "WTF/"
    )

    override fun format(priority: Int, tag: String?, message: String) = "${priorities[priority] ?: ""}${"${tag ?: ""} : "}${message}\n"

    companion object {
        val INSTANCE = DefaultLogFormatter()
    }
}

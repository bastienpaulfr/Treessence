package fr.bipi.treessence.common.formatter

interface Formatter {
    fun format(priority: Int, tag: String?, message: String): String
}

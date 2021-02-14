package fr.bipi.tressence.common.formatter

class SimpleFormatter private constructor() : Formatter {
    override fun format(priority: Int, tag: String?, message: String): String {
        return "${tag ?: ""} : ${message}\n"
    }

    companion object {
        val INSTANCE = SimpleFormatter()
    }
}

package fr.bipi.treessence.common.formatter

class NoTagFormatter private constructor() : Formatter {
    override fun format(priority: Int, tag: String?, message: String): String {
        return "${message}\n"
    }

    companion object {
        val INSTANCE = NoTagFormatter()
    }
}

package fr.bipi.tressence.common.filters

class NoFilter : Filter {
    override fun skipLog(priority: Int, tag: String?, message: String, t: Throwable?): Boolean = false

    override fun isLoggable(priority: Int, tag: String?): Boolean = true

    companion object {
        val INSTANCE: Filter = NoFilter()
    }
}

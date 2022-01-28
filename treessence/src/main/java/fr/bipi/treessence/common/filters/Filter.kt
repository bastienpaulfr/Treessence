package fr.bipi.treessence.common.filters

interface Filter {
    /**
     * @param priority Log priority.
     * @param tag      Tag for log.
     * @param message  Formatted log message.
     * @param t        Accompanying exceptions.
     * @return `true` if the log should be skipped, otherwise `false`.
     * @see timber.log.Timber.Tree.log
     */
    fun skipLog(priority: Int, tag: String?, message: String, t: Throwable?): Boolean
    fun isLoggable(priority: Int, tag: String?): Boolean
}

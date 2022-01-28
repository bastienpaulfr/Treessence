package fr.bipi.treessence.common.filters

import java.util.regex.Pattern

class TagFilter : Filter {
    val tagRegex: String
    private val pattern: Pattern

    constructor(tagRegex: String) {
        this.tagRegex = tagRegex
        pattern = Pattern.compile(tagRegex)
    }

    constructor(pattern: Pattern) {
        this.pattern = pattern
        tagRegex = pattern.pattern()
    }

    override fun skipLog(priority: Int, tag: String?, message: String, t: Throwable?): Boolean {
        return !pattern.matcher(tag ?: "").matches()
    }

    override fun isLoggable(priority: Int, tag: String?): Boolean {
        return pattern.matcher(tag ?: "").matches()
    }
}

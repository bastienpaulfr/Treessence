package fr.bipi.treessence.common.utils

object FileUtils {
    private const val UNIX_SEPARATOR = "/"

    /**
     * Combine strings ensuring that there is a "/" between them
     * "foo" + "bar" = "foo/bar"
     * "foo/" + "/bar" = "foo/bar"
     *
     * @param paths List of paths
     * @return Combined path
     */
    fun combinePath(vararg paths: String): String {
        return paths
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .joinToString(separator = UNIX_SEPARATOR) { path ->
                when {
                    path.startsWith(UNIX_SEPARATOR) -> path.removePrefix(UNIX_SEPARATOR)
                    else -> path
                }
            }
            .let { combinedPath ->
                when {
                    !paths.last().endsWith(UNIX_SEPARATOR) -> combinedPath.removeSuffix(
                        UNIX_SEPARATOR
                    )
                    else -> combinedPath
                }
            }
    }
}
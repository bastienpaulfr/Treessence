package fr.bipi.treessence.common.utils;

public final class FileUtils {

    public static final String UNIX_SEPARATOR = "/";

    private FileUtils() {
    }


    /**
     * Combine strings ensuring that there is a "/" between them
     * "foo" + "bar" = "foo/bar"
     * "foo/" + "/bar" = "foo/bar"
     *
     * @param paths List of paths
     * @return Combined path
     */
    public static String combinePath(String... paths) {
        StringBuilder sb = new StringBuilder();
        boolean endsBySlash = false;
        for (String temp : paths) {
            String path = temp.trim();
            if (sb.length() == 0) {
                sb.append(path);
            } else if (path.length() == 0) {
                continue;
            } else if (endsBySlash) {
                if (path.startsWith(UNIX_SEPARATOR)) {
                    if (path.length() > 1) {
                        sb.append(path.substring(1));
                    }
                } else {
                    sb.append(path);
                }
            } else if (path.startsWith(UNIX_SEPARATOR)) {
                sb.append(path);
            } else {
                sb.append(UNIX_SEPARATOR).append(path);
            }
            endsBySlash = path.endsWith(UNIX_SEPARATOR);
        }
        return sb.toString();
    }

}

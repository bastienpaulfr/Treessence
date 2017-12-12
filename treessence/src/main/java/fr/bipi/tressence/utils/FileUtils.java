package fr.bipi.tressence.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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

    /**
     * Return bytes contained in file
     *
     * @param f File
     * @return byte[] data or null if something goes wrong
     */
    public static byte[] getBytesFromFile(File f) {
        FileInputStream in = null;
        byte[] data = new byte[0];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048]; // Adjust if you want
        int bytesRead;
        try {
            in = new FileInputStream(f);
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            data = out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

}

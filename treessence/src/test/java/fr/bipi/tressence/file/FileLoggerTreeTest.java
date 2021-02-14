package fr.bipi.tressence.file;

import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import timber.log.Timber;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.text.MatchesPattern.matchesPattern;

public class FileLoggerTreeTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private String dir;

    @Before
    public void before() throws IOException {
        dir = temporaryFolder.newFolder().getAbsolutePath();
        Timber.uprootAll();
    }

    @After
    public void after() {
        Timber.uprootAll();
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

    @Test
    public void test() {
        // Get tree
        Timber.Tree t = new FileLoggerTree.Builder()
            .withMinPriority(Log.VERBOSE)
            .withDirName(dir)
            .buildQuietly();
        assertThat(t, is(instanceOf(FileLoggerTree.class)));
        FileLoggerTree tree = (FileLoggerTree) t;

        // Plant it
        Timber.plant(t);

        // Log message
        Timber.v("message");

        // Test message
        String filename = tree.getFileName(0);
        assertThat(filename, is(dir + "/log.0"));
        File f = new File(filename);
        assertThat(f.exists(), is(true));
        assertThat(f.isFile(), is(true));

        String res = new String(getBytesFromFile(f));
        assertThat(res.trim(),
                   matchesPattern("^\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}:\\d{3} " +
                                  "V/FileLoggerTreeTest[(]\\d{1,3}[)] : message"));

        // Clear files
        tree.clear();
        assertThat(f.exists(), is(false));
    }

    private void testFile(String expect, String gotten, boolean exists) {
        assertThat(expect, is(gotten));
        File f0 = new File(gotten);
        assertThat(f0.exists(), is(exists));
        assertThat(f0.isFile(), is(exists));
    }

    @Test
    public void fileLimit() {

        Timber.Tree t = new FileLoggerTree.Builder()
            .withDirName(dir)
            .withFileLimit(2)
            .withSizeLimit(56)
            .buildQuietly();
        assertThat(t, is(instanceOf(FileLoggerTree.class)));
        FileLoggerTree tree = (FileLoggerTree) t;

        // Plant it
        Timber.plant(t);

        // Log message
        Timber.i("1234567890");
        Timber.i("message");

        testFile(tree.getFileName(0), dir + "/log.0", true);
        testFile(tree.getFileName(1), dir + "/log.1", true);
        testFile(tree.getFileName(2), dir + "/log.2", false);

        ArrayList<File> list = new ArrayList<>(tree.getFiles());

        assertThat(list.size(), is(2));
        for (File f : list) {
            assertThat(f.exists(), is(true));
            assertThat(f.isFile(), is(true));
        }

        String res = new String(getBytesFromFile(list.get(0)));
        assertThat(res.trim(),
                   matchesPattern("^\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}:\\d{3} " +
                                  "I/FileLoggerTreeTest[(]\\d{1,3}[)] : message"));
        res = new String(getBytesFromFile(list.get(1)));
        assertThat(res.trim(),
                   matchesPattern("^\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}:\\d{3} " +
                                  "I/FileLoggerTreeTest[(]\\d{1,3}[)] : 1234567890$"));


        // Clear files
        tree.clear();
        for (File f : list) {
            assertThat(f.exists(), is(false));
        }
    }

}

package fr.bipi.tressence.file;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import fr.bipi.tressence.common.utils.FileUtils;
import timber.log.Timber;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;

public class FileLoggerTreeTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private String dir;

    @Before
    public void before() throws IOException {
        dir = temporaryFolder.newFolder().getAbsolutePath();
    }

    @Test
    public void test() throws IOException {
        // Get tree
        Timber.Tree t = new FileLoggerTree.Builder()
            .withDirName(dir)
            .build();
        assertThat(t, is(instanceOf(FileLoggerTree.class)));
        FileLoggerTree tree = (FileLoggerTree) t;

        // Plant it
        Timber.plant(t);

        // Log message
        Timber.i("message");

        // Test message
        String filename = tree.getFileName(0);
        assertThat(filename, is(dir + "/log.0"));
        File f = new File(filename);
        assertThat(f.exists(), is(true));
        assertThat(f.isFile(), is(true));

        String res = new String(FileUtils.getBytesFromFile(f));
        assertThat(res.trim(),
                   matchesPattern("^\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}:\\d{3} " +
                                  "I/FileLoggerTreeTest[(]\\d{1,3}[)] : message"));

        // Clear files
        tree.clear();
        assertThat(f.exists(), is(false));
    }

    @Test
    public void fileLimit() {

        Timber.Tree t = new FileLoggerTree.Builder()
            .withDirName(dir)
            .withFileLimit(2)
            .withSizeLimit(56)
            .build();
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

        String res = new String(FileUtils.getBytesFromFile(list.get(0)));
        assertThat(res.trim(),
                   matchesPattern("^\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}:\\d{3} " +
                                  "I/FileLoggerTreeTest[(]\\d{1,3}[)] : message"));
        res = new String(FileUtils.getBytesFromFile(list.get(1)));
        assertThat(res.trim(),
                   matchesPattern("^\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}:\\d{3} " +
                                  "I/FileLoggerTreeTest[(]\\d{1,3}[)] : 1234567890$"));


        // Clear files
        tree.clear();
        for (File f : list) {
            assertThat(f.exists(), is(false));
        }
    }

    private void testFile(String expect, String gotten, boolean exists) {
        assertThat(expect, is(gotten));
        File f0 = new File(gotten);
        assertThat(f0.exists(), is(exists));
        assertThat(f0.isFile(), is(exists));
    }

}
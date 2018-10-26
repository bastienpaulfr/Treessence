package fr.bipi.tressence.file;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import fr.bipi.tressence.base.FormatterPriorityTree;
import fr.bipi.tressence.base.NoTree;
import fr.bipi.tressence.common.formatter.LogcatFormatter;
import fr.bipi.tressence.common.utils.FileUtils;
import timber.log.Timber;

/**
 * An implementation of `Timber.Tree` which sends log into a circular file.
 * <p>It is using {@link java.util.logging.Logger} to implement circular file logging
 */
public class FileLoggerTree extends FormatterPriorityTree {
    private static final String TAG = "FileLoggerTree";
    private final Logger logger;
    private final FileHandler fileHandler;
    private final String path;
    private final int nbFiles;

    /**
     * @param priority    Priority from which to log
     * @param logger      {@link java.util.logging.Logger} used for logging
     * @param fileHandler {@link java.util.logging.FileHandler} used for logging
     * @param path        Base path of file
     * @param nbFiles     Max number of files
     */
    private FileLoggerTree(int priority,
                           Logger logger,
                           FileHandler fileHandler,
                           String path, int nbFiles) {
        super(priority);
        this.logger = logger;
        this.fileHandler = fileHandler;
        this.path = path;
        this.nbFiles = nbFiles;
    }

    @Override
    protected void log(int priority, String tag, @NotNull String message, Throwable t) {
        if (skipLog(priority, tag, message, t)) {
            return;
        }

        logger.log(fromPriorityToLevel(priority), format(priority, tag, message));
        if (t != null) {
            logger.log(fromPriorityToLevel(priority), "", t);
        }
    }

    @Override
    protected fr.bipi.tressence.common.formatter.Formatter getDefaultFormatter() {
        return LogcatFormatter.INSTANCE;
    }

    /**
     * Delete all log files
     */
    public void clear() {
        if (fileHandler != null) {
            fileHandler.close();
        }
        for (int i = 0; i < nbFiles; i++) {
            File f = new File(getFileName(i));
            if (f.exists() && f.isFile()) {
                //noinspection ResultOfMethodCallIgnored
                f.delete();
            }
        }
    }

    /**
     * Return the file name corresponding to the number
     *
     * @param i Number of file
     * @return Real file name
     */
    public String getFileName(int i) {
        String path;
        if (!this.path.contains("%g")) {
            path = this.path + "." + i;
        } else {
            path = this.path.replace("%g", "" + i);
        }
        return path;
    }

    /**
     * @return All files created by the logger
     */
    public Collection<File> getFiles() {
        Collection<File> col = new ArrayList<>(nbFiles);
        for (int i = 0; i < nbFiles; i++) {
            File f = new File(getFileName(i));
            if (f.exists()) {
                col.add(f);
            }
        }
        return col;
    }

    private Level fromPriorityToLevel(int priority) {
        switch (priority) {
            case Log.VERBOSE:
                return Level.FINER;
            case Log.DEBUG:
                return Level.FINE;
            case Log.INFO:
                return Level.INFO;
            case Log.WARN:
                return Level.WARNING;
            case Log.ERROR:
                return Level.SEVERE;
            case Log.ASSERT:
                return Level.SEVERE;
            default:
                return Level.FINEST;
        }
    }

    @SuppressWarnings("WeakerAccess")
    public static class Builder {
        private static final int SIZE_LIMIT = 1048576;
        private static final int NB_FILE_LIMIT = 3;

        private String fileName = "log";
        private String dir = "";
        private int priority = Log.INFO;
        private int sizeLimit = SIZE_LIMIT;
        private int fileLimit = NB_FILE_LIMIT;
        private boolean appendToFile = true;

        /**
         * Specify a custom file name
         * <p> Default file name is "log" which will result in log.0, log.1, log.2...
         *
         * @param fn File name
         * @return itself
         */
        public Builder withFileName(String fn) {
            this.fileName = fn;
            return this;
        }

        /**
         * Specify a custom dir name
         *
         * @param dn Dir name
         * @return itself
         */
        public Builder withDirName(String dn) {
            this.dir = dn;
            return this;
        }

        /**
         * Specify a custom dir name
         *
         * @param d Dir file
         * @return itself
         */
        public Builder withDir(File d) {
            this.dir = d.getAbsolutePath();
            return this;
        }

        /**
         * Specify a priority from which it can start logging
         * <p> Default is {@link Log#INFO}
         *
         * @param p priority
         * @return itself
         */
        public Builder withMinPriority(int p) {
            this.priority = p;
            return this;
        }

        /**
         * Specify a custom file size limit
         * <p> Default is 1048576 bytes
         *
         * @param nbBytes Custom size limit in bytes
         * @return itself
         */
        public Builder withSizeLimit(int nbBytes) {
            this.sizeLimit = nbBytes;
            return this;
        }

        /**
         * Specify a custom file number limit
         * <p> Default is 3
         *
         * @param f Max number of files to use
         * @return itself
         */
        public Builder withFileLimit(int f) {
            this.fileLimit = f;
            return this;
        }

        /**
         * Specify an option for {@link FileHandler} creation
         *
         * @param b true to append to existing file
         * @return itself
         */
        public Builder appendToFile(boolean b) {
            this.appendToFile = b;
            return this;
        }

        /**
         * Create the file logger tree with options specified.
         *
         * @return {@link FileLoggerTree} or {@link NoTree} if an exception occurred
         */
        @NotNull
        public Timber.Tree build() {
            Timber.Tree tree;
            String path = FileUtils.combinePath(dir, fileName);

            try {
                FileHandler fileHandler;
                Logger logger = MyLogger.getLogger(TAG);
                if (logger.getHandlers().length == 0) {
                    fileHandler = new FileHandler(path, sizeLimit, fileLimit, appendToFile);
                    fileHandler.setFormatter(new NoFormatter());
                    logger.addHandler(fileHandler);
                } else {
                    fileHandler = (FileHandler) logger.getHandlers()[0];
                }
                tree = new FileLoggerTree(priority, logger, fileHandler, path, fileLimit);
            } catch (IOException e) {
                Timber.e(e);
                tree = new NoTree();
            }

            return tree;
        }
    }

    private static class NoFormatter extends Formatter {

        @Override
        public String format(LogRecord record) {
            return record.getMessage();
        }
    }

    /**
     * Custom logger class that has no references to LogManager
     */
    private static class MyLogger extends Logger {

        /**
         * Constructs a {@code Logger} object with the supplied name and resource
         * bundle name; {@code notifyParentHandlers} is set to {@code true}.
         * <p/>
         * Notice : Loggers use a naming hierarchy. Thus "z.x.y" is a child of "z.x".
         *
         * @param name the name of this logger, may be {@code null} for anonymous
         *             loggers.
         */
        MyLogger(String name) {
            super(name, null);
        }

        public static Logger getLogger(String name) {
            return new MyLogger(name);
        }
    }
}

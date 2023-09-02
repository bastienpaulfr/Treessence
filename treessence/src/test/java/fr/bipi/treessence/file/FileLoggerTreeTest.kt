package fr.bipi.treessence.file

import android.util.Log
import fr.bipi.treessence.common.formatter.LogcatFormatter
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import timber.log.Timber
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.util.concurrent.atomic.AtomicInteger

@ExperimentalCoroutinesApi
class FileLoggerTreeTest {
    @Rule
    @JvmField
    val folder: TemporaryFolder = TemporaryFolder()

    @MockK
    private lateinit var logcatFormatter: LogcatFormatter

    private val logId = AtomicInteger(0)
    private lateinit var dirPath: String

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        dirPath = folder.newFolder().absolutePath
        setupDefaultMocks()
        Timber.uprootAll()
    }

    @After
    fun teardown() {
        unmockkAll()
        Timber.uprootAll()
    }

    @Test
    fun testBasicFileLogging() {
        val dirPath = "$dirPath/basic_test_logs"
        val fileLoggerTree = buildTree(1000, dirPath)

        Timber.plant(fileLoggerTree)

        Timber.v("Verbose message") // Checks if priority is respected: Priority is DEBUG
        Timber.d("Debug message")
        Timber.i("Info message")
        Timber.w("Warning message")
        Timber.e("Error message")
        Timber.wtf("Assert message")

        val filename = fileLoggerTree.getFileName(0)
        assertThat(filename, `is`("$dirPath/log.0"))
        val file = File(filename)

        // checks if file exists and is not empty
        assertThat(file.exists(), `is`(true))
        assertThat(file.isFile, `is`(true))
        assertThat(file.length() > 0, `is`(true))

        val fileReader = BufferedReader(FileReader(file))

        assertThat(!fileReader.readLine().contains("Verbose message"), `is`(true))

        verifyFileLogsAgainstRegex(file)

        fileLoggerTree.clear()
        assertThat(!file.exists(), `is`(true))
    }

    @Test
    fun testFileAndSizeLimits() {
        val dirPath = "$dirPath/file_size_test_logs"
        val fileLoggerTree = buildTree(10000, dirPath)
        var file: File

        Timber.plant(fileLoggerTree)

        for (i in 0..1000) { // Logs 100000 messages to fill up the files
            Timber.i("test $i")
        }

        for (i in 0..3) {
            val expectedExists = i < 3 // Expect only the first 3 files to exist
            testFile(fileLoggerTree.getFileName(i), "$dirPath/log.$i", expectedExists)
        }

        for (i in 0..2) {
            file = File(fileLoggerTree.getFileName(i))
            verifyFileLogsAgainstRegex(file)
        }

        fileLoggerTree.clear()

        for (i in 0..2) {
            file =
                File(fileLoggerTree.getFileName(i)) // Checks if the files are deleted after clearing
            assertThat(!file.exists(), `is`(true))
        }
    }

    @Test
    fun testFileLogRotation() {
        val dirPath = "$dirPath/log_rotation_test_logs"
        val sizeLimit = 1000 // size limit of each log file in bytes
        val fileLoggerTree = buildTree(sizeLimit, dirPath)
        var file: File

        Timber.plant(fileLoggerTree)

        // Log a unique message that we can trace
        Timber.d("log_rotation_test")

        // check if log_rotation_test is in the first file
        file = File(fileLoggerTree.getFileName(0))
        assertThat(file.readText().contains("log_rotation_test"), `is`(true))

        // Log enough messages to fill up the first log file
        for (i in 0 until sizeLimit / 100) {
            Timber.i("filler_logs $i")
        }

        // Check that the "Test" message has been rotated to the second log file
        file = File(fileLoggerTree.getFileName(1))
        assertThat(file.readText().contains("log_rotation_test"), `is`(true))

        // Log enough messages to rotate the "log_rotation_test" message out of the log files

        for (i in 0 until 10000) {
            Timber.i("filler_logs $i")
        }

        // Check that the "log_rotation_test" message no longer exists in any of the log files
        for (i in 0..2) {
            file = File(fileLoggerTree.getFileName(i))
            assertThat(file.readText().contains("log_rotation_test"), `is`(false))
            verifyFileLogsAgainstRegex(file)
        }

        fileLoggerTree.clear()

        // Check that all log files are deleted after clearing
        for (i in 0..2) {
            file = File(fileLoggerTree.getFileName(i))
            assertThat(file.exists(), `is`(false))
        }
    }

    @Test
    fun testDirectoryCreationIfNonExistent() {
        val dirPath = "$dirPath/directory_creation_test_logs"
        val file: File

        // check if the directory does not exist before logging
        assertThat(File(dirPath).exists(), `is`(false))

        val fileLoggerTree = buildTree(1000, dirPath)
        Timber.plant(fileLoggerTree)

        var number = File(fileLoggerTree.getFileName(0)).length()

        // check if the file size is 0 before logging
        assertThat(number, `is`(0))

        Timber.d("test message")

        // Check if the directory exists after building the tree
        assertThat(File(dirPath).exists(), `is`(true))

        number = File(fileLoggerTree.getFileName(0)).length()

        println("File size: $number")

        assertThat(number > 0, `is`(true))

        file = fileLoggerTree.files.find { it.name == "log.0" }!!

        verifyFileLogsAgainstRegex(file)

        fileLoggerTree.clear()
        assert(!file.exists())
    }

    @Test
    @Suppress("BlockingMethodInNonBlockingContext")
    fun testMultithreadedLogging() = runTest {
        val dirPath = "$dirPath/multithreaded_test_logs"
        val threadCount = 100 // increase size of files to increase this number
        val fileLoggerTree = buildTree(
            fileSize = 15000,
            dirPath = dirPath,
            coroutineScope = CoroutineScope(Dispatchers.Default),
        )

        Timber.plant(fileLoggerTree)

        val jobList = mutableListOf<Job>()

        for (i in 0 until threadCount) {
            jobList += launch(Dispatchers.IO) {
                Timber.i("Thread $i logging.")
            }
        }

        // Wait for all logging to finish
        jobList.forEach { it.join() }

        // After logging from multiple threads, verify the file logs
        val file = File(fileLoggerTree.getFileName(0))
        val fileReader = BufferedReader(FileReader(file))

        // count lines
        val lineCount = fileReader.lines().count()
        println("File content lines: $lineCount")

        // We should have exactly `threadCount` log entries
        assertThat(threadCount, `is`(lineCount.toInt()))

        verifyFileLogsAgainstRegex(file)

        fileLoggerTree.clear()
        assert(!file.exists())
    }

    @Test
    fun testExceptionLogging() {
        val dirPath = "$dirPath/exception_test_logs"
        val fileLoggerTree = buildTree(10000, dirPath)
        Timber.plant(fileLoggerTree)

        val exceptionMessage = "This is a test exception"
        val exception = RuntimeException(exceptionMessage)

        Timber.e(exception)

        val file = File(fileLoggerTree.getFileName(0))
        // print exception
        println(file.readText())

        // Check that the exception message is logged
        assertThat(file.readText().contains(exceptionMessage), `is`(true))

        // Check that the stack trace is logged
        assertThat(file.readText().contains(exception.stackTraceToString()), `is`(true))

        fileLoggerTree.clear()
        assertThat(!file.exists(), `is`(true))
    }

    private fun priorityToString(priority: Int): String {
        return when (priority) {
            Log.VERBOSE -> "VERBOSE"
            Log.DEBUG -> "DEBUG"
            Log.INFO -> "INFO"
            Log.WARN -> "WARNING"
            Log.ERROR -> "ERROR"
            Log.ASSERT -> "ASSERT"
            else -> "UNKNOWN"
        }
    }

    private fun buildTree(
        fileSize: Int,
        dirPath: String,
        coroutineScope: CoroutineScope? = null,
    ): FileLoggerTree {
        val fileLoggerTree = FileLoggerTree.Builder()
            .withDirName(dirPath)
            .withFileName("log.%g")
            .withSizeLimit(fileSize)
            .withFileLimit(3)
            .withMinPriority(Log.DEBUG)
            .withFormatter(logcatFormatter)
            .withCoroutineScope(coroutineScope)
            .buildQuietly()

        assertThat(
            fileLoggerTree,
            CoreMatchers.instanceOf(FileLoggerTree::class.java),
        )

        fileLoggerTree as FileLoggerTree

        return fileLoggerTree
    }

    // Checks if the file exists and if the file path matches the expected path
    private fun testFile(expected: String, actual: String, shouldExist: Boolean) {
        assertThat(expected, `is`(actual))
        val file = File(actual)
        assertThat(file.exists() && file.isFile, `is`(shouldExist))
    }

    // Compares each log in file with expected custom log format
    private fun verifyFileLogsAgainstRegex(file: File) {
        val fileReader = BufferedReader(FileReader(file))

        fileReader.useLines { lines ->
            lines.forEach { line ->
                assertThat(
                    line.trim().matches(
                        Regex(
                            "^LogId: \\d+ \\| " +
                                "\\d{2}-\\d{2}-\\d{4} \\d{2}:\\d{2}:\\d{2}:\\d{3} \\| " +
                                "(VERBOSE|DEBUG|INFO|WARNING|ERROR|ASSERT|UNKNOWN) \\| " +
                                "Thread: .+ \\| " +
                                "PID: \\d+ \\| " +
                                "Tag: .+ \\| " +
                                ".+$",
                        ),
                    ),
                    `is`(true),
                )
            }
        }
    }

    private fun setupDefaultMocks() {
        every { logcatFormatter.format(any(), any(), any()) } answers {
            val priority = firstArg<Int>()
            val tag = secondArg<String>()
            val message = thirdArg<String>()
            "LogId: ${logId.incrementAndGet()} | 01-01-1970 00:00:00:000 | ${
                priorityToString(
                    priority,
                )
            } | Thread: main | PID: 0 | Tag: $tag | $message" + "\n"
        }
    }
}

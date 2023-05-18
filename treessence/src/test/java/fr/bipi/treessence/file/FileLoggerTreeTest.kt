package fr.bipi.treessence.file

import android.content.Context
import android.util.Log
import fr.bipi.treessence.common.formatter.LogcatFormatter
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.amshove.kluent.internal.assertEquals
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.io.TempDir
import timber.log.Timber
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.nio.file.Path
import java.util.concurrent.atomic.AtomicInteger

@OptIn(ExperimentalCoroutinesApi::class)
class FileLoggerTreeTest {

    @TempDir
    private val tempDir: Path? = null

    @MockK
    private lateinit var context: Context

    @MockK
    private lateinit var file: File

    @MockK
    private lateinit var logcatFormatter: LogcatFormatter

    private val logId = AtomicInteger(0)
    private lateinit var dir: String

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        dir = tempDir?.toAbsolutePath().toString()
        setupDefaultMocks()
        Timber.uprootAll()
    }

    @After
    fun teardown() {
        unmockkAll()
        File(dir).deleteRecursively()
        Timber.uprootAll()
    }

    @Test
    fun testBasicFileLogging() = runTest {
        val dirPath = "$dir/ce_basic_test_logs"
        val fileLoggerTree = buildTree(1000000, dirPath)

        Timber.plant(fileLoggerTree)

        Timber.v("Verbose message") // Checks if priority is respected: Priority is DEBUG
        Timber.d("Debug message")
        Timber.i("Info message")
        Timber.w("Warning message")
        Timber.e("Error message")
        Timber.wtf("Assert message")

        val filename = fileLoggerTree.getFileName(0)
        MatcherAssert.assertThat(filename, CoreMatchers.`is`("$dirPath/log.0"))
        file = File(filename)

        // checks if file exists and is not empty
        assert(file.exists())
        assert(file.isFile)
        assert(file.length() > 0)

        val fileReader = BufferedReader(
            withContext(Dispatchers.IO) {
                FileReader(file)
            }
        )

        assert(
            !withContext(Dispatchers.IO) {
                fileReader.readLine()
            }.contains("Verbose message")
        )

        verifyFileLogsAgainstRegex(file)

        fileLoggerTree.clear()
        assert(!file.exists())
    }

    @Test
    fun testFileAndSizeLimits() = runTest {
        val dirPath = "$dir/ce_file_size_test_logs"
        val fileLoggerTree = buildTree(1000000, dirPath)

        Timber.plant(fileLoggerTree)

        for (i in 0..100000) { // Logs 100000 messages to fill up the files
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
            assert(!file.exists())
        }
    }

    @Test
    fun testFileLogRotation() = runTest {
        val dirPath = "$dir/ce_log_rotation_test_logs"
        val sizeLimit = 10000 // size limit of each log file in bytes
        val fileLoggerTree = buildTree(sizeLimit, dirPath)

        Timber.plant(fileLoggerTree)

        // Log a unique message that we can trace
        withContext(Dispatchers.IO) {
            Timber.d("log_rotation_test")
        }

        delay(1000)

        // check if log_rotation_test is in the first file
        file = File(fileLoggerTree.getFileName(0))
        TestCase.assertTrue(file.readText().contains("log_rotation_test"))

        // Log enough messages to fill up the first log file
        for (i in 0 until sizeLimit / 100) {
            withContext(Dispatchers.IO) {
                Timber.i("filler_logs $i")
            }
        }

        delay(1000)

        // Check that the "Test" message has been rotated to the second log file
        file = File(fileLoggerTree.getFileName(1))
        TestCase.assertTrue(file.readText().contains("log_rotation_test"))

        // Log enough messages to rotate the "log_rotation_test" message out of the log files
        for (i in 0 until 10000) {
            withContext(Dispatchers.IO) {
                Timber.i("filler_logs $i")
            }
        }

        delay(1000)

        // Check that the "log_rotation_test" message no longer exists in any of the log files
        for (i in 0..2) {
            val file = File(fileLoggerTree.getFileName(i))
            TestCase.assertFalse(file.readText().contains("log_rotation_test"))
            verifyFileLogsAgainstRegex(file)
        }

        fileLoggerTree.clear()

        // Check that all log files are deleted after clearing
        for (i in 0..2) {
            val file = File(fileLoggerTree.getFileName(i))
            TestCase.assertFalse(file.exists())
        }
    }

    @Test
    fun testDirectoryCreationIfNonExistent() = runTest {
        val dirPath = "$dir/ce_directory_creation_test_logs"

        // check if the directory does not exist before logging
        assertEquals(false, File(dirPath).exists())

        val fileLoggerTree = buildTree(1000000, dirPath)
        Timber.plant(fileLoggerTree)

        var number = File(fileLoggerTree.getFileName(0)).length()

        // check if the file size is 0 before logging
        assertEquals(0, number)

        withContext(Dispatchers.IO) {
            Timber.d("test message")
        }

        // Check if the directory exists after building the tree
        assertEquals(true, File(dirPath).exists())

        repeat(5) {
            delay(1000)
            number = File(fileLoggerTree.getFileName(0)).length()
            if (number > 0) {
                return@repeat
            }
        }

        println("File size: $number")

        TestCase.assertTrue(number > 0)

        file = fileLoggerTree.files.find { it.name == "log.0" }!!

        verifyFileLogsAgainstRegex(file)

        fileLoggerTree.clear()
        assert(!file.exists())
    }

    @Test
    fun testMultithreadedLogging() = runTest {
        val dirPath = "$dir/ce_multithreaded_test_logs"
        val threadCount = 100 // increase size of files to increase this number
        val fileLoggerTree = buildTree(15000, dirPath)
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
        file = File(fileLoggerTree.getFileName(0))
        val fileReader = BufferedReader(
            withContext(Dispatchers.IO) {
                FileReader(file)
            }
        )

        // count lines
        val lineCount = withContext(Dispatchers.IO) { fileReader.lines().count() }
        println("File content lines: $lineCount")

        // We should have exactly `threadCount` log entries
        assertEquals(threadCount, lineCount.toInt(), "Expected $threadCount log entries")

        verifyFileLogsAgainstRegex(file)

        fileLoggerTree.clear()
        assert(!file.exists())
    }

    @Test
    fun testExceptionLogging() = runTest {
        val dirPath = "$dir/ce_exception_test_logs"
        val fileLoggerTree = buildTree(1000000, dirPath)
        Timber.plant(fileLoggerTree)

        val exceptionMessage = "This is a test exception"
        val exception = RuntimeException(exceptionMessage)

        withContext(Dispatchers.IO) {
            Timber.e(exception)
        }

        delay(1000) // Give it a moment to log

        file = File(fileLoggerTree.getFileName(0))
        // print exception
        println(file.readText())

        // Check that the exception message is logged
        TestCase.assertTrue(file.readText().contains(exceptionMessage))

        // Check that the stack trace is logged
        TestCase.assertTrue(file.readText().contains(exception.stackTraceToString()))

        fileLoggerTree.clear()
        assert(!file.exists())
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

    private fun buildTree(fileSize: Int, dirPath: String): FileLoggerTree {
        val fileLoggerTree = FileLoggerTree.Builder()
            .withDirName(dirPath)
            .withFileName("log.%g")
            .withSizeLimit(fileSize)
            .withFileLimit(3)
            .withMinPriority(Log.DEBUG)
            .withFormatter(logcatFormatter)
            .buildQuietly()

        MatcherAssert.assertThat(
            fileLoggerTree,
            CoreMatchers.instanceOf(FileLoggerTree::class.java)
        )

        fileLoggerTree as FileLoggerTree

        return fileLoggerTree
    }

    // Checks if the file exists and if the file path matches the expected path
    private fun testFile(expected: String, actual: String, shouldExist: Boolean) {
        assertEquals(expected, actual)
        val file = File(actual)
        assertEquals(
            shouldExist,
            file.exists() && file.isFile,
            "Expected existence: $shouldExist, but was ${file.exists() && file.isFile}"
        )
    }

    // Compares each log in file with expected custom log format
    private fun verifyFileLogsAgainstRegex(file: File) {
        val fileReader = BufferedReader(FileReader(file))

        fileReader.useLines { lines ->
            lines.forEach { line ->
                TestCase.assertTrue(
                    line.trim().matches(
                        Regex(
                            "^LogId: \\d+ \\| " +
                                    "\\d{2}-\\d{2}-\\d{4} \\d{2}:\\d{2}:\\d{2}:\\d{3} \\| " +
                                    "(VERBOSE|DEBUG|INFO|WARNING|ERROR|ASSERT|UNKNOWN) \\| " +
                                    "Thread: .+ \\| " +
                                    "PID: \\d+ \\| " +
                                    "Tag: .+ \\| " +
                                    ".+$"
                        )
                    )
                )
            }
        }
    }

    private fun setupDefaultMocks() {
        every { context.filesDir } returns file
        every { file.absolutePath } returns dir
        every { logcatFormatter.format(any(), any(), any()) } answers {
            val priority = firstArg<Int>()
            val tag = secondArg<String>()
            val message = thirdArg<String>()
            "LogId: ${logId.incrementAndGet()} | 01-01-1970 00:00:00:000 | ${
                priorityToString(
                    priority
                )
            } | Thread: main | PID: 0 | Tag: $tag | $message" + "\n"
        }
    }
}
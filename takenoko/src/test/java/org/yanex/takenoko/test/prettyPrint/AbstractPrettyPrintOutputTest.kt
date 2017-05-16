package org.yanex.takenoko.test.prettyPrint

import org.yanex.takenoko.PrettyPrinter
import org.yanex.takenoko.PrettyPrinterConfiguration
import org.junit.Rule
import org.junit.rules.TestName
import org.yanex.kotlinpoet.test.assertEqualsToFile
import org.yanex.takenoko.KoFile
import org.yanex.takenoko.kotlinFile
import java.io.File

abstract class AbstractPrettyPrintOutputTest {
    private companion object {
        val TEST_DATA_DIR = File("src/test/resources/pretty")
    }

    open val prefix: String = ""

    @Rule
    @JvmField
    var testName = TestName()

    fun testFile(packageName: String = "test", block: KoFile.() -> Unit) {
        test { kotlinFile(packageName).apply(block) }
    }

    fun test(block: () -> KoFile) {
        val kotlinFile = block()
        val actual = kotlinFile.accept(PrettyPrinter(PrettyPrinterConfiguration()))
        val testDataFile = File(TEST_DATA_DIR, prefix + testName.methodName + ".txt")
        val testDataDir = testDataFile.parentFile
        if (!testDataDir.exists()) {
            testDataDir.mkdirs()
        }
        assertEqualsToFile("Pretty print result is not the same.", testDataFile, actual)
    }
}
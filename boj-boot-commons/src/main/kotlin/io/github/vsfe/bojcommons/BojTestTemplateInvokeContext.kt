package io.github.vsfe.bojcommons

import io.github.vsfe.bojcommons.exception.InvalidDataException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.Extension
import org.junit.jupiter.api.extension.TestTemplateInvocationContext
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.InputStream
import java.io.PrintStream
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths

class BojTestTemplateInvokeContext(
    private val problemNumber: Int,
    private val identifier: String
) : TestTemplateInvocationContext {
    private lateinit var originalIn: InputStream
    private lateinit var originalOut: PrintStream
    private lateinit var baos: ByteArrayOutputStream

    override fun getDisplayName(invocationIndex: Int): String {
        return "testCase - #$identifier"
    }

    override fun getAdditionalExtensions(): MutableList<Extension> {
        return mutableListOf(createBeforeEachCallback(), createAfterEachCallback())
    }

    private fun createBeforeEachCallback(): BeforeEachCallback {
        return BeforeEachCallback {
            originalIn = System.`in`
            originalOut = System.out

            val testClass = it.requiredTestClass
            val resourcePath = "testcase/p$problemNumber"
            val inputFilePath = "$resourcePath/$identifier.in"
            val resource = testClass.classLoader.getResource(inputFilePath) ?:
                throw InvalidDataException("fail to get testcases. check your directory.")
            val fis = FileInputStream(resource.toURI().path)

            baos = ByteArrayOutputStream()
            val ps = PrintStream(baos)

            System.setIn(fis)
            System.setOut(ps)
        }
    }

    private fun createAfterEachCallback(): AfterEachCallback {
        return AfterEachCallback {
            System.setOut(originalOut)
            System.setIn(originalIn)

            val outputContent = baos.toString(Charset.defaultCharset())

            val testClass = it.requiredTestClass
            val resourcePath = "testcase/p$problemNumber"
            val outputFilePath = "$resourcePath/$identifier.out"
            val resource = testClass.classLoader.getResource(outputFilePath) ?:
                throw InvalidDataException("fail to get testcases. check your directory.")

            val fileContent = Files.readString(Paths.get(resource.toURI()), Charset.defaultCharset())
            TestcaseUtil.compareResult(outputContent, fileContent)
        }
    }
}
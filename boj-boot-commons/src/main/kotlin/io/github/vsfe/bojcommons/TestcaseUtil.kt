package io.github.vsfe.bojcommons

import io.github.vsfe.bojcommons.exception.InvalidDataException
import org.junit.jupiter.api.Assertions
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

object TestcaseUtil {
    @JvmStatic
    fun resolveTestcase(clazz: Class<*>, problemNumber: Int): List<String> {
        try {
            val classLoader = clazz.classLoader
            val path = "testcase/p$problemNumber"
            val fileList = Files.walk(Paths.get(classLoader.getResource(path).toURI()))
                .filter(Files::isRegularFile)
                .map { it.fileName.toString() }
                .toList()

            val inputFileList = fileList.filter { it.endsWith(".in") }
            val outputFileList = fileList.filter { it.endsWith(".out") }

            val inputFileNameList = inputFileList.map { it.split(".").first() }
            val outputFileNameList = outputFileList.map { it.split(".").first() }

            if (inputFileNameList.toSet() != outputFileNameList.toSet()) {
                return listOf()
            }

            return inputFileNameList.sorted()
        } catch (e: IOException) {
            throw InvalidDataException("resolve Testcase failed. check your directory.")
        }
    }

    fun compareResult(userResponse: String, answer: String) {
        val convertedUserResponse = trimLineEnd(userResponse)
        val convertedAnswer = trimLineEnd(answer)

        Assertions.assertEquals(convertedAnswer, convertedUserResponse)
    }

    private fun trimLineEnd(str: String): String =
        str.lines().joinToString("\n") { it.trimEnd() }.trimEnd()
}

package org.vsfe

import org.gradle.api.Project
import java.nio.file.Files
import java.nio.file.Paths

object FileWriter{
    fun writeMainFile(project: Project, problemNumber: Int, outputPath: String) {
        val fileContent = createMainFile(project, problemNumber)
        writeFile(outputPath, "Main.java", fileContent)
    }

    fun writeTestFile(project: Project, problemNumber: Int, problemTimout: Double, outputPath: String) {
        val fileContent = createTestFile(project, problemNumber, problemTimout)
        writeFile(outputPath, "Boj${problemNumber}Test.java", fileContent)
    }

    fun writeTestcase(baseDir: String, problemInfo: ProblemInfo) {
        val (inputData, outputData) = problemInfo.testCases.map {
            it.input to it.output
        }.unzip()

        inputData.forEachIndexed { idx, data -> writeTestcaseInner(baseDir, idx, true, data) }
        outputData.forEachIndexed { idx, data -> writeTestcaseInner(baseDir, idx, false, data) }
    }

    private fun writeTestcaseInner(baseDir: String, idx: Int, isInput: Boolean, data: String) {
        val fileExtension = if (isInput) "in" else "out"
        writeFile(baseDir, "$idx.$fileExtension", data)
    }

    private fun writeFile(baseDir: String, fileName: String, content: String) {
        Files.createDirectories(Paths.get(baseDir))
        Files.write(Paths.get("$baseDir/$fileName"), content.toByteArray())
    }

    private fun createMainFile(project: Project, problemNumber: Int) =
        """
            package ${project.group}.p$problemNumber;
            
            public class Main {
                public static void main(String[] args) {
                    // write code...
                }
            }
        """.trimIndent()

    private fun createTestFile(project: Project, problemNumber: Int, timeout: Double) =
        """
            package ${project.group}.p$problemNumber;

            import io.github.vsfe.bojcommons.BojTest;
            import io.github.vsfe.bojcommons.BojTestExtension;
            import org.junit.jupiter.api.DisplayName;
            import org.junit.jupiter.api.TestTemplate;
            import org.junit.jupiter.api.Timeout;
            import org.junit.jupiter.api.extension.ExtendWith;
            
            import java.util.concurrent.TimeUnit;

            @BojTest(problemNumber = $problemNumber)
            @ExtendWith(BojTestExtension.class)
            public class Boj${problemNumber}Test {
                @TestTemplate
                @DisplayName("BOJ $problemNumber")
                @Timeout(value = ${(timeout * 1000).toLong()}L, unit = TimeUnit.MILLISECONDS)
                void test() {
                    Main.main(new String[0]);
                }
            }
        """.trimIndent()
}
package io.github.vsfe.bojcommons

import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.TestTemplateInvocationContext
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider
import io.github.vsfe.bojcommons.exception.NotSupportedTestException
import java.util.stream.Stream

class BojTestExtension : TestTemplateInvocationContextProvider {
    override fun supportsTestTemplate(extensionContext: ExtensionContext): Boolean = true

    override fun provideTestTemplateInvocationContexts(extensionContext: ExtensionContext): Stream<TestTemplateInvocationContext> {
        val testClass = extensionContext.requiredTestClass
        val bojTestAnnotation = testClass.getAnnotation(BojTest::class.java) ?:
            throw NotSupportedTestException("Not supported test class. Please use @BojTest annotation.")
        val problemNumber = bojTestAnnotation.problemNumber
        val testCases = TestcaseUtil.resolveTestcase(this::class.java, problemNumber)

        return testCases.stream()
            .map { BojTestTemplateInvokeContext(problemNumber, it) }
    }
}
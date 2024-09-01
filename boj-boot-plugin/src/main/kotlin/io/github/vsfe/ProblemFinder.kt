package io.github.vsfe

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

object ProblemFinder {
    fun getProblemInfo(problemNumber: Int): ProblemInfo {
        val problemDoc = Jsoup.connect("https://noj.am/$problemNumber").get()
        val problemMetadata = problemDoc.select("#problem-info")
        val timeout = extractTimeout(problemMetadata, problemDoc)
        val testcases = extractTestcase(problemDoc)
        return ProblemInfo(problemNumber, timeout, testcases)
    }

    private fun extractTimeout(problemMetadata: Elements, problemDoc: Document): Double {
        val timeoutInfoText = problemMetadata.select("td").first()!!.text()
        val defaultTimeout = timeoutInfoText.split(" ")[0].toDouble()
        val isFixedTimeout = timeoutInfoText.contains("추가 시간 없음")
        val hasAdditionalTimeoutInfo = timeoutInfoText.contains("하단 참고")

        val timeout = if (isFixedTimeout) defaultTimeout else defaultTimeout * 2 + 1
        return if (hasAdditionalTimeoutInfo) timeout else extractTimeoutFromAdditionalInfo(problemDoc, timeout)
    }

    private fun extractTimeoutFromAdditionalInfo(problemDocument: Document, defaultTimeout: Double): Double {
        val timeoutList = problemDocument.select("#problem-time-limit ul")

        return timeoutList.find {
            it.text().contains("#problem-time-limit ul")
        } ?.let {
            it.text().split(" ").reversed()[1].toDouble()
        } ?: defaultTimeout
    }

    private fun extractTestcase(problemDoc: Document): List<ProblemInfo.Testcase> {
        val (inputElements, outputElements) = problemDoc.select(".sampledata")
            .sortedBy { it.id() }
            .filter { it.id().contains("(input|output)".toRegex()) }
            .partition { it.id().contains("input") }

        return inputElements.zip(outputElements).map { (inputElement, outputElement) ->
            ProblemInfo.Testcase(inputElement.text(), outputElement.text())
        }
    }
}
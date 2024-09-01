package io.github.vsfe

data class ProblemInfo(
    val problemNumber: Int,
    val problemTimeout: Double,
    val testCases: List<Testcase>
) {
    data class Testcase(
        val input: String,
        val output: String
    )
}
package nl.vanwollingen.aoc.aoc2025

import nl.vanwollingen.aoc.util.Puzzle

fun main() = Day02.solve()

object Day02 : Puzzle(true, false) {

    private val productIds = parseInput()

    override fun parseInput() = input.split(",").flatMap { range ->
        val (start, end) = range.split("-").map { it.toLong() }
        start..end
    }

    override fun part1() = productIds.sumOf { productId -> isRepeatingHalf(productId) }
    override fun part2() = productIds.sumOf { productId -> hasRepeatedPattern(productId) }

    private fun isRepeatingHalf(number: Long): Long {
        val string = number.toString()
        if (string.length % 2 != 0) return 0
        val first = string.subSequence(0, string.length / 2)
        val second = string.subSequence(string.length / 2, string.length)
        return if (first == second) number else 0
    }

    private fun hasRepeatedPattern(number: Long): Long {
        val string = number.toString()
        var patternToTest = string.substring(0, string.length / 2)

        while (patternToTest.isNotEmpty()) {
            val chunks = string.chunked(patternToTest.length)
            if (chunks.all { it == patternToTest }) {
                return string.toLong()
            }
            patternToTest = patternToTest.dropLast(1)
        }
        return 0L
    }
}

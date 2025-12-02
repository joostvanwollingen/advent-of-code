package nl.vanwollingen.aoc.aoc2025

import nl.vanwollingen.aoc.util.Puzzle

fun main() = Day02.solve()

object Day02 : Puzzle(true, false) {

    private val ranges = parseInput()
    override fun parseInput() = input.split(",").map { range ->
        val (start, end) = range.split("-").map { it.toLong() }
        start..end
    }

    override fun part1(): Any {
        return ranges.sumOf { range ->
            range.sumOf { num ->
                val string = num.toString()
                if (string.length % 2 != 0) 0
                else {
                    val first = string.subSequence(0, string.length / 2)
                    val second = string.subSequence(string.length / 2, string.length)
                    if (first == second) num else 0
                }
            }
        }
    }

    override fun part2(): Any {
        return ranges.sumOf { range ->
            range.sumOf { num ->
                val string = num.toString()
                val pattern = findPattern(string)
                if (pattern == null) 0L
                else {
                    num
                }
            }
        }
    }

    private fun findPattern(string: String): String? {
        var patternToTest = string.substring(0, string.length / 2)

        while (patternToTest.isNotEmpty()) {
            val chunks = string.chunked(patternToTest.length)
            if (chunks.all { it == patternToTest }) {
                return patternToTest
            }
            patternToTest = patternToTest.dropLast(1)
        }
        return null
    }
}
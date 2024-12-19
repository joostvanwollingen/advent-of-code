package nl.vanwollingen.aoc.aoc2024

import nl.vanwollingen.aoc.util.Puzzle

fun main() = Day19.solve()

object Day19 : Puzzle(exampleInput = false) {

    private val data = parseInput()
    private val towelPatterns = data.first
    private val designs = data.second

    override fun parseInput(): Pair<List<String>, List<String>> {
        val (towelPatternsString, designsString) = input.split("\n\n")
        val towelPatterns = towelPatternsString.split(", ")
        val designs = designsString.lines()
        return towelPatterns to designs
    }

    override fun part1() = designs.map { design ->
        countMatches(design, 0, towelPatterns) > 0
    }.filter { it }.size

    override fun part2() = designs.sumOf { design ->
        countMatches(design, 0, towelPatterns)
    }

    private fun countMatches(
        source: String,
        start: Int = 0,
        patterns: List<String>,
        memo: MutableMap<Int, Long> = mutableMapOf(),
    ): Long {
        if (start == source.length) return 1
        if (start in memo) return memo[start]!!

        var totalMatches = 0L
        for (pattern in patterns) {
            if (source.startsWith(pattern, start)) {
                totalMatches += countMatches(source, start + pattern.length, patterns, memo)
            }
        }

        memo[start] = totalMatches
        return totalMatches
    }
}
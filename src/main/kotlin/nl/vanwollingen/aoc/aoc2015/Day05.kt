package nl.vanwollingen.aoc.aoc2015

import nl.vanwollingen.aoc.util.Puzzle

fun main() {
    val d5 = Day05(2015, 5)
    d5.solvePart1()
    d5.solvePart2()
}

class Day05(year: Int, day: Int) : Puzzle(year, day) {

    private fun naughtyOrNice(text: String): Boolean {
        val vowels = Regex("[aeiou]")
        val banned = Regex("ab|cd|pq|xy")
        val repeating = Regex("(.)\\1")

        return vowels.findAll(text).toList().size >= 3 && banned.findAll(text).toList().isEmpty() && repeating.findAll(text).toList().isNotEmpty()
    }

    private fun naughtierOrNicer(text: String): Boolean {
        val split = Regex("(.)[a-z]{1}\\1")
        val repeating = Regex(".*(.{2}).*\\1")
        return split.findAll(text).toList().isNotEmpty() && repeating.findAll(text).toList().isNotEmpty()
    }

    override fun solvePart1() {
        var count = 0
        input.lines().forEach { line ->
            if (naughtyOrNice(line)) count++
        }
        println(count)
    }

    override fun solvePart2() {
        var count = 0
        input.lines().forEach { line ->
            if (naughtierOrNicer(line)) count++
        }
        println(count)
    }
}
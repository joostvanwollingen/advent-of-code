package nl.vanwollingen.aoc.aco2018

import nl.vanwollingen.aoc.util.Puzzle

fun main() {
    val p = Day01()
    p.solvePart1()
}

class Day01 : Puzzle() {

    private val frequencyChanges: List<FrequencyChange> = parseInput()

    data class FrequencyChange(val change: Long)

    override fun parseInput(): List<FrequencyChange> = input.lines().map { l ->
        FrequencyChange(l.toLong())
    }

    override fun part1() {
        log(frequencyChanges.sumOf { it.change })
    }

    override fun part2() {
        TODO("Not yet implemented")
    }
}
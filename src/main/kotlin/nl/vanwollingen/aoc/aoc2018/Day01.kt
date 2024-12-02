package nl.vanwollingen.aoc.aoc2018

import nl.vanwollingen.aoc.util.Puzzle
import nl.vanwollingen.aoc.util.exceptions.TargetStateReachedException

fun main() {
    val p = Day01()
    p.solvePart1()
    p.solvePart2()
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
        val visited: MutableMap<Long, Int> = mutableMapOf(0L to 1)
        var currentFrequency = 0L

        while (true) {
            frequencyChanges.forEach { fc ->
                currentFrequency += fc.change
                visited[currentFrequency] = visited.getOrDefault(currentFrequency, 0) + 1
                if (visited.getOrDefault(currentFrequency, 0) > 1) {
                    log(currentFrequency)
                    throw TargetStateReachedException("$currentFrequency")
                }
            }
        }
    }
}
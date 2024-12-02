package nl.vanwollingen.aoc.aoc2024

import nl.vanwollingen.aoc.util.Puzzle
import kotlin.math.abs

fun main() {
    Day02.solvePart1()
    Day02.solvePart2()
}

object Day02 : Puzzle(exampleInput = false) {

    private val reports = parseInput()

    override fun parseInput() = input.lines().map { line ->
        line.split(" ").map(String::toInt)
    }

    override fun part1() {
        log(reports.count(::reportIsSafe))
    }

    override fun part2() {
        log(reports.count { report ->
            val mutations = reportMutations(report)
            reportIsSafe(report) || mutations.any { reportIsSafe(it) }
        })
    }

    private fun reportIsSafe(level: List<Int>): Boolean {
        var direction: String? = null

        val safeLevels = level.windowed(2, 1) { (left, right) ->

            val newDirection = if (left < right) {
                "UP"
            } else if (left == right) {
                "EQUAL"
            } else {
                "DOWN"
            }

            if (direction == null) {
                direction = newDirection
            }

            if (newDirection == "EQUAL" || newDirection != direction) {
                return@windowed false
            }

            direction = newDirection

            val diff = left - right

            return@windowed abs(diff) in 1..3
        }

        return !safeLevels.contains(false)
    }
}

fun reportMutations(report: List<Int>): List<List<Int>> {
    val skipList = report.indices
    val newLists = skipList.map {
        val mutableReport = report.toMutableList()
        mutableReport.removeAt(it)
        mutableReport
    }
    return newLists
}
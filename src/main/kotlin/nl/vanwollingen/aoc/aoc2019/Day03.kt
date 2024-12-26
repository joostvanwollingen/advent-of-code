package nl.vanwollingen.aoc.aoc2019

import nl.vanwollingen.aoc.util.Puzzle
import kotlin.math.abs

fun main() = Day03.solve()

object Day03 : Puzzle(exampleInput = false) {
    private val wire1 = input.lines().first().split(",")
    private val wire2 = input.lines().last().split(",")

    override fun part1(): Any {
        val visited: MutableMap<Pair<Int, Int>, Pair<Int?, Int?>> = mutableMapOf()

        doSteps(wire1, visited, true)
        doSteps(wire2, visited, false)

        return visited
            .filter { it.value.first != null && it.value.second != null }
            .minOf { getManhattanDistance(0 to 0, it.key) }
    }

    override fun part2(): Any {
        val visited: MutableMap<Pair<Int, Int>, Pair<Int?, Int?>> = mutableMapOf()

        doSteps(wire1, visited, true)
        doSteps(wire2, visited, false)

        return visited
            .filter { it.value.first != null && it.value.second != null }
            .minOf { it.value.first!! + it.value.second!! }
    }

    private fun doSteps(
        wire: List<String>,
        visited: MutableMap<Pair<Int, Int>, Pair<Int?, Int?>>,
        wire1: Boolean,
    ) {
        var current = 0 to 0
        var stepsTaken = 1
        wire.forEach { step ->
            val direction = step[0]
            val steps = step.substring(1).toInt()
            var vector = 0 to 0
            when (direction) {
                'L' -> {
                    vector = 0 to -1
                }

                'R' -> {
                    vector = 0 to 1
                }

                'U' -> {
                    vector = 1 to 0
                }

                'D' -> {
                    vector = -1 to 0
                }
            }
            repeat(steps) {
                current = (current.first + vector.first) to (current.second + vector.second)
                val stepsCurrent = visited.getOrDefault(current, null to null)
                val stepsUpdate = if (wire1) stepsTaken to stepsCurrent.second else stepsCurrent.first to stepsTaken
                visited[current] = stepsUpdate
                stepsTaken += 1
            }

        }
    }

    private fun getManhattanDistance(start: Pair<Int, Int>, end: Pair<Int, Int>): Int {
        return abs(start.first - end.first) + abs(start.second - end.second)
    }
}
package nl.vanwollingen.aoc.aoc2016

import nl.vanwollingen.aoc.util.Puzzle
import nl.vanwollingen.aoc.util.grid.Point
import kotlin.math.max
import kotlin.math.min

fun main() {
    val d2 = Day02()
    d2.solvePart1()
    d2.solvePart2()
}

class Day02(output: Boolean = false) : Puzzle(output) {

    private val keypad = listOf(
            1 to Point(-1, -1),
            2 to Point(-1, 0),
            5 to Point(0, 0),
            8 to Point(1, 0),
            3 to Point(-1, 1),
            6 to Point(0, 1),
            9 to Point(1, 1),
            4 to Point(0, -1),
            7 to Point(1, -1),
    )

    override fun solvePart1() {
        val current = Point(0, 0)
        input.lines().forEach { l ->
            l.forEach { c ->
                when (c) {
                    'U' -> current.y = max(-1, current.y - 1)
                    'D' -> current.y = min(1, current.y + 1)
                    'L' -> current.x = max(-1, current.x - 1)
                    'R' -> current.x = min(1, current.x + 1)
                }
            }
            log(keypad.filter { it.second == current }[0].first, false)
        }
        log("")
    }

    private val otherKeypad = listOf(
            1 to Point(-2, 0),
            3 to Point(-1, 0),
            7 to Point(0, 0),
            'B' to Point(1, 0),
            'D' to Point(2, 0),

            4 to Point(-1, 1),
            8 to Point(0, 1),
            'C' to Point(1, 1),

            9 to Point(0, 2),

            2 to Point(-1, -1),
            6 to Point(0, -1),
            'A' to Point(1, -1),

            5 to Point(0, -2),
    )

    override fun solvePart2() {
        val current = Point(0, -2)
        input.lines().forEach { l ->
            l.forEach { c ->
                when (c) {
                    'U' -> current.y = otherKeypad.firstOrNull { it.second.y == current.y - 1 && it.second.x == current.x }?.second?.y
                            ?: current.y

                    'D' -> current.y = otherKeypad.firstOrNull { it.second.y == current.y + 1 && it.second.x == current.x }?.second?.y
                            ?: current.y

                    'L' -> current.x = otherKeypad.firstOrNull { it.second.x == current.x - 1 && it.second.y == current.y }?.second?.x
                            ?: current.x

                    'R' -> current.x = otherKeypad.firstOrNull { it.second.x == current.x + 1 && it.second.y == current.y }?.second?.x
                            ?: current.x
                }
            }
            log(otherKeypad.first { it.second == current }.first, false)
        }
    }
}
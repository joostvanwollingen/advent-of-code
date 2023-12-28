package nl.vanwollingen.aoc.aoc2015

import nl.vanwollingen.aoc.util.Puzzle

fun main() {
    val d2 = Day02(2015, 2)
    d2.solvePart1()
    d2.solvePart2()
}

class Day02(year: Int, day: Int) : Puzzle(year, day) {

    private val gifts = parseInput()
    override fun parseInput(): List<Gift> = input.lines().map { line ->
        Gift.fromString(line)
    }

    override fun solvePart1() {
        println(gifts.sumOf { it.surface + it.slack })
    }

    override fun solvePart2() {
        println(gifts.sumOf { it.ribbon })
    }

    data class Gift(val l: Int, val w: Int, val h: Int) {
        val surface = 2 * l * w + 2 * w * h + 2 * h * l
        val slack = listOf(l * w, w * h, l * h).min()
        val ribbon = l * w * h + (mutableListOf(l, w, h) - listOf(l, w, h).max()).reduce { one, two -> one + one + two + two }

        companion object {
            fun fromString(line: String): Gift {
                val (l, w, h) = line.split("x")
                return Gift(l.toInt(), w.toInt(), h.toInt())
            }
        }
    }
}
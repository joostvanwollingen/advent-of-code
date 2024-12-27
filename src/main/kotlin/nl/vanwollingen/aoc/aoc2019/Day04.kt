package nl.vanwollingen.aoc.aoc2019

import nl.vanwollingen.aoc.util.Puzzle

fun main() = Day04.solve()

object Day04 : Puzzle() {
    override fun part1() =
        (256310..732736).count {
            isValid(it)
        }

    override fun part2() =
        (256310..732736).count {
            isValid3(it)
        }

    private fun isValid(number: Int): Boolean {
        var hasTwoAdjacentDigits = false
        var isIncreasing = true

        "$number".windowed(2, 1) {
            if (it[0] == it[1]) hasTwoAdjacentDigits = true
            if (it[0] > it[1]) isIncreasing = false
        }

        return hasTwoAdjacentDigits && isIncreasing
    }

    private fun isValid3(number: Int): Boolean {
        var hasTwoAdjacentDigits = false
        var isIncreasing = true

        val asString = "$number"

        for (i in 0..<asString.length - 1) {
            val one = asString[i]
            val two = asString[i + 1]

            if (one == two) {
                val previous = if (i > 0) asString[i - 1] else null
                val next = if (i + 2 < asString.length) asString[i + 2] else null
                if (previous != one && next != one) hasTwoAdjacentDigits = true
            }

            if (one > two) isIncreasing = false
        }

        return hasTwoAdjacentDigits && isIncreasing
    }
}
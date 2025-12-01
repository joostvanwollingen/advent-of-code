package nl.vanwollingen.aoc.aoc2025

import nl.vanwollingen.aoc.util.Puzzle
import kotlin.math.abs

fun main() = Day01.solve()

object Day01 : Puzzle(false, false) {
    override fun part1(): Any {
        var dial = 50
        var atZero = 0
        input.lines().forEach { rawLine ->
            val (direction, count) = rawLine.subSequence(0, 1)[0] to rawLine.subSequence(1, rawLine.length).toString()
                .toInt()

            dial = updateDial(dial, direction, count)

            if (dial == 0) atZero++
        }
        return atZero
    }

    override fun part2(): Any {
        var dial = 50
        var atZero = 0
        input.lines().forEach { rawLine ->

            var (direction, count) = rawLine.subSequence(0, 1)[0] to rawLine.subSequence(1, rawLine.length).toString()
                .toInt()

            while (count > 0) {
                if (direction == 'L') {
                    dial -= 1
                } else {
                    dial += 1
                }
                if (dial > 99) dial = 0
                if (dial < 0) dial = 99
                if (dial == 0) atZero++

                count -= 1
            }
        }
        return atZero
    }

    private fun updateDial(dial: Int, direction: Char, count: Int): Int {
        val mod = count % 100
        when (direction) {
            'L' -> {
                var minus = dial - mod
                if (minus < 0) {
                    minus += 100
                }
                return abs(minus)
            }

            'R' -> {
                var plus = dial + mod
                if (plus > 99) {
                    plus = 100 - plus
                }
                return abs(plus)
            }

            else -> throw Error("Wrong Direction")
        }
    }
}
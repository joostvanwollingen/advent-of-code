package nl.vanwollingen.aoc.aoc2017

import nl.vanwollingen.aoc.util.Puzzle

fun main() {
    val d9 = Day09()
    d9.solvePart1()
    d9.solvePart2()
}

class Day09(output: Boolean = false) : Puzzle(output) {
    override fun part1() {
        var totalScore = 0
        var currentGroupScore = 0
        var ignoreNext = false
        var openGarbage = false

        for (c in input) {
            if (ignoreNext) {
                ignoreNext = false
                continue
            }

            when (c) {

                '{' -> {
                    if (!openGarbage) currentGroupScore += 1
                }

                '}' -> {
                    if (!openGarbage) {
                        totalScore += currentGroupScore
                        currentGroupScore -= 1
                    }
                }

                '<' -> {
                    openGarbage = true
                }

                '>' -> {
                    openGarbage = false
                }

                '!' -> {
                    ignoreNext = true

                }
            }
        }
        log(totalScore)
    }

    override fun part2() {
        var countInGarbage = 0
        var ignoreNext = false
        var openGarbage = false

        for (c in input) {
            if (ignoreNext) {
                ignoreNext = false
                continue
            }

            when (c) {

                '<' -> {
                    if (!openGarbage) openGarbage = true else countInGarbage += 1
                }

                '>' -> {
                    openGarbage = false
                }

                '!' -> {
                    ignoreNext = true

                }

                else -> {
                    if (openGarbage) countInGarbage += 1
                }
            }
        }
        log(countInGarbage)
    }
}
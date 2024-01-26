package nl.vanwollingen.aoc.aoc2017

import nl.vanwollingen.aoc.util.Puzzle

fun main() {
    val d1 = Day01()
    d1.solvePart1()
    d1.solvePart2()
}

class Day01(output: Boolean = false) : Puzzle(output) {
    override fun part1() {
        log(solveFirstCaptcha(input))
    }

    override fun part2() {
        log(solveSecondCaptcha(input))
    }

    private fun solveFirstCaptcha(input: String): Int {
        var sum = 0
        for (i in input.indices) {
            if (i + 1 <= input.lastIndex) {
                if (input[i] == input[i + 1]) sum += Integer.valueOf(input[i].toString())
            } else if (input[i] == input[0]) sum += Integer.valueOf(input[i].toString())
        }
        return sum
    }

    private fun solveSecondCaptcha(input: String): Int {
        var sum = 0
        for (i in input.indices) {
            val deltaIndex = (i + (input.length / 2)) % input.length
            if (input[i] == input[deltaIndex]) sum += Integer.valueOf(input[i].toString())
        }
        return sum
    }
}
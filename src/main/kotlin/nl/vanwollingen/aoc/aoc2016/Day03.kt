package nl.vanwollingen.aoc.aoc2016

import nl.vanwollingen.aoc.util.Puzzle

fun main() {
    val d3 = Day03()
    d3.solvePart1()
    d3.solvePart2()
}

class Day03(output: Boolean = false) : Puzzle(output) {
    override fun solvePart1() {
        var valid = 0
        input.lines().forEach { line ->
            val (first, second, third) = Regex("(\\d+)").findAll(line).map { it.groupValues.first().toInt() }.toList()
            valid += if (isValidTriangle(first, second, third)) 1 else 0
        }
        log(valid)
    }

    override fun solvePart2() {
        var valid = 0
        var firstColumn = mutableListOf<Int>()
        var secondColumn = mutableListOf<Int>()
        var thirdColumn = mutableListOf<Int>()
        input.lines().forEach { line ->
            val (first, second, third) = Regex("(\\d+)").findAll(line).map { it.groupValues.first().toInt() }.toList()
            firstColumn += first
            secondColumn += second
            thirdColumn += third

            if (firstColumn.size == 3) {
                valid += if (isValidTriangle(firstColumn[0], firstColumn[1], firstColumn[2])) 1 else 0
                valid += if (isValidTriangle(secondColumn[0], secondColumn[1], secondColumn[2])) 1 else 0
                valid += if (isValidTriangle(thirdColumn[0], thirdColumn[1], thirdColumn[2])) 1 else 0
                firstColumn.clear()
                secondColumn.clear()
                thirdColumn.clear()
            }
        }
        log(valid)
    }

    fun isValidTriangle(first: Int, second: Int, third: Int): Boolean = first + second > third && second + third > first && first + third > second
}
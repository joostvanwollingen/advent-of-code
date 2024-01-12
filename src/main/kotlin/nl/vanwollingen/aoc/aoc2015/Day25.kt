package nl.vanwollingen.aoc.aoc2015

import nl.vanwollingen.aoc.util.Puzzle

fun main() {
    val d25 = Day25()
    d25.part1()
}

class Day25(output: Boolean = false) : Puzzle(output) {
    override fun part1() {
        val targetRow = 2981
        val targetColumn = 3075
        val firstAnswer = 20151125L

        val rowValue = calculateRowValue(targetRow, 1)
        val columnValue = calculateColumnValue(targetColumn, targetRow, rowValue)

        var answer = firstAnswer
        for (i in 1..<columnValue) {
            answer = next(answer)
            if (i % 100000 == 0) debug(i)
        }
        log("Answer: $answer")
    }

    override fun part2() {
        TODO("Not yet implemented")
    }

    fun next(nr: Long): Long = (nr * 252533) % 33554393
    private fun calculateRowValue(rowNum: Int, start: Int): Int {
        var current = start
        for (i in 1..<rowNum) {
            current += i
        }
        return current
    }

    private fun calculateColumnValue(columnNumber: Int, rowNum: Int, start: Int): Int {
        var current = start
        var delta: Int = 0
        for (i in 1 until columnNumber) {
            delta = i + rowNum
            current += delta
        }
        return current
    }
}
package nl.vanwollingen.aoc.aoc2023

import nl.vanwollingen.aoc.util.PuzzleInputUtil
import nl.vanwollingen.aoc.util.grid.columns

fun main() {
//    val input = PuzzleInputUtil.load("2023/day13.test.input")
    val input = PuzzleInputUtil.load("2023/day13.input")
    Day13().solvePart1(input)
}

class Day13 {
    fun solvePart1(input: String) {
        val reflections = input.reader().readText().split("\n\n")

        var columnReflectionIndex: Int = 0
        var rowReflectionIndex: Int = 0
        var leftOf = 0L
        var aboveOf = 0L

        reflections.forEach { ref ->
            println("Reflection: ${reflections.indexOf(ref)}")
            rowReflectionIndex = getReflectionIndex(ref.lines(), "row", 1)
            columnReflectionIndex = getReflectionIndex(ref.columns(), offset = 1)
            leftOf += columnReflectionIndex
            aboveOf += rowReflectionIndex
            println("leftOf: $leftOf")
            println("aboveOf: $aboveOf")
        }

        println("")
        println("Summary")
        println("leftOf: $leftOf")
        println("aboveOf: $aboveOf")
        println("=> ${(100 * aboveOf) + leftOf}")

    }

    private fun Day13Part1(input: String) {
        val reflections = input.reader().readText().split("\n\n")

        var columnReflectionIndex: Int = 0
        var rowReflectionIndex: Int = 0
        var leftOf = 0L
        var aboveOf = 0L

        reflections.forEach { ref ->
            println("Reflection: ${reflections.indexOf(ref)}")
            columnReflectionIndex = getReflectionIndex(ref.columns())
            rowReflectionIndex = getReflectionIndex(ref.lines(), "row")
            leftOf += columnReflectionIndex
            aboveOf += rowReflectionIndex
        }

        println("")
        println("Summary")
        println("leftOf: $leftOf")
        println("aboveOf: $aboveOf")
        println("=> ${(100 * aboveOf) + leftOf}")
    }

    fun getReflectionIndex(input: List<String>, v: String = "column", offset: Int = 0): Int {
        var step = 0
        var columnReflectionIndex: Int = 0

        input.windowed(2, 1) {
            step++
            if (it.first() == it.last()) {
                println("Same $v $it at step $step")
                if (verifyRealReflection(input, step, offset)) {
                    println("Reflection was real")
                    columnReflectionIndex = step
                }
                return@windowed
            }
        }
        return columnReflectionIndex
    }

    fun verifyRealReflection(input: List<String>, step: Int, offset: Int): Boolean {
        val left = input.subList(0, step)
        val right = input.subList(step, input.size)
        val zipped = left.reversed().zip(right)
        return zipped.all { it.first == it.second }
    }

    private fun String.charDiffs(other: String): Int {
        var differences = 0;
        for (i in this.indices) {
            if (this[i] != other[i]) {
                differences++
            }
        }
        return differences
    }
}
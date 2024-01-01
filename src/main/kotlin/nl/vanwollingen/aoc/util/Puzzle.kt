package nl.vanwollingen.aoc.util

abstract class Puzzle(year: Int, day: Int, output: Boolean = false) {

    val input by lazy { PuzzleInputUtil.load("$year/Day${day.toString().padStart(2, '0')}.input") }
    open fun parseInput(): Any {
        TODO("Not yet implemented")
    }

    abstract fun solvePart1()
    abstract fun solvePart2()

    init {
        showOutput = output
    }

    companion object {
        var showOutput = false
        fun log(message: Any) {
            println(message.toString())
        }

        fun debug(message: Any) {
            if (showOutput) println(message.toString())
        }
    }
}
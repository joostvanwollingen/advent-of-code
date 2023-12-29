package nl.vanwollingen.aoc.util

abstract class Puzzle(year: Int, day: Int) {

    val input: String = PuzzleInputUtil.load("$year/Day${day.toString().padStart(2, '0')}.input")
    open fun parseInput(): Any {
        TODO("Not yet implemented")
    }

    abstract fun solvePart1()
    abstract fun solvePart2()
}
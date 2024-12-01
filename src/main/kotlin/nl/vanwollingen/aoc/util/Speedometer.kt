package nl.vanwollingen.aoc.util

fun main() {
    Speedometer(nl.vanwollingen.aoc.aoc2024.Day01, 100).measurePart1()
}

class Speedometer(val puzzle: Puzzle, val number: Int) {
    fun measurePart1() {
        val runs: MutableMap<Int, Long> = mutableMapOf()

        for (i in 1..number) {
            runs[i] = puzzle.solvePart1()
        }

        runs.forEach{
            println("${it.key}: ${it.value} μs")
        }
        println("min: ${runs.values.min()}")
        println("max: ${runs.values.max()}")
        println("avg: ${runs.values.average()}")
    }
}
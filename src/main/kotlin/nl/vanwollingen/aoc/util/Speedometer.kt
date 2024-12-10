package nl.vanwollingen.aoc.util

import nl.vanwollingen.aoc.aoc2024.Day09
import kotlin.time.TimedValue

fun main() {
    Speedometer(100) { Day09.solvePart1() }.measure()
    Speedometer(100) { Day09.solvePart2() }.measure()
}

class Speedometer<T>(val number: Int, val block: () -> TimedValue<T>) {
    fun measure() {
        val runs: MutableList<TimedValue<*>> = mutableListOf()

        for (i in 1..number) {
            runs.add(block())
        }

        runs.forEachIndexed { i, v ->
            println("${i + 1}: ${v.duration.inWholeMicroseconds} μs")
        }
        println("min: ${runs.minBy { it.duration }.duration.inWholeMicroseconds}")
        println("max: ${runs.maxBy { it.duration }.duration.inWholeMicroseconds}")
        println("avg: ${runs.sumOf { it.duration.inWholeMicroseconds }.div(runs.size)}")
    }
}
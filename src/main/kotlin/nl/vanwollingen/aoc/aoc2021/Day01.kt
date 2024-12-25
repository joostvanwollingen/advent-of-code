package nl.vanwollingen.aoc.aoc2021

import nl.vanwollingen.aoc.util.Puzzle

fun main() = Day01.solve()

object Day01 : Puzzle(exampleInput = false) {
    override fun part1() = input
        .lines()
        .map { it.toInt() }
        .zipWithNext()
        .count { it.first < it.second }

    override fun part2(): Int {
        val measurements = input
            .lines()
            .map { it.toInt() }

        var increased = 0

        var previousWindow = measurements[0] + measurements[1] + measurements[2]

        for (i in 1..measurements.size-3) {
            val newWindow = measurements[i] + measurements[i+1] + measurements[i+2]
            if(newWindow > previousWindow) increased++
            previousWindow = newWindow
        }
        return increased
    }
}
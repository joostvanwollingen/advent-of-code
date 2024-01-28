package nl.vanwollingen.aoc.aoc2017

import nl.vanwollingen.aoc.util.Puzzle

fun main() {
    val d5 = Day05()
    d5.solvePart1()
    d5.solvePart2()
}

class Day05(output: Boolean = false) : Puzzle(output) {
    private var jumps = parseInput()

    override fun parseInput(): MutableList<Int> = input.lines().map { Integer.valueOf(it.toString()) }.toMutableList()

    override fun part1() {
        var index = 0
        var steps = 0

        while (true) {
            steps++
            val oldIndex = index
            index += jumps[index]
            if (index >= jumps.size) break
            jumps[oldIndex] = jumps[oldIndex] + 1
        }
        log(steps)
    }

    override fun part2() {
        jumps = parseInput() //reset inputs
        var index = 0
        var steps = 0

        while (true) {
            steps++
            val oldIndex = index
            val offset = jumps[index]
            index += jumps[index]
            if (index >= jumps.size) break
            if (offset >= 3) jumps[oldIndex] = jumps[oldIndex] - 1 else jumps[oldIndex] = jumps[oldIndex] + 1
        }
        log(steps)
    }
}
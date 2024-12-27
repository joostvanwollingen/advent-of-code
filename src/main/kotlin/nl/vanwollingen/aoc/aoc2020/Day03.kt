package nl.vanwollingen.aoc.aoc2020

import nl.vanwollingen.aoc.util.Puzzle

fun main() = Day03.solve()

object Day03 : Puzzle(exampleInput = false) {

    val grid = parseInput()
    val maxY = grid.maxOf { it.key.first }
    val maxX = grid.maxOf { it.key.second } + 1

    override fun part1() = countTreesOnSlope(1 to 3)

    override fun part2() = listOf(
        1 to 1,
        1 to 3,
        1 to 5,
        1 to 7,
        2 to 1
    )
        .map { countTreesOnSlope(it).toLong() }
        .reduce { a, b -> a * b }

    private fun countTreesOnSlope(vector: Pair<Int, Int>): Int {
        var current = 0 to 0
        var trees = 0

        while (current.first <= maxY) {
            if (grid[current.first to current.second % maxX] == '#') {
                trees++
            }
            current = (current.first + vector.first) to (current.second + vector.second)
        }

        return trees
    }

    override fun parseInput() =
        input.lines().mapIndexed { y, line ->
            line.mapIndexed { x, c ->
                (y to x) to c
            }
        }.flatten().toMap()
}
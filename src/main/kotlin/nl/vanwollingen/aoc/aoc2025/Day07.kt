package nl.vanwollingen.aoc.aoc2025

import nl.vanwollingen.aoc.util.Puzzle
import java.util.*

fun main() = Day07.solve()

object Day07 : Puzzle(true, false) {

    private val grid = parseInput()
    private val gridLines = input.lines().count()
    private val gridWidth = input.lines().size

    override fun parseInput(): List<List<Char>> = input.lines().map { it.map { it } }

    override fun part1() = enterManifold((1 to grid[0].indexOf('S')))

    override fun part2() =
        enterQuantumManifold(
            grid,
            Array(grid.size) { Array(grid[0].size) { null } },
            1,
            grid[0].indexOf('S')
        )

    private fun enterQuantumManifold(
        grid: List<List<Char>>, state: Array<Array<Any?>>, y: Int, x: Int
    ): Long {
        for (i in y until grid.size) {

            // If we've already computed something for this cell, return it
            when (val v = state[i][x]) {
                is Long -> return v
                "|" -> continue  // already visited dead path
            }

            val cell = grid[i][x]

            if (cell == '^') {
                val left = enterQuantumManifold(grid, state, i, x - 1)
                val right = enterQuantumManifold(grid, state, i, x + 1)
                val sum = left + right
                state[i][x] = sum
                return sum
            }

            // mark in 'state' that this path is explored but not a branch
            state[i][x] = "|"
        }

        return 1
    }

    private fun enterManifold(start: Pair<Int, Int>): Long {
        var splittersHit = 0L
        val tachyonBeams: Queue<Pair<Int, Int>> = LinkedList()
        val visited: MutableMap<Pair<Int, Int>, Boolean> = mutableMapOf(start to true)
        tachyonBeams.add(start)

        while (tachyonBeams.isNotEmpty()) {
            val (y, x) = tachyonBeams.poll()
            if (y + 1 == gridLines) {
                continue
            }

            val nextLocation = grid[y][x]
            val nextBeams: List<Pair<Int, Int>> = if (nextLocation == '^') {
                splittersHit++
                setOf(
                    y to x - 1, y to x + 1
                )
            } else {
                setOf(
                    y + 1 to x
                )
            }.filterNot { it.second < 0 || it.second > gridWidth }
            for (beam in nextBeams) {
                if (!visited.contains(beam)) {
                    tachyonBeams.add(beam)
                    visited[beam] = true
                }
            }
        }
        return splittersHit
    }

    private fun logGrid(grid: List<List<Char>>, visited: Set<Pair<Int, Int>>) {
        if (printDebug) {
            for (y in grid.indices) {
                for (x in grid[y].indices) {
                    if (grid[y][x] != '^' && visited.contains(y to x)) {
                        debug("|", false)
                    } else {
                        debug(grid[y][x], false)
                    }

                }
                debug("")
            }
        }
    }
}
package nl.vanwollingen.aoc.aoc2025

import nl.vanwollingen.aoc.util.Puzzle

fun main() = Day04.solve()

object Day04 : Puzzle(true, false) {

    val grid = parseInput()
    val maxY = grid.size - 1
    val maxX = grid[0].size - 1

    override fun parseInput(): List<List<Char>> = input.lines().map { it.map { it } }

    override fun part1(): Any {
        var positionsReachable = 0
        for (y in grid.indices) {
            for (x in grid[y].indices) {
                if (grid[y][x] == '@') {
                    val adjacentRolls = countAdjacentPaperRolls(grid, y, x)
                    positionsReachable += if (adjacentRolls < 4) 1 else 0
                }
            }
        }
        return positionsReachable
    }

    override fun part2(): Any {
        var currentGrid = grid
        var rollsRemoved = 0

        while (true) {
            var positionsReachable = 0
            val removedRolls = mutableListOf<Pair<Int, Int>>()
            for (y in currentGrid.indices) {
                for (x in currentGrid[y].indices) {
                    if (currentGrid[y][x] == '@') {
                        val adjacentRolls = countAdjacentPaperRolls(currentGrid, y, x)
                        if (adjacentRolls < 4) {
                            positionsReachable += 1
                            rollsRemoved += 1
                            removedRolls.add(y to x)
                        }
                    }
                }
            }
            if (positionsReachable == 0) break
            currentGrid = updateGrid(currentGrid, removedRolls)
        }
        return rollsRemoved
    }

    private fun updateGrid(theGrid: List<List<Char>>, removedRolls: MutableList<Pair<Int, Int>>): List<List<Char>> {
        val newGrid = theGrid.map { it.toMutableList() }.toMutableList()
        for (y in theGrid.indices) {
            for (x in theGrid[y].indices) {
                if (removedRolls.contains(y to x)) {
                    newGrid[y][x] = 'x'
                } else {
                    newGrid[y][x] = theGrid[y][x]
                }
            }
        }
        return newGrid
    }

    private fun countAdjacentPaperRolls(currentGrid: List<List<Char>>, y: Int, x: Int): Int {
        val positionsToCheck = setOf(
            y + 1 to x - 1,
            y + 1 to x,
            y + 1 to x + 1,
            y to x - 1,
            y to x + 1,
            y - 1 to x - 1,
            y - 1 to x,
            y - 1 to x + 1,
        ).filterNot { (nY, nX) ->
            nY > maxY || nX > maxX || nY < 0 || nX < 0
        }

        return positionsToCheck.count { (pY, pX) ->
            currentGrid[pY][pX] == '@'
        }
    }
}
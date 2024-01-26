package nl.vanwollingen.aoc.aoc2016

import nl.vanwollingen.aoc.util.Puzzle
import nl.vanwollingen.aoc.util.combinatorics.permutations
import java.util.*
import kotlin.math.min

fun main() {
    val d24 = Day24(true)
    d24.solvePart1()
    d24.solvePart2()
}

class Day24(output: Boolean = false) : Puzzle(output) {

    private val gridAndDestinations = parseInput()
    private val grid = gridAndDestinations.first
    private val destinations = gridAndDestinations.second
    private val costsByPoi: Map<Pair<Int, Int>, Array<Array<Int>>> =
        destinations.associateWith { d -> calculateCosts(grid, d.first, d.second) }
    private val startAndEnd = 27 to 177

    override fun parseInput(): Pair<Array<Array<Char>>, List<Pair<Int, Int>>> {
        val lines = input.lines()
        val grid = Array(lines.size) { Array(lines[0].length) { '-' } }
        val destinations: MutableList<Pair<Int, Int>> = mutableListOf()

        for (i in lines.indices) {
            val line = lines[i]
            for (j in line.indices) {
                grid[i][j] = line[j]
                if (line[j] != '#' && line[j] != '.') {
                    destinations += i to j
                }
            }
        }
        return grid to destinations
    }

    override fun part1() {
        debug {
            grid.forEachIndexed { i, line ->
                line.forEachIndexed { j, c ->
                    if (c != '#' && c != '.') {
                        print("\u001b[43m$c\u001b[0m")
                    } else {
                        print(c)
                    }
                }
                println("")
            }
        }

        log(destinations.permutations(destinations.size).toList().filter { it[0] == startAndEnd }
            .minOf { getRouteCosts(it, costsByPoi) })
    }

    override fun part2() {
        val destinations = destinations + destinations.single { it == startAndEnd }
        log(destinations.permutations(destinations.size).toList()
            .filter { it[0] == startAndEnd && it[it.lastIndex] == startAndEnd }.minOf { getRouteCosts(it, costsByPoi) })
    }

    private fun getRouteCosts(route: List<Pair<Int, Int>>, costsByPoi: Map<Pair<Int, Int>, Array<Array<Int>>>): Int {
        var costs = 0
        route.windowed(2, 1) { poi ->
            costs += costsByPoi[poi.first()]!![poi.last().first][poi.last().second]
        }
        return costs
    }

    private fun calculateCosts(grid: Array<Array<Char>>, y: Int, x: Int): Array<Array<Int>> {
        val costs = Array(grid.size) {
            Array(grid[0].size) {
                Int.MAX_VALUE
            }
        }

        val currentNode = y to x
        val q: Queue<Pair<Int, Int>> = LinkedList()

        q.add(currentNode)
        costs[currentNode.first][currentNode.second] = 0

        while (q.isNotEmpty()) {
            val nextNode = q.remove()
            val allowedDirections = grid.possibleMoves(nextNode.first, nextNode.second)

            allowedDirections.forEach { dir ->
                if (costs[dir.first][dir.second] == Int.MAX_VALUE) {
                    val cost = costs[nextNode.first][nextNode.second] + 1
                    costs[dir.first][dir.second] = min(cost, costs[dir.first][dir.second])
                    q.add(dir)
                }
            }
        }
        return costs
    }

    private fun Array<Array<Char>>.possibleMoves(y: Int, x: Int): List<Pair<Int, Int>> {
        val moves = mutableListOf<Pair<Int, Int>>()

        val north = this.getOrNull(y - 1)?.getOrNull(x)
        val south = this.getOrNull(y + 1)?.getOrNull(x)
        val east = this.getOrNull(y)?.getOrNull(x + 1)
        val west = this.getOrNull(y)?.getOrNull(x - 1)

        if (north == '.' || north?.isDigit() == true) moves += y - 1 to x
        if (south == '.' || south?.isDigit() == true) moves += y + 1 to x
        if (east == '.' || east?.isDigit() == true) moves += y to x + 1
        if (west == '.' || west?.isDigit() == true) moves += y to x - 1

        return moves
    }
}
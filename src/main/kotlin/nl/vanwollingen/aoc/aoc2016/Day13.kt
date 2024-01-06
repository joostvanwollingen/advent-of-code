package nl.vanwollingen.aoc.aoc2016

import nl.vanwollingen.aoc.util.Puzzle
import java.util.*

fun main() {
    val d13 = Day13(2016, 13)
    d13.solvePart1()
    d13.solvePart2()
}

class Day13(year: Int, day: Int, output: Boolean = false) : Puzzle(year, day, output) {
    private lateinit var distancesForPartTwo: Array<IntArray>

    override fun solvePart1() {
        val grid = populateGrid(50, 50)
        val visited = Array(grid.size) { IntArray(grid[0].size) }
        val distances = tentativeDistanceGrid(50, 50)

        val startLocation = 1 to 1
        val endLocation = 39 to 31

        distances[startLocation.y()][startLocation.x()] = 0

        val q: Queue<Pair<Int, Int>> = LinkedList()
        q.add(startLocation)
        while (q.isNotEmpty()) {
            val current = q.remove()
            val next = unvisitedNeighbours(current.first, current.second, grid, visited)

            next.forEach { neighbour ->
                val currentDistance = distances[neighbour.y()][neighbour.x()]
                val calculatedDistance = distances[current.y()][current.x()] + 1
                if (calculatedDistance < currentDistance) distances[neighbour.y()][neighbour.x()] = calculatedDistance
            }
            visited[current.y()][current.x()] = 1
            q.addAll(next)
        }
        grid.print(ty = endLocation.y(), tx = endLocation.x(), visited = visited)
        log(distances[endLocation.y()][endLocation.x()])
        this.distancesForPartTwo = distances
    }

    override fun solvePart2() {
        var withinDistance50 = 0
        for (y in distancesForPartTwo.indices) {
            for (x in 0..<distancesForPartTwo[0].size) {
                if (distancesForPartTwo[y][x] <= 50) withinDistance50++
            }
        }
        log(withinDistance50)
    }

    private fun unvisitedNeighbours(y: Int, x: Int, grid: Array<IntArray>, visited: Array<IntArray>): List<Pair<Int, Int>> {
        if (y > grid.size || y < 0 || x < 0 || x > grid[0].size) {
            throw Exception("Out of bounds")
        }

        val result: MutableList<Pair<Int, Int>> = mutableListOf()

        if (y - 1 >= 0) {
            if (visited[y - 1][x] == 0) {
                val north = grid[y - 1][x]
                if (north == 0) result += y - 1 to x
            }
        }

        if (y + 1 < grid.size) {
            if (visited[y + 1][x] == 0) {
                val south = grid[y + 1][x]
                if (south == 0) result += y + 1 to x
            }
        }

        if (x - 1 >= 0) {
            if (visited[y][x - 1] == 0) {
                val west = grid[y][x - 1]
                if (west == 0) result += y to x - 1
            }
        }

        if (x + 1 < grid[0].size) {
            if (visited[y][x + 1] == 0) {
                val east = grid[y][x + 1]
                if (east == 0) result += y to x + 1
            }
        }
        return result
    }

    private fun populateGrid(y: Int, x: Int): Array<IntArray> {
        val grid = Array(y) { IntArray(x) }

        for (i in 0..<y) {
            for (j in 0..<x) {
                val number: Int = (j * j) + (3 * j) + (2 * j * i) + i + (i * i) + input.toInt()
                grid[i][j] = if (number.countOneBits().isEven()) 0 else 1
            }
        }

        return grid
    }

    private fun Int.isEven(): Boolean = this % 2 == 0

    private fun Array<IntArray>.print(ty: Int = 1, tx: Int = 1, visited: Array<IntArray>? = null) {
        var prefix = ""
        val suffix = "\u001B[0m"
        for (y in indices) {
            print("${y}:\t")
            for (x in 0..<this[y].size) {
                if (tx == x && ty == y) {
                    prefix = "\u001B[44m"
                }
                if (visited != null && visited[y][x] != 0) {
                    prefix = "\u001B[43m"
                }
                if (this[y][x] == 1) print("$prefix#$suffix") else print("$prefix.$suffix")
                prefix = ""
            }
            println("")
        }
        println("")
    }

    private fun tentativeDistanceGrid(y: Int, x: Int): Array<IntArray> {
        val distances = Array(y) { IntArray(x) }
        for (row in distances) {
            for (j in 0..<distances[0].size) {
                row[j] = Int.MAX_VALUE
            }
        }
        return distances
    }

    private fun Pair<Int, Int>.x() = this.second
    private fun Pair<Int, Int>.y() = this.first
}

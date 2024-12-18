package nl.vanwollingen.aoc.aoc2024

import nl.vanwollingen.aoc.util.Puzzle
import java.util.*

fun main() = Day18.solve()

object Day18 : Puzzle(exampleInput = false) {

    override fun part1(): Int {
        val (grid: MutableMap<Pair<Int, Int>, Char>, start, end) = buildGrid()
        dropBytes(1024, grid)
        return stepsToExit(grid, start, end)
    }

    override fun part2() = binarySearchForDropBytes(part1(), input.lines().size, 0 to 0, 70 to 70)

    private fun binarySearchForDropBytes(
        startValue: Int, endValue: Int, start: Pair<Int, Int>, end: Pair<Int, Int>
    ): Int {
        var low = startValue
        var high = endValue
        var result = -1

        while (low <= high) {
            val mid = low + (high - low) / 2
            val grid = buildGrid().first
            dropBytes(mid, grid) // Apply the dropBytes function to modify the grid

            val steps = stepsToExit(grid, start, end)

            if (steps == Int.MAX_VALUE) {
                result = mid // Potential candidate, continue searching for lower values
                high = mid - 1
            } else {
                low = mid + 1 // Mid is too low, search higher
            }
        }

        return result
    }

    private fun stepsToExit(
        grid: MutableMap<Pair<Int, Int>, Char>, start: Pair<Int, Int>, end: Pair<Int, Int>
    ): Int {
        val dist = mutableMapOf<Pair<Int, Int>, Int>().apply {
            putAll(grid.keys.toList().map { it to Int.MAX_VALUE })
            put(start, 0)
        }

        val visited: MutableMap<Pair<Int, Int>, Boolean> = grid.map { it.key to false }.toMap().toMutableMap()

        val q = LinkedList<Pair<Int, Int>>().apply { add(start) }

        while (q.isNotEmpty()) {
            val current = q.poll()
            if (visited[current] == true) {
                continue
            }

            val neighbours: List<Pair<Int, Int>> = getNeighbours(current, grid, visited)
            neighbours.forEach { neighbour ->
                val currentDistance = dist[neighbour]
                val calculatedDistance = dist[current]!! + 1
                if (calculatedDistance < currentDistance!!) dist[neighbour] = calculatedDistance
            }
            visited[current] = true
            q.addAll(neighbours)
        }
        return dist[end]!!
    }

    private fun dropBytes(number: Int, grid: MutableMap<Pair<Int, Int>, Char>) {
        input.lines().take(number).forEach { line ->
            val (x, y) = line.split(",").map { it.toInt() }
            grid[y to x] = '#'
        }
    }

    private fun buildGrid(): Triple<MutableMap<Pair<Int, Int>, Char>, Pair<Int, Int>, Pair<Int, Int>> {
        val grid: MutableMap<Pair<Int, Int>, Char> = mutableMapOf<Pair<Int, Int>, Char>().apply {
            for (y in 0..70) {
                for (x in 0..70) {
                    put(y to x, '.')
                }
            }
        }
        val start = 0 to 0
        val end = 70 to 70

        return Triple(grid, start, end)
    }

    private fun getNeighbours(
        current: Pair<Int, Int>,
        grid: MutableMap<Pair<Int, Int>, Char>,
        visited: MutableMap<Pair<Int, Int>, Boolean>,
    ): List<Pair<Int, Int>> {
        val (y, x) = current
        val north =
            grid[y - 1 to x]?.let { if (it != '#') y - 1 to x else null }?.let { if (visited[it] == true) null else it }
        val south =
            grid[y + 1 to x]?.let { if (it != '#') y + 1 to x else null }?.let { if (visited[it] == true) null else it }
        val east =
            grid[y to x + 1]?.let { if (it != '#') y to x + 1 else null }?.let { if (visited[it] == true) null else it }
        val west =
            grid[y to x - 1]?.let { if (it != '#') y to x - 1 else null }?.let { if (visited[it] == true) null else it }
        return listOfNotNull(north, south, east, west)
    }
}
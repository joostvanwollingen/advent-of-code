package nl.vanwollingen.aoc.aoc2024

import nl.vanwollingen.aoc.util.Puzzle
import java.util.*
import kotlin.math.abs

fun main() = Day20.solve()

object Day20 : Puzzle(exampleInput = false) {

    private val grid = parseInput()

    override fun part1(): Any {
        val end = grid.filter { it.value == 'E' }.keys.first()
        val dist = getDistances(grid, end)
        val path = dist.filter { it.value != Int.MAX_VALUE }
        val maxDist = path.maxOf { it.value }

        val distSaved = mutableMapOf<Pair<Pair<Int, Int>, Pair<Int, Int>>, Int>()

        path.keys.forEach { location ->
            val initialDistance = dist[location]!!
            val neighbors = getPathNeighbors(location, grid, path)
            neighbors.forEach { n ->
                val nDist = dist.getOrDefault(n, null) ?: throw Error("neigbhor not in path")
                distSaved[location to n] = getSaved(maxDist, initialDistance, nDist)
            }
        }

        val actualSaved = distSaved.entries.groupingBy { it.value }.eachCount()
        return actualSaved.filterKeys { it >= 100 }.entries.sumOf { it.value }
    }

    override fun part2(): Any {
        val end = grid.filter { it.value == 'E' }.keys.first()
        val dist = getDistances(grid, end)
        val path = dist.filter { it.value != Int.MAX_VALUE }
        val distSaved = mutableMapOf<Pair<Pair<Int, Int>, Pair<Int, Int>>, Int>()

        path.keys.forEach { location ->
            val initialDistance = dist[location]!!
            val neighbors = radius(location,20, path).filter { path[it] != null }
            neighbors.forEach { n ->
                val nDist = dist.getOrDefault(n, null) ?: throw Error("neigbhor not in path")
                distSaved[location to n] = initialDistance - nDist - getManhattanDistance(location, n)
            }
        }

        val actualSaved = distSaved.entries.groupingBy { it.value }.eachCount()
        return actualSaved.filterKeys { it >= 100 }.entries.sumOf { it.value }
    }

    private fun getManhattanDistance(one: Pair<Int, Int>, other:Pair<Int, Int>) = abs(one.second - other.second) + abs(one.first - other.first)
    private fun getSaved2(maxDist: Int, s: Int, e: Int) = maxDist - (maxDist - (s - e))

    private fun radius(current: Pair<Int, Int>, radius: Int = 20, path: Map<Pair<Int, Int>, Int>): List<Pair<Int, Int>> {
        val (y, x) = current
        val result = mutableListOf<Pair<Int, Int>>()
        for (dy in -radius..radius){
            for (dx in -radius..radius) {
                if (abs(dx) + abs(dy) <= radius){
                    result.add(y+dy to x+dx)
                }
            }
        }
        return result.minus(current)
    }

    private fun getSaved(maxDist: Int, s: Int, e: Int) = maxDist - (maxDist - (s - e) + 2)

    private fun getDistances(
        grid: MutableMap<Pair<Int, Int>, Char>, start: Pair<Int, Int>
    ): Map<Pair<Int, Int>, Int> {
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
        return dist
    }


    override fun parseInput(): MutableMap<Pair<Int, Int>, Char> {
        val grid: MutableMap<Pair<Int, Int>, Char> = mutableMapOf()
        var y = 0

        input.lines().forEach { line ->
            var x = 0
            line.forEach { cell ->
                grid[y to x] = cell
                x++
            }
            y++
        }
        return grid
    }

    private fun getPathNeighbors(
        current: Pair<Int, Int>,
        grid: Map<Pair<Int, Int>, Char>,
        visited: Map<Pair<Int, Int>, Int>,
    ): List<Pair<Int, Int>> {
        val (y, x) = current
        val north =
            grid[y - 2 to x]?.let { if (it != '#') y - 2 to x else null }?.let { if (visited[it] == null) null else it }
        val south =
            grid[y + 2 to x]?.let { if (it != '#') y + 2 to x else null }?.let { if (visited[it] == null) null else it }
        val east =
            grid[y to x + 2]?.let { if (it != '#') y to x + 2 else null }?.let { if (visited[it] == null) null else it }
        val west =
            grid[y to x - 2]?.let { if (it != '#') y to x - 2 else null }?.let { if (visited[it] == null) null else it }
        return listOfNotNull(north, south, east, west)
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
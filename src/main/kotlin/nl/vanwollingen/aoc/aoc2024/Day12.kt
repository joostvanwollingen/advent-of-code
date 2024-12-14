package nl.vanwollingen.aoc.aoc2024

import nl.vanwollingen.aoc.util.Puzzle
import java.util.LinkedList

fun main() = Day12.solve()

object Day12 : Puzzle(exampleInput = true) {

    private val grid = parseInput()
    private val maxY = grid.keys.max()
    private val maxX = grid.values.maxOf { it.keys.max() }
    private val regions: MutableList<Set<Pair<Int, Int>>> = getRegions()

    override fun parseInput(): Map<Int, Map<Int, Char>> {
        val grid: MutableMap<Int, Map<Int, Char>> = mutableMapOf()
        var y = 0
        input.lines().forEach { line ->
            val rowMap = mutableMapOf<Int, Char>()
            var x = 0
            line.forEach { cell ->
                rowMap[x++] = cell
            }
            grid[y++] = rowMap
        }
        return grid
    }

    override fun part1(): Int {
        if (printDebug) {
            regions.forEach { region ->
                val (y, x) = region.first()
                val size = region.size
                val perim = getPerimeter(region, grid)
                println("${grid[y]!![x]!!} (${size * perim}) Size: $size Perimeter: $perim Nodes: $region")
            }
        }

        return regions.sumOf { getPerimeter(it, grid) * it.size }
    }

    override fun part2(): Int {
        regions.forEach { region ->
            val (y, x) = region.first()
            log("${grid[y]!![x]!!} ${outerPerimeterWalk(region)}")
        }
        return 0
    }

    private fun outerPerimeterWalk(region: Set<Pair<Int, Int>>): Int {
        var (y, x) = region.minWithOrNull(compareBy({ it.second }, { it.first }))!!

        val regionType = grid[y]!![x]!!
        var direction = Pair(-1, 0)
        val visited = mutableSetOf(Pair(y, x) to direction)

        var sides = 0
        var hasTurnedOnce = false


        while (true) {

            val nextSquare =
                grid[y + direction.first]?.get(x + direction.second)

            if (nextSquare == null) {
                direction = direction.turn90Degrees()
                continue
            }

            val leftHandNeighbour =
                getLeftHandNeighbour(direction).let { grid[it.first]?.get(it.second) }.takeIf { it != null }


            val nextSquareIsSameRegion =
                grid[y + direction.first]?.get(x + direction.second) == regionType

            if (nextSquareIsSameRegion) {
                if (direction.first == -1) {
                    y -= 1
                    x -= 1
                }
                if (direction.first == 1) {
                    y += 1
                    x += 1
                }
            }


            if (!nextSquareIsSameRegion) {
                direction = direction.turn90Degrees()
                if (hasTurnedOnce) sides++
                hasTurnedOnce = true
            } else {
                y += direction.first
                x += direction.second
            }

//            if (!visited.add(Pair(y, x) to direction)) {
//                return sides
//            }
            printGrid(grid, y to x, direction)
        }
        throw Error("Missed starting")
    }

    private fun getLeftHandNeighbour(direction: Pair<Int, Int>): Pair<Int, Int> {
        return when (direction) {
            -1 to 0 -> {
                0 to -1
            }

            1 to 0 -> {
                0 to 1
            }

            0 to -1 -> {
                1 to 0
            }

            0 to 1 -> {
                -1 to 0
            }

            else -> throw Error("strange direction received $direction")
        }
    }

    private fun getRegions(): MutableList<Set<Pair<Int, Int>>> {
        val visited: MutableSet<Pair<Int, Int>> = mutableSetOf()
        val regions: MutableList<Set<Pair<Int, Int>>> = mutableListOf()

        for (y in 0..maxY) {
            for (x in 0..maxX) {
                val region = getRegionNodes(y, x, visited)
                if (region.isNotEmpty()) regions.add(region)
            }
        }
        return regions
    }

    private fun getPerimeter(region: Set<Pair<Int, Int>>, grid: Map<Int, Map<Int, Char>>): Int {
        val (y, x) = region.first()
        val regionType = grid[y]!![x]!!
        return region.sumOf { (y, x) ->
            4 - getNeighbours(y, x, regionType).size
        }
    }

    private fun getRegionNodes(y: Int, x: Int, visited: MutableSet<Pair<Int, Int>>): Set<Pair<Int, Int>> {
        val currentCell = y to x
        if (visited.contains(currentCell)) return emptySet()

        val regionSet = mutableSetOf(currentCell)
        val regionType = grid[y]!![x]!!

        val q = LinkedList(getNeighbours(y, x, regionType))

        while (q.peek() != null) {
            val (newY, newX) = q.poll()
            regionSet.add(newY to newX)
            val newNeighbours = getNeighbours(newY, newX, regionType).filterNot { regionSet.contains(it) }
            q.addAll(newNeighbours)
            regionSet.addAll(newNeighbours)
        }
        visited.addAll(regionSet)
        return regionSet
    }

    private fun getNeighbours(y: Int, x: Int, target: Char): List<Pair<Int, Int>> {
        val north = grid[y - 1]?.get(x)?.let { if (it == target) y - 1 to x else null }
        val south = grid[y + 1]?.get(x)?.let { if (it == target) y + 1 to x else null }
        val east = grid[y]?.get(x + 1)?.let { if (it == target) y to x + 1 else null }
        val west = grid[y]?.get(x - 1)?.let { if (it == target) y to x - 1 else null }
        return listOfNotNull(north, south, east, west)
    }

    private fun Pair<Int, Int>.turn90Degrees(): Pair<Int, Int> {
        if (first == -1) return Pair(0, 1)
        if (first == 1) return Pair(0, -1)
        if (second == -1) return Pair(-1, 0)
        if (second == 1) return Pair(1, 0)
        else throw Error("wtf")
    }

    fun printGrid(
        grid: Map<Int, Map<Int, Char>>,
        guardPosition: Pair<Int, Int>,
        direction: Pair<Int, Int>,
    ) {
        val gridHeight = grid.keys.maxOrNull() ?: 0
        val gridWidth = grid.values.maxOf { it.keys.maxOrNull() ?: 0 }

        val player =
            if (direction.first == -1) "^" else if (direction.first == 1) "v" else if (direction.second == -1) "<" else ">"

        for (row in 0..gridHeight) {
            for (col in 0..gridWidth) {
                when {
                    guardPosition == Pair(row, col) -> print(player) // Print guard position
                    else -> print(grid[row]!![col])
                }
            }
            println() // New line after each row
        }

        println()
    }
}
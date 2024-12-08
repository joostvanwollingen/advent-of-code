package nl.vanwollingen.aoc.aoc2024

import nl.vanwollingen.aoc.util.Puzzle
import nl.vanwollingen.aoc.util.combinatorics.combinations

fun main() = Day08.solve()

object Day08 : Puzzle(exampleInput = false, printDebug = false) {

    private val grid = parseInput()

    private val antennaByFrequency = grid.flatMap { (row, columns) ->
        columns.mapNotNull { (col, value) -> if (value != '.') value to Pair(row, col) else null }
    }.groupBy(
        keySelector = { it.first }, //Frequency
        valueTransform = { it.second } //Location
    )

    private val freqToPairMap = antennaByFrequency.map { it.key to it.value.combinations(2).toList() }.toMap()

    private val gridMaxY = grid.keys.max()
    private val gridMaxX = grid.values.maxOf { it.keys.max() }

    override fun parseInput(): MutableMap<Int, MutableMap<Int, Char>> {
        val grid: MutableMap<Int, MutableMap<Int, Char>> = mutableMapOf()
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
        val validAntiNodes = mutableSetOf<Pair<Int, Int>>()

        freqToPairMap.entries.forEach { freq ->
            debug(freq.key)
            freq.value.forEach { pair ->
                val (a, b) = pair
                val vector = getVector(a, b)
                val antiNodeA = getAntiNode(b, getVector(a, b))
                val antiNodeB = getAntiNode(a, getVector(a, b), true)
                debug("$a to $b: v${vector} anti: $antiNodeA $antiNodeB")
                if (withinGrid(antiNodeA, gridMaxY, gridMaxX)) validAntiNodes.add(antiNodeA)
                if (withinGrid(antiNodeB, gridMaxY, gridMaxX)) validAntiNodes.add(antiNodeB)
            }
        }

        return validAntiNodes.size
    }

    override fun part2(): Int {
        val validAntiNodes = mutableSetOf<Pair<Int, Int>>()
        val gridMaxY = grid.keys.max()
        val gridMaxX = grid.values.maxOf { it.keys.max() }

        freqToPairMap.entries.forEach { freq ->
            debug(freq.key)
            freq.value.forEach { pair ->
                val (a, b) = pair
                val vector = getVector(a, b)
                validAntiNodes.addAll(getAntiNodesUpToBoundary(a, vector, false, gridMaxY, gridMaxX))
                validAntiNodes.addAll(getAntiNodesUpToBoundary(b, vector, true, gridMaxY, gridMaxX))
            }
        }

        return validAntiNodes.size
    }

    private fun withinGrid(
        node: Pair<Int, Int>,
        y: Int,
        x: Int
    ): Boolean = (node.first in 0..y) && (node.second in 0..x)

    private fun getAntiNodesUpToBoundary(
        start: Pair<Int, Int>,
        vector: Pair<Int, Int>,
        negate: Boolean = false,
        y: Int, x: Int
    ): Set<Pair<Int, Int>> {
        val antiNodes: MutableSet<Pair<Int, Int>> = mutableSetOf()
        var lastNode = start
        while (true) {
            lastNode = getAntiNode(lastNode, vector, negate)
            if (!withinGrid(lastNode, y, x)) break
            antiNodes.add(lastNode)
        }
        return antiNodes
    }

    private fun getAntiNode(start: Pair<Int, Int>, vector: Pair<Int, Int>, negate: Boolean = false): Pair<Int, Int> {
        return if (negate) start.first + vector.first * -1 to start.second + vector.second * -1
        else start.first + vector.first to start.second + vector.second
    }

    private fun getVector(start: Pair<Int, Int>, end: Pair<Int, Int>): Pair<Int, Int> =
        Pair(end.first - start.first, end.second - start.second)
}
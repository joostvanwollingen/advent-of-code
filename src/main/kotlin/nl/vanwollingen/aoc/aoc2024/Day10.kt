package nl.vanwollingen.aoc.aoc2024

import nl.vanwollingen.aoc.util.Puzzle

fun main() = Day10.solve()

object Day10 : Puzzle(exampleInput = false, printDebug = true) {

    private val data = parseInput()
    private val mapData = data.first
    private val trailHeads = data.second
    private var hitTarget = 0

    override fun parseInput(): Pair<MutableMap<Int, Map<Int, Int>>, MutableList<Pair<Int, Int>>> {
        val trailHeads = mutableListOf<Pair<Int, Int>>()
        val heightMap = mutableMapOf<Int, Map<Int, Int>>()
        input.lines().forEachIndexed { y, line ->
            val lineMap = mutableMapOf<Int, Int>()
            line.forEachIndexed { x, c ->
                if (c == '0') {
                    trailHeads.add(y to x)
                }
                lineMap[x] = "$c".toInt()
            }
            heightMap[y] = lineMap
        }
        return heightMap to trailHeads
    }

    override fun part1(): Int =
        trailHeads.sumOf { (y, x) ->
            findTargetFrom(y, x, 0, mutableSetOf())
                .count { (y, x) -> mapData[y]!![x] == 9 }
        }

    override fun part2(): Int = hitTarget

    private fun findTargetFrom(
        y: Int,
        x: Int,
        currentStep: Int,
        visited: MutableSet<Pair<Int, Int>>,
        direction: Int = 1,
        target: Int = 9
    ): MutableSet<Pair<Int, Int>> {
        visited.add(y to x)
        if (mapData[y]!![x] == target) hitTarget++
        val neighbours = getNeighbours(y, x).filter { (nY, nX) -> mapData[nY]?.get(nX) == currentStep + direction }
        if (neighbours.isNotEmpty()) {
            visited.addAll(neighbours.flatMap { (nY, nX) ->
                findTargetFrom(
                    nY,
                    nX,
                    currentStep + direction,
                    visited,
                    direction,
                    target
                )
            })
        }
        return visited
    }

    private fun getNeighbours(y: Int, x: Int): List<Pair<Int, Int>> {
        val north = mapData[y - 1]?.get(x)?.let { y - 1 to x }
        val south = mapData[y + 1]?.get(x)?.let { y + 1 to x }
        val east = mapData[y]?.get(x + 1)?.let { y to x + 1 }
        val west = mapData[y]?.get(x - 1)?.let { y to x - 1 }
        return listOfNotNull(north, south, east, west)
    }
}
package nl.vanwollingen.aoc.aoc2023

import nl.vanwollingen.aoc.grid.Point
import nl.vanwollingen.aoc.util.AocUtil
import kotlin.streams.asStream
import kotlin.streams.toList

fun main() {
    val gridd: List<List<Tile>> = Day21().grid
    val grid = gridd.flatten()
    val gardenPlotsVisited: MutableSet<Tile> = mutableSetOf()
    var newGardenPlots: MutableSet<Tile> = mutableSetOf()
    val steps = 6

    val startingPoint = grid.first { it.s == "S" }

    newGardenPlots.add(startingPoint)

    for (i in 1..64) {

        newGardenPlots = newGardenPlots.map { plot ->
            grid.filter { g ->
                plot.l.getManhattanNeighbours().contains(g.l) && g.s != "#"
            }
        }.flatten().toMutableSet()
//        gardenPlotsVisited.addAll(newGardenPlots)
//        grid.print(newGardenPlots.map { it.l })
//        println()
    }
    println(newGardenPlots)
    println(newGardenPlots.size)
}


class Day21() {
//    val grid = readGridArray(AocUtil.load("day21.test.input"))
    val grid = readGridArray(AocUtil.load("day21.input"))

    private fun readGridArray(input: String): List<List<Tile>> {
        var lines = input.lines()
        var tiles: MutableList<MutableList<Tile>> = mutableListOf()
        lines.mapIndexed { index, l ->
            var line: MutableList<Tile> = mutableListOf()
            line += getTilesFromLine(index, l)
            tiles += line
        }
        return tiles
    }

    private fun getTilesFromLine(index: Int, line: String): List<Tile> {
        val matches = Regex("([#-|./\\\\])").findAll(line)
        return matches.asStream().map { match ->
            Tile(
                match.value, Point(index + 1, match.range.first + 1)
            )
        }.toList()
    }
}
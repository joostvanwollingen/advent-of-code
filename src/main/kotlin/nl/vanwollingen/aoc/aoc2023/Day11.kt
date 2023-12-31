package nl.vanwollingen.aoc.aoc2023

import nl.vanwollingen.aoc.util.grid.Point
import nl.vanwollingen.aoc.util.PuzzleInputUtil
import kotlin.streams.asStream

fun main() {
    val input = PuzzleInputUtil.load("2023/day11.input").lines()
    Day11().solvePart1(input, 2)
    Day11().solvePart1(input, 1000000)
}

class Day11 {

    fun solvePart1(input: List<String>, multiplier: Int) {
        val grid = readGrid(input).groupBy { Day10.PointKey(it.l.y, it.l.x) }.mapValues { it.value.first() }.assignGalaxyIds()

        val galaxies = grid.filter { it.value.s == "#" }

        val minY = grid.values.distinctBy { it.l.y }.map { it.l.y }.min()
        val maxY = grid.values.distinctBy { it.l.y }.map { it.l.y }.max()
        val minX = grid.values.distinctBy { it.l.x }.map { it.l.x }.min()
        val maxX = grid.values.distinctBy { it.l.x }.map { it.l.x }.max()

        val rowsWithGalaxies = grid.filter { it.value.s == "#" }.values.distinctBy { it.l.y }.map { it.l.y }
        val columnsWithGalaxies = grid.filter { it.value.s == "#" }.values.distinctBy { it.l.x }.map { it.l.x }

        val spaceRows = (minY..maxY) - rowsWithGalaxies.toSet()
        val spaceColumns = (minX..maxX) - columnsWithGalaxies.toSet()

        var result: Long = 0
        val glxy = galaxies.values.toList()
        for (i in 0..galaxies.size - 2) {
            for (j in i + 1 until galaxies.size) {
                result += glxy[i].l.distanceTo(glxy[j].l, spaceRows, spaceColumns, multiplier)
            }
        }
        println(result)
    }

    private fun Point.distanceTo(end: Point, spaceRows: List<Int>, spaceColumns: List<Int>, multiplier: Int = 2): Long {
        val distance: Long = Math.abs(this.x - end.x).toLong() + Math.abs(this.y - end.y).toLong()

        val minY = Math.min(this.y, end.y)
        val minX = Math.min(this.x, end.x)
        val maxY = Math.max(this.y, end.y)
        val maxX = Math.max(this.x, end.x)

        val expandedSpace = ((minY..maxY).intersect(spaceRows).size + (minX..maxX).intersect(spaceColumns).size) * (multiplier - 1)

        return distance + expandedSpace
    }

    private fun Map<Day10.PointKey, Space>.assignGalaxyIds(): Map<Day10.PointKey, Space> {
        var count = 1
        this.forEach {
            if (it.value.s == "#") {
                it.value.id = count++
            }
        }
        return this
    }

    private fun readGrid(lines: List<String>): List<Space> {
        val spaces: MutableList<Space> = mutableListOf()
        lines.mapIndexed { index, line ->
            spaces += getSpaceFromLine(index, line, "#")
        }
        return spaces
    }

    private fun Map<Day10.PointKey, Space>.print() {
        val groupedByLine = this.values.groupBy { it.l.y }
        groupedByLine.forEach { line ->
            val sortedLine = line.value.sortedBy { it.l.x }
            print("${line.key.toString().padStart(3, '0')}: ")
            for (space in sortedLine) {
                print(space.toColorString())
            }
            println()
        }
    }

    private data class Space(val s: String, val l: Point) {
        var id: Int = 0
        fun toColorString(): String = when (s) {
            "#" -> "\u001b[43m" + this.s + "\u001b[0m"
            else -> s
        }
    }

    private fun getSpaceFromLine(index: Int, line: String, pattern: String): List<Space> {
        val matches = Regex("([#.])").findAll(line)
        return matches.asStream().map { match ->
            Space(match.value, Point(index + 1, match.range.first + 1))
        }.toList()
    }
}
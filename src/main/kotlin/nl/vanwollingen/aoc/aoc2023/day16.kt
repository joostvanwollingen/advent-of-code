package nl.vanwollingen.aoc.aoc2023

import nl.vanwollingen.aoc.grid.Point
import nl.vanwollingen.aoc.util.AocUtil
import kotlin.streams.asStream
import kotlin.streams.toList

fun main() {
    val input = AocUtil.load("day16.test.input")
    val solution = AocUtil.load("day16.test.solution.input")

    val grid = readGridArray(input.lines())
    val solutionGrid = readGrid(solution.lines())
    val maxY = grid.map { it.maxBy { n -> n.l.y } }.maxBy { it.l.y }.l.y
    val minY = grid.map { it.minBy { n -> n.l.y } }.minBy { it.l.y }.l.y
    val maxX = grid.map { it.maxBy { n -> n.l.x } }.maxBy { it.l.x }.l.x
    val minX = grid.map { it.minBy { n -> n.l.x } }.minBy { it.l.x }.l.x

    var beams: MutableList<Beam> = mutableListOf(Beam(Point(1, 0), DeltaPoint(y = 0, x = 1)))

    var myBeam = Beam(Point(1, 0), DeltaPoint(y = 0, x = 1))
    while (myBeam.canMove(minX, maxX, minY, maxY)) {
        myBeam.move(grid, minX, maxX, minY, maxY)
        grid.flatten().print(myBeam.visited)
    }

    myBeam.spawnedBeams


    var newBeams: MutableList<Beam> = mutableListOf()
    while (beams.count { it.canMove(minX, maxX, minY, maxY) } > 0) {
        newBeams.clear()
        for (beam in beams.filter { it.canMove(minX, maxX, minY, maxY) }) {
            println("Beam at: ${beam.l} moving to ${beam.d}) ${beam.canMove(minX, maxX, minY, maxY)}")
            newBeams += beam.move(grid, minX, maxX, minY, maxY)
            grid.flatten().print(beam.visited)
        }
        beams.clear()
        beams += newBeams
    }

    val visited = beams.flatMap { b -> b.visited }
    grid.flatten().print(visited)

}

private fun handleBeam(beam: Beam, grid: List<List<Tile>>): List<Beam> {
    return emptyList()
}

operator fun Point.plus(deltaPoint: DeltaPoint): Point {
    return Point(this.y + deltaPoint.y, this.x + deltaPoint.x)
}

private data class Beam(var l: Point, var d: DeltaPoint, var visited: MutableList<Point> = mutableListOf()) {
    var spawnedBeams: MutableList<Beam> = mutableListOf()
    fun move(grid: List<List<Tile>>, minX: Int, maxX: Int, minY: Int, maxY: Int): MutableList<Beam> {
        if (canMove(minX, maxX, minY, maxY)) {
            val newTile = grid[(l + d).y - 1][(l + d).x - 1]
            this.l = newTile.l
            this.visited += this.l

            if (newTile.s == ".") {
            }

            if (newTile.s == "|" && d.x == 0) {
            } // hit | from below or top
            if (newTile.s == "|" && d.x != 0) { // hit | from left or right
                d = DeltaPoint(-1, 0)
                spawnedBeams += Beam(this.l, DeltaPoint(1, 0))
            }

            if (newTile.s == "-" && d.y == 0) {
            } // hit - from left or right
            if (newTile.s == "-" && d.y != 0) {  // hit from below or top
                d = DeltaPoint(0, -1)
                spawnedBeams += Beam(this.l, DeltaPoint(0, 1))
            }
            if (newTile.s == "\\") {
                when {
                    d.x == 1 -> d = DeltaPoint(1, 0) // -->\ goes down
                    d.x == -1 -> d = DeltaPoint(-1, 0) // \<-- goes up
                    d.y == -1 -> d = DeltaPoint(0, -1) // hit from bottom, goes left
                    d.y == 1 -> d = DeltaPoint(0, -1) // hit from top, goes right
                }
            }
            if (newTile.s == "/") {
                when {
                    d.x == 1 -> d = DeltaPoint(-1, 0) // -->/ goes up
                    d.x == -1 -> d = DeltaPoint(1, 0) // \<-- goes down
                    d.y == -1 -> d = DeltaPoint(0, 1) // hit from bottom, goes right
                    d.y == 1 -> d = DeltaPoint(0, -1) // hit from top, goes left
                }
            }
        }
        return mutableListOf(this)
    }

    fun canMove(minX: Int, maxX: Int, minY: Int, maxY: Int): Boolean {
        val n = l + d
        return n.y >= minY && n.y <= maxY && n.x >= minX && n.x <= maxX
    }

}

data class DeltaPoint(val y: Int = 0, val x: Int = 0)

private fun readGrid(lines: List<String>): List<Tile> {
    val tiles: MutableList<Tile> = mutableListOf()
    lines.mapIndexed { index, line ->
        tiles += getTilesFromLine(index, line)
    }
    return tiles
}

private fun readGridArray(lines: List<String>): List<List<Tile>> {
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

fun List<Tile>.print(visited: List<Point> = listOf()) {
    val groupedByLine = this.groupBy { it.l.y }
    groupedByLine.forEach { line ->
        val sortedLine = line.value.sortedBy { it.l.y }
        print("${line.key.toString().padStart(3, '0')}: ")
        for (tile in sortedLine) {
            print(tile.toColorString(visited.any { it == tile.l }))
        }
        println()
    }
}

data class Tile(val s: String, val l: Point) {
    var id: Int = 0
    fun toColorString(visited: Boolean): String = when {
        visited -> "\u001b[44m" + this.s + "\u001b[0m"
        s in "/|-\\" -> "\u001b[32m" + this.s + "\u001b[0m"
        s == "#" -> "\u001b[43m" + this.s + "\u001b[0m"
        else -> s
    }
}
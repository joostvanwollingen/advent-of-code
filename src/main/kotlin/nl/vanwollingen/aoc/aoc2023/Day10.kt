package nl.vanwollingen.aoc.aoc2023

import nl.vanwollingen.aoc.util.PuzzleInputUtil
import nl.vanwollingen.aoc.util.grid.Point
import nl.vanwollingen.aoc.util.grid.getSurroundingPoints
import kotlin.streams.asStream
import kotlin.system.exitProcess

fun main() {
    Day10().solvePart1()
}

class Day10 {
    fun solvePart1() {
//    val lines = PuzzleInputUtil.load("2023/day10.test.input").lines()
        val lines = PuzzleInputUtil.load("2023/day10.input").lines()
        val grid = readGrid(lines)
        val start = grid.first { it.s == "S" }
        val possibleStartConnections = start.getConnections(grid)
        //grid.print(start)
//    println(possibleStartConnections)

        var possibleNextConnection: Pipe? = possibleStartConnections.first()
        val visited = mutableListOf(start, possibleNextConnection)
        var steps = 0L
        while (possibleNextConnection != null) {
            val notVisitedConnections = possibleNextConnection.getConnections(grid).filterNot { visited.contains(it) }

            if (notVisitedConnections.size > 1) {
//            println("more than 1 connection found for $possibleNextConnection : $notVisitedConnections at step $steps")
                exitProcess(1)
            }
            if (notVisitedConnections.isEmpty()) {
//            println("no more connections founds for $possibleNextConnection at step $steps")
                steps++
                break
            }

            possibleNextConnection = notVisitedConnections.first()
            visited += possibleNextConnection
            steps++
        }
        println(visited.size)
    }

    private fun Point.outside(minX: Int, maxX: Int, minY: Int, maxY: Int): Boolean {
        return this.y < minY || this.y > maxY || this.x < minX || this.x > maxX
    }

    data class PointKey(val y: Int, val x: Int)

    data class Pipe(val s: String, val l: Point)

    private fun Pipe.getConnections(grid: List<Pipe>): List<Pipe> {
        val surroundingPoints = l.getSurroundingPoints()
        val surroundingPipes = grid.filter { surroundingPoints.contains(it.l) }
        return surroundingPipes.filter { this.canConnect(it) && it.canConnect(this) }
    }

    private fun Pipe.getFillableNeighbours(grid: Map<PointKey, Pipe>, currentLine: Int? = null): List<Pipe> {
        var surroundingPipes = this.getSurroundingPipes(grid, l.getSurroundingPoints())
        if (currentLine != null) surroundingPipes = surroundingPipes.filter { it.l.y == currentLine }
        return surroundingPipes.filter { it.s == "." }
    }

    private fun Pipe.getSurroundingPipes(grid: Map<PointKey, Pipe>, points: List<Point>): List<Pipe> =
            points.mapNotNull { grid[PointKey(it.y, it.x)] }


    private fun readGrid(lines: List<String>): List<Pipe> {
        val pipes: MutableList<Pipe> = mutableListOf()
        lines.mapIndexed { index, line ->
            pipes += getPipesFromLine(index, line)
        }
        return pipes
    }

    private fun getPipesFromLine(index: Int, line: String): List<Pipe> {
        val matches = Regex("([|\\-LJ7F.S])").findAll(line)
        return matches.asStream().map { match ->
            Pipe(
                    match.value, Point(index + 1, match.range.first + 1)
            )
        }.toList()
    }

    private fun List<Pipe>.print(
            focus: Pipe? = null, visited: List<Pipe> = emptyList()
    ) {
        val groupedByLine = this.groupBy { it.l.y }
        groupedByLine.forEach { line ->
            val sortedLine = line.value.sortedBy { it.l.x }
            print("${line.key.toString().padStart(3, '0')}: ")
            for (pipe in sortedLine) {
                print(pipe.toColorString(focus?.l == pipe.l, focus?.canConnect(pipe), visited.contains(pipe)))
            }
            println()
        }
    }

    private fun Pipe.canConnect(other: Pipe): Boolean = when (this.s) {
        "." -> false
        "|" -> x(other.l.x) && (this.l.y - 1 == other.l.y || this.l.y + 1 == other.l.y)
        "-" -> y(other.l.y) && (this.l.x + 1 == other.l.x || this.l.x - 1 == other.l.x)
        "L" -> y(other.l.y + 1) && x(other.l.x) || this.l.x + 1 == other.l.x && y(other.l.y)
        "J" -> x(other.l.x) && this.l.y == other.l.y + 1 || y(other.l.y) && x(other.l.x + 1)
        "7" -> x(other.l.x) && this.l.y == other.l.y - 1 || y(other.l.y) && x(other.l.x + 1)
        "F" -> x(other.l.x) && this.l.y == other.l.y - 1 || y(other.l.y) && x(other.l.x - 1)
        "S" -> this.l.getSurroundingPoints().contains(other.l)
        else -> throw Exception("Unknown pipe symbol ${this.s}")
    }

    private fun Pipe.x(other: Int) = this.l.x == other
    private fun Pipe.y(other: Int) = this.l.y == other

    fun Pipe.toColorString(isFocus: Boolean? = false, canConnect: Boolean? = false, isVisited: Boolean? = false): String {
        if (isFocus == true) return "\u001b[43m" + this.s + "\u001b[0m"
        if (this.s == "S") return "\u001b[32m" + this.s + "\u001b[0m"
        if (isVisited == true) return "\u001b[23m" + this.s + "\u001b[0m"
        if (canConnect == true) return "\u001b[44m" + this.s + "\u001b[0m"
        return "\u001b[33m" + this.s + "\u001b[0m"
    }
}
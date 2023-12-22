package nl.vanwollingen.aoc.aoc2023

import nl.vanwollingen.aoc.grid.Point
import nl.vanwollingen.aoc.grid.getSurroundingPoints
import nl.vanwollingen.aoc.util.AocUtil
import java.util.LinkedList
import java.util.Queue
import kotlin.streams.asStream
import kotlin.streams.toList
import kotlin.system.exitProcess

fun main() {
//    val lines = nl.vanwollingen.aoc.util.AocUtil.load("day10.test.input").lines()
    val lines = AocUtil.load("day10.test3.input").lines() //4
//    val lines = nl.vanwollingen.aoc.util.AocUtil.load("day10.test4.input").lines() //4
//    val lines = nl.vanwollingen.aoc.util.AocUtil.load("day10.test5.input").lines() //8
//    val lines = nl.vanwollingen.aoc.util.AocUtil.load("day10.test6.input").lines() //10
//    val lines = nl.vanwollingen.aoc.util.AocUtil.load("day10.input").lines()
    val grid = readGrid(lines)
    val start = grid.first { it.s == "S" }
    val possibleStartConnections = start.getConnections(grid)
    //grid.print(start)
//    println(possibleStartConnections)
//
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
//    grid.print(visited = visited.filterNotNull().toList())
//    val answer = ((visited.size - 1) / 2) + 1
//    println("Answer = $answer from ${visited[answer]}")

    //Part 2
    var maxX = visited.maxBy { it!!.l.x }!!.l.x
    var minX = visited.minBy { it!!.l.x }!!.l.x
    var maxY = visited.maxBy { it!!.l.y }!!.l.y
    var minY = visited.minBy { it!!.l.y }!!.l.y

    val noVisited = grid - visited.map { it!! }.toSet()
    val mightBeFillable = noVisited.filter {
        it.s == "." && it.l.x <= maxX && it.l.x >= minX && it.l.y >= minY && it.l.y <= maxY
    }
//    grid.print()

    val gridK = grid.groupBy { PointKey(it.l.y, it.l.x) }.mapValues { it.value.first() }
    var filled: MutableList<Pipe> = mutableListOf()

    mightBeFillable.forEach {
        var searched: MutableList<Pipe> = mutableListOf()
        var q: Queue<Pipe> = LinkedList(listOf(it))
        var focus: Pipe? = q.peek()
        var currentLine = minY

        println("Starting search from ${focus!!.l}")
        while (focus != null) {
            grid.print(focus, searched)

            val notFilledNeighbours =
                focus.getFillableNeighbours(gridK, currentLine).filterNot { searched.contains(it) }

            q.addAll(notFilledNeighbours)
            q = LinkedList(q.distinct())
            searched += focus!!

            if (q.peek() != null) focus = q.remove() else {
                currentLine++
                if (currentLine > maxY) {
//                    grid.print(visited = searched, focus = focus)
//                    println("Filled")
//                    grid.print(visited = filled, focus = focus)

                    break
                }
            }
        }
//        println("final")
//        println("filled: $filled")
//        grid.print(visited = filled.map { it!! })
        if (!searched.flatMap { s -> s.getFillableNeighbours(gridK) }
                .any { s -> s.l.outside(minX, maxX, minY, maxY) }) filled += searched
        filled = filled.distinct() as MutableList<Pipe>
    }
    grid.print(visited = filled.map { it!! })
    println(filled.size)
}

private fun Point.outside(minX: Int, maxX: Int, minY: Int, maxY: Int): Boolean {
    return this.y < minY || this.y > maxY || this.x < minX || this.x > maxX
}

data class PointKey(val y: Int, val x: Int)
typealias p = PointKey

data class Pipe(val s: String, val l: Point)

private fun Pipe.getConnections(grid: List<Pipe>): List<Pipe> {
    val surroundingPoints = l.getSurroundingPoints()
    val surroundingPipes = grid.filter { surroundingPoints.contains(it.l) }
    return surroundingPipes.filter { this.canConnect(it) && it.canConnect(this) }
}

private fun Pipe.getFillableNeighbours(grid: Map<PointKey, Pipe>, currentLine: Int? = null): List<Pipe> {
    var surroundingPipes = this.getSurroundingPipes(grid, l.getSurroundingPoints())
    if(currentLine!=null) surroundingPipes = surroundingPipes.filter { it.l.y == currentLine }
    return surroundingPipes.filter { it.s == "." }
}

private fun Pipe.getSurroundingPipes(grid: Map<PointKey, Pipe>, points: List<Point>): List<Pipe> =
    points.mapNotNull { grid[p(it.y, it.x)] }


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
//    if (isDiscoveredSymbol) return "\u001b[32m" + this.value + "\u001b[0m"
//    if (isSearched) return "\u001b[43m" + this.value + "\u001b[0m"
//    if (this.isNumber) return "\u001b[44m" + this.value + "\u001b[0m"
//    if (this.isSymbol) return "\u001b[31m" + this.value + "\u001b[0m"

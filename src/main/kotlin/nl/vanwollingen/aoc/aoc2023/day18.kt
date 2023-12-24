package nl.vanwollingen.aoc.aoc2023

import nl.vanwollingen.aoc.grid.Point
import nl.vanwollingen.aoc.util.PuzzleInputUtil
import java.util.*

class Day18() {
    val input = PuzzleInputUtil.load("2023/day18.test.input")

    //        val input = nl.vanwollingen.aoc.util.AocUtil.load("day18.input")
    fun solve1() {
        var currentPoint = Point(0, 0)
        val grid: MutableMap<Point, String> = mutableMapOf(currentPoint to "")
        input.lines().forEach { instruction ->
            val direction = instruction.split(" ")[0]
            val deltaPoint = getDeltaPoint(direction)
            val steps = instruction.split(" ")[1].toInt()
            val color = instruction.split(" ")[2].drop(1).dropLast(1)
            for (i in 1..steps) {
                currentPoint += deltaPoint
                grid[currentPoint] = color
            }
        }
        grid.print()
        println(grid.shoeLace())
        val points = grid.spanFill(grid.keys.first())
        println(points)
    }

    private fun getDeltaPoint(direction: String): nl.vanwollingen.aoc.aoc2023.DeltaPoint = when (direction) {
        "R" -> nl.vanwollingen.aoc.aoc2023.DeltaPoint(0, 1)
        "L" -> nl.vanwollingen.aoc.aoc2023.DeltaPoint(0, -1)
        "U" -> nl.vanwollingen.aoc.aoc2023.DeltaPoint(-1, 0)
        "D" -> nl.vanwollingen.aoc.aoc2023.DeltaPoint(1, 0)
        else -> throw Exception("Unknown direction $direction")
    }
}

fun main() {
    nl.vanwollingen.aoc.aoc2023.Day18().solve1()
}

private fun Map<Point, String>.print() {
    val minY = this.keys.minBy { it.y }.y
    val maxY = this.keys.maxBy { it.y }.y
    val minX = this.keys.minBy { it.x }.x
    val maxX = this.keys.maxBy { it.x }.x
    for (y in minY..maxY) {
        print("${y.toString().padStart(3, '0')} ")
        for (x in minX..maxX) {
            val p = if (this[Point(y, x)] != null) "#" else "."
            print(p)
        }
        println()
    }
}

private fun Map<Point, String>.shoeLace(): Long {

    var sum = 0L
    this.keys.windowed(2, 1) {
        val left = it.first()
        val right = it.last()
        val xY = left.x * right.y
        val yX = left.y * right.x
        sum += (xY - yX)
    }
    return sum / 2
}

private fun Map<Point, String>.spanFill(startingPoint: Point): List<Point> {
    val minY = this.keys.minBy { it.y }.y
    val maxY = this.keys.maxBy { it.y }.y
    val minX = this.keys.minBy { it.x }.x
    val maxX = this.keys.maxBy { it.x }.x

    if (startingPoint.outside(minX, maxX, minY, maxY)) return emptyList()

    var q: Queue<Point> = LinkedList()
    var filled: MutableList<Point> = mutableListOf()
    q.add(startingPoint)

    var current: Point?
    while (!q.isEmpty()) {
        current = q.remove()
        var lx = current.x
        var rx = current.x
        while (!Point(current.y, lx - 1).outside(minX, maxX, minY, maxY)) {
            filled.add(Point(current.y, lx - 1))
            lx -= 1
        }
        while (!Point(current.y, rx).outside(minX, maxX, minY, maxY)) {
            filled.add(Point(current.y, rx))
            rx += 1
        }
        nl.vanwollingen.aoc.aoc2023.scan(
            lx, current.x - 1, current.y + 1, q, minY, maxY, minX, maxX
        )
        nl.vanwollingen.aoc.aoc2023.scan(lx, current.x - 1, current.y - 1, q, minY, maxY, minX, maxX)
    }

    return filled
}

fun scan(lx: Int, rx: Int, y: Int, q: Queue<Point>, minY: Int, maxY: Int, minX: Int, maxX: Int) {
    var spanAdded = false
    for (x in lx..rx) {
        if (Point(y, x).outside(minX, maxX, minY, maxY)) {
            spanAdded = false
        } else if (!spanAdded) {
            q.add(Point(y, x))
            spanAdded = true
        }
    }
}

private fun Point.outside(minX: Int, maxX: Int, minY: Int, maxY: Int): Boolean {
    return this.y < minY || this.y > maxY || this.x < minX || this.x > maxX
}
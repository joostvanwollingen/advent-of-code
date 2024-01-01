package nl.vanwollingen.aoc.util.grid

import kotlin.math.abs

data class Point(var y: Int, var x: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Point

        if (y != other.y) return false
        if (x != other.x) return false

        return true
    }

    override fun hashCode(): Int {
        var result = y
        result = 31 * result + x
        return result
    }
}

fun Point.getSurroundingPoints(): List<Point> {
    val points = mutableListOf<Point>()
    for (i in this.x - 1..this.x + 1) {
        for (o in this.y - 1..this.y + 1) {
            points.add(Point(o, i))
        }
    }
    return points.minus(this)
}

fun List<Point>.getSurroundingPoints(): List<Point> {
    return this.asSequence().map { point -> point.getSurroundingPoints() }.flatten().minus(this).toSet()
            .toList()
}

fun Point.getManhattanNeighbours(): List<Point> = listOf(
        Point(this.y + 1, x),
        Point(this.y - 1, x),
        Point(this.y, x - 1),
        Point(this.y, x + 1),
)

fun Point.getManhattanDistance(other: Point): Int {
    return abs(this.x - other.x) + abs(this.y - other.y)
}

private fun Point.distanceTo(end: Point): Long = abs(this.x - end.x).toLong() + abs(this.y - end.y).toLong()

fun String.columns(): List<String> {
    val ret = mutableMapOf<Int, String>()
    this.lines().mapIndexed { index, line ->
        for (s in line.indices) {
            ret[s] = (ret[s] ?: "") + line[s].toString()
        }
    }
    return ret.values.toList()
}

fun transposeToRows(sorted: List<String>): MutableList<String> {
    val lines = mutableListOf<String>()
    var line = ""
    for (char in 0 until sorted.maxOf { it.length }) {
        for (i in sorted.indices) {
            line += sorted[i][char]
            if (i == sorted.size - 1) {
                lines += line
                line = ""
            }
        }
    }
    return lines
}

fun Point.outside(minX: Int, maxX: Int, minY: Int, maxY: Int): Boolean {
    return this.y < minY || this.y > maxY || this.x < minX || this.x > maxX
}
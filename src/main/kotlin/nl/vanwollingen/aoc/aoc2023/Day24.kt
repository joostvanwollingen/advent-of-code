package nl.vanwollingen.aoc.aoc2023

import nl.vanwollingen.aoc.util.PuzzleInputUtil
import nl.vanwollingen.aoc.util.quickmaths.mansnothot.bigshaq.Line
import nl.vanwollingen.aoc.util.quickmaths.mansnothot.bigshaq.Point
import nl.vanwollingen.aoc.util.quickmaths.mansnothot.bigshaq.findIntersection
import nl.vanwollingen.aoc.util.quickmaths.mansnothot.bigshaq.intercept


fun main() {
    val d24 = Day24()
    val allHail = PuzzleInputUtil.parse("2023/day24.input") { input ->
        Day24.Hail.fromString(input)
    }.toList()

    val testArea = Day24.TestArea(200000000000000, 400000000000000, 200000000000000, 400000000000000)
    d24.solvePart1(allHail, testArea)
    d24.solvePart2(allHail)
}

class Day24 {

    fun solvePart1(allHail: List<Hail>, testArea: TestArea) {
        var count = 0
        for (i in 0..allHail.size) {
            for (j in i + 1..<allHail.size) {
                val a = allHail[i]
                val b = allHail[j]

                val aIntercept = intercept(a.x, a.deltaX, a.y, a.deltaY)
                val bIntercept = intercept(b.x, b.deltaX, b.y, b.deltaY)

                val aLine = Line(Point(aIntercept.first, 0.0), Point(0.0, aIntercept.second))
                val bLine = Line(Point(bIntercept.first, 0.0), Point(0.0, bIntercept.second))
                val aBIntersection = findIntersection(aLine, bLine)

                if (testArea.isInside(aBIntersection) && isInFuture(aBIntersection, a) && isInFuture(aBIntersection, b)) count++
            }
        }
        println(count)
    }

    fun solvePart2(allHail: List<Hail>) {
        val sortedHail = allHail.sortedBy { it.x }
        for (i in 0..sortedHail.size) {
            for (j in i + 1..<sortedHail.size) {
                val a = sortedHail[i]
                val b = sortedHail[j]

                val aIntercept = intercept(a.x, a.deltaX, a.y, a.deltaY)
                val bIntercept = intercept(b.x, b.deltaX, b.y, b.deltaY)

                val aLine = Line(Point(aIntercept.first, 0.0), Point(0.0, aIntercept.second))
                val bLine = Line(Point(bIntercept.first, 0.0), Point(0.0, bIntercept.second))
                val aBIntersection = findIntersection(aLine, bLine)
                println("${a.x} ${b.x} ${b.deltaX}")

            }
        }
    }

    private fun isInFuture(intersectionPoint: Point, hail: Hail): Boolean {
        return isInFuture(hail.deltaX, hail.x, intersectionPoint.x) && isInFuture(hail.deltaY, hail.y, intersectionPoint.y)
    }

    private fun isInFuture(delta: Double, l: Double, intersect: Double): Boolean {
        var inFuture = false
        if (delta > 0) {
            if (l < intersect) {
                inFuture = true
            }
        } else {
            if (l > intersect) {
                inFuture = true
            }
        }
        return inFuture
    }

    class Hail(val x: Double, val y: Double, val z: Double, val deltaX: Double, val deltaY: Double, val deltaZ: Double) {
        companion object {
            fun fromString(input: String): Hail {
                val (coordinates, deltas) = input.split(Regex(" @\\s+"))
                val (x, y, z) = coordinates.split(", ")
                val (deltaX, deltaY, deltaZ) = deltas.split(Regex(",\\s+"))
                return Hail(x.toDouble(), y.toDouble(), z.toDouble(), deltaX.toDouble(), deltaY.toDouble(), deltaZ.toDouble())
            }
        }

        override fun toString(): String {
            return "Hail(x=$x, y=$y, z=$z, deltaX=$deltaX, deltaY=$deltaY, deltaZ=$deltaZ)"
        }
    }

    data class TestArea(val minX: Long, val maxX: Long, val minY: Long, val maxY: Long) {
        fun isInside(intersectionPoint: Point): Boolean = intersectionPoint.x >= this.minX && intersectionPoint.x <= this.maxX && intersectionPoint.y >= this.minY && intersectionPoint.y <= this.maxY
    }
}
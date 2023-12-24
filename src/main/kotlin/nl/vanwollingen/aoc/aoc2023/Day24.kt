package nl.vanwollingen.aoc.aoc2023

import nl.vanwollingen.aoc.util.PuzzleInputUtil
import nl.vanwollingen.aoc.util.quickmaths.mansnothot.bigshaq.Line
import nl.vanwollingen.aoc.util.quickmaths.mansnothot.bigshaq.Point
import nl.vanwollingen.aoc.util.quickmaths.mansnothot.bigshaq.findIntersection
import nl.vanwollingen.aoc.util.quickmaths.mansnothot.bigshaq.intercept


fun main() {
    val d24 = Day24()
    val allHail = PuzzleInputUtil.parse("2023/day24.test.input") { input ->
        Day24.Hail.fromString(input)
    }.toList()

    val testArea = Day24.TestArea(7F, 27F, 7F, 27F)
    d24.solvePart1(allHail, testArea)
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

                val aLine = Line(Point(aIntercept.first, 0F), Point(0F, aIntercept.second))
                val bLine = Line(Point(bIntercept.first, 0F), Point(0F, bIntercept.second))
                val aBIntersection = findIntersection(aLine, bLine)

                if (testArea.isInside(aBIntersection) && isInFuture(aBIntersection, a) && isInFuture(aBIntersection, b)) count++
            }
        }
        println(count)
    }

    fun isInFuture(intersectionPoint: Point, hail: Hail): Boolean {
        return inFuture(hail.deltaX, hail.x, intersectionPoint.x) &&
                inFuture(hail.deltaY, hail.y, intersectionPoint.y)
    }

    private fun inFuture(delta: Float, l: Float, intersect: Float): Boolean {
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


    fun solvePart2() {

    }

    class Hail(val x: Float, val y: Float, val z: Float, val deltaX: Float, val deltaY: Float, val deltaZ: Float) {
        companion object {
            fun fromString(input: String): Hail {
                val (coordinates, deltas) = input.split(Regex(" @\\s+"))
                val (x, y, z) = coordinates.split(", ")
                val (deltaX, deltaY, deltaZ) = deltas.split(Regex(",\\s+"))
                return Hail(x.toFloat(), y.toFloat(), z.toFloat(), deltaX.toFloat(), deltaY.toFloat(), deltaZ.toFloat())
            }
        }

        override fun toString(): String {
            return "Hail(x=$x, y=$y, z=$z, deltaX=$deltaX, deltaY=$deltaY, deltaZ=$deltaZ)"
        }
    }

    data class TestArea(val minX: Float, val maxX: Float, val minY: Float, val maxY: Float) {
        fun isInside(intersectionPoint: Point): Boolean =
                intersectionPoint.x >= this.minX && intersectionPoint.x <= this.maxX && intersectionPoint.y >= this.minY && intersectionPoint.y <= this.maxY
    }
}
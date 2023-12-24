package nl.vanwollingen.aoc.aoc2023

import nl.vanwollingen.aoc.util.PuzzleInputUtil


fun main() {
    val d24 = Day24()
    val allHail = PuzzleInputUtil.parse("2023/day24.test.input") { input ->
        Day24.Hail.fromString(input)
    }.toList()

    val testArea = Day24.TestArea(7.toDouble(), 27.toDouble(), 7.toDouble(), 27.toDouble())

    d24.solvePart1(allHail, testArea)
}

class Day24 {

    fun zero(main: Double, delta: Int) = main / delta
    fun intercept(x: Double, dx: Int, y: Double, dy: Int): Pair<Double, Double> {
        val yzero = zero(x, dx)

        val xzero = zero(y, dy)

        return x - (xzero * dx) to y - (yzero * dy)
    }


    fun solvePart1(allHail: List<Hail>, testArea: TestArea) {
        val hailA = intercept(19.toDouble(), -2, 13.toDouble(), 1)
        val hailB = intercept(18.toDouble(), -1, 19.toDouble(), -1)

//        println(hailA)
//        println(hailB)

        val first = LineF(PointF(45F, 0F), PointF(0F, 22.5F))
        val second = LineF(PointF(-1.0F, 0F), PointF(0F, 1.0F))
        println(findIntersection(first, second))

//        println(intersectionOfTwoLines(45.0, 22.5, -1.0, 1.0))


    }


    fun solvePart2() {

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

    data class TestArea(val minX: Double, val maxX: Double, val minY: Double, val maxY: Double)

    fun findIntersection(l1: LineF, l2: LineF): PointF {
        val a1 = l1.e.y - l1.s.y
        val b1 = l1.s.x - l1.e.x
        val c1 = a1 * l1.s.x + b1 * l1.s.y

        val a2 = l2.e.y - l2.s.y
        val b2 = l2.s.x - l2.e.x
        val c2 = a2 * l2.s.x + b2 * l2.s.y

        val delta = a1 * b2 - a2 * b1
        // If lines are parallel, intersection point will contain infinite values
        return PointF((b2 * c1 - b1 * c2) / delta, (a1 * c2 - a2 * c1) / delta)
    }

    class PointF(val x: Float, val y: Float) {
        override fun toString() = "{$x, $y}"
    }

    class LineF(val s: PointF, val e: PointF)
}
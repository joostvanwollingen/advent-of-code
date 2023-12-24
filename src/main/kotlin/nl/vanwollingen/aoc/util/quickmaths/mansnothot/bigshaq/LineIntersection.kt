package nl.vanwollingen.aoc.util.quickmaths.mansnothot.bigshaq

//https://rosettacode.org/wiki/Find_the_intersection_of_two_lines#Kotlin
fun findIntersection(l1: Line, l2: Line): Point {
    val a1 = l1.e.y - l1.s.y
    val b1 = l1.s.x - l1.e.x
    val c1 = a1 * l1.s.x + b1 * l1.s.y

    val a2 = l2.e.y - l2.s.y
    val b2 = l2.s.x - l2.e.x
    val c2 = a2 * l2.s.x + b2 * l2.s.y

    val delta = a1 * b2 - a2 * b1
    // If lines are parallel, intersection point will contain infinite values
    return Point((b2 * c1 - b1 * c2) / delta, (a1 * c2 - a2 * c1) / delta)
}

class Point(val x: Float, val y: Float) {
    override fun toString() = "{$x, $y}"
}

class Line(val s: Point, val e: Point) {
    override fun toString(): String {
        return "Line(s=$s, e=$e)"
    }
}

fun zero(main: Float, delta: Float) = main / delta
fun intercept(x: Float, dx: Float, y: Float, dy: Float): Pair<Float, Float> {
    val yzero = zero(x, dx)

    val xzero = zero(y, dy)

    return x - (xzero * dx) to y - (yzero * dy)
}
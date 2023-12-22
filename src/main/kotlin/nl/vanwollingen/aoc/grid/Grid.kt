package nl.vanwollingen.aoc.grid

data class Point(val y: Int, val x: Int) {
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
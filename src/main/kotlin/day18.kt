class Day18() {
    val input = AocUtil.load("day18.test.input")

    //    val input = AocUtil.load("day18.input")
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
        println(grid.determineArea())
    }

    private fun getDeltaPoint(direction: String): DeltaPoint = when (direction) {
        "R" -> DeltaPoint(0, 1)
        "L" -> DeltaPoint(0, -1)
        "U" -> DeltaPoint(-1, 0)
        "D" -> DeltaPoint(1, 0)
        else -> throw Exception("Unknown direction $direction")
    }
}

fun main() {
    Day18().solve1()
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

private fun Map<Point, String>.determineArea(): Long {
    val minY = this.keys.minBy { it.y }.y
    val maxY = this.keys.maxBy { it.y }.y
    val minX = this.keys.minBy { it.x }.x
    val maxX = this.keys.maxBy { it.x }.x
    var area = 0L
    for (y in minY..maxY) {
        var openRange = false
        var countedInRange = 0L
        for (x in minX..maxX) {
            if (this[Point(y, x)] != null && openRange) {
                area += countedInRange
                countedInRange = 0L
            }

            if (this[Point(y, x)] != null) {
                openRange = true
            }

            if (openRange) {
                countedInRange++
            }

            if (x == maxX && openRange && this[Point(y, x)] != null) {
                area += countedInRange
            }


        }
    }
    return area
}

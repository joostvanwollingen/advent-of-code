import java.util.SortedMap
import kotlin.streams.asStream
import kotlin.streams.toList

fun main() {
//    val input = AocUtil.load("day14.test.input").lines()
    val input = AocUtil.load("day14.input").lines()
    var grid = readGrid(input).groupBy { PointKey(it.l.y, it.l.x) }.mapValues { it.value.first() }.toMutableMap()
//    grid.values.toList().print()
//    println("")

    var cycle = 0
    var oldGrid = grid

    val northSort = grid.keys.groupBy { it.y }.toSortedMap()
    val westSort = grid.keys.groupBy { it.x }.toSortedMap()
    val southSort = grid.keys.sortedByDescending { it.y }.groupBy { it.y }.toMap()
    val eastSort = grid.keys.sortedByDescending { it.x }.groupBy { it.x }.toMap()

    val gridHash = grid.hashCode()

    while (cycle < 1) {
        grid = moveAll(grid, northSort, "up")
//        grid = moveAll(grid, westSort, "left")
//        grid = moveAll(grid, southSort, "down")
//        grid = moveAll(grid, eastSort, "right")
//        grid.calcWeights()
//        println(grid.hashCode())
        cycle++
    }
    grid.calcWeights()
    grid.values.toList().print()
}

private fun moveAll(
    ogGrid: MutableMap<PointKey, Rock>, sortedMap: Map<Int, List<PointKey>>, direction: String
): MutableMap<PointKey, Rock> {
    var grid = ogGrid

    sortedMap.forEach { row ->
        for (pk in row.value) {
            var currentPk = pk
//            println("pk: $currentPk canMove: ${grid.canMove(currentPk, "up")}")
            while (grid.canMove(currentPk, direction)) {
                grid = grid.move(currentPk, direction)
                currentPk = getPointFromDirection(currentPk, direction)
//                grid.values.toList().print()
//                println("")
            }
        }
    }
    return grid
}

private fun MutableMap<PointKey, Rock>.calcWeights(): Long {
    val minY = this.values.minBy { it.l.y }.l.y
    val maxY = this.values.maxBy { it.l.y }.l.y
    var score = 0L
    val groupedByLine = this.values.groupBy { it.l.y }
    groupedByLine.forEach { line ->
//        println("Line ${line.key}")
        val stones = line.value.count { it.s == "O" }
        val linePoints = maxY - (line.key - 1)
        val lineScore = stones * linePoints
//        println("Stones $stones")
//        println("Line score = $lineScore ($linePoints)")
        if (stones > 0) {
            score += lineScore
        }
//        println("Score: $score")
    }
    println(score)
    return score
}

private fun readGrid(lines: List<String>): List<Rock> {
    val rocks: MutableList<Rock> = mutableListOf()
    lines.mapIndexed { index, line ->
        rocks += getRockFromLine(index, line)
    }
    return rocks
}

private fun MutableMap<PointKey, Rock>.moveAll(from: PointKey, direction: String): MutableMap<PointKey, Rock> {
    var current = from
    val start = this[from] ?: throw Exception("start $from not found")

    while (this.canMove(current, direction)) {
        current = getPointFromDirection(current, direction)
    }
    this.putAll(
        mapOf(
            from to Rock(".", start.l),
            current to Rock(start.s, Point(current.y, current.x))
        )
    )
    return this
}


private fun MutableMap<PointKey, Rock>.move(from: PointKey, direction: String): MutableMap<PointKey, Rock> {
    if (this.canMove(from, direction)) {
        val start = this[from] ?: throw Exception("start $from not found")
        val endPoint = getPointFromDirection(from, direction)
        this[from] = Rock(".", start.l)
        this[endPoint] = Rock(start.s, Point(endPoint.y, endPoint.x))
    }
    return this
}


private fun Map<PointKey, Rock>.canMove(from: PointKey, direction: String): Boolean {
    val start = this[from] ?: return false
    if (start.s == "#" || start.s == ".") return false
    val end = getPointFromDirection(from, direction)
    val gridBounds = getGridBoundaries(this.keys)
    if (!end.within(gridBounds)) return false
    if (this[end]?.s != ".") return false
    return true
}

private fun PointKey.within(gridBounds: GridBounds): Boolean =
    this.y >= gridBounds.minY && this.y <= gridBounds.maxY && this.x >= gridBounds.minX && this.x <= gridBounds.maxX


fun getGridBoundaries(grid: Set<PointKey>): GridBounds {
    val minY = grid.minBy { it.y }.y
    val maxY = grid.maxBy { it.y }.y
    val maxX = grid.maxBy { it.x }.x
    val minX = grid.minBy { it.x }.x
    return GridBounds(minY, maxY, minX, maxX)
}

data class GridBounds(val minY: Int, val maxY: Int, val minX: Int, val maxX: Int)


fun getPointFromDirection(from: PointKey, direction: String): PointKey = when (direction) {
    "up" -> p(from.y - 1, from.x)
    "down" -> p(from.y + 1, from.x)
    "left" -> p(from.y, from.x - 1)
    "right" -> p(from.y, from.x + 1)
    else -> throw Exception("Direction $direction not possible")
}

private data class Rock(val s: String, val l: Point) {
    var id: Int = 0
    fun toColorString(): String = when (s) {
        "." -> "\u001b[0m" + this.s + "\u001b[0m"
        "#" -> "\u001b[43m" + this.s + "\u001b[0m"
        "O" -> "\u001b[32m" + this.s + "\u001b[0m"
        else -> s
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Rock

        if (s != other.s) return false
        if (l != other.l) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = s.hashCode()
        result = 31 * result + l.hashCode()
        result = 31 * result + id
        return result
    }

}

private fun getRockFromLine(index: Int, line: String): List<Rock> {
    val matches = Regex("(#|\\.|O)").findAll(line)
    return matches.asStream().map { match ->
        Rock(
            match.value, Point(index + 1, match.range.first + 1)
        )
    }.toList()
}

private fun List<Rock>.print() {
    val groupedByLine = this.groupBy { it.l.y }
    groupedByLine.forEach { line ->
        val sortedLine = line.value.sortedBy { it.l.y }
        print("${line.key.toString().padStart(3, '0')}: ")
        for (rock in sortedLine) {
            print(rock.toColorString())
        }
        println()
    }
}
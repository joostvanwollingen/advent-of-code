import java.io.File

private const val maxWidth = 10

fun main() {
    val lines = File("day3.input").readLines().withIndex()
    var grid: Map<Int, Map<Int, Pixel>> = getGrid(lines)
    val symbols = grid.map { it.key to it.value.filter { pixel -> pixel.value.isSymbol } }
    val searchCoordinates = extractSearchCoordinates(symbols)
    val numberHits = searchCoordinates.filter { coord ->
        grid[coord.first]!![coord.second]!!.isNumber
    }
    var results: List<Int> = mutableListOf()
    var usedCoords: Map<Int, Pair<Int, Pair<Int, Int>>> = mutableMapOf()
    for (line in 1..maxWidth) {
        var lineHits = numberHits.filter { it.first == line }.sortedBy { it.second }
        lineHits.forEach { hit ->
            if (!isUsed(usedCoords, line, hit)) {
                var low = findConnected(grid,line,  hit.first to hit.second - 1, -1, hit.second, usedCoords)
                var high = findConnected(grid,line, hit.first to hit.second + 1, 1, hit.second, usedCoords)
                results += getNumberFromLine(grid!![line]!!, low, high)
                for (i in low..high) {
                    usedCoords += mapOf(line to (line to (line to i)))
                }
            }
        }
    }
    println(results.sum())
}

fun getNumberFromLine(map: Map<Int, Pixel>, low: Int, high: Int): Int {
    var result = ""
    for (column in low..high) {
        result += map[column]!!.value
    }
    return result.toInt()
}

fun findConnected(
    grid: Map<Int, Map<Int, Pixel>>,
    line: Int,
    start: Pair<Int, Int>,
    increment: Int,
    low: Int,
    usedCoords: Map<Int, Pair<Int, Pair<Int, Int>>>
): Int {
    var lowest = low
    if (start.second in 1..maxWidth && !isUsed(usedCoords, line, start)) {
        var pixel = grid[start.first]!![start.second]!!
        if (pixel.isNumber) {
            lowest = start.second
            return findConnected(grid, line, start.first to start.second + increment, increment, lowest, usedCoords)
        }
    }
    return lowest
}

fun isUsed(usedCoords: Map<Int, Pair<Int, Pair<Int, Int>>>, line: Int, start: Pair<Int, Int>): Boolean =
    usedCoords.filter { it.key == line }.values
        .filter { it.first == start.first }
        .any { coord -> coord.second.first == start.first && coord.second.second == start.second }


fun extractSearchCoordinates(symbols: List<Pair<Int, Map<Int, Pixel>>>): List<Pair<Int, Int>> {
    var searchCoordinates: List<Pair<Int, Int>> = mutableListOf()
    symbols.forEach { line ->
        var surroundingCoordinates: MutableList<Pair<Int, Int>> = mutableListOf()

        line.second.map {
            var lineNumber = line.first
            var symbolColumn = it.key
            //left
            if (symbolColumn != 1) surroundingCoordinates += lineNumber to symbolColumn - 1
            //right
            if (symbolColumn != maxWidth) surroundingCoordinates += lineNumber to symbolColumn + 1
            //up
            if (lineNumber != 1) surroundingCoordinates += lineNumber - 1 to symbolColumn
            //down
            if (lineNumber != maxWidth) surroundingCoordinates += lineNumber + 1 to symbolColumn
            //left-up
            if (symbolColumn != 1 && lineNumber != 1) surroundingCoordinates += lineNumber - 1 to symbolColumn - 1
            //left-down
            if (symbolColumn != maxWidth && lineNumber != 1) surroundingCoordinates += lineNumber + 1 to symbolColumn - 1
            //right-up
            if (symbolColumn != maxWidth && lineNumber != 1) surroundingCoordinates += lineNumber - 1 to symbolColumn + 1
            //right-down
            if (symbolColumn != maxWidth && lineNumber != maxWidth) surroundingCoordinates += lineNumber + 1 to symbolColumn + 1

        }
        searchCoordinates += surroundingCoordinates
    }
    return searchCoordinates.toList()
}

private fun getGrid(lines: Iterable<IndexedValue<String>>): Map<Int, Map<Int, Pixel>> {
    var grid: Map<Int, Map<Int, Pixel>> = emptyMap()
    lines.forEach { line ->
        var lineMap: Map<Int, Pixel> = emptyMap()
        var characters = line.value.toCharArray()
        characters.withIndex().forEach {
            lineMap = lineMap.plus(it.index + 1 to Pixel(it.value.toString()))
        }
        grid = grid.plus(line.index + 1 to lineMap)
    }
    return grid
}


data class Pixel(val value: String) {
    val isDot = value == "."
    val isNumber = Regex("[0-9]").containsMatchIn(value)
    val isSymbol = Regex("([*#+$=&%/@])").containsMatchIn(value)
}
import java.io.File

fun main() {
    val lines = File("day3.input").readLines().withIndex()
    var grid: Map<Int, Map<Int, Pixel>> = getGrid(lines)
    val symbols = getSymbolLocations(grid)
    val searchCoordinates = symbols.map { symbol -> symbol to extractSearchCoordinates(symbol) }.sortedBy { it.first.first }
    var result:List<Pair<Pair<Int,Int>, Int>> = emptyList()
    val usedCoords:List<Pair<Int, Pair<Int, Int>>> = emptyList()
    searchCoordinates.forEach { symbol ->
        symbol.second.forEach { searchCoordinate ->
            val pixel = grid.getPixel(searchCoordinate)
            if(pixel.isNumber){
                val left = findConnected(grid, searchCoordinate, -1, searchCoordinate.second, usedCoords)
                val right = findConnected(grid, searchCoordinate, 1, searchCoordinate.second, usedCoords)
                result += symbol.first to getNumberFromLine(grid[searchCoordinate.first]!!, left, right)
            }
        }
    }
    println(result)
    println(result.toSet().sumOf { it.second })


}

fun Map<Int, Map<Int, Pixel>>.getPixel(coordinates: Pair<Int, Int>): Pixel =
    this[coordinates.first]!![coordinates.second]!!


private fun getSymbolLocations(grid: Map<Int, Map<Int, Pixel>>): List<Pair<Int, Int>> {
    val symbolsByLine =
        grid.map { it.key to it.value.filter { pixel -> pixel.value.isSymbol } }.filter { it.second.isNotEmpty() }
    return symbolsByLine.map { it.second.entries.map { sm -> it.first to sm.key } }.flatten()
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
    start: Pair<Int, Int>,
    increment: Int,
    low: Int,
    usedCoords: List<Pair<Int, Pair<Int, Int>>>
): Int {
    var lowest = low
    if (start.second in 1..140 && !isUsed(usedCoords, start)) {
        var pixel = grid[start.first]!![start.second]!!
        if (pixel.isNumber) {
            lowest = start.second
            return findConnected(grid, start.first to start.second + increment, increment, lowest, usedCoords)
        }
    }
    return lowest
}

fun isUsed(usedCoords: List<Pair<Int, Pair<Int, Int>>>, start: Pair<Int, Int>): Boolean =
    usedCoords.filter { it.first == start.first }
        .filter { coord -> coord.second.first == start.first && coord.second.second == start.second }.isNotEmpty()


fun extractSearchCoordinates(symbol: Pair<Int, Int>): List<Pair<Int, Int>> {

    val surroundingCoordinates: MutableList<Pair<Int, Int>> = mutableListOf()
    val lineNumber = symbol.first
    val symbolColumn = symbol.second

    //left
    if (symbolColumn != 1) surroundingCoordinates += lineNumber to symbolColumn - 1
    //right
    if (symbolColumn != 140) surroundingCoordinates += lineNumber to symbolColumn + 1
    //up
    if (lineNumber != 1) surroundingCoordinates += lineNumber - 1 to symbolColumn
    //down
    if (lineNumber != 140) surroundingCoordinates += lineNumber + 1 to symbolColumn
    //left-up
    if (symbolColumn != 1 && lineNumber != 1) surroundingCoordinates += lineNumber - 1 to symbolColumn - 1
    //left-down
    if (symbolColumn != 140 && lineNumber != 1) surroundingCoordinates += lineNumber + 1 to symbolColumn - 1
    //right-up
    if (symbolColumn != 140 && lineNumber != 1) surroundingCoordinates += lineNumber - 1 to symbolColumn + 1
    //right-down
    if (symbolColumn != 140 && lineNumber != 140) surroundingCoordinates += lineNumber + 1 to symbolColumn + 1

    return surroundingCoordinates.sortedWith(compareBy({it.first}, {it.second}))
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
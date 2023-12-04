import java.io.File

fun main() {
    val lines = AocUtil.load("day3.input").lines()
    val grid: Map<Int, Map<Int, Pixel>> = getGrid(lines.withIndex())

    //Part 1
    val symbols = getSymbolLocations(grid)
    val searchCoordinates =
        symbols.map { symbol -> symbol to extractSearchCoordinates(symbol) }.sortedBy { it.first.first }
    val result: MutableList<Pair<Pair<Int, Int>, Int>> = mutableListOf()

    searchCoordinates.reversed().forEach { symbol ->
        symbol.second.forEach { searchCoordinate ->
            val pixel = grid.getPixel(searchCoordinate)
            if (pixel.isNumber) {
                val left = findConnected(grid, searchCoordinate, -1, searchCoordinate.second)
                val right = findConnected(grid, searchCoordinate, 1, searchCoordinate.second)
                result += symbol.first to getNumberFromLine(grid[searchCoordinate.first]!!, left, right)
            }
        }
    }
    println(result.toSet().sumOf { it.second })
    grid.print(symbols, searchCoordinates.map { it.second }.flatten())

    //Part 2
    val gears = getSymbolLocations(grid, "*")
    val gearSearchCoordinates =
        gears.map { symbol -> symbol to extractSearchCoordinates(symbol) }.sortedBy { it.first.first }
    val gearResult: MutableList<Pair<Pair<Int, Int>, Int>> = mutableListOf()
    gearSearchCoordinates.reversed().forEach { symbol ->
        symbol.second.forEach { searchCoordinate ->
            val pixel = grid.getPixel(searchCoordinate)
            if (pixel.isNumber) {
                val left = findConnected(grid, searchCoordinate, -1, searchCoordinate.second)
                val right = findConnected(grid, searchCoordinate, 1, searchCoordinate.second)
                gearResult += symbol.first to getNumberFromLine(grid[searchCoordinate.first]!!, left, right)
            }
        }
    }
    println(gearResult.toSet().groupBy { it.first }
        .filter { it.value.size == 2 }.values.sumOf { it[0].second * it[1].second })

}

fun getSymbolLocations(grid: Map<Int, Map<Int, Pixel>>, s: String): List<Pair<Int, Int>> {
    val symbolsByLine =
        grid.map { it.key to it.value.filter { pixel -> pixel.value.isSymbol && pixel.value.value == s } }
            .filter { it.second.isNotEmpty() }
    return symbolsByLine.map { it.second.entries.map { sm -> it.first to sm.key } }.flatten()
}

fun Map<Int, Map<Int, Pixel>>.getPixel(coordinates: Pair<Int, Int>): Pixel =
    this[coordinates.first]!![coordinates.second]!!

fun Map<Int, Map<Int, Pixel>>.print(
    symbols: List<Pair<Int, Int>> = emptyList(), searchCoordinates: List<Pair<Int, Int>> = emptyList()
) {
    this.entries.forEach { line ->
        print("${line.key.toString().padStart(3, '0')}: ")
        for (coord in line.value) {
            val isDiscoveredSymbol =
                symbols.any { symbol -> symbol.first == line.key && symbol.second == coord.key }
            val isSearched =
                searchCoordinates.any { symbol -> symbol.first == line.key && symbol.second == coord.key }

            print(coord.value.toColorString(isDiscoveredSymbol, isSearched))
        }
        println()
    }
}

fun Pixel.toColorString(isDiscoveredSymbol: Boolean, isSearched: Boolean): String {
    if (isDiscoveredSymbol) return "\u001b[32m" + this.value + "\u001b[0m"
    if (isSearched) return "\u001b[43m" + this.value + "\u001b[0m"
    if (this.isNumber) return "\u001b[44m" + this.value + "\u001b[0m"
    if (this.isSymbol) return "\u001b[31m" + this.value + "\u001b[0m"
    return this.value
}

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
    grid: Map<Int, Map<Int, Pixel>>, start: Pair<Int, Int>, increment: Int, low: Int
): Int {
    var lowest = low
    if (start.second in 1..140) {
        val pixel = grid[start.first]!![start.second]!!
        if (pixel.isNumber) {
            lowest = start.second
            return findConnected(grid, start.first to start.second + increment, increment, lowest)
        }
    }
    return lowest
}

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

    return surroundingCoordinates.sortedWith(compareBy({ it.first }, { it.second }))
}

private fun getGrid(lines: Iterable<IndexedValue<String>>): Map<Int, Map<Int, Pixel>> {
    var grid: Map<Int, Map<Int, Pixel>> = emptyMap()
    lines.forEach { line ->
        var lineMap: Map<Int, Pixel> = emptyMap()
        val characters = line.value.toCharArray()
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
    val isSymbol = Regex("([*#+$=&%/@-])").containsMatchIn(value)
}
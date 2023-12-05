fun main() {
    val input = AocUtil.load("day5.input")
    val testInput = AocUtil.load("day5.test.input")
    solvePartOne(testInput)
    solvePartTwo()
}

fun solvePartOne(input: String) {
    val seeds = input.lines().get(0).split(":")[1].trim().split(" ").map { seed -> seed.toInt() }
    val conversionMaps = input.reader().readText().drop(20).split("\n\n").map { line -> line.split(":") }
        .map { line -> line.first() to line.last().split("\n").filter { line -> line.isNotEmpty() } }.map { line ->
            line.first to line.second.map { r -> NumberRange(r) }
        }.map { m -> ConversionMap(m.first, m.second) }

    for (seed in seeds) {
        var sourceNumber = seed
        var destination: Int? = null
        for (map in conversionMaps) {
            destination = map.convert(sourceNumber)
            sourceNumber = destination
        }
        println("$seed $destination")
    }

}

fun solvePartTwo() {}

data class ConversionMap(val name: String, val conversionRanges: List<NumberRange>) {
    fun convert(sourceNumber: Int): Int {
        val conversions = conversionRanges.map { range ->
            range.convert(sourceNumber)
        }.filter { c -> c != sourceNumber }
        return if (conversions.isNotEmpty()) conversions.first()!! else sourceNumber
    }
}

data class NumberRange(val input: String) {
    private val splitInput = input.split(" ")
    val destinationRangeStart = splitInput[0].toInt()
    val sourceRangeStart: Int = splitInput[1].toInt()
    val rangeLength: Int = splitInput[2].toInt()
    fun convert(sourceNumber: Int): Int {
        var sourceRange = sourceRangeStart..sourceRangeStart + rangeLength
        var destinationRange = destinationRangeStart..destinationRangeStart + rangeLength
        if (sourceRange.indexOf(sourceNumber) != -1) {
            return destinationRange.elementAt(sourceRange.indexOf(sourceNumber))
        }
        return sourceNumber
    }
}

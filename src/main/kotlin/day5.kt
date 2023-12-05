fun main() {
//    val input = AocUtil.load("day5.input")
    val input = AocUtil.load("day5.test.input")
//    solvePartOne(input)
    solvePartTwo(input)
}

fun solvePartOne(input: String) {
    val line1Length = input.lines().get(0).length
    val seeds = input.lines().get(0).split(":")[1].trim().split(" ").map { seed -> seed.toLong() }
    val conversionMaps = input.reader().readText().drop(line1Length).split("\n\n").map { line -> line.split(":") }
        .map { line -> line.first() to line.last().split("\n").filter { line -> line.isNotEmpty() } }.map { line ->
            line.first to line.second.map { r -> NumberRange(r) }
        }.map { m -> ConversionMap(m.first, m.second) }

    val seedLocations: MutableList<Pair<Long, Long>> = mutableListOf()
    for (seed in seeds) {
        println("Converting seed $seed")
        var sourceNumber = seed
        var destination: Long? = null
        for (map in conversionMaps) {
            destination = map.convert(sourceNumber)
            sourceNumber = destination
            println("$sourceNumber - ${map.name} - $destination")

        }
        seedLocations.add(seed to destination!!)
    }
    println(seedLocations)
    println("Lowest ${seedLocations.minBy { i -> i.second }}")

}

fun solvePartTwo(input: String) {
    val seedRanges = input.lines().get(0).split(":")[1].trim().split(" ")
    val seeds = mutableListOf<Long>()
    seedRanges.forEachIndexed { index, s ->
        if (index % 2 == 0) {
            var start = s.toLong()
            var end = s.toLong() + seedRanges[index+1].toLong() -1
            for (b in start..end) {
                seeds.add(b)
            }
        }
    }
    println(seeds.sortedBy { it })
    println(seeds.size)
}

data class ConversionMap(val name: String, val conversionRanges: List<NumberRange>) {
    fun convert(sourceNumber: Long): Long {
        val conversions = conversionRanges.map { range ->
            range.convert(sourceNumber)
        }.filter { c -> c != sourceNumber }
        return if (conversions.isNotEmpty()) conversions.first()!! else sourceNumber
    }
}

data class NumberRange(val input: String) {
    private val splitInput = input.split(" ")
    val destinationRangeStart = splitInput[0].toLong()
    val sourceRangeStart = splitInput[1].toLong()
    val rangeLength = splitInput[2].toLong()
    val sourceRangeEnd = sourceRangeStart + rangeLength
    var sourceRange = sourceRangeStart..sourceRangeStart + rangeLength
    var destinationRange = destinationRangeStart..destinationRangeStart + rangeLength
    fun convert(sourceNumber: Long): Long {
        if (sourceNumber < sourceRangeStart) return sourceNumber
        if (sourceNumber > sourceRangeEnd) return sourceNumber
        return destinationRange.elementAt(sourceRange.indexOf(sourceNumber))
    }
}

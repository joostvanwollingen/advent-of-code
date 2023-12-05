fun main() {
//    val input = AocUtil.load("day5.input")
    val input = AocUtil.load("day5.test.input")
//    solvePartOne(input)
    solvePartTwo(input)
}

fun solvePartOne(input: String) {
    val seeds = input.lines().get(0).split(":")[1].trim().split(" ").map { seed -> seed.toLong() }
    val conversionMaps = getConversionMaps(input)

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
    val seedRangeStartEnd = input.lines().get(0).split(":")[1].trim().split(" ")
    val seedRanges: MutableList<Pair<Long, Long>> = mutableListOf()
    seedRangeStartEnd.forEachIndexed { index, s ->
        if (index % 2 == 0) {
            var start = s.toLong()
            var end = s.toLong() + seedRangeStartEnd[index + 1].toLong() - 1
            seedRanges.add(start to end)
        }
    }

    val conversionMaps = getConversionMaps(input)
    val seeds: MutableMap<Long, Long> = mutableMapOf()
    val finalSeeds: MutableMap<Long, Long> = mutableMapOf()
    for (seedRange in seedRanges) {
        println("Working on seedRange $seedRange")
        for (i in 0..6) {
            println(conversionMaps[i].name + " start")
            if (i == 0) {
                val currentSeedRange = (seedRange.first..seedRange.second)
                println("Converting seedRange $seedRange to seeds")
                val canConvert = conversionMaps[i].canConvert(
                    seedRange.first, seedRange.second
                )
                if (!canConvert) {
                    println("$seedRange can't be converted by ${conversionMaps[i].name}")
                    currentSeedRange.map { seed -> seeds.put(seed, seed) }
                } else {
                    println("$seedRange can be converted by ${conversionMaps[i].name}")
                    currentSeedRange.map { seed -> seeds.put(seed, conversionMaps[i].convert(seed)) }
                }
            } else {
                seeds.forEach { seed ->
                    seeds[seed.key] = conversionMaps[i].convert(seed.value)
                }
            }
            println(seeds)
            println(conversionMaps[i].name + " end")
        }
        println("$seedRange fully converted")
        println(seeds)
        println(seeds.size)
        finalSeeds.putAll(seeds)
        seeds.clear()
    }
    println(finalSeeds)
    println(finalSeeds.size)
    println(finalSeeds.minBy { it.value })
}


data class ConversionMap(val name: String, val conversionRanges: List<NumberRange>) {
    fun convert(sourceNumber: Long): Long {
        val conversions = conversionRanges.map { range ->
            range.convert(sourceNumber)
        }.filter { c -> c != sourceNumber }
        return if (conversions.isNotEmpty()) conversions.first()!! else sourceNumber
    }

    fun canConvert(sourceStart: Long, sourceEnd: Long): Boolean = conversionRanges.any { map ->
        sourceStart >= map.sourceRangeStart && sourceEnd <= map.sourceRangeEnd
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

private fun getConversionMaps(input: String): List<ConversionMap> {
    val line1Length = input.lines().get(0).length

    return input.reader().readText().drop(line1Length).removePrefix("\n\n").split("\n\n")
        .map { line -> line.split(":") }
        .map { line -> line.first() to line.last().split("\n").filter { line -> line.isNotEmpty() } }.map { line ->
            line.first to line.second.map { r -> NumberRange(r) }
        }.map { m -> ConversionMap(m.first, m.second) }
}
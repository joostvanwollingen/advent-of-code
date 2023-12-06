import java.lang.Long.max
import java.lang.Long.min

fun main() {
    val input = AocUtil.load("day5.input")
//    val input = AocUtil.load("day5.test.input")
//    solvePartOne(input)
    solvePartTwo(input) //wrong:(3894936732, 50716417)
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
    val seedRanges: MutableList<Pair<Long, Long>> = getSeedRanges(seedRangeStartEnd)

    val conversionMaps = getConversionMaps(input)
    var lowest: Pair<Long, Long>? = null
    var currentSeed: Pair<Long, Long>? = null

    val remap = conversionMaps[0].remap(seedRanges)

//
//        for (seed in seedRange.first..seedRange.second) {
//            currentSeed = seed to conversionMaps[6].convertSingle(
//                conversionMaps[5].convertSingle(
//                    conversionMaps[4].convertSingle(
//                        conversionMaps[3].convertSingle(
//                            conversionMaps[2].convertSingle(
//                                conversionMaps[1].convertSingle(
//                                    conversionMaps[0].convertSingle(seed)
//                                )
//                            )
//                        )
//                    )
//                )
//            )
//            if (lowest == null) lowest = currentSeed
//            if (lowest.second > currentSeed.second) lowest = currentSeed
//        }
//    }
//    println(lowest)
}

private fun getSeedRanges(seedRangeStartEnd: List<String>): MutableList<Pair<Long, Long>> {
    val seedRanges: MutableList<Pair<Long, Long>> = mutableListOf()
    seedRangeStartEnd.forEachIndexed { index, s ->
        if (index % 2 == 0) {
            var start = s.toLong()
            var end = s.toLong() + seedRangeStartEnd[index + 1].toLong() - 1
            seedRanges.add(start to end)
        }
    }
    return seedRanges
}


data class ConversionMap(val name: String, val conversionRanges: List<NumberRange>) {
    fun convert(sourceNumber: Long): Long {
        val conversions = conversionRanges.map { range ->
            range.convert(sourceNumber)
        }.filter { c -> c != sourceNumber }
        return if (conversions.isNotEmpty()) conversions.first()!! else sourceNumber
    }

    fun remap(seedRanges: List<Pair<Long, Long>>) {//: Map<Triple<Long, Long, Long>, NumberRange> {
        val result: Map<Pair<Long, Long>, Triple<Long,Long,Long>> = mutableMapOf()
        val sortedConversionRanges = conversionRanges.sortedBy { it.sourceRangeStart }
            .map { Triple(it.destinationRangeStart, it.sourceRangeStart, it.sourceRangeEnd) }
        seedRanges.forEach {
            val firstIndex = firstRangeWithEndThatFits(sortedConversionRanges, it.first)
            val lastIndex = firstRangeWithEndThatFits(sortedConversionRanges, it.second)
            val currentMax = it.first

            for (i in firstIndex..lastIndex) {
                if (currentMax > sortedConversionRanges[i].second) continue
                result.plus((currentMax to min(it.second, sortedConversionRanges[i].third) )
            }
        }
    }

    fun convertSingle(sourceNumber: Long): Long {
        var converter =
            conversionRanges.filter { m -> sourceNumber >= m.sourceRangeStart && sourceNumber <= m.sourceRangeEnd }
        if (converter.isNotEmpty()) {
            return converter.first().convert(sourceNumber)
        }
        return sourceNumber
    }
}

data class NumberRange(val input: String) {
    private val splitInput = input.split(" ")
    val destinationRangeStart = splitInput[0].toLong()
    val sourceRangeStart = splitInput[1].toLong()
    val rangeLength = splitInput[2].toLong()
    val sourceRangeEnd = sourceRangeStart + rangeLength

    //    var sourceRange = sourceRangeStart..sourceRangeStart + rangeLength
//    var destinationRange = destinationRangeStart..destinationRangeStart + rangeLength
    fun convert(sourceNumber: Long): Long {
        if (sourceNumber < sourceRangeStart) return sourceNumber
        if (sourceNumber > sourceRangeEnd) return sourceNumber
//        return destinationRange.elementAt(sourceRange.indexOf(sourceNumber))
        return destinationRangeStart + (sourceNumber - sourceRangeStart)
    }
}

fun firstRangeWithEndThatFits(ranges: List<Triple<Long, Long, Long>>, t: Long): Int {
    val n = ranges.size
    for (i: Int in 1..n) {
        if (ranges[i - 1].third > t) {
            return i - 1
        }
    }
    return -1
}

private fun getConversionMaps(input: String): List<ConversionMap> {
    val line1Length = input.lines().get(0).length

    return input.reader().readText().drop(line1Length).removePrefix("\n\n").split("\n\n")
        .map { line -> line.split(":") }
        .map { line -> line.first() to line.last().split("\n").filter { line -> line.isNotEmpty() } }.map { line ->
            line.first to line.second.map { r -> NumberRange(r) }
        }.map { m -> ConversionMap(m.first, m.second) }
}
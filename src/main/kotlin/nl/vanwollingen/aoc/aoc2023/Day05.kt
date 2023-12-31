package nl.vanwollingen.aoc.aoc2023

import nl.vanwollingen.aoc.util.PuzzleInputUtil

fun main() {
    val input = PuzzleInputUtil.load("2023/day5.input")
//    val input = nl.vanwollingen.aoc.util.AocUtil.load("day5.test.input")
    Day5().solvePartOne(input)
    Day5().solvePartTwo(input) //wrong:(3894936732, 50716417)
}

class Day5 {
    fun solvePartOne(input: String) {
        val seeds = input.lines()[0].split(":")[1].trim().split(" ").map { seed -> seed.toLong() }
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
        val seedRangeStartEnd = input.lines()[0].split(":")[1].trim().split(" ")
        val seedRanges: List<Pair<Long, Long>> = getSeedRanges(seedRangeStartEnd).dropLast(1)
        val conversionMaps = getConversionMaps(input)
        var lowestSeedSeen: Pair<Long, Long> = Pair(Long.MAX_VALUE, Long.MAX_VALUE)

        seedRanges.forEach { seedRange ->
            var currentSeedRange = mutableListOf(seedRange)
            conversionMaps.forEachIndexed { _, cMap ->
                val convertedRanges = currentSeedRange.flatMap { range -> extracted(cMap, range) }
                currentSeedRange = if (convertedRanges.isNotEmpty()) convertedRanges.toMutableList() else currentSeedRange
            }
            val lowestObservedInRange = currentSeedRange.minBy { it.first }
            println("Lowest in Range $lowestObservedInRange")
            if (lowestSeedSeen.first > lowestObservedInRange.first) lowestSeedSeen = lowestObservedInRange
            println("$seedRange ====lowest===> : $lowestSeedSeen")
        }
        println("Overall Lowest $lowestSeedSeen")
        println(seedRanges.filter { it.first >= lowestSeedSeen.first }.filter { it.second <= lowestSeedSeen.second })
    }

    private fun extracted(cMap: ConversionMap, currentSeedRange: Pair<Long, Long>): MutableList<Pair<Long, Long>> {
        val firstIndex = firstRangeWithEndThatFits(cMap.convTriplesSorted, currentSeedRange.first)
        val lastIndex = firstRangeWithEndThatFits(cMap.convTriplesSorted, currentSeedRange.second)
        val newRanges: MutableList<Pair<Long, Long>> = if (firstIndex != -1) newRangesFrom(cMap.convTriplesSorted, firstIndex, lastIndex, currentSeedRange.first, currentSeedRange.second) else {
            mutableListOf(currentSeedRange)
        }
        println("${cMap.name.padStart(30, ' ')} $currentSeedRange - $newRanges")
        return newRanges
    }

    private fun newRangesFrom(convertors: List<Triple<Long, Long, Long>>, firstIndex: Int, lastIndex: Int, rangeStart: Long, rangeEnd: Long): MutableList<Pair<Long, Long>> {//: Any {
        val result: MutableList<Pair<Long, Long>> = mutableListOf()
        var newRangeStart = rangeStart
        for (i in firstIndex..lastIndex) {
            if (rangeEnd <= convertors[i].second) { //Range fully contained
                result += newRangeStart + convertors[i].third to rangeEnd + convertors[i].third
            } else if (rangeEnd > convertors[i].second) { //Range ends in later index
                result += newRangeStart to convertors[i].second
                newRangeStart = convertors[i].second + 1
            }
        }
        return result
    }

    data class ConversionMap(val name: String, val conversionRanges: List<NumberRange>) {

        val convTriplesSorted = conversionRanges.map { Triple(it.sourceRangeStart, it.sourceRangeEnd, it.destinationRangeStart) }.sortedBy { it.first }

        fun convert(sourceNumber: Long): Long {
            val conversions = conversionRanges.map { range ->
                range.convert(sourceNumber)
            }.filter { c -> c != sourceNumber }
            return if (conversions.isNotEmpty()) conversions.first() else sourceNumber
        }
    }

    data class NumberRange(val input: String) {
        private val splitInput = input.split(" ")
        val destinationRangeStart = splitInput[0].toLong()
        val sourceRangeStart = splitInput[1].toLong()
        private val rangeLength = splitInput[2].toLong()
        val sourceRangeEnd = sourceRangeStart + rangeLength

        //    var sourceRange = sourceRangeStart..sourceRangeStart + rangeLength
//    var destinationRange = destinationRangeStart..destinationRangeStart + rangeLength
        fun convert(sourceNumber: Long): Long {
            if (sourceNumber < sourceRangeStart) return sourceNumber
            if (sourceNumber > sourceRangeEnd) return sourceNumber
//        return destinationRange.elementAt(sourceRange.indexOf(sourceNumber))
            return destinationRangeStart + (sourceNumber - sourceRangeStart)
        }

        override fun toString(): String {
            return "range(dest=$destinationRangeStart, start=$sourceRangeStart, end=$sourceRangeEnd)"
        }

    }

    private fun firstRangeWithEndThatFits(ranges: List<Triple<Long, Long, Long>>, t: Long): Int {
        val n = ranges.size
        for (i: Int in 1..n) {
            if (ranges[i - 1].second > t) {
                return i - 1
            }
        }
        return -1
    }

    private fun getConversionMaps(input: String): List<ConversionMap> {
        val line1Length = input.lines()[0].length

        return input.reader().readText().drop(line1Length).removePrefix("\n\n").split("\n\n").map { line -> line.split(":") }.map { line -> line.first() to line.last().split("\n").filter { it.isNotEmpty() } }.map { line ->
            line.first to line.second.map { r -> NumberRange(r) }
        }.map { m -> ConversionMap(m.first, m.second) }
    }

    private fun getSeedRanges(seedRangeStartEnd: List<String>): List<Pair<Long, Long>> {
        val seedRanges: MutableList<Pair<Long, Long>> = mutableListOf()
        seedRangeStartEnd.forEachIndexed { index, s ->
            if (index % 2 == 0) {
                val start = s.toLong()
                val end = s.toLong() + seedRangeStartEnd[index + 1].toLong() - 1
                seedRanges.add(start to end)
            }
        }
        return seedRanges.sortedBy { it.first }
    }
}
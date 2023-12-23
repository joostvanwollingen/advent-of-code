package nl.vanwollingen.aoc.aoc2023

import nl.vanwollingen.aoc.util.PuzzleInputUtil
import java.time.Instant
import java.util.Date

fun main() {
    val input = PuzzleInputUtil.load("day5.input")
    val day5 = Day5Again()
    day5.solvePart1(input)
    day5.solvePart2(input)
}

class Day5Again {
    fun solvePart2(input: String) {
        println(Date.from(Instant.now()))
        val seeds = input.lines()[0].split(": ")[1].split(" ").map { it -> it.toLong() }
        val seedRanges = seeds.windowed(2, 2).map {
            SeedRange(it[0], it[0] + it[1])
        }
        val maps = getMaps(input)

        val seedRange = seedRanges[0]
        val sortedRangeMaps = maps.map { it.key to it.value.sortedBy { l->l.start } }
        println(seedRange)
//    println(findNewRanges(seedRange, sortedRangeMaps)

        val newSeedRanges = sortedRangeMaps.fold(seedRanges) { acc, pair -> acc.flatMap { findNewRanges(it, pair.second) } }
        println(newSeedRanges)
        println(newSeedRanges.minBy { it.start }.start)
        println(Date.from(Instant.now()))

    }

    fun solvePart1(input: String) {
        val seeds = input.lines()[0].split(": ")[1].split(" ").map { it -> it.toLong() }
        val maps = getMaps(input)
        var currentSeed: Long = 0
        val endSeeds: MutableList<Pair<Long, Long>> = mutableListOf()
        seeds.forEach { seed ->
            currentSeed = seed
            println(currentSeed)
            maps.forEach { map ->
                println(map.key)
                for (rangeMap in map.value) {
                    println(rangeMap)
                    if (currentSeed >= rangeMap.start && currentSeed <= rangeMap.end) {
                        currentSeed = (currentSeed - rangeMap.start) + rangeMap.destination
                        println(currentSeed)
                        break
                    }
                }
            }
            endSeeds += seed to currentSeed
        }
        println(endSeeds)
        println(endSeeds.minBy { it.second })
    }

    private fun getMaps(lines: String): Map<String, List<RangeMap>> {
        val returnMaps: MutableMap<String, List<RangeMap>> = mutableMapOf()
        val maps = lines.reader().readText().split("\n\n").drop(1)
        maps.map { line ->
            val (mapName, rangeStrings) = line.split(":")
            val ranges = rangeStrings.split("\n").filter { it.isNotEmpty() }.map { it.split(" ") }
            val rangeObjects = ranges.map { it ->
                RangeMap(it[1].toLong(), (it[1].toLong() + it[2].toLong()) - 1, it[0].toLong())
            }
            returnMaps.put(mapName, rangeObjects)

        }
        return returnMaps
    }

    data class RangeMap(val start: Long, val end: Long, val destination: Long)
    data class SeedRange(val start: Long, val end: Long)

    private fun findNewRanges(seedRange: SeedRange, otherRanges: List<RangeMap>): List<SeedRange> {
        var start = seedRange.start
        val end = seedRange.end

        val newSeedRanges = mutableListOf<SeedRange>()

        for (otherRange in otherRanges) {
            if (start <= otherRange.end && end >= otherRange.start) {
                if (start < otherRange.start) {
                    newSeedRanges.add(SeedRange(start, otherRange.start - 1))
                    start = otherRange.start
                }
                val offset = otherRange.destination - otherRange.start
//            val offset = 0
                val newStart = if (start >= otherRange.start) start else otherRange.start
                val newEnd = if (end <= otherRange.end) end else otherRange.end

                newSeedRanges.add(SeedRange(newStart + offset, newEnd + offset))
                start = newEnd + 1
            }
        }

        if (start <= end) {
            newSeedRanges.add(SeedRange(start, end))
        }

        return newSeedRanges
    }
}
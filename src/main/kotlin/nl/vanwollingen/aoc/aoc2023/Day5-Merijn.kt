package nl.vanwollingen.aoc.aoc2023

import nl.vanwollingen.aoc.util.AocUtil
import java.time.Instant
import java.util.Date

fun main() {
//    val input = nl.vanwollingen.aoc.util.AocUtil.load("day5.test.input")
    val input = AocUtil.load("day5.input")
//    solvePartOneDay5(input)
    solvePartTwoDay5(input)
//    test()
//    test2()
}

fun solvePartTwoDay5(input: String) {
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

fun solvePartOneDay5(input: String) {
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

fun getMaps(lines: String): Map<String, List<RangeMap>> {
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

fun test() {
    val seedRange = RangeMap(12, 62, -1)
    val otherRanges = listOf(
        Triple(1L, 10L, 5L),
        Triple(11L, 20L, 5L),
        Triple(21L, 30L, 5L),
        Triple(31L, 40L, 5L),
        Triple(41L, 50L, 5L),
        Triple(51L, 60L, 5L),
        Triple(61L, 70L, 5L),
        Triple(71L, 80L, 5L),
    )
    val firstIndex = firstRangeWithEndThatFits(otherRanges, seedRange.start)
    val secondIndex = firstRangeWithEndThatFits(otherRanges, seedRange.end)
    println(firstIndex)
    println(secondIndex)

}

data class SeedRange(val start: Long, val end: Long)


fun findNewRanges(seedRange: SeedRange, otherRanges: List<RangeMap>): List<SeedRange> {
    var start = seedRange.start
    var end = seedRange.end

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

fun test2() {
    println("Begin overlapt niet, einde wel")
    var seed = SeedRange(10, 50)
//    val otherRanges = mutableListOf(RangeMap(15, 20, -1), RangeMap(25,30, -1))
    var otherRanges = mutableListOf(RangeMap(20, 60, 10))
    val newRanges = findNewRanges(seed, otherRanges)
    println(newRanges)
    println("")

    println("Other bevat seed volledig")
    otherRanges = mutableListOf(RangeMap(5, 59, 0))
    println(findNewRanges(seed, otherRanges))
    println("")

    println("Seed bevat other volledige")
    otherRanges = mutableListOf(RangeMap(11, 49, 0))
    println(findNewRanges(seed, otherRanges))
    println("")

    println("begin overlap, einde niet")
    otherRanges = mutableListOf(RangeMap(10, 59, 0))
    println(findNewRanges(seed, otherRanges))
    println("")

    println("gat in ranges")
    otherRanges = mutableListOf(
        RangeMap(5, 15, 0),
        RangeMap(45, 55, 0)
    )
    println(findNewRanges(seed, otherRanges))
    println("")

    println("geen overlap")
    otherRanges = mutableListOf(
        RangeMap(5, 9, 0),
        RangeMap(51, 55, 0)
    )
    println(findNewRanges(seed, otherRanges))
    println("")

    println("exacte overlap")
    otherRanges = mutableListOf(RangeMap(10, 50, 0))
    println(findNewRanges(seed, otherRanges))
    println("")

}

//10-----------------------50
//   15--20    25---30
//10-14 15-20 21-24 25-30 31-50


//seed         s1--------------e1
//             10              20           10+20=30
//range            s2------------e2
//                 12            22         12+22=34
//range    s3-----e3
//===> s1-s2
//===>s2-e1
//min(s1,s2)
//max(s1,s2)
//min(e1,e2)
//max(e1,e2)

//seed loopt door naar volgende map
//seed s1---e1
//map         s2----e2

//Knip in 2
//seed s1------e1
//map     s2-----e2


//knip in 3
//seed ------
//map   ---

//knip in 2
//seed          -----
//map       -----

//past in 1 map
//seed    --
//map   -------
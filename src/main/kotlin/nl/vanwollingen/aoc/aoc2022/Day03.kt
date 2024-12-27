package nl.vanwollingen.aoc.aoc2022

import nl.vanwollingen.aoc.util.Puzzle

fun main() = Day03.solve()

object Day03 : Puzzle(exampleInput = false) {

    private val rucksacks = parseInput()

    override fun part1() = rucksacks.sumOf {
        val (compartment, otherCompartment) = it
        val itemType = compartment.map { c -> c to otherCompartment.contains(c) }.first { it.second }.first
        priority(itemType)
    }

    override fun part2() = rucksacks.windowed(3, 3)
        .sumOf {
            val rucksack1 = it[0].first + it[0].second
            val rucksack2 = it[1].first + it[1].second
            val rucksack3 = it[2].first + it[2].second

            val itemType = rucksack1
                .map { itemType -> itemType to (rucksack2.contains(itemType) to rucksack3.contains(itemType)) }
                .first { it.second.first && it.second.second }.first

            priority(itemType)
        }

    private fun priority(c: Char): Int {
        val offset = if (c.isLowerCase()) 96 else 38
        return c.code - offset
    }

    override fun parseInput(): List<Pair<String, String>> = input.lines().map { line ->
        val mid = line.length / 2
        line.substring(0, mid) to line.substring(mid)
    }
}
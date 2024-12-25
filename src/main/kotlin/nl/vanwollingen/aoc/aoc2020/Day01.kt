package nl.vanwollingen.aoc.aoc2020

import nl.vanwollingen.aoc.util.Puzzle

fun main() = Day01.solve()

object Day01 : Puzzle() {
    override fun part1(): Any {
        val entries = input.lines().map { it.toLong() }
        return entries.forEach { entry1 ->
            entries.forEach { entry2 ->
                if (entry1 + entry2 == 2020L) {
                    return entry1 * entry2
                }
            }
        }
    }

    override fun part2(): Any {
        val entries = input.lines().map { it.toLong() }
        return entries.forEach { entry1 ->
            entries.forEach { entry2 ->
                entries.forEach { entry3 ->
                    if (entry1 + entry2 + entry3 == 2020L) {
                        return entry1 * entry2 * entry3
                    }
                }
            }
        }
    }
}
package nl.vanwollingen.aoc.aoc2022

import nl.vanwollingen.aoc.util.Puzzle

fun main() = Day01.solve()

object Day01 : Puzzle(exampleInput = false) {
    val elves = input.split("\n\n")

    override fun part1() = elves
        .maxOf { elf ->
            elf
                .lines()
                .sumOf { it.toInt() }
        }

    override fun part2() = elves
        .map { elf ->
            elf
                .lines()
                .sumOf { it.toInt() }
        }
        .sortedDescending()
        .take(3)
        .sumOf { it }
}
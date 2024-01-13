package nl.vanwollingen.aoc.aoc2015

import nl.vanwollingen.aoc.util.Puzzle
import nl.vanwollingen.aoc.util.exceptions.TargetStateReachedException

fun main() {
    val d20 = Day20()
    d20.solvePart1()
    d20.solvePart2()
}

class Day20() : Puzzle() {

    private val targetPresentsAmount = parseInput()
    override fun parseInput() = input.toInt()

    override fun part1() {
        for (house in 1..1000000) {
            var elves = listOf<Int>()
            for (elf in house downTo 1) {
                if (house % elf == 0) elves += elf
            }
            val gifts = elves.sumOf { elf -> elf * 10 }
            if (gifts >= targetPresentsAmount) {
                log("House $house received more than $targetPresentsAmount gifts ($gifts)")
                throw TargetStateReachedException()
            }
            if (house % 100000 == 0) log("House $house received $gifts gifts")
        }
    }

    override fun part2() {
        for (house in 1..1000000) {
            val elves = mutableListOf<Int>()
            for (elf in house downTo 1) {
                if (house % elf == 0 && house / elf <= 50) elves += elf
            }
            val gifts = elves.sumOf { elf -> elf * 11 }
            if (gifts >= targetPresentsAmount) {
                log("House $house received more than $targetPresentsAmount gifts ($gifts)")
                throw TargetStateReachedException()
            }
            if (house % 100000 == 0) log("House $house received $gifts gifts")
        }
    }
}
package nl.vanwollingen.aoc.aoc2015

import nl.vanwollingen.aoc.util.Puzzle
import nl.vanwollingen.aoc.util.combinatorics.combinations

fun main() {
    val d17 = Day17()
    d17.part1()
    d17.part2()
}

class Day17() : Puzzle() {
    val buckets = input.lines().map { it.toInt() }
    override fun part1() {
        var combinationsThatHold150 = 0
        for (i in 1..20) {
            for (seq in buckets.combinations(i)) {
                if (seq.sum() == 150) combinationsThatHold150++
            }
        }
        println(combinationsThatHold150)
    }

    override fun part2() {
        val combinationsThatHold150: MutableMap<Int, Int> = mutableMapOf()
        for (i in 1..20) {
            for (seq in buckets.combinations(i)) {
                if (seq.sum() == 150) {
                    combinationsThatHold150[seq.size] = (combinationsThatHold150.getOrDefault(seq.size, 0)) + 1
                }
            }
        }
        println(combinationsThatHold150.minBy { it.key }.value)
    }
}
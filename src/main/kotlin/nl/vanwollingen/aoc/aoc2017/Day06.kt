package nl.vanwollingen.aoc.aoc2017

import nl.vanwollingen.aoc.util.Puzzle

fun main() {
    val d6 = Day06()
    d6.solvePart1()
    d6.solvePart2()
}

class Day06(output: Boolean = false) : Puzzle(output) {

    private val banks = parseInput()

    override fun parseInput(): Map<Int, Int> = input.split(Regex("\\W")).mapIndexed { i, it -> i to it.toInt() }.toMap()

    override fun part1() {
        val banks = banks.toMutableMap()
        val hashes = mutableListOf<String>()
        log(calculateCycle(banks, hashes))
    }

    override fun part2() {
        val banks = banks.toMutableMap()
        val hashes = mutableListOf<String>()
        calculateCycle(banks, hashes)

        val banksHash = banksAsString(banks)
        val first = hashes.indexOfFirst { it == banksHash } + 1
        val last = hashes.size + 1

        log(last - first)
    }

    private fun calculateCycle(
        banks: MutableMap<Int, Int>, hashes: MutableList<String>
    ): Int {
        var currentBank = banksAsString(banks)
        var cycles = 0

        while (!hashes.contains(currentBank)) {
            hashes += currentBank

            val highest = banks.maxByOrNull { it.value } ?: throw Exception("Empty")
            val bankOfHighest = highest.key
            val valueOfHighest = highest.value
            banks[bankOfHighest] = 0

            for (i in 1..valueOfHighest) {
                val indexToAssign = ((bankOfHighest + i) % banks.size)
                banks[indexToAssign] = banks[indexToAssign]!! + 1
            }

            currentBank = banksAsString(banks)
            cycles++
        }
        return cycles
    }

    private fun banksAsString(banks: MutableMap<Int, Int>) = banks.entries.fold("") { acc, it -> "$acc${it.value}" }
}
package nl.vanwollingen.aoc.aoc2025

import nl.vanwollingen.aoc.util.Puzzle
import nl.vanwollingen.aoc.util.combinatorics.combinations

fun main() = Day03.solve()

object Day03 : Puzzle(true, false) {
    private val joltageBanks = input.lines().map { it.chunked(1).map { it.toLong() } }

    override fun part1() = joltageBanks.sumOf { bank ->
        val combinations = bank.combinations(2).toList()
        combinations.maxOf { "${it[0]}${it[1]}".toLong() }
    }

    override fun part2() =
        joltageBanks.sumOf { bank ->
            val positions = mutableListOf<Int>()
            var leftOffset = 0
            while (couldFit(bank.size, leftOffset, positions.size)) {
                val nextHighest = findHighestFromRight(bank, leftOffset, bank.size - (12 - positions.size))
                positions += nextHighest
                leftOffset = nextHighest + 1
                if (positions.size == 12) break
            }
            val concat: StringBuilder = StringBuilder()
            positions.forEach {
                concat.append(bank[it])
            }
            concat.toString().toLong()
        }


    private fun couldFit(length: Int, leftOffset: Int, positionSize: Int): Boolean {
        val positionsLeft = 12 - positionSize
        return length - leftOffset >= positionsLeft
    }

    private fun findHighestFromRight(bank: List<Long>, leftOffset: Int, rightOffset: Int): Int {
        var highest = 0L
        var highestPosition = 0
        for (i in leftOffset..rightOffset) {
            if (highest < bank[i]) {
                highest = bank[i]
                highestPosition = i
            }
        }
        return highestPosition
    }

}
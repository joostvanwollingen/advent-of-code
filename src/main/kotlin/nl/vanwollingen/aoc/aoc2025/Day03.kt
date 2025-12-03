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

    override fun part2() = joltageBanks.sumOf { bank ->
        val positions = StringBuilder()
        var leftOffset = 0
        while (couldFit(bank.size, leftOffset, positions.length)) {
            val nextHighest = findHighestBetweenOffset(bank, leftOffset, bank.size - (12 - positions.length))
            positions.append(bank[nextHighest])
            leftOffset = nextHighest + 1
            if (positions.length == 12) break
        }
        positions.toString().toLong()
    }


    private fun couldFit(length: Int, leftOffset: Int, positionSize: Int): Boolean {
        val positionsLeft = 12 - positionSize
        return length - leftOffset >= positionsLeft
    }

    private fun findHighestBetweenOffset(bank: List<Long>, leftOffset: Int, rightOffset: Int): Int {
        var highest = 0L
        var highestPosition = 0
        for (i in leftOffset..rightOffset) {
            if (highest < bank[i]) {
                highest = bank[i]
                highestPosition = i
                if(bank[i]==9L) return highestPosition
            }
        }
        return highestPosition
    }

}
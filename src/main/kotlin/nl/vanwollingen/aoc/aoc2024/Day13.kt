package nl.vanwollingen.aoc.aoc2024

import nl.vanwollingen.aoc.util.Puzzle

fun main() = Day13.solve()

object Day13 : Puzzle(exampleInput = false) {
    private val clawMachines = parseInput()

    override fun parseInput(): List<ClawMachine> = input.split("\n\n").map { ClawMachine.fromString(it) }

    //https://github.com/maksverver/AdventOfCode/blob/master/2024/13.py :(
    //https://todd.ginsberg.com/post/advent-of-code/2024/day13/ :(
    override fun part1(): Long = clawMachines.sumOf { it.pressButtons() }

    override fun part2(): Long = clawMachines.sumOf {
        it.movePrize(10000000000000).pressButtons()
    }

    data class ClawMachine(
        private val a: Pair<Long, Long>, private val b: Pair<Long, Long>, private val prize: Pair<Long, Long>
    ) {

        fun movePrize(factor: Long): ClawMachine = copy(
            prize = pX + factor to pY + factor
        )

        private val aX get() = a.first
        private val aY get() = a.second
        private val bX get() = b.first
        private val bY get() = b.second
        private val pX get() = prize.first
        private val pY get() = prize.second

        fun pressButtons(): Long {
            val det = aX * bY - aY * bX
            val a = (pX * bY - pY * bX) / det
            val b = (aX * pY - aY * pX) / det
            return if (aX * a + bX * b == pX && aY * a + bY * b == pY) {
                a * 3 + b
            } else 0
        }

        companion object {
            val regex = "X.(\\d+), Y.(\\d+)".toRegex()
            fun fromString(input: String): ClawMachine {
                val (a, b, prize) = input.lines()
                val aMatch = regex.find(a)!!.groupValues
                val bMatch = regex.find(b)!!.groupValues
                val prizeMatch = regex.find(prize)!!.groupValues
                return ClawMachine(
                    aMatch[1].toLong() to aMatch[2].toLong(),
                    bMatch[1].toLong() to bMatch[2].toLong(),
                    prizeMatch[1].toLong() to prizeMatch[2].toLong()
                )
            }
        }
    }
}
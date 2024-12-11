package nl.vanwollingen.aoc.aoc2024

import nl.vanwollingen.aoc.util.Puzzle
import java.util.*


fun main() = Day11.solve()

object Day11 : Puzzle(exampleInput = false, printDebug = true) {

    private val memo = mutableMapOf<Pair<Long, Int>, Long>()
    private val stones = LinkedList(input.split(" ").map { it.toLong() })

    override fun part1(): Long = stones.sumOf { stone -> blinkWithMemo(stone, 25, memo) }
    override fun part2(): Long = stones.sumOf { stone -> blinkWithMemo(stone, 75, memo) }

    //https://github.com/remcosiemonsma/adventofcode/blob/main/2024/src/main/java/nl/remcoder/adventofcode/Day11.java
    private fun blinkWithMemo(stone: Long, blinks: Int, memo: MutableMap<Pair<Long, Int>, Long>): Long {
        val memoKey = stone to blinks
        memo[memoKey]?.let { return it }

        val result: Long
        if (blinks == 0) {
            result = 1
        } else if (stone == 0L) {
            result = blinkWithMemo(1L, blinks - 1, memo)
        } else if (isLengthEven(stone)) {
            val s = "$stone"
            val l = s.length / 2
            val left = s.substring(0, l).toLong()
            val right = s.substring(l, s.length).toLong()
            result = blinkWithMemo(left, blinks - 1, memo) + blinkWithMemo(right, blinks - 1, memo)
        } else {
            result = blinkWithMemo(stone * 2024, blinks - 1, memo)
        }

        memo[memoKey] = result
        return result
    }

    private fun isLengthEven(value: Long): Boolean {
        var number = value
        var digitCount = 0

        // Handle negative numbers
        if (number < 0) {
            number = -number
        }

        while (number > 0) {
            digitCount++
            number /= 10
        }

        return digitCount % 2 == 0
    }
}
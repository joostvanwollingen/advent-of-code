package nl.vanwollingen.aoc.aoc2024

import nl.vanwollingen.aoc.util.Puzzle
import kotlin.math.abs

fun main() = Day01.solve()

object Day01 : Puzzle() {

    private val parsedInput = parseInput()
    private val left = parsedInput.first
    private val right = parsedInput.second

    override fun parseInput(): Pair<MutableList<Int>, MutableList<Int>> {
        val left: MutableList<Int> = mutableListOf()
        val right: MutableList<Int> = mutableListOf()

        input.lines().forEach { line ->
            val (first, second) = line.split("   ")
            left.add(first.toInt())
            right.add(second.toInt())
        }
        left.sort()
        right.sort()

        return left to right
    }

    override fun part1() = left.zip(right) { a, b -> abs(a - b) }.sum()

    override fun part2(): Int {
        val rightCountByNumber: Map<Int, Int> = countByNumber(right)
        return left.sumOf { number -> rightCountByNumber.getOrDefault(number, 0).times(number) }
    }

    private fun countByNumber(list: List<Int>): Map<Int, Int> {
        val countByNumber = mutableMapOf<Int, Int>()
        list.forEach { item ->
            countByNumber[item] = countByNumber.getOrDefault(item, 0) + 1
        }
        return countByNumber
    }
}
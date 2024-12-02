package nl.vanwollingen.aoc.aoc2024

import nl.vanwollingen.aoc.util.Puzzle
import kotlin.math.abs

fun main() {
    Day01.solvePart1()
    Day01.solvePart2()
}

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

    override fun part1() {
        val result = left.zip(right) { a, b -> abs(a - b) }.sum()
        log(result)
    }

    override fun part2() {
        val rightCountByNumber: Map<Int, Int> = countByNumber(right)
        val score = left.sumOf { number -> rightCountByNumber.getOrDefault(number, 0).times(number) }
        log(score)
    }

    private fun countByNumber(list: List<Int>): Map<Int, Int> {
        val countByNumber = mutableMapOf<Int, Int>()
        list.forEach { item ->
            countByNumber[item] = countByNumber.getOrDefault(item, 0) + 1
        }
        return countByNumber
    }
}
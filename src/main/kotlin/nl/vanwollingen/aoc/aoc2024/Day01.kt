package nl.vanwollingen.aoc.aoc2024

import nl.vanwollingen.aoc.util.Puzzle
import kotlin.math.abs

fun main() {
    Day01.solvePart1()
    Day01.solvePart2()
}

object Day01 : Puzzle() {

    private val parsedInput = parseInput()

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
        val (left, right) = parsedInput
        val result = left.zip(right) { a, b -> abs(a-b) }.sum()
        println(result)
    }

    override fun part2() {
        val (left, right) = parsedInput
        val rightCountByNumber:Map<Int,Int>  = countByNumber(right)
        var score = 0
        for(number in left) {
            score += rightCountByNumber.getOrDefault(number, 0).times(number)
        }
        println(score)
    }

    private fun countByNumber(list: List<Int>) : Map<Int, Int> {
        val countByNumber = mutableMapOf<Int, Int>()
        for(item in list){
            countByNumber[item] = countByNumber.getOrDefault(item, 0) + 1
        }
        return countByNumber
    }
}
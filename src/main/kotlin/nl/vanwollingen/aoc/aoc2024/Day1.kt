package nl.vanwollingen.aoc.aoc2024

import nl.vanwollingen.aoc.util.Puzzle

fun main() {
    Day1.part1()
    Day1.part2()
}


object Day1 : Puzzle(exampleInput = false) {

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
        val (left, right) = parseInput()
        val result = left.zip(right) { a, b -> Math.abs(a-b)}.sum()
        println(result)
    }

    override fun part2() {
        val (left, right) = parseInput()
        val rightCountByNumber:Map<Int,Int>  = right.map { it to right.count { ele-> ele == it } }.distinct().toMap()
        var score = 0
        for(number in left) {
            score += rightCountByNumber.getOrDefault(number, 0).times(number)
        }
        println(score)
    }
}
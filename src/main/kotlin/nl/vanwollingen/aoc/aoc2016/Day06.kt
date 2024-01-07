package nl.vanwollingen.aoc.aoc2016

import nl.vanwollingen.aoc.util.Puzzle

fun main() {
    val d6 = Day06()
    d6.solvePart1()
    d6.solvePart2()
}

class Day06(output: Boolean = false) : Puzzle(output) {
    override fun solvePart1() {
        log(getCountPerPosition(input).map { it.value.maxBy { c -> c.value } }.map { it.key }.joinToString(""))
    }

    override fun solvePart2() {
        log(getCountPerPosition(input).map { it.value.minBy { c -> c.value } }.map { it.key }.joinToString(""))
    }

    private fun getCountPerPosition(input: String): Map<Int, MutableMap<Char, Int>> {
        val countPerPosition: Map<Int, MutableMap<Char, Int>> = mutableMapOf(
                0 to mutableMapOf(),
                1 to mutableMapOf(),
                2 to mutableMapOf(),
                3 to mutableMapOf(),
                4 to mutableMapOf(),
                5 to mutableMapOf(),
                6 to mutableMapOf(),
                7 to mutableMapOf(),
        )
        input.lines().forEach { line ->
            line.forEachIndexed { i, c ->
                if (countPerPosition[i]?.get(c) == null) {
                    countPerPosition[i]?.set(c, 1)
                } else {
                    countPerPosition[i]?.set(c, countPerPosition[i]?.get(c)!! + 1)
                }
            }
        }
        return countPerPosition
    }
}
package nl.vanwollingen.aoc.aoc2019

import nl.vanwollingen.aoc.util.Puzzle

fun main() = Day01.solve()

object Day01 : Puzzle(exampleInput = false) {

    private val modules = input.lines().map { it.toInt() }

    override fun part1() = modules.sumOf(::fuelRequired)

    override fun part2() = calculateFuel(modules)

    private fun calculateFuel(modules: List<Int>): Int {
        val fuel = modules.map(::fuelRequired).filter { it > 0 }
        return if (fuel.isEmpty()) 0 else fuel.sum() + calculateFuel(fuel)
    }

    private fun fuelRequired(mass: Int): Int = mass / 3 - 2
}
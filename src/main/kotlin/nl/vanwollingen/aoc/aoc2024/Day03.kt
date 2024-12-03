package nl.vanwollingen.aoc.aoc2024

import nl.vanwollingen.aoc.util.Puzzle
import kotlin.math.max
import kotlin.math.min

fun main() = Day03.solve()

object Day03 : Puzzle(exampleInput = false) {

    private const val DO = "do()"
    private const val DONT = "don't()"

    override fun parseInput() = input.lines().map { line ->
        val regex = Regex("(mul\\((\\d{1,3}),(\\d{1,3})\\))")
        val results = regex.findAll(line).toList().map { it.groupValues }.map { it[2].toInt() to it[3].toInt() }
        results
    }.flatten()

    override fun part1(): Any {
        val multiplications = parseInput()
        return multiplications.sumOf { it.first * it.second }
    }

    override fun part2(): Any {
        val instructions = input.lines().map { line ->
            val regex = Regex("(mul\\(\\d+,\\d+\\)|do\\(\\)|don't\\(\\))")
            val results =
                regex.findAll(line).toList().map { it.value }
            results
        }.flatten()

        var skip = 0
        var sum = 0
        val mulRegex = Regex("mul\\((\\d+),(\\d+)\\)")

        for (i in instructions.indices) {
            val instruction = instructions[i]

            if (instruction == DO) {
                skip = min(skip + 1, 1)
                continue
            }

            if (instruction == DONT) {
                skip = max(skip - 1, -1)
                continue
            }

            if (skip >= 0) {
                val match = mulRegex.find(instruction)
                match?.let {
                    sum += match.groupValues[1].toInt() * it.groupValues[2].toInt()
                }
                skip = 0
            }
        }
        return sum
    }

}
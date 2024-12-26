package nl.vanwollingen.aoc.aoc2021

import nl.vanwollingen.aoc.util.Puzzle

fun main() = Day02.solve()

object Day02 : Puzzle(exampleInput = false) {
    override fun part1(): Int {
        var depth = 0
        var horizontal = 0

        input.lines()
            .map { it.split(" ") }
            .forEach { instruction ->
                val direction = instruction[0]
                val amount = instruction[1].toInt()
                when (direction) {
                    "forward" -> {
                        horizontal += amount
                    }

                    "down" -> {
                        depth += amount
                    }

                    "up" -> {
                        depth -= amount
                    }
                }
            }
        return depth * horizontal
    }

    override fun part2(): Int {
        var depth = 0
        var horizontal = 0
        var aim = 0

        input.lines()
            .map { it.split(" ") }
            .forEach { instruction ->
                val direction = instruction[0]
                val amount = instruction[1].toInt()
                when (direction) {
                    "forward" -> {
                        horizontal += amount
                        depth += aim * amount
                    }

                    "down" -> {
                        aim += amount
                    }

                    "up" -> {
                        aim -= amount
                    }
                }
            }
        return depth * horizontal
    }
}
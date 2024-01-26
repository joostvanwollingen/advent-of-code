package nl.vanwollingen.aoc.aoc2017

import nl.vanwollingen.aoc.util.Puzzle

fun main() {
    val d2 = Day02()
    d2.solvePart1()
    d2.solvePart2()
}

class Day02(output: Boolean = false) : Puzzle(output) {

    val lines = parseInput()

    override fun parseInput(): List<List<Int>> = input.lines().map { line ->
        line.split(Regex("\\W")).map { number ->
            number.toInt()
        }
    }

    override fun part1() {
        log(lines.sumOf { line -> line.max() - line.min() })
    }

    override fun part2() {
        var sum = 0
        lines.forEach { line ->
            for (i in line.indices) {
                for (j in i + 1..<line.size) {
                    if (line[i] % line[j] == 0) {
                        sum += line[i] / line[j]
                    } else if (line[j] % line[i] == 0) {
                        sum += line[j] / line[i]
                    }
                }
            }
        }
        log(sum)
    }
}
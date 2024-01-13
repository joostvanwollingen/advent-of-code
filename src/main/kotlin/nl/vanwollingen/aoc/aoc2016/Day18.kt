package nl.vanwollingen.aoc.aoc2016

import nl.vanwollingen.aoc.util.Puzzle

fun main() {
    val d18 = Day18()
    d18.solvePart1()
    d18.solvePart2()
}

class Day18(output: Boolean = false) : Puzzle(output) {

    override fun part1() {
        var grid: MutableList<String> = mutableListOf(input)

        for (i in 0..38) {
            grid += findTraps(grid[i])
        }
        log(grid.sumOf { it.count { c -> c == '.' } })
    }

    override fun part2() {
        var grid: MutableList<String> = mutableListOf(input)

        for (i in 0..400000 - 2) {
            grid += findTraps(grid[i])
        }
        log(grid.sumOf { it.count { c -> c == '.' } })
    }

    private fun findTraps(s: String): String {
        val sb = StringBuilder()

        for (i in s.indices) {
            val (l, c, r) = listOf(i - 1, i, i + 1)

            val leftIsTrap = (s.getOrNull(l) ?: '.') == '^'
            val centerIsTrap = s[c] == '^'
            val rightIsTrap = (s.getOrNull(r) ?: '.') == '^'

            if (leftIsTrap && centerIsTrap && !rightIsTrap) sb.append('^')
            else if (!leftIsTrap && centerIsTrap && rightIsTrap) sb.append('^')
            else if (leftIsTrap && !centerIsTrap && !rightIsTrap) sb.append('^')
            else if (!leftIsTrap && !centerIsTrap && rightIsTrap) sb.append('^')
            else sb.append('.')

        }
        return sb.toString()
    }
}
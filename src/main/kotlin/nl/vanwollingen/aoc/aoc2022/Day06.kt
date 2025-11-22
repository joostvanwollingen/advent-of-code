package nl.vanwollingen.aoc.aoc2022

import nl.vanwollingen.aoc.util.Puzzle

fun main() = Day06.solve()

object Day06 : Puzzle(true, false) {

    override fun part1() = findMarker(input)
    override fun part2() = findMarker(input, 14)

    private fun findMarker(dataStream: String, markerSize: Int = 4): Int {
        val history = ArrayDeque<Char>()
        dataStream.forEachIndexed { i, c ->
            if (history.size == markerSize) {
                if (history.all { h -> history.count { it == h } == 1 }) return i
                history.removeFirst()
            }
            history.addLast(c)
        }
        return 0
    }
}
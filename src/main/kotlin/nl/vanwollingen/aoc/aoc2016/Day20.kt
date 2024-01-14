package nl.vanwollingen.aoc.aoc2016

import nl.vanwollingen.aoc.util.Puzzle

fun main() {
    val d20 = Day20(false)
    d20.solvePart1()
    d20.solvePart2()
}

class Day20(output: Boolean = false) : Puzzle(output) {

    private val ranges = parseInput()

    override fun parseInput() = input.lines().map {
        val (start, end) = it.split("-")
        Pair(start.toLong(), end.toLong())
    }.sortedBy { it.first }

    override fun part1() {
        var lowestUnblocked = 0L

        ranges.forEach {
            val start = it.first
            val end = it.second
            if (lowestUnblocked >= start && lowestUnblocked <= end) {
                lowestUnblocked = end + 1
            }
        }
        log(lowestUnblocked)
    }

    override fun part2() {
        val allowed: MutableList<LongRange> = mutableListOf()
        var highestEndSeen = Long.MIN_VALUE
        var count = 0

        ranges.windowed(2, 1) {
            debug("${count++.toString().padStart(4, ' ')}: ", false)

            val (s1: Long, e1: Long) = it.first()
            val (s2: Long, e2: Long) = it.last()

            if (s1 > highestEndSeen && highestEndSeen != Long.MIN_VALUE && highestEndSeen + 1 != s1) {
                debug("Start > highest $it ($highestEndSeen)")
                allowed += LongRange(highestEndSeen, s1)
            }

            if (e1 > highestEndSeen) {
                highestEndSeen = e1

                if (e1 > e2) {
                    debug("L contains R $it ($highestEndSeen)")
                    return@windowed
                }
                if (s2 > s1 && s2 <= e1 && e1 < e2) {
                    debug("Overlap $it ($highestEndSeen)")
                    return@windowed
                } else if (e1 + 1 == s2) {
                    debug("Connected $it ($highestEndSeen)")
                    return@windowed
                } else if (e1 < s2) {
                    debug("Gap $it ($highestEndSeen) (+${s2 - e1 - 1})")
                    allowed += LongRange(e1, s2)
                    highestEndSeen = s2
                    return@windowed
                }
                debug("Miss $it ($highestEndSeen)")
            } else {
                debug("Skip $it ($highestEndSeen)")
                return@windowed
            }
        }
        log(allowed.sumOf { it.count() - 2 })
    }
}
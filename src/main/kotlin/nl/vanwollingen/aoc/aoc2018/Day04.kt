package nl.vanwollingen.aoc.aoc2018

import nl.vanwollingen.aoc.util.Puzzle

fun main() {
    val d4 = Day04()
    d4.solvePart1()
    d4.solvePart2()
}

class Day04 : Puzzle() {

    private val guardDuty = input.lines().sortedBy { it.substring(0, 17) }
    private val sleepers = parseGuardDuties(guardDuty)

    override fun part1() {
        val guardThatSleepsTheMost = sleepers.values.flatten().groupBy { it }.maxBy { it.value.size }.key
        val sleepiestMinute = sleepers.maxBy { it.value.count { g -> g == guardThatSleepsTheMost } }.key
        println(sleepiestMinute * guardThatSleepsTheMost)
    }

    override fun part2() {
        val guardThatSleepsTheMost = sleepers.map { m ->
            m.key to m.value.groupBy { it }.values.maxBy { it.size }
        }.maxBy { it.second.size }
        println(guardThatSleepsTheMost.first * guardThatSleepsTheMost.second.first())
    }

    private fun parseGuardDuties(guardDuties: List<String>): Map<Int, List<Int>> {
        val regex = Regex("^.*#(\\d+).*")
        val sleepers = mutableMapOf<Int, MutableList<Int>>()

        var guard = 0
        var startSleep = 0

        guardDuties.forEach {
            val minute = it.substring(15, 17).toInt()
            val action = it.substring(19, 20)

            when (action) {
                "f" -> {
                    startSleep = minute
                }

                "w" -> {
                    for (i in startSleep..<minute) {
                        val current: MutableList<Int> = sleepers.getOrDefault(i, mutableListOf())
                        current.add(guard)
                        sleepers[i] = current
                    }
                }

                "G" -> {
                    guard = regex.matchEntire(it)?.groupValues?.getOrNull(1)?.toInt() ?: guard
                }
            }
        }
        return sleepers
    }
}
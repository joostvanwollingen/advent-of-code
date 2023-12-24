package nl.vanwollingen.aoc.aoc2023

import nl.vanwollingen.aoc.util.PuzzleInputUtil

fun main() {
    val d6 = Day6()
    val input1 = PuzzleInputUtil.load("day6.input")
    val input2 = PuzzleInputUtil.load("day6.2.input")

    d6.solvePart1(input1)
    d6.solvePart2(input2)
}

class Day6 {
    fun solvePart1(input: String) {
        val times = input.lines().first().split(":")[1].trim().split(" ").filter { it.isNotEmpty() }
        val distance = input.lines().last().split(":")[1].trim().split(" ").filter { it.isNotEmpty() }
        val races = times.zip(distance).map { r -> Race(r.first.toLong(), r.second.toLong(), true) }
        println(races.map { race -> race.numberOfWinningOutcomes }.reduce(Int::times))
    }

    fun solvePart2(input: String) {
        val times = input.lines().first().split(":")[1].trim().split(" ").filter { it.isNotEmpty() }
        val distance = input.lines().last().split(":")[1].trim().split(" ").filter { it.isNotEmpty() }
        val races = times.zip(distance).map { r -> Race(r.first.toLong(), r.second.toLong(), false) }
        println(races[0].calcWithoutStoringResults())
    }

    data class Race(val raceDuration: Long, val previousRecord: Long, val calculateOutcomes: Boolean = false) {
        private val distanceToWin = this.previousRecord + 1
        private val possibleOutcomes = if (calculateOutcomes) calc() else emptyMap()
        private val winningOutcomes = possibleOutcomes.filter { outcome -> outcome.value.second >= distanceToWin }
        val numberOfWinningOutcomes = winningOutcomes.size

        private fun calc(): Map<Long, Pair<Long, Long>> {
            val results: MutableMap<Long, Pair<Long, Long>> = mutableMapOf()
            for (i in 0..raceDuration) {
                results[i] = Pair(i, (raceDuration - i) * i)
            }
            return results
        }

        fun calcWithoutStoringResults(): Long {
            var results = 0L
            for (i in 0..raceDuration) {
                if ((raceDuration - i) * i >= distanceToWin) {
                    results = results.plus(1)
                }
            }
            return results
        }

        override fun toString(): String {
            return "Race(raceDuration=$raceDuration, previousRecord=$previousRecord, distanceToWin=$distanceToWin, possibleOutcomes=$possibleOutcomes, winningOutcomes=$winningOutcomes, numberOfWinningOutcomes=$numberOfWinningOutcomes)"
        }
    }
}


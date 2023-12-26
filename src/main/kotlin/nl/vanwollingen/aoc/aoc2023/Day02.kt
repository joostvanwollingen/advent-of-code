package nl.vanwollingen.aoc.aoc2023

import nl.vanwollingen.aoc.util.PuzzleInputUtil

fun main() {
    val day2 = Day2()
    day2.solvePart1()
    day2.solvePart2()
}

class Day2() {

    private val games = PuzzleInputUtil.parse("2023/day2.input", Game::class.java)
    private val maxRedAllowed = 12
    private val maxGreenAllowed = 13
    private val maxBlueAllowed = 14
    var allowedGameIds = 0
    var powerSum = 0
    fun solvePart1() {
        println(allowedGameIds)
    }

    fun solvePart2() {
        println(powerSum)
    }

    private fun solve() {
        games.forEach {
            if (it.grabs.maxBlue <= maxBlueAllowed && it.grabs.maxGreen <= maxGreenAllowed && it.grabs.maxRed <= maxRedAllowed) {
                allowedGameIds += it.id
            }
            powerSum += it.grabs.power
        }
    }

    data class Game(val input: String) {
        val id = input.split(":").first().split(" ").last().toInt()
        val grabs = Grabs(input.split(":").last().split(";").map { Grab(it) }.toList())
        override fun toString(): String {
            return "Game(id=$id, games (${grabs.grabs.size}) =${grabs})"
        }
    }

    data class Grabs(val grabs: List<Grab>) {
        val maxRed = grabs.maxByOrNull { it.red }?.red ?: 0
        val maxBlue = grabs.maxByOrNull { it.blue }?.blue ?: 0
        val maxGreen = grabs.maxByOrNull { it.green }?.green ?: 0
        val power = maxRed * maxBlue * maxGreen
        override fun toString(): String {
            return "Grabs(grabs=$grabs, maxRed=$maxRed, maxBlue=$maxBlue, maxGreen=$maxGreen, power=$power)"
        }
    }

    data class Grab(val input: String) {
        private val redRegex = Regex("(\\d+) red")
        private val blueRegex = Regex("(\\d+) blue")
        private val greenRegex = Regex("(\\d+) green")

        val red = redRegex.find(input)?.groupValues?.last()?.toInt() ?: 0
        val blue = blueRegex.find(input)?.groupValues?.last()?.toInt() ?: 0
        val green = greenRegex.find(input)?.groupValues?.last()?.toInt() ?: 0

        override fun toString(): String {
            return "\n\tGrab(red=$red, blue=$blue, green=$green)"
        }
    }

    init {
        solve()
    }
}
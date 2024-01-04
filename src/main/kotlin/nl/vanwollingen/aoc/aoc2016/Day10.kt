package nl.vanwollingen.aoc.aoc2016

import nl.vanwollingen.aoc.util.Puzzle
import kotlin.math.max
import kotlin.math.min

fun main() {
    val d10 = Day10(2016, 10)
    d10.solvePart1()
    d10.solvePart2()
}

class Day10(year: Int, day: Int, output: Boolean = false) : Puzzle(year, day, output) {
    private val outcomes = processChips(parseInput())
    override fun solvePart1() {
        log(outcomes.first)
    }

    override fun solvePart2() {
        log(outcomes.second)
    }

    private fun processChips(bots: List<Bot>): Pair<Int, Int> {
        var botsWith2Chips = bots.filter { it.slot1 != null && it.slot2 != null }
        val output: MutableMap<Int, Int> = mutableMapOf()
        var luckyBot = 0

        while (botsWith2Chips.isNotEmpty()) {
            botsWith2Chips.forEach { bot ->
                val lower = min(bot.slot1!!, bot.slot2!!)
                val higher = max(bot.slot1!!, bot.slot2!!)

                if (bot.lowerToOutput) {
                    output[bot.lowerTo] = lower
                } else {
                    val lowerTarget = bots.first { it.id == bot.lowerTo }
                    lowerTarget.receive(lower)
                }

                if (bot.higherToOutput) {
                    output[bot.higherTo] = higher
                } else {
                    val higherTarget = bots.first { it.id == bot.higherTo }
                    higherTarget.receive(higher)
                }

                bot.slot1 = null
                bot.slot2 = null
            }
            botsWith2Chips = bots.filter { it.slot1 != null && it.slot2 != null }
            botsWith2Chips.filter { it.slot1 == 61 && it.slot2 == 17 || it.slot1 == 17 && it.slot2 == 61 }.let { if (it.isNotEmpty()) luckyBot = it[0].id }
        }
        return luckyBot to (output[0]!! * output[1]!! * output[2]!!)
    }

    override fun parseInput(): List<Bot> {
        val bots: MutableList<Bot> = mutableListOf()
        val startingValues: MutableList<Pair<Int, Int>> = mutableListOf()
        val startingRegex = Regex("value (\\d+) goes to bot (\\d+)")

        input.lines().forEach { line ->
            if (line.startsWith("value")) {
                val starting = startingRegex.findAll(line).toList().first().groupValues
                startingValues += starting[1].toInt() to starting[2].toInt()
            } else bots += Bot.fromString(line)
        }

        startingValues.forEach { v ->
            val bot = bots.first { it.id == v.second }
            bot.receive(v.first)
        }

        return bots
    }

    data class Bot(val id: Int, var slot1: Int? = null, var slot2: Int? = null, val lowerTo: Int, val higherTo: Int, val lowerToOutput: Boolean, val higherToOutput: Boolean) {
        fun receive(chip: Int) {
            if (slot1 == null) slot1 = chip
            else if (slot2 == null) slot2 = chip
            else throw Exception("Already has 2 chips: $this")
        }

        companion object {
            fun fromString(input: String): Bot {
                val matches = Regex("bot (\\d+) gives low to (bot|output) (\\d+) and high to (bot|output) (\\d+)").findAll(input).toList().first().groupValues
                return Bot(id = matches[1].toInt(), slot1 = null, slot2 = null, lowerTo = matches[3].toInt(), higherTo = matches[5].toInt(), lowerToOutput = matches[2] == "output", higherToOutput = matches[4] == "output")
            }
        }
    }
}
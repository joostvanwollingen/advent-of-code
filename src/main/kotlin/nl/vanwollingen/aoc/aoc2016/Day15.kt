package nl.vanwollingen.aoc.aoc2016

import nl.vanwollingen.aoc.util.Puzzle

fun main() {
    val d15 = Day15()
    d15.part1()
    d15.part2()
}

class Day15(output: Boolean = false) : Puzzle(output) {

    private val discs = parseInput()
    override fun parseInput(): List<Disc> {
        val parseLineRegex = Regex("Disc #(\\d) has (\\d+) positions; at time=0, it is at position (\\d+).")
        return input.lines().mapIndexed { i, line ->
            val parsedLine = parseLineRegex.findAll(line).toList().first().groupValues
            Disc(parsedLine[1].toInt(), parsedLine[2].toInt(), parsedLine[3].toInt())
        }
    }

    override fun part1() {
        log(synchronizeDiscs(discs))
    }

    override fun part2() {
        val extraDisc = parseInput().plus(Disc(7, 11, 0))
        log(synchronizeDiscs(extraDisc))
    }

    private fun synchronizeDiscs(discs: List<Disc>): Int {
        var t = 0
        while (true) {
            t++
            discs.forEach { disc ->
                disc.tick()
            }
            if (discs.all { disc -> disc.positionAfterTicks(disc.id) == 0 }) {
                return t
            }
        }
    }

    data class Disc(val id: Int, val numberOfPositions: Int, val startPosition: Int, var currentPosition: Int = startPosition) {
        fun positionAfterTicks(numberOfTicks: Int): Int {
            var ticks = numberOfTicks
            var newPosition = currentPosition
            while (ticks > 0) {
                newPosition += 1
                if (newPosition == numberOfPositions) newPosition = 0
                ticks--
            }
            return newPosition
        }

        fun tick(): Int {
            currentPosition = positionAfterTicks(1)
            return currentPosition
        }
    }
}
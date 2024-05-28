package nl.vanwollingen.aoc.aoc2017

import nl.vanwollingen.aoc.util.Puzzle

fun main() {
    val d13 = Day13()
    d13.solvePart1()
}

class Day13(output: Boolean = false) : Puzzle(output) {

    private val layers = parseInput().associateBy { it.depth }
    override fun parseInput(): List<Layer> {
        return input.lines().map { l -> Layer.fromString(l) }
    }

    override fun part1() {
        val lastLayer = layers.maxBy { it.value.depth }.value.depth
        var packetLayer = -1
        var severity = 0

        while (packetLayer < lastLayer) {
            packetLayer++

            val currentLayer = layers[packetLayer]
            if (currentLayer != null && currentLayer.scanner == 1) {
                severity += currentLayer.depth * currentLayer.range
            }

            layers.forEach { it.value.moveScanner() }
        }
        log(severity)
    }

    override fun part2() {
        TODO("Not yet implemented")
    }

    data class Layer(val depth: Int, val range: Int, var scanner: Int = 1) {
        private var scannerDirection = DOWN
        fun moveScanner() {
            if (scanner == range) {
                scannerDirection = UP
            }

            if (scanner == 1) {
                scannerDirection = DOWN
            }
            scanner += scannerDirection
        }

        companion object {
            const val DOWN: Int = 1
            const val UP: Int = -1
            fun fromString(input: String): Layer {
                input.split(": ").also { return Layer(it.first().toInt(), it.last().toInt()) }
            }
        }
    }
}
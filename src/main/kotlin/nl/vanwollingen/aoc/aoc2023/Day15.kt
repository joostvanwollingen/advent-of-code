package nl.vanwollingen.aoc.aoc2023

import nl.vanwollingen.aoc.util.PuzzleInputUtil

fun main() {
    val input = PuzzleInputUtil.load("2023/day15.input")

    Day15().solvePart1(input)
    Day15().solvePart2(input)
}

class Day15 {
    fun solvePart2(input: String) {
        val b: MutableMap<Long, MutableMap<String, Long>> = mutableMapOf()

        val lenses = input.split(",").map { Regex("([a-z]+)([-=])+([0-9]?)").findAll(it) }.map { it.first() }.map { lens ->
            Lens(lens.groupValues[1], lens.groupValues[2], lens.groupValues.getOrNull(3)?.toLongOrNull())
        }

        val h: Map<String, Long> = lenses.distinctBy { it.name }.associate { it.name to hashString(it.name) }

        lenses.forEach { lens ->
            if (b[h[lens.name]!!].isNullOrEmpty()) b[h[lens.name]!!] = mutableMapOf()

            if (lens.operation == "-") {
                if (b[h[lens.name]]!!.containsKey(lens.name)) {
                    b[h[lens.name]!!] = b[h[lens.name]]!!.minus(lens.name).toMutableMap()
                }
            }
            if (lens.operation == "=") {
                if (b[h[lens.name]]?.containsKey(lens.name) == true) {
                    b[h[lens.name]]!!.replace(lens.name, lens.focalLength!!)
                } else {
                    b[h[lens.name]]?.put(lens.name, lens.focalLength!!)
                }
            }
        }
        println(b.entries.flatMap { entry ->
            val boxNumber: Long = entry.key + 1
            entry.value.entries.mapIndexed { i, it -> boxNumber.times(it.value).times(i + 1) }
        }.sum())
    }

    fun solvePart1(input: String) {
        val toBeHashed = input.split(",")
        println(toBeHashed.sumOf { hashString(it) })
    }

    data class Lens(val name: String, val operation: String, val focalLength: Long?)

    private fun hashString(input: String): Long = input.toCharArray().fold(0L) { acc, char -> (acc + char.code).times(17).rem(256) }
}
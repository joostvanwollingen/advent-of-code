package nl.vanwollingen.aoc.aoc2016

import nl.vanwollingen.aoc.util.Puzzle

fun main() {
    val d11 = Day11()
    d11.part1()
}

class Day11(output: Boolean = false) : Puzzle(output) {

    private val combinations = parseInput()
    override fun part1() {
        var elevator: Int = 1
//        combinations.first { it.element == "plutonium" }.generator = 3
        combinations.print(elevator)

        log(combinations.checkRadiation())
    }

    override fun part2() {
        TODO("Not yet implemented")
    }

    override fun parseInput(): List<RtgChipCombination> {
        val regex = Regex("([\\w+]+)(?:-compatible)? (generator|microchip)+")
        val combinations: MutableList<RtgChipCombination> = mutableListOf()
        var floor = 1
        input.lines().forEach { line ->
            val matches = regex.findAll(line)
            matches.forEach { match ->
                val element = match.groupValues[1]
                val type = match.groupValues[2]
                val combination = combinations.firstOrNull { c -> c.element == element }
                        ?: RtgChipCombination(element).also {
                            combinations += it
                        }
                combination.setFloor(type, floor)
            }
            floor++
        }
        return combinations
    }

    data class RtgChipCombination(val element: String, var generator: Int = 0, var microchip: Int = 0) {
        fun setFloor(type: String, floor: Int) {
            if (type == "generator") {
                generator = floor
            } else {
                microchip = floor
            }
        }
    }

    private fun List<RtgChipCombination>.print(elevator: Int) {
        val minFloor = 1
        val maxFloor = 4
        for (i in maxFloor downTo minFloor) {
            print("F$i ${if (elevator == i) "E" else "."}")

            val generatorsOnFloor = this.filter { c -> c.generator == i }.map { "${it.element[0].uppercase()}G" }
            val chipsOnFloor = this.filter { c -> c.microchip == i }.map { "${it.element[0].uppercase()}M" }
            generatorsOnFloor.plus(chipsOnFloor).sorted().forEach { print("\t$it\t") }

            println("")
        }
        println("")
    }

    private fun List<RtgChipCombination>.checkRadiation(): Boolean {
        val minFloor = 1
        val maxFloor = 4
        var chipsWithoutGenerator: List<RtgChipCombination> = mutableListOf()
        var generatorWithoutChips: List<RtgChipCombination> = mutableListOf()
        for (i in maxFloor downTo minFloor) {
            chipsWithoutGenerator = this.filter { c -> c.microchip == i && c.generator != i }
            generatorWithoutChips = this.filter { c -> c.generator == i && c.microchip != i }
            chipsWithoutGenerator.forEach { exposedChip ->
                val bad = generatorWithoutChips.filter { it.generator == exposedChip.microchip && it.element != exposedChip.element }
                if(bad.isNotEmpty()) return false
            }
        }
        return true
    }
}

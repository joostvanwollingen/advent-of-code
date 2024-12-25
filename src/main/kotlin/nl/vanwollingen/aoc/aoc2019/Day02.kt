package nl.vanwollingen.aoc.aoc2019

import nl.vanwollingen.aoc.util.Puzzle

fun main() = Day02.solve()

object Day02 : Puzzle(exampleInput = false) {

    private fun loadIntCodes() = input.split(",").map(String::toInt).toMutableList()

    override fun part1() = loadIntCodes()
        .apply {
            this[1] = 12
            this[2] = 2
        }
        .run { runProgram(this) }


    override fun part2(): Any {
        val outputRequired = 19690720
        var i =0
        for (noun in 0..99) {
            for (verb in 99 downTo 0) {
                val intCodes = loadIntCodes()
                intCodes[1] = noun
                intCodes[2] = verb
                if (runProgram(intCodes) == outputRequired) return 100 * noun + verb
            }
        }
        return 0
    }

    private fun runProgram(intCodes: MutableList<Int>): Int {
        var i = 0
        while (true) {
            val opcode = intCodes[i]
            when (opcode) {
                1 -> {
                    val addition = intCodes[intCodes[i + 1]] + intCodes[intCodes[i + 2]]
                    val target = intCodes[i + 3]
                    intCodes[target] = addition
                }

                2 -> {
                    val multiplication = intCodes[intCodes[i + 1]] * intCodes[intCodes[i + 2]]
                    val target = intCodes[i + 3]
                    intCodes[target] = multiplication
                }

                99 -> {
                    return intCodes[0]
                }
            }
            i += 4
        }
    }
}
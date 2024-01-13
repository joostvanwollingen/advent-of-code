package nl.vanwollingen.aoc.aoc2015

import nl.vanwollingen.aoc.util.Puzzle

fun main() {
    val d23 = Day23()
    d23.solvePart1()
    d23.solvePart2()
}

class Day23() : Puzzle() {

    override fun part1() {
        val a = Register("a", 0)
        val b = Register("b", 0)
        executeInstructions(a, b, input.lines())
        println(b)
    }

    override fun part2() {
        val a = Register("a", 1)
        val b = Register("b", 0)
        executeInstructions(a, b, input.lines())
        println(b)
    }

    private fun executeInstructions(a: Register, b: Register, instructions: List<String>) {
        var i = 0
        while (i < input.lines().size) {
            val f = instructions[i].split(", ").flatMap { it.split(" ") }

            val (instruction, register, offset: Int?) = parseInstruction(f, a, b)

            when (instruction) {
                "hlf" -> {
                    register.value /= 2
                    i++
                    continue
                }

                "tpl" -> {
                    register.value *= 3
                    i++
                    continue
                }

                "inc" -> {
                    register.value += 1
                    i++
                    continue
                }

                "jmp" -> {
                    i += offset!!
                    continue
                }

                "jie" -> {
                    if (register.value % 2 == 0L) i += offset!! else i++
                    continue
                }

                "jio" -> {
                    if (register.value == 1L) i += offset!! else i++
                    continue
                }
            }
        }
    }

    private fun parseInstruction(f: List<String>, a: Register, b: Register): Triple<String, Register, Int?> {
        var instruction = f[0]
        var register = if (f[1] == "a") a else b
        var offset: Int? = null

        if (listOf("jie", "jio").contains(instruction)) {
            register = if (f[1] == "a") a else b
            offset = f[2].toInt()
        }

        if (instruction == "jmp") {
            offset = f[1].toInt()
        }
        return Triple(instruction, register, offset)
    }

    data class Register(val name: String, var value: Long)
}
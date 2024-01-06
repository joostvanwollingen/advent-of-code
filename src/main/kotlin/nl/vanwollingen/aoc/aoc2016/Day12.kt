package nl.vanwollingen.aoc.aoc2016

import nl.vanwollingen.aoc.util.Puzzle

fun main() {
    val d12 = Day12(2016, 12)
    d12.solvePart1()
    d12.solvePart2()
}

class Day12(year: Int, day: Int, output: Boolean = false) : Puzzle(year, day, output) {

    var a = Register("a", 0)
    var b = Register("b", 0)
    var c = Register("c", 0)
    var d = Register("d", 0)

    private val registers: Map<String, Register> = mapOf("a" to a, "b" to b, "c" to c, "d" to d)

    private fun processInstruction(instruction: String, i: Int): Int {
        if (instruction.startsWith("inc")) {
            val currentRegister = instruction[4].toString()

            registers[currentRegister]!!.value += 1
        }

        if (instruction.startsWith("dec")) {
            val currentRegister = instruction[4].toString()

            registers[currentRegister]!!.value -= 1
        }

        if (instruction.startsWith("cpy")) {
            val source = instruction.split(" ")[1]
            val target = instruction.split(" ")[2]
            val sourceVal = if (registers.keys.contains(source)) registers[source]!!.value else source.toInt()

            registers[target]!!.value = sourceVal
        }

        if (instruction.startsWith("jnz")) {
            val register = instruction.split(" ")[1]
            val jumpValue = instruction.split(" ")[2].toInt()
            val checkVal = if (registers.keys.contains(register)) registers[register]!!.value else register.toInt()

            if (checkVal != 0) return i + jumpValue
        }
        return i + 1
    }

    override fun solvePart1() {
        val lines = input.lines()
        var i = 0

        while (i < lines.size) {
            i = processInstruction(lines[i], i)
        }

        log(a)
    }

    override fun solvePart2() {
        val lines = input.lines()
        var i = 0

        c.value = 1

        while (i < lines.size) {
            i = processInstruction(lines[i], i)
        }

        log(a)
    }

    data class Register(val name: String, var value: Int)
}
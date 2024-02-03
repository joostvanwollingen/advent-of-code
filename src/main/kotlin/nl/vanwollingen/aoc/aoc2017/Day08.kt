package nl.vanwollingen.aoc.aoc2017

import nl.vanwollingen.aoc.util.Puzzle
import kotlin.math.max

fun main() {
    val d8 = Day08()
    d8.solvePart1()
    d8.solvePart2()
}

class Day08(output: Boolean = false) : Puzzle(output) {

    private val instructions = parseInput(input)
    override fun part1() {
        val registers = mutableMapOf<String, Int>()
        instructions.forEach { interpretInstruction(it, registers) }
        registers.remove("highest")
        log(registers.maxBy { it.value })
    }

    override fun part2() {
        val registers = mutableMapOf<String, Int>()
        instructions.forEach { interpretInstruction(it, registers) }
        log(registers["highest"]!!)
    }

    private fun interpretInstruction(instruction: Instruction, registers: MutableMap<String, Int>) {
        val register = registers.getOrDefault(instruction.register, 0)
        val leftOperand = registers.getOrDefault(instruction.leftOperand, 0)
        var doOperation = false

        when (instruction.comparison) {
            "==" -> if (leftOperand == instruction.rightOperand) doOperation = true
            ">=" -> if (leftOperand >= instruction.rightOperand) doOperation = true
            "<=" -> if (leftOperand <= instruction.rightOperand) doOperation = true
            ">" -> if (leftOperand > instruction.rightOperand) doOperation = true
            "<" -> if (leftOperand < instruction.rightOperand) doOperation = true
            "!=" -> if (leftOperand != instruction.rightOperand) doOperation = true
        }

        if (doOperation) {
            registers[instruction.register] =
                if (instruction.operation == "inc") register + instruction.amount else register - instruction.amount
            registers["highest"] =
                max(registers.getOrDefault("highest", 0), registers.getOrDefault(instruction.register, 0))
        }
    }

    fun parseInput(input: String): List<Instruction> = input.lines().map { Instruction.fromString(it) }

    data class Instruction(
        val register: String,
        val operation: String,
        val amount: Int,
        val leftOperand: String,
        val comparison: String,
        val rightOperand: Int
    ) {
        companion object {
            fun fromString(input: String): Instruction {
                val split = input.split(" ")
                val register = split[0]
                val operation = split[1]
                val amount = split[2].toInt()
                val leftOperand = split[4]
                val comparison = split[5]
                val rightOperand = split[6].toInt()
                return Instruction(register, operation, amount, leftOperand, comparison, rightOperand)
            }
        }
    }
}
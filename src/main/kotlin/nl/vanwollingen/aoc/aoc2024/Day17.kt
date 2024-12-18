package nl.vanwollingen.aoc.aoc2024

import nl.vanwollingen.aoc.util.Puzzle

fun main() = Day17.solve()

object Day17 : Puzzle(exampleInput = false) {

    override fun parseInput(): Pair<Triple<Long, Long, Long>, List<Long>> {
        val (registerStrings, opCodesString) = input.split("\n\n")
        val (registerA, registerB, registerC) = registerStrings.lines().map { it.split(": ")[1].toLong() }
        val opCodes = opCodesString.split(": ")[1].split(",").map { it.toLong() }
        return Triple(registerA, registerB, registerC) to opCodes
    }

    override fun part1(): Any {
        val (registers, opcodes) = parseInput()
        return runProgram(registers.first, opcodes).joinToString(",")
    }

    //https://github.com/tymscar/Advent-Of-Code/blob/master/2024/kotlin/src/main/kotlin/com/tymscar/day17/part2/part2.kt
    override fun part2(): Any {
        val (_, opcodes) = parseInput()
        val target = opcodes.reversed()

        var possibleStartingValues = listOf(0L)
        target.forEach { instruction ->
            possibleStartingValues = possibleStartingValues.flatMap { value ->
                findPossibleStarting(value, instruction, opcodes)
            }
        }

        return possibleStartingValues.min()
    }

    private fun findPossibleStarting(a: Long, expectedOutput: Long, opcodes: List<Long>): List<Long> {
        val possibleA = mutableListOf<Long>()
        for (contender in 0L..7L) {
            val contenderRegisterA = (a shl 3) or contender
            if (runProgram(contenderRegisterA, opcodes).first() == expectedOutput) {
                possibleA.add(contenderRegisterA)
            }
        }
        return possibleA
    }

    private fun runProgram(a: Long, opcodes: List<Long>): List<Long> {
        var registerA = a
        var registerB = 0L
        var registerC = 0L
        var instructionPointer = 0
        val out = mutableListOf<Long>()

        while (instructionPointer + 1 < opcodes.size) {
            val instruction = opcodes[instructionPointer]
            val literalOperand = opcodes[instructionPointer + 1]
            val comboOperand = getComboOperand(literalOperand, registerA, registerB, registerC)

            when (instruction) {
                0L -> registerA = adv(registerA, comboOperand)
                1L -> registerB = bxl(registerB, literalOperand)
                2L -> registerB = bst(comboOperand)
                3L -> instructionPointer = jnz(registerA, literalOperand, instructionPointer).toInt()
                4L -> registerB = bxl(registerB, registerC)
                5L -> out.add(bst(comboOperand))
                6L -> registerB = adv(registerA, comboOperand)
                7L -> registerC = adv(registerA, comboOperand)
            }

            if (instruction != 3L) instructionPointer += 2
        }
        return out
    }

    private fun getComboOperand(operand: Long, a: Long, b: Long, c: Long): Long = when (operand) {
        0L -> 0
        1L -> 1
        2L -> 2
        3L -> 3
        4L -> a
        5L -> b
        6L -> c
        7L -> throw Error("received 7 combo operand")
        else -> throw Error("unknown combo operand")
    }

    private fun jnz(registerA: Long, literalOperand: Long, instructionPointer: Int): Long {
        if (registerA == 0L) return instructionPointer + 2L
        return literalOperand
    }

    private fun bxl(a: Long, b: Long): Long = a xor b

    private fun bst(a: Long): Long = a % 8

    private fun adv(numerator: Long, denominator: Long): Long = numerator / 1.shl(denominator.toInt())
}
package nl.vanwollingen.aoc.infi.aoc2024

import nl.vanwollingen.aoc.util.Puzzle
import java.util.LinkedList

fun main() = Y2024Infi().solve()

//WC8N85QQ8649
class Y2024Infi : Puzzle(exampleInput = false) {
    override fun part1(): Int {
        val instructions: Map<Int, Pair<String, String?>> = getInstructions()

        var sum = 0
        val map: MutableList<Pair<Triple<Int, Int, Int>, Int>> = mutableListOf()
        for (x in 0..29) {
            for (y in 0..29) {
                for (z in 0..29) {
//                    map.add(Triple(x, y, z) to instructions(x, y, z, operations))
                    sum += calculateSnowContents(x, y, z, instructions)
                }
            }
        }
//        return map.sumOf { it.second }
        return sum
    }


    override fun part2(): Any {
        TODO("Not yet implemented")
    }

    private fun calculateSnowContents(x: Int, y: Int, z: Int, instructions: Map<Int, Pair<String, String?>>): Int {
        var counter = 0
        val stack = LinkedList<Int>()

        while (true) {
            val (operation: String, operand: String?) = instructions[counter] ?: throw Error("fail")

            when (operation) {
                "push" -> {
                    stack.push(determineOperand(operand, x, y, z))
                    counter++
                }

                "jmpos" -> {
                    if (stack.poll() >= 0) {
                        counter += determineOperand(operand, x, y, z)
                    }
                    counter++
                }

                "ret" -> {
                    return stack.peek()
                }

                "add" -> {
                    stack.push(stack.poll() + stack.poll())
                    counter++
                }
            }
        }
    }

    private fun determineOperand(operandString: String?, x: Int, y: Int, z: Int): Int {
        val operand = when (operandString?.lowercase()) {
            "x" -> x
            "y" -> y
            "z" -> z
            else -> operandString?.toInt()
        }
        return operand!!
    }

    private fun getInstructions(): Map<Int, Pair<String, String?>> {
        val operations: Map<Int, Pair<String, String?>> = input.lines().mapIndexed { y, line ->
            val split = line.split(" ")
            val operation = split[0]
            val operand: String? = if (split.size > 1) split[1] else null
            y to (operation to operand)
        }.toMap()
        return operations
    }
}
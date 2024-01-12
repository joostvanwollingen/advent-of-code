package nl.vanwollingen.aoc.aoc2015

import nl.vanwollingen.aoc.util.Puzzle
import nl.vanwollingen.aoc.util.grid.Point
import kotlin.math.max

fun main() {
    val d6 = Day06()
    d6.part1()
    d6.part2()
}

class Day06() : Puzzle() {

    private val instructions = parseInput()

    override fun parseInput(): List<InstructionSet> {
        val instruction = Regex("(turn on|turn off|toggle) (.*) through (.*)")
        return input.lines().map { line ->
            val matches = instruction.findAll(line).first()
            val ins = matches.groups[1]!!.value
            val (startX, startY) = matches.groups[2]!!.value.split(",")
            val (endX, endY) = matches.groups[3]!!.value.split(",")
            InstructionSet(Instruction.from(ins), Point(startY.toInt(), startX.toInt()), Point(endY.toInt(), endX.toInt()))
        }
    }

    override fun part1() {
        val matrix: MutableSet<Point> = mutableSetOf()
        instructions.forEach { instructionSet ->
            for (x in instructionSet.start.x..instructionSet.end.x) {
                for (y in instructionSet.start.y..instructionSet.end.y) {
                    when (instructionSet.instruction) {
                        Instruction.TURN_ON -> {
                            matrix.add(Point(y, x))
                        }

                        Instruction.TURN_OFF -> {
                            matrix.remove(Point(y, x))
                        }

                        Instruction.TOGGLE -> {
                            if (matrix.contains(Point(y, x))) {
                                matrix.remove(Point(y, x))
                            } else {
                                matrix.add(Point(y, x))
                            }
                        }
                    }
                }
            }
        }
        println(matrix.size)
    }

    override fun part2() {
        val matrix: MutableMap<Point, Int> = mutableMapOf()
        instructions.forEach { instructionSet ->
            for (x in instructionSet.start.x..instructionSet.end.x) {
                for (y in instructionSet.start.y..instructionSet.end.y) {
                    val p = Point(y, x)
                    if (matrix[p] == null) matrix[p] = 0
                    when (instructionSet.instruction) {
                        Instruction.TURN_ON -> {
                            matrix[p] = matrix[p]!! + 1
                        }

                        Instruction.TURN_OFF -> {
                            matrix[p] = max(matrix[p]!! - 1, 0)
                        }

                        Instruction.TOGGLE -> {
                            matrix[p] = matrix[p]!! + 2
                        }
                    }
                }
            }
        }
        println(matrix.values.sum())
    }

    data class InstructionSet(val instruction: Instruction, val start: Point, val end: Point)
    enum class Instruction(val value: String) {
        TURN_ON("turn on"), TURN_OFF("turn off"), TOGGLE("toggle");

        companion object {
            fun from(ins: String): Instruction = when (ins) {
                "turn on" -> TURN_ON
                "turn off" -> TURN_OFF
                "toggle" -> TOGGLE
                else -> throw Exception("Failed to map $ins")
            }
        }
    }
}
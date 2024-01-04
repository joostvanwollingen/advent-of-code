package nl.vanwollingen.aoc.aoc2016

import nl.vanwollingen.aoc.util.Puzzle

fun main() {
    val d8 = Day08(2016, 8, true)
    d8.solvePart1()
}

class Day08(year: Int, day: Int, output: Boolean = false) : Puzzle(year, day, output) {
    var grid = Array(6) { IntArray(50) }
    private val instructions = parseInput()
    override fun parseInput(): List<Instruction> = input.lines().map { Instruction.fromString(it) }

    override fun solvePart1() {
        instructions.forEach {
            processInstruction(it)
            grid.forEach { r ->
                debug(r.joinToString(""))
            }
            println("")
        }
        grid.forEach { debug(it.joinToString("")) }
        log(grid.sumOf { it.count { r -> r == 1 } })
    }

    private fun processInstruction(instruction: Instruction) {
        when (instruction.command) {
            Instruction.CommandType.RECT -> {
                val rowRange = 0..<instruction.b
                val columnRange = 0..<instruction.a

                for (row in rowRange) {
                    for (column in columnRange) {
                        grid[row][column] = 1
                    }
                }
            }

            Instruction.CommandType.ROTATE_ROW -> {
                val row = instruction.a
                val newRow = IntArray(grid[row].size)
                for (i in grid[row].indices) {
                    if (grid[row][i] == 1) {
                        grid[row][i] = 0
                        val newColumn: Int = if (i + instruction.b < grid[row].size) i + instruction.b else i + instruction.b - grid[row].size
                        newRow[newColumn] = 1
                    }
                }
                grid[row] = newRow
            }

            Instruction.CommandType.ROTATE_COLUMN -> {
                var column = instruction.a
                var newColumn = IntArray(grid.size)
                for (i in grid.indices) {
                    if (grid[i][column] == 1) {
                        grid[i][column] = 0
                        val newRow: Int = if (i + instruction.b < grid.size) i + instruction.b else i + instruction.b - grid.size
                        newColumn[newRow] = 1
                    }
                }
                for (i in grid.indices) {
                    grid[i][column] = newColumn[i]
                }
            }
        }
    }

    override fun solvePart2() {
        TODO("Not yet implemented")
    }

    data class Instruction(val command: CommandType, val a: Int, val b: Int) {
        enum class CommandType {
            RECT, ROTATE_ROW, ROTATE_COLUMN
        }

        companion object {
            fun fromString(input: String): Instruction {
                if (input.contains("rect")) {
                    val parsed = Regex("(\\d+)x(\\d+)").findAll(input).first().groupValues
                    return Instruction(CommandType.RECT, parsed[1].toInt(), parsed[2].toInt())
                }
                if (input.contains("row")) {
                    val parsed = Regex("(\\d+) by (\\d+)").findAll(input).first().groupValues
                    return Instruction(CommandType.ROTATE_ROW, parsed[1].toInt(), parsed[2].toInt())
                }
                if (input.contains("column")) {
                    val parsed = Regex("(\\d+) by (\\d+)").findAll(input).first().groupValues
                    return Instruction(CommandType.ROTATE_COLUMN, parsed[1].toInt(), parsed[2].toInt())
                }
                throw Exception("Couldn't parse input")
            }
        }
    }
}
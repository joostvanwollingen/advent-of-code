package nl.vanwollingen.aoc.aoc2025

import nl.vanwollingen.aoc.util.Puzzle

fun main() = Day06.solve()

object Day06 : Puzzle(true, false) {

    private val operations = input.lines().last().trim().split(Regex("\\s+"))
    private val operands = input.lines().map { it.trim().split(Regex("\\s+")) }.dropLast(1)

    override fun part1() = operations.indices.sumOf { col ->
        val op = operations[col]
        val columnValues = operands.map { it[col].toLong() }
        applyOperation(columnValues, op)
    }

    override fun part2(): Long {
        val transposed = transpose(input.lines().dropLast(1))
        return transposed.indices.sumOf { col ->
            val op = operations[col]
            val values = transposed[col].map { it.trim().toLong() }
            applyOperation(values, op)
        }
    }

    private fun applyOperation(values: List<Long>, op: String): Long =
        when (op) {
            "+" -> values.sum()
            "*" -> values.fold(1L) { acc, v -> acc * v }
            else -> error("Unknown operation: $op")
        }

    private fun transpose(rawLines: List<String>): List<List<String>> {
        val width = rawLines.maxOf { it.length }
        val result = mutableListOf<List<String>>()
        val buffer = mutableListOf<String>()
        var line = ""

        for (c in 0 until width) {
            for (i in rawLines.indices) {
                line += rawLines[i].getOrNull(c) ?: ""
                if (i == rawLines.size - 1) {
                    if (line.isNotBlank()) {
                        buffer += line
                        line = ""
                    } else {
                        result.add(buffer.toList())
                        buffer.clear()
                    }
                }
            }
        }
        result.add(buffer.toList())
        return result
    }
}
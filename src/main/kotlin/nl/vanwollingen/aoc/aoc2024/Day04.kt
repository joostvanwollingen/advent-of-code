package nl.vanwollingen.aoc.aoc2024

import nl.vanwollingen.aoc.util.Puzzle

fun main() = Day04.solve()

object Day04 : Puzzle(exampleInput = false) {

    private val XMAS = parseInput()

    override fun parseInput(): Map<Int, Map<Int, Char>> {
        val grid: MutableMap<Int, Map<Int, Char>> = mutableMapOf()
        var y = 0
        input.lines().forEach { line ->
            val rowMap = mutableMapOf<Int, Char>()
            var x = 0
            line.forEach { cell ->
                rowMap[x++] = cell
            }
            grid[y++] = rowMap
        }
        return grid
    }

    override fun part1(): Int {
        val rowCount = XMAS.values.sumOf {
            val line = it.values.joinToString("")
            countOccurrences(line, "XMAS") + countOccurrences(line, "SAMX")
        }

        val columnCount = XMAS.values.indices.sumOf { colNum ->
            val column = XMAS.values.map { it.values.elementAt(colNum) }.joinToString("")
            countOccurrences(column, "XMAS") + countOccurrences(column, "SAMX")
        }

        val diagonals = getAllDiagonals(XMAS).sumOf { diagional ->
            countOccurrences(diagional, "XMAS") + countOccurrences(diagional, "SAMX")
        }

        return diagonals + rowCount + columnCount
    }

    override fun part2(): Int {
        var count = 0
        for (y in XMAS.keys.toIntArray()) {
            for (x in XMAS[y]!!.keys.toIntArray()) {
                val topLeft = XMAS[y]?.get(x)?.toString()

                if (topLeft in listOf("S", "M")) {
                    val topRight = XMAS[y]?.get(x + 2)?.toString()
                    val centre = XMAS[y + 1]?.get(x + 1)?.toString()
                    val bottomLeft = XMAS[y + 2]?.get(x)?.toString()
                    val bottomRight = XMAS[y + 2]?.get(x + 2)?.toString()

                    if (listOf(topLeft, topRight, centre, bottomRight, bottomLeft).none { it == "X" || it == null }) {
                        val downRight = topLeft + centre + bottomRight
                        val upRight = bottomLeft + centre + topRight
                        if ((downRight == "SAM" || downRight == "MAS") && (upRight == "SAM" || upRight == "MAS")) count++
                    }
                }
            }
        }
        return count
    }

    private fun countOccurrences(mainString: String, subString: String): Int {
        return mainString.windowed(subString.length).count { it == subString }
    }

    //ChatGPT'ed :(
    private fun getAllDiagonals(grid: Map<Int, Map<Int, Char>>): List<String> {
        val n = grid.keys.size // Assume grid is a square, so size of outer map is the size of the grid.
        val primaryDiagonals = mutableListOf<String>()
        val secondaryDiagonals = mutableListOf<String>()

        // Collect primary-style diagonals
        for (offset in -(n - 1) until n) {
            val diagonal = StringBuilder()
            for (i in 0 until n) {
                val j = i + offset
                if (j in 0 until n) {
                    grid[i]?.get(j)?.let { diagonal.append(it) }
                }
            }
            if (diagonal.isNotEmpty()) primaryDiagonals.add(diagonal.toString())
        }

        // Collect secondary-style diagonals
        for (offset in -(n - 1) until n) {
            val diagonal = StringBuilder()
            for (i in 0 until n) {
                val j = (n - 1) - i + offset
                if (j in 0 until n) {
                    grid[i]?.get(j)?.let { diagonal.append(it) }
                }
            }
            if (diagonal.isNotEmpty()) secondaryDiagonals.add(diagonal.toString())
        }

        return primaryDiagonals + secondaryDiagonals
    }
}
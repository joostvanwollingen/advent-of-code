package nl.vanwollingen.aoc.aoc2022

import nl.vanwollingen.aoc.util.Puzzle

fun main() = Day05.solve()

object Day05 : Puzzle(exampleInput = false, printDebug = false) {

    override fun parseInput(): Pair<MutableMap<Int, MutableList<Char>>, List<Move>> {
        val (crateLines, moveLines) = input.split("\n\n")
        val numberOfColumns = (crateLines.lines().max().length + 1) / 4
        val columnIndexes = generateSequence(2) { it + 4 }.take(numberOfColumns).toList()
        val crates = mutableMapOf<Int, MutableList<Char>>()

        crateLines.lines().dropLast(1).forEach { line ->
            for (i in 0..<numberOfColumns) {
                val index = columnIndexes[i]
                if (line.getOrNull(index) != null && line[index] != ' ') {
                    crates[i] = (crates.getOrDefault(i, mutableListOf()) + line[index - 1]).toMutableList()
                }
            }
        }

        return crates to moveLines.lines().map { Move.fromString(it) }
    }


    override fun part1(): String {
        val (cratez, moves) = parseInput()
        val crates = cratez.toSortedMap()

        for (move in moves) {
            debug(crates)

            val cratesToMove =
                crates[move.source]?.take(move.amount)?.reversed() ?: throw Error("invalid crate stack index")
            crates[move.target]?.addAll(0, cratesToMove)
            crates[move.source] = crates[move.source]?.subList(move.amount, crates[move.source]?.size!!)

            debug(crates)
            debug("")
        }

        return crates.map { it.value.first() }.joinToString("")
        return "f"
    }

    override fun part2(): Any {
        val (cratez, moves) = parseInput()
        val crates = cratez.toSortedMap()

        for (move in moves) {
            debug(crates)

            val cratesToMove =
                crates[move.source]?.take(move.amount) ?: throw Error("invalid crate stack index")
            crates[move.target]?.addAll(0, cratesToMove)
            crates[move.source] = crates[move.source]?.subList(move.amount, crates[move.source]?.size!!)

            debug(crates)
            debug("")
        }

        return crates.map { it.value.first() }.joinToString("")
    }

    data class Move(val source: Int, val target: Int, val amount: Int) {

        companion object {
            val regex = "move (\\d+) from (\\d+) to (\\d+)".toRegex()

            fun fromString(input: String): Move {
                val matches = regex.findAll(input).first().groupValues
                return Move(matches[2].toInt() - 1, matches[3].toInt() - 1, matches[1].toInt())
            }
        }
    }
}
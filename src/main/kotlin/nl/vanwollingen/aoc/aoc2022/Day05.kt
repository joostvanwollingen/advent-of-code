package nl.vanwollingen.aoc.aoc2022

import nl.vanwollingen.aoc.util.Puzzle
import java.util.*

fun main() = Day05.solve()

object Day05 : Puzzle(exampleInput = false, printDebug = false) {

    override fun parseInput(): Pair<SortedMap<Int, MutableList<Char>>, List<Move>> {
        val (crateLines, moveLines) = input.split("\n\n")

        val numberOfColumns = (crateLines.lines().maxOf { it.length } + 1) / 4
        val columnIndexes = generateSequence(2) { it + 4 }.take(numberOfColumns).toList()

        val crates = sortedMapOf<Int, MutableList<Char>>()
        repeat(numberOfColumns) { crates[it] = mutableListOf() }

        crateLines
            .lines()
            .dropLast(1)
            .forEach { line ->
                for (i in 0 until numberOfColumns) {
                    val index = columnIndexes[i]
                    val crate = line.getOrNull(index-1) ?: continue
                    if (crate != ' ') crates[i]!!.add(crate)
                }
            }

        crates.values.forEach { it.reverse() }

        val moves = moveLines.lineSequence().map { Move.fromString(it) }.toList()
        return crates to moves
    }

    override fun part1(): String {
        val (crates, moves) = parseInput()
        return doMoves(crates, moves, reverse = true)
    }

    override fun part2(): String {
        val (crates, moves) = parseInput()
        return doMoves(crates, moves, reverse = false)
    }

    private fun doMoves(
        crates: SortedMap<Int, MutableList<Char>>,
        moves: List<Move>,
        reverse: Boolean
    ): String {
        for (move in moves) {
            val source = crates[move.source]!!
            val target = crates[move.target]!!

            val taken = source.takeLast(move.amount)
            val cratesToMove = if (reverse) taken.reversed() else taken

            target.addAll(cratesToMove)
            repeat(move.amount) { source.removeLast() }
        }

        return crates.values.joinToString("") { it.last().toString() }
    }

    data class Move(val source: Int, val target: Int, val amount: Int) {
        companion object {
            private val regex = Regex("""move (\d+) from (\d+) to (\d+)""")

            fun fromString(input: String): Move {
                val (amount, src, dst) = regex.find(input)!!
                    .destructured

                return Move(
                    source = src.toInt() - 1,
                    target = dst.toInt() - 1,
                    amount = amount.toInt()
                )
            }
        }
    }
}

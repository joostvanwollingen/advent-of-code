package nl.vanwollingen.aoc.aoc2021

import nl.vanwollingen.aoc.util.Puzzle

fun main() = Day04.solve()

object Day04 : Puzzle(exampleInput = false) {

    private val drawNumbers = input.lines().first().split(",").map { it.toInt() }
    private val bingoBoards = input.split("\n\n").drop(1).map { BingoBoard.fromString(it) }

    override fun part1(): Int {
        drawNumbers.forEach { number ->
            bingoBoards.forEach { board ->
                val win = board.numberSeen(number)
                if (win != null) return win
            }
        }
        return 0
    }

    override fun part2(): Int {
        drawNumbers.forEach { number ->
            bingoBoards.forEach { board ->
                val win = board.numberSeen(number)
                if (bingoBoards.none { it.score == 0 }) return win!!
            }
        }
        return 0
    }

    data class BingoBoard(val numbers: List<Int>) {

        private val seenNumbers = numbers.associateWith { false }.toMutableMap()
        private val slots = numbers.mapIndexed { i, number -> i to number }.toMap()
        var score = 0

        fun numberSeen(newNumber: Int): Int? {
            seenNumbers[newNumber] = true
            val isWinner = checkWinners()
            if (isWinner) {
                val notUsed: Int = seenNumbers.entries.filter { !it.value }.sumOf { it.key }
                if (score == 0) score = notUsed * newNumber

                return score
            }
            return null
        }

        private fun checkWinners(): Boolean {
            val firstRow = listOf(0, 1, 2, 3, 4).all { seenNumbers[slots[it]] == true }
            val secondRow = listOf(5, 6, 7, 8, 9).all { seenNumbers[slots[it]] == true }
            val thirdRow = listOf(10, 11, 12, 13, 14).all { seenNumbers[slots[it]] == true }
            val fourthRow = listOf(15, 16, 17, 18, 19).all { seenNumbers[slots[it]] == true }
            val fifthRow = listOf(20, 21, 22, 23, 24).all { seenNumbers[slots[it]] == true }

            val firstColumn = listOf(0, 5, 10, 15, 20).all { seenNumbers[slots[it]] == true }
            val secondColumn = listOf(1, 6, 11, 16, 21).all { seenNumbers[slots[it]] == true }
            val thirdColumn = listOf(2, 7, 12, 17, 22).all { seenNumbers[slots[it]] == true }
            val fourthColumn = listOf(3, 8, 13, 18, 23).all { seenNumbers[slots[it]] == true }
            val fifthColumn = listOf(4, 9, 14, 19, 24).all { seenNumbers[slots[it]] == true }

            return firstRow || secondRow || thirdRow || fourthRow || fifthRow || firstColumn || secondColumn || thirdColumn || fourthColumn || fifthColumn
        }

        companion object {
            fun fromString(input: String) =
                BingoBoard(input.lines().flatMap { it.split(" ") }.filter { it.isNotBlank() }.map { it.toInt() })
        }
    }
}
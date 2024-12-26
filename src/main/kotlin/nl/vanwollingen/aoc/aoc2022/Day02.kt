package nl.vanwollingen.aoc.aoc2022

import nl.vanwollingen.aoc.util.Puzzle

fun main() = Day02.solve()

object Day02 : Puzzle(exampleInput = false) {

    private val scores = mapOf("X" to 1, "Y" to 2, "Z" to 3)

    override fun part1() = input.lines().sumOf {
        val (opponent, player) = it.split(" ")
        var score = scores[player]!!

        when (opponent) {
            "A" -> { //ROCK
                when (player) {
                    "X" -> {
                        score += 3
                    }

                    "Y" -> {
                        score += 6
                    }

                    "Z" -> {}
                }
            }

            "B" -> { //PAPER
                when (player) {
                    "X" -> {}
                    "Y" -> {
                        score += 3
                    }

                    "Z" -> {
                        score += 6
                    }
                }
            }

            "C" -> { //SCISSOR
                when (player) {
                    "X" -> {
                        score += 6
                    }

                    "Y" -> {}
                    "Z" -> {
                        score += 3
                    }
                }
            }
        }
        score
    }


    override fun part2() = input.lines().sumOf {
        val (opponent, player) = it.split(" ")
        var score = 0

        when (opponent) {
            "A" -> { //ROCK
                when (player) {
                    "X" -> {
                        score += scores["Z"]!!
                    }

                    "Y" -> {
                        score += 3
                        score += scores["X"]!!
                    }

                    "Z" -> {
                        score += 6
                        score += scores["Y"]!!
                    }
                }
            }

            "B" -> { //PAPER
                when (player) {
                    "X" -> {
                        score += scores["X"]!!
                    }

                    "Y" -> {
                        score += 3
                        score += scores["Y"]!!
                    }

                    "Z" -> {
                        score += 6
                        score += scores["Z"]!!
                    }
                }
            }

            "C" -> { //SCISSOR
                when (player) {
                    "X" -> {
                        score += scores["Y"]!!
                    }

                    "Y" -> {
                        score += 3
                        score += scores["Z"]!!
                    }

                    "Z" -> {
                        score += 6
                        score += scores["X"]!!
                    }
                }
            }
        }
        score
    }
}
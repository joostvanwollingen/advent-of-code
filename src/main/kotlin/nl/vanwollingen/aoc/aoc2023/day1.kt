package nl.vanwollingen.aoc.aoc2023

import nl.vanwollingen.aoc.util.PuzzleInputUtil

fun main() {
    Day1.solvePart1()
    Day1.solvePart2()
}

class Day1() {
    companion object {
        fun solvePart1() {
            val lines = PuzzleInputUtil.load("day1.input").lines()
            val result = lines.sumOf { "${Day1.getFirstNumber(it)}${Day1.getLastNumber(it)}".toInt() }
            println(result)
        }

        fun solvePart2() {
            val lines = PuzzleInputUtil.load("day1.input").lines()
            val result = lines.sumOf { "${Day1.getFirstNumber(it, true)}${Day1.getLastNumber(it, true)}".toInt() }
            println(result)
        }

        private fun getNumbers(input: String, withLetters: Boolean = false): Sequence<MatchResult> {
            val regex = if (withLetters) Regex("([0-9])|(?=(one|two|three|four|five|six|seven|eight|nine)).") else Regex("([0-9])")
            return regex.findAll(input)
        }

        private fun getFirstNumber(input: String, withLetters: Boolean = false): String {
            val matches = getNumbers(input, withLetters)
            return toNumberString(matches.first().groupValues.findLast { it.isNotEmpty() }!!)
        }

        private fun getLastNumber(input: String, withLetters: Boolean = false): String {
            val matches = getNumbers(input, withLetters)
            return toNumberString(matches.last().groupValues.findLast { it.isNotEmpty() }!!)
        }

        private fun toNumberString(input: String): String {
            return when (input) {
                "one" -> "1"
                "two" -> "2"
                "three" -> "3"
                "four" -> "4"
                "five" -> "5"
                "six" -> "6"
                "seven" -> "7"
                "eight" -> "8"
                "nine" -> "9"
                else -> input
            }
        }
    }
}
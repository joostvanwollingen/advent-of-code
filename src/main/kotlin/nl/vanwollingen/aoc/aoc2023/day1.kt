package nl.vanwollingen.aoc.aoc2023

import nl.vanwollingen.aoc.util.AocUtil

fun main() {
    val lines = AocUtil.load("day1.real.txt").lines()
    val result = lines.sumOf { "${getFirstNumber(it)}${getLastNumber(it)}".toInt() }
    println(result)
}

fun getNumbers(input: String): Sequence<MatchResult> {
    val regex = Regex("([0-9])|(?=(one|two|three|four|five|six|seven|eight|nine)).")
    return regex.findAll(input)
}

fun getFirstNumber(input: String): String {
    val matches = getNumbers(input)
    return toNumberString(matches.first().groupValues.findLast { it.isNotEmpty() }!!)
}

fun getLastNumber(input: String): String {
    val matches = getNumbers(input)
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
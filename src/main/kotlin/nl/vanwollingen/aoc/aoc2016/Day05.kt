package nl.vanwollingen.aoc.aoc2016

import nl.vanwollingen.aoc.util.Puzzle
import nl.vanwollingen.aoc.util.hashing.findLeadingZeroes

fun main() {
    val d5 = Day05(true)
    d5.solvePart1()
    d5.solvePart2()
}

class Day05(output: Boolean = true) : Puzzle(output) {
    override fun solvePart1() {
        var current = 1
        val numbersToFind = 8
        var start = 0L
        var password = ""

        while (current <= numbersToFind) {
            val result = findLeadingZeroes(input, "00000", start)
            start = result.first + 1
            password += result.second[5]
            current++
            debug("($current): $result. Next $start. Current pass: $password")
        }
        log(password)
    }

    override fun solvePart2() {
        var current = 1
        var start = 0L
        val password: MutableMap<Int, Char> = mutableMapOf()

        while (!password.keys.containsAll(listOf(0, 1, 2, 3, 4, 5, 6, 7))) {
            val result = findLeadingZeroes(input, "00000", start)
            start = result.first + 1
            if (result.second[5].isDigit() && result.second[5].digitToInt() < 8) {
                val position = result.second[5].digitToInt()
                if (password[position] == null) {
                    password[position] = result.second[6]
                    current++
                    debug(prettyPrintPassword(password))
                }
            }
        }
        log(prettyPrintPassword(password))
    }

    private fun prettyPrintPassword(charArray: MutableMap<Int, Char>): String {
        var res = ""
        for (i in 0..7) {
            res += charArray[i] ?: '_'
        }
        return res
    }
}
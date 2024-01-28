package nl.vanwollingen.aoc.aoc2017

import nl.vanwollingen.aoc.util.Puzzle

fun main() {
    val d4 = Day04()
    d4.solvePart1()
    d4.solvePart2()
}

class Day04(output: Boolean = false) : Puzzle(output) {

    private val passphrases = parseInput()

    override fun parseInput(): List<List<String>> = input.lines().map { it.split(" ") }

    override fun part1() {
        log(passphrases.filterNot { hasDuplicateWords(it) }.count())
    }

    override fun part2() {
        log(passphrases.filterNot { hasAnagram(it) }.count())
    }

    private fun hasDuplicateWords(passphrase: List<String>): Boolean {
        passphrase.forEachIndexed { i, it ->
            if (passphrase.lastIndexOf(it) != i) return true
        }
        return false
    }

    private fun hasAnagram(passphrases: List<String>): Boolean {
        for (i in passphrases.indices) {
            for (j in i + 1..<passphrases.size) {
                if (passphrases[i].toCharArray().sorted() == passphrases[j].toCharArray().sorted()) return true
            }
        }
        return false
    }
}
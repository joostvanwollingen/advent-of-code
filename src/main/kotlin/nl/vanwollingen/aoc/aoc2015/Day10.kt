package nl.vanwollingen.aoc.aoc2015

import nl.vanwollingen.aoc.util.Puzzle

fun main() {
    val d10 = Day10()
    d10.solvePart1()
}

class Day10() : Puzzle() {
    override fun solvePart1() {
        var curr = input
        for(i in 1..50) {
            curr = lookAndSay(curr)
        }
        println(curr.length)
    }

    override fun solvePart2() {
        TODO("Not yet implemented")
    }

    fun lookAndSay(input: String): String {
        val chars = input.toCharArray()
        var prevChar: Char? = null
        var currCount = 0
        val counts: MutableList<Pair<Char, Int>> = mutableListOf()
        for (i in chars.indices) {
            val currentChar = chars[i]
            if (currentChar == prevChar || prevChar == null) {
                currCount++
            } else {
                counts += prevChar to currCount
                currCount = 1
            }
            prevChar = currentChar
        }
        counts += prevChar!! to currCount
        val string = buildString { counts.forEach { append("${it.second}${it.first}") } }
        return string
    }
}
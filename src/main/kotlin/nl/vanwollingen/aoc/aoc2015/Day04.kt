package nl.vanwollingen.aoc.aoc2015

import nl.vanwollingen.aoc.util.Puzzle
import java.math.BigInteger
import java.security.MessageDigest

fun main() {
    val d4 = Day04()
    d4.part1()
    d4.part2()
}

class Day04() : Puzzle() {

    override fun part1() {
        findLeadingZeroes("00000")
    }

    override fun part2() {
        findLeadingZeroes("000000")
    }

    private fun findLeadingZeroes(search: String) {
        val md = MessageDigest.getInstance("MD5")
        var count = 0L
        var hash = ""
        while (!hash.startsWith(search)) {
            hash = BigInteger(1, md.digest("${input}${count}".toByteArray())).toString(16).padStart(32, '0')
            count++
        }
        println(count - 1)
    }
}
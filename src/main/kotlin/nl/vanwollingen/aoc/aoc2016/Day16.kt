package nl.vanwollingen.aoc.aoc2016

import nl.vanwollingen.aoc.util.Puzzle

fun main() {
    val d16 = Day16()
    d16.solvePart1()
    d16.solvePart2()
}

class Day16(output: Boolean = false) : Puzzle(output) {
    override fun part1() {
        val data = input
        val diskContent = fillDisk(data, 272)
        val checksum = checkSumOdd(diskContent)
        log(checksum)
    }

    override fun part2() {
        val data = input
        val diskContent = fillDisk(data, 35651584)
        val checksum = checkSumOdd(diskContent)
        log(checksum)
    }

    private tailrec fun fillDisk(data: String, diskSize: Int): String {
        if (data.length >= diskSize) {
            return data.substring(0, diskSize)
        }
        return fillDisk(doCurve(data), diskSize)
    }

    private fun doCurve(data: String): String {
        var b = data
        b = b.reversed()
        b = b.flipBits()
        return data + 0 + b
    }

    private fun checkSum(data: String): String {
        val sb = StringBuilder()
        data.windowed(2, 2) {
            if (it[0] == it[1]) sb.append(1) else sb.append(0)
        }
        return sb.toString()
    }

    private fun checkSumOdd(data: String): String {
        var checksum = data
        while (checksum.length % 2 == 0) {
            checksum = checkSum(checksum)
        }
        return checksum
    }

    private fun String.flipBits(): String = this.map { c -> if (c == '1') 0 else 1 }.joinToString("")
}




package nl.vanwollingen.aoc.aoc2016

import nl.vanwollingen.aoc.util.Puzzle

fun main() {
    val d9 = Day09(2016, 9)
    d9.solvePart1()
    d9.solvePart2()
}

class Day09(year: Int, day: Int, output: Boolean = false) : Puzzle(year, day, output) {

    override fun solvePart1() {
        val output = decompress(input)
        log(output)
    }

    override fun solvePart2() {
        val output = decompress(input, true)
        log(output)
    }

    private fun decompress(input: String, decompress: Boolean = false): Long {
        var openMarker = false
        var marker = ""
        var stringLength: Long = 0
        var i = 0

        top@ while (i < input.length) {
            if (input[i] == '(') openMarker = true

            if (openMarker) {
                marker += input[i]
            }

            if (input[i] == ')' && openMarker) {
                val res = processMarker(marker, i, input, decompress)
                i = res.first
                stringLength += res.second
                openMarker = false
                marker = ""
                if (i >= input.length) break
                continue@top
            }

            if (!openMarker) {
                stringLength++
            }
            i++

        }
        return stringLength
    }

    private fun processMarker(marker: String, i: Int, input: String, decompress: Boolean): Pair<Int, Long> {
        val splitMarker = marker.drop(1).dropLast(1).split("x")
        val length = splitMarker[0].toInt()
        val times = splitMarker[1].toInt()
        val stringLength: Long = if (decompress) decompress(input.slice(i + 1..i + length), decompress) else input.slice(i + 1..i + length).length.toLong()

        return i + length + 1 to stringLength * times
    }
}
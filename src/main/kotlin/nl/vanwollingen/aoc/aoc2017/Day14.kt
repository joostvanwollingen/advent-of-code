package nl.vanwollingen.aoc.aoc2017

import nl.vanwollingen.aoc.util.Puzzle
import java.util.*

fun main() {
    val d14 = Day14()
    d14.solvePart1()
}

class Day14(output: Boolean = false) : Puzzle(output) {
    override fun part1() {
        val sum = (0..127).sumOf { i ->
            knotHash("$input-$i").toBigInteger(16).toString(2).count { it == '1' }
        }
        log(sum)
    }

    override fun part2() {
        TODO("Not yet implemented")
    }

    private fun knotHash(hashInput: String): String {
        var list = IntRange(0, 255).toMutableList()
        val input = parseAsAscii(hashInput)
        val suffix = listOf(17, 31, 73, 47, 23)
        val finalLengths = input + suffix

        var currentPosition = 0
        var skipSize = 0
        var round = 1

        while (round <= 64) {
            for (length in finalLengths) {
                val reverse = getReversedSection(list, currentPosition, length)
                list = makeNewList(list, reverse, currentPosition, length)

                currentPosition = (currentPosition + length + skipSize) % list.size
                skipSize++
            }
            round++
        }

        val denseHash = list.chunked(16).map { it.xorList() }
        return denseHash.joinToString("") { it.toString(16) }
    }

    private fun List<Int>.xorList(): Int {
        var r = 0
        for (p in this) {
            r = r xor p
        }
        return r
    }

    private fun parseAsAscii(input: String): List<Int> = input.map { it.code }

    private fun makeNewList(
        list: MutableList<Int>, reverse: List<Int>, currentPosition: Int, input: Int
    ): MutableList<Int> {
        val q: Queue<Int> = LinkedList()
        q.addAll(reverse)
        for (i in currentPosition..<currentPosition + input) {
            list[i % list.size] = q.poll()
        }
        return list
    }

    private fun getReversedSection(list: List<Int>, currentPosition: Int, input: Int): List<Int> {
        val subList = mutableListOf<Int>()
        for (i in currentPosition..<currentPosition + input) {
            subList += list[i % list.size]
        }
        return subList.reversed()
    }
}
package nl.vanwollingen.aoc.aoc2017

import nl.vanwollingen.aoc.util.Puzzle
import java.util.*

fun main() {
    val d10 = Day10()
    d10.solvePart1()
}

class Day10(output: Boolean = false) : Puzzle(output) {
    override fun part1() {
        var list = IntRange(0,255).toMutableList()
        val inputs = input.split(",").map { it.toInt() }
        var currentPosition = 0

        for ((skipSize, input) in inputs.withIndex()) {
            val reverse = getReversedSection(list, currentPosition, input)
            list = makeNewList(list, reverse, currentPosition, input)

            currentPosition = (currentPosition + input + skipSize) % list.size
        }
        log("${list[0] * list[1]}")
    }

    private fun makeNewList(
        list: MutableList<Int>,
        reverse: List<Int>,
        currentPosition: Int,
        input: Int
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

    override fun part2() {
        TODO("Not yet implemented")
    }
}
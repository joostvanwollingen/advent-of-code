package nl.vanwollingen.aoc.aoc2018

import nl.vanwollingen.aoc.util.Puzzle
import kotlin.math.abs

fun main() {
    val d5 = Day05()
    d5.solvePart1()
    d5.solvePart2()
}

class Day05 : Puzzle(exampleInput = false) {

    override fun part1() {
        println(reactPolymer(input))
    }

    override fun part2() {
        val improvedPolymer: MutableMap<Char, Int> = mutableMapOf()
        for (c in 'a'..'z') {
            val polymer = input.replace(c.toString(), "", true)
            improvedPolymer[c] = reactPolymer(polymer)
        }
        println(improvedPolymer.minBy { it.value })
    }

    //https://www.reddit.com/r/adventofcode/comments/a3912m/comment/eb4fiod/
    private fun reactPolymer(input: String): Int {
        val stack = ArrayDeque<Char>()

        for (ele in input) {
            val last = stack.lastOrNull()

            if (last != null && abs(last - ele) == 32) {
                stack.removeLast()
            } else {
                stack.addLast(ele)
            }
        }
        return stack.size
    }
}
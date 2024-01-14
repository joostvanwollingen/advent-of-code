package nl.vanwollingen.aoc.aoc2016

import nl.vanwollingen.aoc.util.Puzzle
import java.util.*

fun main() {
    val d19 = Day19()
    d19.solvePart1()
    d19.solvePart2()
}

class Day19(output: Boolean = false) : Puzzle(output) {
    override fun part1() {
        val elves = Array(input.toInt()) { 1 }
        while (elves.count { it != 0 } > 1) {
            for (i in elves.indices) {
                if (elves[i] == 0) continue
                val nextElf = nextElf(elves, i)
                elves[i] += elves[nextElf]
                elves[nextElf] = 0
            }
        }
        log(elves.indexOfFirst { it != 0 } + 1)
    }

    private fun nextElf(elves: Array<Int>, i: Int): Int {
        var start = if (elves.getOrNull(i + 1) != null) i + 1 else 0
        while (true) {
            if (elves.getOrNull(start) == null) start = 0
            if (elves.getOrNull(start) != 0) return start
            start++
        }
    }

    override fun part2() {
        //I cheated here and looked at https://github.com/exoji2e/aoc2016/blob/master/19/B.java
        val left: Deque<Int> = LinkedList()
        val right: Deque<Int> = LinkedList()

        for (i in 1..input.toInt()) {                                     //Split circle in two
            if (i <= input.toInt() / 2) left.add(i)
            else right.add(i)
        }

        while (left.isNotEmpty() && right.isNotEmpty()) {                       //While there are still elves in the circle
            val indexOfCurrentElf: Int = left.pollFirst()                       //The current elf that gets gifts
            if (left.size == right.size) left.pollLast() else right.pollFirst() //If even halves, pick left, if odd right
            right.addLast(indexOfCurrentElf)                                    //Current elf is rotated to the right
            left.addLast(right.pollFirst())                                     //Balance the halves
        }
        log(left.first)
    }
}
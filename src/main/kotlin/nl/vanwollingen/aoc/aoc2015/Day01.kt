package nl.vanwollingen.aoc.aoc2015

import nl.vanwollingen.aoc.util.Puzzle
import kotlin.system.exitProcess

fun main() {
    val d1 = Day01()
    d1.solvePart1()
    d1.solvePart2()
}

class Day01() : Puzzle() {
    val up = '('

    override fun part1() {
        var floor = 0
        input.forEach {
            if (it == up) floor++ else floor--
        }
        println(floor)
    }

    override fun part2() {
        var floor = 0
        input.forEachIndexed { i, it ->
            if (it == up) floor++ else floor--
            if (floor < 0) {
                println(i + 1)
                exitProcess(1)
            }
        }
        println(floor)
    }
}
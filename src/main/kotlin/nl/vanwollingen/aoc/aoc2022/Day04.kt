package nl.vanwollingen.aoc.aoc2022

import nl.vanwollingen.aoc.util.Puzzle

fun main() = Day04.solve()

object Day04 : Puzzle(exampleInput = false) {

    private val assignmentPairs = input.lines().map { it.split(",") }.map {
        val left = it[0].split("-")
        val right = it[1].split("-")
        left[0].toInt()..left[1].toInt() to right[0].toInt()..right[1].toInt()
    }

    override fun part1() = assignmentPairs.count { (left, right) ->
        left.first >= right.first && left.last <= right.last || right.first >= left.first && right.last <= left.last
    }

    override fun part2() = assignmentPairs.count { (left, right) ->
        left.intersect(right).isNotEmpty() || right.intersect(left).isNotEmpty()

    }
}

// A----B
//    C----D

// A--------B
//     C--D

//      A------B
//    C---D
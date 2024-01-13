package nl.vanwollingen.aoc.aoc2015

import nl.vanwollingen.aoc.util.Puzzle
import nl.vanwollingen.aoc.util.grid.Point

fun main() {
    val d3 = Day03()
    d3.solvePart1()
    d3.solvePart2()
}

class Day03() : Puzzle() {

    private val north = '^'
    private val south = 'v'
    private val east = '>'
    private val west = '<'

    override fun part1() {
        var current = Point(0, 0)
        val presentLocations: MutableMap<Point, Int> = mutableMapOf()

        input.forEach { move ->
            current = deliverGift(move, current, presentLocations)
        }

        println(presentLocations.size + 1)
    }

    private fun deliverGift(move: Char, current: Point, presentLocations: MutableMap<Point, Int>): Point {
        var current1 = current
        if (move == north) current1 = Point(current1.y + 1, current1.x)
        if (move == south) current1 = Point(current1.y - 1, current1.x)
        if (move == east) current1 = Point(current1.y, current1.x + 1)
        if (move == west) current1 = Point(current1.y, current1.x - 1)

        if (presentLocations[current1] == null) {
            presentLocations.put(current1, 1)
        } else {
            presentLocations[current1] = presentLocations[current1]!! + 1
        }
        return current1
    }

    override fun part2() {
        var currentSanta = Point(0, 0)
        var currentRoboSanta = Point(0, 0)
        val presentLocations: MutableMap<Point, Int> = mutableMapOf()

        input.windowed(2, 2) {
            val moveSanta = it.first()
            val moveRoboSanta = it.last()
            currentSanta = deliverGift(moveSanta, currentSanta, presentLocations)
            currentRoboSanta = deliverGift(moveRoboSanta, currentRoboSanta, presentLocations)
        }
        println(presentLocations.size)
    }
}
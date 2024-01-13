package nl.vanwollingen.aoc.aoc2016

import nl.vanwollingen.aoc.util.Puzzle
import nl.vanwollingen.aoc.util.exceptions.TargetStateReachedException
import nl.vanwollingen.aoc.util.grid.Point
import nl.vanwollingen.aoc.util.grid.getManhattanDistance

fun main() {
    val d1 = Day01(true)
    d1.solvePart1()
    d1.solvePart2()
}

class Day01(output: Boolean = false) : Puzzle(output) {

    private val directions = parseInput()

    override fun parseInput(): List<Direction> = input.split(", ").map { Direction(it[0], it.substring(1..<it.length).toInt()) }

    override fun part1() {
        var heading = Heading.NORTH
        val current = Point(0, 0)

        directions.forEach { dir ->
            heading = getNewHeading(heading, dir)
            when (heading) {
                Heading.NORTH -> current.y += dir.count
                Heading.SOUTH -> current.y -= dir.count
                Heading.EAST -> current.x += dir.count
                Heading.WEST -> current.x -= dir.count
            }
        }
        log(Point(0, 0).getManhattanDistance(current))
    }

    override fun part2() {
        var heading = Heading.NORTH
        val current = Point(0, 0)
        var history: MutableList<Point> = mutableListOf()

        directions.forEach { dir ->
            heading = getNewHeading(heading, dir)
            var steps = 0
            while (steps < dir.count) {
                when (heading) {
                    Heading.NORTH -> current.y += 1
                    Heading.SOUTH -> current.y -= 1
                    Heading.EAST -> current.x += 1
                    Heading.WEST -> current.x -= 1
                }
                steps++
                if (history.contains(current)) {
                    log(Point(0, 0).getManhattanDistance(current))
                    throw TargetStateReachedException("${Point(0, 0).getManhattanDistance(current)}")
                }
                history += current.copy()
            }
        }
    }

    private fun getNewHeading(heading: Heading, dir: Direction): Heading = when (heading) {
        Heading.NORTH -> if (dir.to == 'L') Heading.WEST else Heading.EAST
        Heading.SOUTH -> if (dir.to == 'L') Heading.EAST else Heading.WEST
        Heading.EAST -> if (dir.to == 'L') Heading.NORTH else Heading.SOUTH
        Heading.WEST -> if (dir.to == 'L') Heading.SOUTH else Heading.NORTH
    }

    enum class Heading { NORTH, SOUTH, EAST, WEST }

    data class Direction(val to: Char, val count: Int)
}
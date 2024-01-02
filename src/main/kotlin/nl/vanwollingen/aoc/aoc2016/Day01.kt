package nl.vanwollingen.aoc.aoc2016

import nl.vanwollingen.aoc.util.Puzzle
import nl.vanwollingen.aoc.util.grid.Point
import nl.vanwollingen.aoc.util.grid.getManhattanDistance
import kotlin.system.exitProcess

fun main() {
    val d1 = Day01(2016, 1, true)
    d1.solvePart1()
    d1.solvePart2()
}

class Day01(year: Int, day: Int, output: Boolean = false) : Puzzle(year, day, output) {

    private val directions = parseInput()

    override fun parseInput(): List<Direction> = input.split(", ").map { Direction(it[0], it.substring(1..<it.length).toInt()) }

    override fun solvePart1() {
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
        log(current)
        log(Point(0, 0).getManhattanDistance(current))
    }

    override fun solvePart2() {
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
                    log(current)
                    log(Point(0, 0).getManhattanDistance(current))
                    exitProcess(1)
                }
                history += current.copy()
            }
        }
        log(current)
        log(Point(0, 0).getManhattanDistance(current))
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
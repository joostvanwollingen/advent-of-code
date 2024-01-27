package nl.vanwollingen.aoc.aoc2017

import nl.vanwollingen.aoc.util.Puzzle
import nl.vanwollingen.aoc.util.exceptions.TargetStateReachedException
import nl.vanwollingen.aoc.util.grid.Point
import nl.vanwollingen.aoc.util.grid.getManhattanDistance

fun main() {
    val d3 = Day03()
    d3.solvePart1()
    d3.solvePart2()
}

private const val RIGHT = 1
private const val UP = 2
private const val LEFT = 3
private const val DOWN = 4

class Day03(output: Boolean = false) : Puzzle(output) {
    override fun part1() {
        val grid: MutableMap<Int, Point> = mutableMapOf()
        var y = 0
        var x = 0

        var maxX = 0
        var maxY = 0
        var minY = 0
        var minX = 0

        var direction = 1

        for (i in 1..input.toInt()) {
            if (i == 1) {
                grid[i] = Point(y, x)
            } else if (direction == RIGHT) {
                x += 1
                grid[i] = Point(y, x)
                if (x > maxX) {
                    direction = UP
                    maxX += 1
                }
            } else if (direction == UP) {
                y -= 1
                grid[i] = Point(y, x)
                if (y < minY) {
                    direction = LEFT
                    minY -= 1
                }
            } else if (direction == LEFT) {
                x -= 1
                grid[i] = Point(y, x)
                if (x < minX) {
                    direction = DOWN
                    minX -= 1
                }
            } else if (direction == DOWN) {
                y += 1
                grid[i] = Point(y, x)
                if (y > maxY) {
                    direction = RIGHT
                    maxY += 1
                }
            }
        }

        log(grid[input.toInt()]!!.getManhattanDistance(Point(0, 0)))
    }

    override fun part2() {
        val grid: MutableMap<Point, Int> = mutableMapOf()
        var y = 0
        var x = 0

        var maxX = 0
        var maxY = 0
        var minY = 0
        var minX = 0

        var direction = 1

        for (i in 1..input.toInt()) {
            if (i == 1) {
                val currentPoint = Point(y, x)
                grid[currentPoint] = 1
            } else if (direction == RIGHT) {
                x += 1
                val currentPoint = Point(y, x)
                val sum = currentPoint.getNeighbours(grid)
                grid[currentPoint] = sum
                if (x > maxX) {
                    direction = UP
                    maxX += 1
                }
            } else if (direction == UP) {
                y -= 1
                val currentPoint = Point(y, x)
                val sum = currentPoint.getNeighbours(grid)
                grid[currentPoint] = sum
                if (y < minY) {
                    direction = LEFT
                    minY -= 1
                }
            } else if (direction == LEFT) {
                x -= 1
                val currentPoint = Point(y, x)
                val sum = currentPoint.getNeighbours(grid)
                grid[currentPoint] = sum
                if (x < minX) {
                    direction = DOWN
                    minX -= 1
                }
            } else if (direction == DOWN) {
                y += 1
                val currentPoint = Point(y, x)
                val sum = currentPoint.getNeighbours(grid)
                grid[currentPoint] = sum
                if (y > maxY) {
                    direction = RIGHT
                    maxY += 1
                }
            }
        }
        log(grid)
    }

    private fun Point.getNeighbours(grid: MutableMap<Point, Int>): Int {
        val sum = grid.filter {
            (it.key.x == x && (it.key.y == y - 1 || it.key.y == y + 1)) || it.key.y == y && (it.key.x == x - 1 || it.key.x == x + 1) || it.key.x == x + 1 && (it.key.y == y + 1 || it.key.y == y - 1) || it.key.x == x - 1 && (it.key.y == y + 1 || it.key.y == y - 1)
        }.values.sum()
        if (sum > input.toInt()) {
            log(sum)
            throw TargetStateReachedException("${sum}")
        }
        return sum
    }
}
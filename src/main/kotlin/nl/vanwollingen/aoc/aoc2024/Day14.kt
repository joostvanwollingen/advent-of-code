package nl.vanwollingen.aoc.aoc2024

import nl.vanwollingen.aoc.util.Puzzle
import org.jetbrains.kotlinx.dataframe.api.dataFrameOf
import org.jetbrains.kotlinx.kandy.dsl.plot
import org.jetbrains.kotlinx.kandy.letsplot.export.save
import org.jetbrains.kotlinx.kandy.letsplot.layers.tiles

fun main() = Day14.solve()

object Day14 : Puzzle(exampleInput = false, printDebug = true) {

    private val robots = parseInput()
    private val gridX = if (!exampleInput) 101 else 11
    private val gridY = if (!exampleInput) 103 else 7

    override fun parseInput(): List<Robot> = input.lines().map { Robot.fromString(it) }

    override fun part1() =
        robots.map { it.move(100, gridX, gridY) }.run { divideQuadrants(this, gridX, gridY) }.map { it.size }
            .reduce { acc, i -> acc * i }


    override fun part2(): Any {
        for (times in 0..6457) {
            val newPositions = robots.map { it.move(times, gridX, gridY) }

            val xValues = newPositions.map { it.first }
            val yValues = newPositions.map { it.second }

            // Create a data frame
            val data = dataFrameOf("x" to xValues, "y" to yValues)

            // Plot the points
            data.plot {
                tiles {
                    x("x")
                    y("y")
                }
            }.save("$times.png")

        }
        return 6457
    }

    private fun printGrid(newPositions: List<Pair<Int, Int>>, gridX: Int, gridY: Int) {
        val map = newPositions.map { it to "1" }.toMap()
        for (x in 0..gridX) {
            for (y in 0..gridY) {
                val c = map.getOrDefault(x to y, ".")
                if (c == "1") log("\u001b[43m$c\u001b[0m", false) else log(".", false)

            }
            log("")
        }
    }

    private fun divideQuadrants(locations: List<Pair<Int, Int>>, gridX: Int, gridY: Int): List<List<Pair<Int, Int>>> {
        val middleX = gridX / 2
        val middleY = gridY / 2
        val topLeft = mutableListOf<Pair<Int, Int>>()
        val topRight = mutableListOf<Pair<Int, Int>>()
        val bottomLeft = mutableListOf<Pair<Int, Int>>()
        val bottomRight = mutableListOf<Pair<Int, Int>>()

        locations.forEach { (x, y) ->
            if (x == middleX) return@forEach
            if (y == middleY) return@forEach

            if (x < middleX && y < middleY) topLeft.add(x to y)
            if (x > middleX && y < middleY) topRight.add(x to y)
            if (x < middleX && y > middleY) bottomLeft.add(x to y)
            if (x > middleX && y > middleY) bottomRight.add(x to y)
        }
        return listOf(topLeft, topRight, bottomLeft, bottomRight)
    }

    data class Robot(val x: Int, val y: Int, val dx: Int, val dy: Int) {

        fun move(times: Int, maxX: Int, maxY: Int): Pair<Int, Int> {
            val newX = ((x + dx * times) % maxX).let { if (it < 0) it + maxX else it }
            val newY = ((y + dy * times) % maxY).let { if (it < 0) it + maxY else it }
            return Pair(newX, newY)
        }


        companion object {
            fun fromString(input: String): Robot {
                val (positionString, vectorString) = input.split(" ")
                val (x, y) = positionString.subSequence(2, positionString.length).split(",")
                val (dx, dy) = vectorString.subSequence(2, vectorString.length).split(",")
                return Robot(x.toInt(), y.toInt(), dx.toInt(), dy.toInt())
            }
        }
    }
}
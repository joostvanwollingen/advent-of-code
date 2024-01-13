package nl.vanwollingen.aoc.aoc2015

import nl.vanwollingen.aoc.util.Puzzle
import nl.vanwollingen.aoc.util.grid.Point
import nl.vanwollingen.aoc.util.grid.getSurroundingPoints
import kotlin.streams.asStream

fun main() {
    val d18 = Day18()
    d18.solvePart1()
    d18.solvePart2()
}

class Day18() : Puzzle() {
    var grid = parseInput()
    override fun parseInput(): MutableList<Light> = readGridArray(input.lines())

    override fun part1() {
        for (i in 1..100) {
            println(i)
            grid = animateLights(grid)
        }
        grid.print()
        println(grid.count { it.state == Light.LightState.ON })
    }

    override fun part2() {
        for (i in 1..100) {
            println(i)
            grid = animateLights(grid, true)
        }
        println(grid.count { it.state == Light.LightState.ON })
    }

    private fun animateLights(grid: MutableList<Light>, stuckLights: Boolean = false): MutableList<Light> {
        if (stuckLights) turnCornersOn(grid)

        grid.parallelStream().forEach { light ->
            val neighbours = grid.parallelStream().filter {
                light.l.getSurroundingPoints().contains(it.l)
            }.toList()

            val neighBoursWithLightOn = neighbours.count { it.state == Light.LightState.ON }

            when (light.state) {
                Light.LightState.OFF -> {
                    if (neighBoursWithLightOn == 3) {
                        light.nextState = Light.LightState.ON
                    } else {
                        light.nextState = light.state
                    }
                }

                Light.LightState.ON -> {
                    if (neighBoursWithLightOn == 2 || neighBoursWithLightOn == 3) {
                        light.nextState = light.state
                    } else {
                        light.nextState = Light.LightState.OFF
                    }
                }
            }
        }
        grid.forEach { l -> l.state = l.nextState }
        if (stuckLights) turnCornersOn(grid)
        return grid
    }

    private fun turnCornersOn(grid: MutableList<Light>) {
        val corners = listOf(
                Point(1, 1),
                Point(1, 100),
                Point(100, 1),
                Point(100, 100),
        )
        corners.forEach { corner ->
            val gridCorner = grid.first { light ->
                light.l == corner
            }
            gridCorner.state = Light.LightState.ON
        }
    }

    data class Light(var state: LightState, val l: Point) {
        var nextState = LightState.ON

        enum class LightState { ON, OFF }

        fun toColorString(): String = when (state) {
            LightState.OFF -> "\u001b[32m" + "." + "\u001b[0m"
            LightState.ON -> "\u001b[43m" + "#" + "\u001b[0m"
        }
    }

    private fun readGridArray(lines: List<String>): MutableList<Light> {
        val lights: MutableList<Light> = mutableListOf()
        lines.mapIndexed { index, l ->
            val line: MutableList<Light> = mutableListOf()
            line += getLightsFromLine(index, l)
            lights += line
        }
        return lights
    }

    private fun getLightsFromLine(index: Int, line: String): List<Light> {
        val matches = Regex("([#.])").findAll(line)
        return matches.asStream().map { match ->
            Light(if (match.value == "#") Light.LightState.ON else Light.LightState.OFF, Point(index + 1, match.range.first + 1))
        }.toList()
    }

    fun List<Light>.print() {
        val groupedByLine = this.groupBy { it.l.y }
        groupedByLine.forEach { line ->
            val sortedLine = line.value.sortedBy { it.l.y }
            print("${line.key.toString().padStart(3, '0')}: ")
            for (tile in sortedLine) {
                print(tile.toColorString())
            }
            println()
        }
    }
}
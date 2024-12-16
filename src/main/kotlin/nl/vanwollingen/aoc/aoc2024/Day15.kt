package nl.vanwollingen.aoc.aoc2024

import nl.vanwollingen.aoc.util.Puzzle
import org.jetbrains.kotlinx.dataframe.api.dataFrameOf
import org.jetbrains.kotlinx.kandy.dsl.plot
import org.jetbrains.kotlinx.kandy.letsplot.export.save
import org.jetbrains.kotlinx.kandy.letsplot.layers.tiles

fun main() = Day15.solve()

object Day15 : Puzzle(exampleInput = false) {



    override fun parseInput(): Pair<MutableMap<Pair<Int, Int>, Char>, Map<Int, Pair<Int, Int>>> {
        val grid: MutableMap<Pair<Int, Int>, Char> = mutableMapOf()
        var y = 0
        val (coordinates, moves) = input.split("\n\n")

        coordinates.lines().forEach { line ->
            var x = 0
            line.forEach { cell ->
                if (cell in listOf('O', '#', '@')) {
                    grid[y to x] = cell
                }
                x++
            }
            y++
        }

        val allMoves = moves.lines().joinToString("").mapIndexed { i, c ->
            i to getDirection(c)
        }.toMap()

        return grid to allMoves
    }

    private fun reparse(): Pair<MutableMap<Pair<Int, Int>, Char>, Map<Int, Pair<Int, Int>>> {
        val grid: MutableMap<Pair<Int, Int>, Char> = mutableMapOf()
        var y = 0
        val (coordinates, moves) = input.split("\n\n")

        coordinates.lines().forEach { line ->
            var x = 0
            line.forEach { cell ->
                when (cell) {
                    'O' -> {
                        grid[y to x] = '['
                        grid[y to x + 1] = ']'
                    }

                    '#' -> {
                        grid[y to x] = cell
                        grid[y to x + 1] = cell
                    }

                    '@' -> {
                        grid[y to x] = cell
                    }
                }
                x += 2
            }
            y++
        }

        val allMoves = moves.lines().joinToString("").mapIndexed { i, c ->
            i to getDirection(c)
        }.toMap()

        return grid to allMoves
    }

    override fun part1(): Int {
       val data = parseInput()
       var grid = data.first
       val moves = data.second

        var robot = grid.filter { it.value == '@' }.keys.first()
        moves.forEach { (i, direction) ->
            val nextTile = robot + direction
            val nextTileValue = grid[nextTile]

            when (nextTileValue) {
                null -> { //empty tile
                    grid.remove(robot)
                    grid[nextTile] = '@'
                    robot += direction
                }

                '#' -> {
                    //noOp
                }

                'O' -> {
                    if (pushBox(nextTile, direction, grid)) {
                        grid.remove(robot)
                        grid[nextTile] = '@'
                        robot += direction
                    }
                }
            }
//            log(direction)
//            printGrid(grid, grid.keys.maxOf { it.first }, grid.keys.maxOf { it.second })
        }
        return grid.filter { it.value == 'O' }.keys.sumOf { (it.first * 100) + it.second }
    }

    override fun part2(): Int {
        val data = parseInput()
        val moves = data.second

        var grid = reparse().first
        var robot = grid.filter { it.value == '@' }.keys.first()
        printGrid(grid, grid.keys.maxOf { it.first }, grid.keys.maxOf { it.second })

        moves.forEach { (i, direction) ->
            val nextTile = robot + direction
            val nextTileValue = grid[nextTile]

            when (nextTileValue) {
                null -> { //empty tile
                    grid.remove(robot)
                    grid[nextTile] = '@'
                    robot += direction
                }

                '#' -> {
                    //noOp
                }

                in listOf('[', ']') -> {
                    val boxesToBePushed: Set<Pair<Int, Int>> = findPushableBoxes(nextTile, direction, grid)
                    if (boxesToBePushed.isNotEmpty()) {
                        val oldValues = boxesToBePushed.map { it to grid[it] }
                        boxesToBePushed.forEach { grid.remove(it) }
                        oldValues.forEach { box ->
                            if(box.second != null ) grid[box.first + direction] = box.second!!
                        }

                        grid.remove(robot)
                        grid[nextTile] = '@'
                        robot += direction
                    }
                }
            }
//            log(if(direction.first == -1) "UP" else if(direction.first == 1) "DOWN" else if (direction.second == -1) "LEFT" else "RIGHT")
//            printGrid(grid, grid.keys.maxOf { it.first }, grid.keys.maxOf { it.second })

            //convert *.png(n) out.gif
            val xValues = grid.keys.map { it.first }
            val yValues = grid.keys.map { it.second }
            val displayValues = grid.values.toList() // Extract the display values from the grid

            // Create a data frame
            val data = dataFrameOf("x" to xValues, "y" to yValues, "value" to displayValues)

            // Plot the points
            data.plot {
                tiles {
                    x("y")
                    y("x")
                    fillColor("value") // Use the display values to color cells
                }
            }.save("$i.png")
        }
        printGrid(grid, grid.keys.maxOf { it.first }, grid.keys.maxOf { it.second })

        return grid.filter { it.value == '[' }.keys.sumOf { (it.first * 100) + it.second }
    }

    private fun findPushableBoxes(
        start: Pair<Int, Int>,
        direction: Pair<Int, Int>,
        grid: MutableMap<Pair<Int, Int>, Char>,
        visited: MutableSet<Pair<Int, Int>>? = mutableSetOf()
    ): Set<Pair<Int, Int>> {
        val pushable = visited ?: mutableSetOf()
        var current = start

        if (direction.first == 0) { //pushing left or right
            while (grid[current] != null) {
                if (grid[current + direction] == null) { //found empty space
                    pushable.add(current)
                    return pushable
                }
                if (grid[current + direction] == '#') { //found a wall
                    return emptySet()
                }

                pushable.add(current)
                current += direction
            }
        }

        if (direction.first != 0) { //pushing up or down
            while (grid[current] != null) {

                var right = if (grid[current] == '[') {
                    current + (0 to 1)
                } else {
                    current
                }

                var left = if (grid[current] == '[') {
                    current
                } else {
                    current + (0 to -1)
                }

                if (grid[left] == '#' || grid[right] == '#') { //found a wall or empty space
                    return emptySet()
                }

                pushable.add(left)
                pushable.add(right)

                if (grid[left + direction] == null && grid[right + direction] == null) {
                    pushable.add(left)
                    pushable.add(right)
                    return pushable
                }

                val pushRight =
                    findPushableBoxes(right + direction, direction, grid, pushable)

                val pushLeft =
                    findPushableBoxes(left + direction, direction, grid, pushable)

                if (pushRight.isEmpty() || pushLeft.isEmpty()) return emptySet()

                pushable.addAll(pushLeft)
                pushable.addAll(pushRight)
                current += direction
            }
        }

        if(grid[current] == null) { return pushable + setOf(current) }

        return emptySet()
    }


    private fun pushBox(
        start: Pair<Int, Int>, direction: Pair<Int, Int>, grid: MutableMap<Pair<Int, Int>, Char>
    ): Boolean {
        val nextTile = start + direction
        val nextTileValue = grid[nextTile]
        when (nextTileValue) {
            null -> {
                grid[nextTile] = 'O'
                return true
            }

            '#' -> {
                return false
            }

            'O' -> {
                return pushBox(nextTile, direction, grid)
            }
        }
        return false
    }


    private fun getDirection(c: Char): Pair<Int, Int> {
        return when (c) {
            '<' -> 0 to -1
            '>' -> 0 to 1
            'v' -> 1 to 0
            '^' -> -1 to 0
            else -> throw Error("no bueno")
        }
    }

    private operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
        return first + other.first to second + other.second
    }

    private operator fun Pair<Int, Int>.minus(other: Pair<Int, Int>): Pair<Int, Int> {
        return first - other.first to second - other.second
    }

    private fun printGrid(newPositions: MutableMap<Pair<Int, Int>, Char>, gridX: Int, gridY: Int) {
        for (x in 0..gridX) {
            for (y in 0..gridY) {
                val c = newPositions.getOrDefault(x to y, ".")
                if (c == '@') log("\u001b[31m$c\u001b[0m", false) else log(c, false)
            }
            log("")
        }
        log("")
    }
}
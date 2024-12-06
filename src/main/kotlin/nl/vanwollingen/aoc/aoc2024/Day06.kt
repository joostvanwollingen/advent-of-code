package nl.vanwollingen.aoc.aoc2024

import nl.vanwollingen.aoc.util.Puzzle
import nl.vanwollingen.aoc.util.grid.Point

fun main() = Day06.solve()

object Day06 : Puzzle(exampleInput = false, printDebug = false) {

    val grid = parseInput()

    override fun parseInput(): MutableMap<Int, MutableMap<Int, Char>> {
        val grid: MutableMap<Int, MutableMap<Int, Char>> = mutableMapOf()
        var y = 0
        input.lines().forEach { line ->
            val rowMap = mutableMapOf<Int, Char>()
            var x = 0
            line.forEach { cell ->
                rowMap[x++] = cell
            }
            grid[y++] = rowMap
        }
        return grid
    }

    override fun part1(): Int {
        var guardY = if (exampleInput) 6 else 69 //TODO find
        var guardX = if (exampleInput) 4 else 91
        log("Guard: ${grid[guardY]?.get(guardX)}")

        var direction = Point(-1, 0)
        val visited = mutableSetOf(Point(guardY, guardX))
        val gridRange = if (exampleInput) 0..<9 else 0..<129

        while (guardY in gridRange && guardX in gridRange) {
            val nextSquareIsOccupied = grid[guardY + direction.y]?.get(guardX + direction.x) == '#'
            if (nextSquareIsOccupied) {
                direction = direction.turn90Degrees()
            } else {
                guardY += direction.y
                guardX += direction.x
            }
            visited += Point(guardY, guardX)
        }
        return visited.size
    }

    override fun part2(): Long {
        val emptyCells = mutableListOf<Pair<Int, Int>>()

        // Collect all empty cells
        for ((row, cols) in grid) {
            for ((col, value) in cols) {
                if (value == '.') emptyCells.add(Pair(row, col))
            }
        }

        return emptyCells
            .parallelStream()
            .filter { cell->
//            debug(cell)
            val (row, col) = cell
            if (grid[row]?.get(col) == '.') {
                if(hasLoop(grid, row, col)) return@filter true
            }
            return@filter false
        }.count()
    }

    private fun Point.turn90Degrees(): Point {
        if (y == -1) return Point(0, 1)
        if (y == 1) return Point(0, -1)
        if (x == -1) return Point(-1, 0)
        if (x == 1) return Point(1, 0)
        else throw Error("wtf")
    }

    private fun hasLoop(grid: Map<Int, Map<Int, Char>>, row: Int, col:Int): Boolean {
        var guardY = if (exampleInput) 6 else 69 //TODO find
        var guardX = if (exampleInput) 4 else 91

        var direction = Point(-1, 0)
        val visited = mutableSetOf(Point(guardY, guardX) to direction)
        val gridRange = if (exampleInput) 0..9 else 0..129

        while (guardY in gridRange && guardX in gridRange) {

            val nextSquareIsOccupied = grid[guardY + direction.y]?.get(guardX + direction.x) == '#' || row == guardY + direction.y && col == guardX + direction.x

            if (nextSquareIsOccupied) {
                direction = direction.turn90Degrees()
            } else {
                guardY += direction.y
                guardX += direction.x
            }

            if (!visited.add(Point(guardY, guardX) to direction)) {
                debug("Loop detected at ($guardY, $guardX), direction: $direction")
//                printGrid(grid, Point(guardY, guardX), visited)
                return true
            }
        }
        return false
    }

    fun printGrid(
        grid: Map<Int, Map<Int, Char>>,
        guardPosition: Point,
        visited: Set<Pair<Point, Point>>
    ) {
        val gridHeight = grid.keys.maxOrNull() ?: 0
        val gridWidth = grid.values.maxOf { it.keys.maxOrNull() ?: 0 }
        val visitedMap = visited.associateBy(keySelector = {it.first}, valueTransform = {it.second})

        for (row in 0..gridHeight) {
            for (col in 0..gridWidth) {
                when {
                    guardPosition == Point(row, col) -> print("^") // Print guard position
                    visitedMap[Point(row, col)] != null -> if(visitedMap[Point(row, col)]?.y !=0) print("|") else print("-") // Print visited cells
                    grid[row]?.get(col) == '#' -> print("#") // Print obstacle
                    else -> print(".") // Print empty space
                }
            }
            println() // New line after each row
        }
    }
}





package nl.vanwollingen.aoc.aoc2016

import nl.vanwollingen.aoc.util.Puzzle
import nl.vanwollingen.aoc.util.exceptions.TargetStateReachedException
import nl.vanwollingen.aoc.util.grid.Point
import nl.vanwollingen.aoc.util.hashing.md5
import java.util.LinkedList
import java.util.Queue

fun main() {
    val d17 = Day17()
    d17.solvePart1()
    d17.solvePart2()
}

class Day17(output: Boolean = false) : Puzzle(output) {
    override fun part1() {
        val q: Queue<Pair<String, Point>> = LinkedList()
        q.add("" to Point(0, 0))

        while (q.isNotEmpty()) {
            val (path, current) = q.remove()
            val allowedDirections = determineOpenDoors(hashRoom(path), current.x, current.y)

            allowedDirections.forEach { dir ->
                if (dir.second.x == 3 && dir.second.y == 3) {
                    log(path + dir.first)
                    throw TargetStateReachedException()
                }
                q.add(path + dir.first to dir.second)
            }
        }
    }

    override fun part2() {
        val q: Queue<Pair<String, Point>> = LinkedList()
        q.add("" to Point(0, 0))
        var attempts = 0
        var longestPathSeen = Int.MIN_VALUE

        while (q.isNotEmpty() && attempts < 100000) {
            val (path, current) = q.remove()
            val allowedDirections = determineOpenDoors(hashRoom(path), current.x, current.y)

            allowedDirections.forEach { dir ->
                if (dir.second.x == 3 && dir.second.y == 3) {
                    val currentPath = path + dir.first
                    if (currentPath.length > longestPathSeen) longestPathSeen = currentPath.length
                } else {
                    q.add(path + dir.first to dir.second)
                }
            }
            attempts++
        }
        log(longestPathSeen)
    }

    private fun determineOpenDoors(roomHash: String, x: Int, y: Int): List<Pair<String, Point>> {
        val up = 0
        val down = 1
        val left = 2
        val right = 3

        val openDoors = listOf('b', 'c', 'd', 'e', 'f')
        val openDirections: MutableList<Pair<String, Point>> = mutableListOf()

        if (roomHash[up] in openDoors) {
            if (y > 0) openDirections += "U" to Point(y - 1, x)
        }

        if (roomHash[down] in openDoors) {
            if (y < 3) openDirections += "D" to Point(y + 1, x)
        }

        if (roomHash[left] in openDoors) {
            if (x > 0) openDirections += "L" to Point(y, x - 1)
        }

        if (roomHash[right] in openDoors) {
            if (x < 3) openDirections += "R" to Point(y, x + 1)
        }

        return openDirections
    }

    private fun hashRoom(path: String): String = md5("$input$path")
}
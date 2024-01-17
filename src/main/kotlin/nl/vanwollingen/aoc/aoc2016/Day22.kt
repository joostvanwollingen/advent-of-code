package nl.vanwollingen.aoc.aoc2016

import nl.vanwollingen.aoc.util.Puzzle
import java.util.*
import kotlin.math.min

fun main() {
    val d22 = Day22()
    d22.solvePart1()
    d22.solvePart2()
}

class Day22(output: Boolean = false) : Puzzle(output) {

    private val nodes = parseInput()

    override fun part1() {
        var viablePairs = 0

        for (i in nodes.indices) {
            if (nodes[i].used == 0) continue
            for (j in 1..<nodes.size) {
                if (nodes[i].used <= nodes[j].avail) viablePairs++
            }
        }
        log(viablePairs)
    }

    override fun part2() {
        val grid = Array(25) { y ->
            Array(37) { x ->
                nodes.first { it.x == x && it.y == y }
            }
        }
        val goalDataNode = nodes.first { node -> node.y == 0 && node.x == nodes.maxBy { it.x }.x }
        val shortestRoute = getShortestRoute(grid, goalDataNode)

        log(
            shortestRoute + 1 //to move the data to the empty node
                    + (goalDataNode.x - 1) * 5 //to move the goal data to the start
        )
    }

    private fun getShortestRoute(grid: Array<Array<Node>>, goalDataNode: Node): Int {
        val costs = Array(grid.size) { y ->
            Array(grid[0].size) { x ->
                Int.MAX_VALUE
            }
        }

        val currentNode = nodes.first { node -> node.used == 0 }
        val q: Queue<Node> = LinkedList()

        q.add(currentNode)
        costs[currentNode.y][currentNode.x] = 0

        while (q.isNotEmpty()) {
            val nextNode = q.remove()
            val allowedDirections = grid.moveAbleNeighbours(nextNode.y, nextNode.x)

            allowedDirections.forEach { dir ->
                if (costs[dir.y][dir.x] == Int.MAX_VALUE) {
                    if (dir.y == goalDataNode.y && dir.x == goalDataNode.x - 1) {
                        return costs[nextNode.y][nextNode.x] + 1
                    }
                    val cost = costs[nextNode.y][nextNode.x] + 1
                    costs[dir.y][dir.x] = min(cost, costs[dir.y][dir.x])
                    q.add(dir)
                }
            }
        }
        throw Exception("Didn't reach goal")
    }

    override fun parseInput(): List<Node> = input.lines().drop(2).map { line ->
        Node.fromString(line)
    }

    data class Node(val x: Int, val y: Int, val size: Int, var used: Int, var avail: Int, var percentage: Int) {

        companion object {

            fun fromString(input: String): Node {
                val matches = Regex("/dev/grid/node-x(\\d+)-y(\\d+) *(\\d+)T *(\\d+)T *(\\d+)T *(\\d+)%").findAll(input)
                    .first().groupValues
                return Node(
                    matches[1].toInt(),
                    matches[2].toInt(),
                    matches[3].toInt(),
                    matches[4].toInt(),
                    matches[5].toInt(),
                    matches[6].toInt()
                )
            }
        }
    }

    private fun Array<Array<Node>>.moveAbleNeighbours(y: Int, x: Int): List<Node> {
        return listOfNotNull(
            this.getOrNull(y - 1)?.getOrNull(x),
            this.getOrNull(y + 1)?.getOrNull(x),
            this.getOrNull(y)?.getOrNull(x + 1),
            this.getOrNull(y)?.getOrNull(x - 1),
        ).filter { it.used <= 85 }
    }
}
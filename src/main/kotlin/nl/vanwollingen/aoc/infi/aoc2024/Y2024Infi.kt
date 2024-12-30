package nl.vanwollingen.aoc.infi.aoc2024

import nl.vanwollingen.aoc.util.Puzzle
import java.util.LinkedList
import kotlin.random.Random

fun main() = Y2024Infi().solve()

//WC8N85QQ8649
class Y2024Infi : Puzzle(exampleInput = false) {
    private val instructions: Map<Int, Pair<String, String?>> = getInstructions()

    override fun part1() = buildSnowMap().values.sum()

    override fun part2(): Int {
        val snowMap: Map<Triple<Int, Int, Int>, Int> = buildSnowMap()

        val clouds = mutableListOf<Int>()
        val visited: MutableSet<Triple<Int, Int, Int>> = mutableSetOf()

        for (x in 0..29) {
            for (y in 0..29) {
                for (z in 0..29) {
                    val current = Triple(x, y, z)
                    if (snowMap[current] != null && !visited.contains(current)) {
                        clouds += findCloud(current, visited, snowMap)
                    }
                }
            }
        }
        return clouds.size
    }

    private fun buildSnowMap(): Map<Triple<Int, Int, Int>, Int> {
        val snowMap: MutableMap<Triple<Int, Int, Int>, Int> = mutableMapOf()

        for (x in 0..29) {
            for (y in 0..29) {
                for (z in 0..29) {
                    calculateSnowContents(x, y, z, instructions).let { snow ->
                        if (snow > 0) {
                            snowMap[Triple(x, y, z)] = snow
                        }
                    }
                }
            }
        }
        return snowMap
    }

    private fun findCloud(
        current: Triple<Int, Int, Int>,
        visited: MutableSet<Triple<Int, Int, Int>>,
        snowMap: Map<Triple<Int, Int, Int>, Int>
    ): Int {
        val cloudId = Random.nextInt()
        val cloudQueue = LinkedList<Triple<Int, Int, Int>>()
        cloudQueue.add(current)

        while (cloudQueue.isNotEmpty()) {
            val next = cloudQueue.poll()
            val neighbors = getNeighbourClouds(next, snowMap)
            visited.add(next)
            cloudQueue.addAll(neighbors - visited)
            visited.addAll(neighbors)
        }
        return cloudId
    }

    private fun getNeighbourClouds(
        next: Triple<Int, Int, Int>,
        map: Map<Triple<Int, Int, Int>, Int>,
    ): List<Triple<Int, Int, Int>> {
        val (x, y, z) = next

        val west = Triple(x - 1, y, z)
        val east = Triple(x + 1, y, z)
        val north = Triple(x, y + 1, z)
        val south = Triple(x, y - 1, z)
        val up = Triple(x, y, z + 1)
        val down = Triple(x, y, z - 1)

        return listOf(west, east, up, down, north, south).mapNotNull { if (map[it] != null) it else null }
    }

    private fun calculateSnowContents(
        x: Int, y: Int, z: Int,
        instructions: Map<Int, Pair<String, String?>>,
    ): Int {
        var counter = 0
        val stack = LinkedList<Int>()

        while (true) {
            val (operation: String, operand: String?) = instructions[counter] ?: throw Error("fail")

            when (operation) {
                "push" -> {
                    stack.push(determineOperand(operand, x, y, z))
                    counter++
                }

                "jmpos" -> {
                    if (stack.poll() >= 0) {
                        counter += determineOperand(operand, x, y, z)
                    }
                    counter++
                }

                "ret" -> {
                    return stack.peek()
                }

                "add" -> {
                    stack.push(stack.poll() + stack.poll())
                    counter++
                }
            }
        }
    }

    private fun determineOperand(operandString: String?, x: Int, y: Int, z: Int): Int {
        val operand = when (operandString?.lowercase()) {
            "x" -> x
            "y" -> y
            "z" -> z
            else -> operandString?.toInt()
        }
        return operand!!
    }

    private fun getInstructions(): Map<Int, Pair<String, String?>> {
        val operations: Map<Int, Pair<String, String?>> = input.lines().mapIndexed { y, line ->
            val split = line.split(" ")
            val operation = split[0]
            val operand: String? = if (split.size > 1) split[1] else null
            y to (operation to operand)
        }.toMap()
        return operations
    }
}
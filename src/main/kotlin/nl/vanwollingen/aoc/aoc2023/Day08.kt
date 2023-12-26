package nl.vanwollingen.aoc.aoc2023

import nl.vanwollingen.aoc.util.PuzzleInputUtil
import nl.vanwollingen.aoc.util.quickmaths.mansnothot.bigshaq.findLcm

fun main() {
    val input = PuzzleInputUtil.load("2023/day8.input")
    val instructions = input.lines()[0].toCharArray().map { it.toString() }
    val directions: List<Day8.Node> = input.lines().subList(2, input.lines().size).map {
        val start = it.split("=")[0].trim()
        val destination = it.split("=")[1].trim().replace("(", "").replace(")", "").split(", ")
        Day8.Node(start, destination[0], destination[1])
    }

    Day8().solvePart1(instructions, directions)
    Day8().solvePart2(instructions, directions)
}

class Day8 {
    fun solvePart1(instructions: List<String>, directions: List<Node>) {
        var currentNode = directions.first { it.start == "AAA" }
        var currentInstruction = 0
        var steps = 0
        while (currentNode.start != "ZZZ") {
            currentNode = determineNextNode(instructions[currentInstruction], currentNode, directions)
            if (currentInstruction == instructions.size - 1) {
                currentInstruction = 0
            } else currentInstruction++
            steps++
        }
        println("$steps to reach $currentNode")
    }

    fun solvePart2(instructions: List<String>, directions: List<Node>) {
        var map = createMap(directions)
        var startingNodes = directions.filter { it.start.endsWith("A") }.map { it.start }
        var zNodes = startingNodes.map { findPathToNodesEndingWith(it, "Z", instructions, map) }
        var LCM = findLcm(zNodes.flatMap { it.keys })
        println(LCM)

    }

    private fun findPathToNodesEndingWith(start: String, target: String, instructions: List<String>, map: Map<String, Map<String, String>>): MutableMap<Long, String> {
        var currentInstruction = 0
        var steps = 1L
        var currentNode = start
        var path: MutableMap<Long, String> = mutableMapOf()
        while (path.isEmpty()) {
            var instructionSymbol = instructions[currentInstruction]

            currentNode = map[currentNode]!![instructionSymbol]!!
            if (currentNode.endsWith(target)) path[steps] = currentNode
            if (currentInstruction == instructions.size - 1) {
                currentInstruction = 0
            } else currentInstruction++
            steps++
        }
        return path
    }

    private fun createMap(directions: List<Node>): Map<String, Map<String, String>> = directions.associate { it.start to mapOf("L" to it.L, "R" to it.R) }

    data class Node(val start: String, val L: String, val R: String)

    private fun determineNextNode(instruction: String, currentNode: Node, directions: List<Node>): Node {
        return if (instruction == "L") {
            directions.first { it.start == currentNode.L }
        } else {
            directions.first { it.start == currentNode.R }
        }
    }
}
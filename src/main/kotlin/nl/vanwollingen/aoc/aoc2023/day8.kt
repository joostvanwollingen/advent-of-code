package nl.vanwollingen.aoc.aoc2023

import nl.vanwollingen.aoc.util.PuzzleInputUtil

fun main() {
//    val input = nl.vanwollingen.aoc.util.AocUtil.load("day8.test.input")
//    val input = nl.vanwollingen.aoc.util.AocUtil.load("day8.test2.input")
//    val input = nl.vanwollingen.aoc.util.AocUtil.load("day8.test3.input")
    val input = PuzzleInputUtil.load("2023/day8.input")
    val instructions = input.lines()[0].toCharArray().map { it.toString() }
    val directions: List<Node> = input.lines().subList(2, input.lines().size).map {
        val start = it.split("=")[0]!!.trim()
        val destination = it.split("=")[1]!!.trim().replace("(", "").replace(")", "").split(", ")
        Node(start, destination[0], destination[1])
    }
//    solvePartOneDay8(instructions, directions)
//    solvePartTwoDay8(instructions, directions)
    solvePartTwoDay8Again(instructions, directions)
}

fun solvePartTwoDay8Again(instructions: List<String>, directions: List<Node>) {
    var map = createMap(directions)
    var startingNodes = directions.filter { it.start.endsWith("A") }.map { it.start }
    var zNodes = startingNodes.map { findPathToNodesEndingWith(it, "Z", instructions, map) }
    var LCM = findLCMOfListOfNumbers(zNodes.flatMap { it.keys })
    println(LCM)

}

fun findLCM(a: Long, b: Long): Long {
    val larger = if (a > b) a else b
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == 0L && lcm % b == 0L) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}

fun findLCMOfListOfNumbers(numbers: List<Long>): Long {
    var result = numbers[0]
    for (i in 1 until numbers.size) {
        result = findLCM(result, numbers[i])
    }
    return result
}

fun findPathToNodesEndingWith(
    start: String,
    target: String,
    instructions: List<String>,
    map: Map<String, Map<String, String>>
): MutableMap<Long, String> {
    var currentInstruction = 0
    var steps = 1L
    var currentNode = start
    var path: MutableMap<Long, String> = mutableMapOf()
    while (path.isEmpty()) {
        var instructionSymbol = instructions[currentInstruction]

        print("$steps: ${currentNode}  --> ${instructions[currentInstruction]} --> ")
        currentNode = map[currentNode]!![instructionSymbol]!!
        if (currentNode.endsWith(target)) path[steps] = currentNode
        print("${currentNode}\n")
        if (currentInstruction == instructions.size - 1) {
            println("Looping instructions at step $steps")
            currentInstruction = 0
        } else currentInstruction++
        steps++
    }
    return path
}

fun solvePartTwoDay8(instructions: List<String>, directions: List<Node>) {
    var map = createMap(directions)

    var currentNodes = directions.filter { it.start.endsWith("A") }.map { it.start }
    var currentInstruction = 0
    var steps = 0L

    while (currentNodes.filterNot { it.endsWith("Z") }.isNotEmpty()) {
//        print("$steps: $currentNodes --> ${instructions[currentInstruction]} --> ")
        var instructionSymbol = instructions[currentInstruction]
        currentNodes = currentNodes.map { map[it]!![instructionSymbol]!! }
//        print("${currentNodes.size}\n")
        if (currentInstruction == instructions.size - 1) {
//            println("Looping instructions at step $steps")
            currentInstruction = 0
        } else currentInstruction++
        steps++
//        if(steps%10000000==0) {println("$steps: Nodes $currentNodes")}
    }
    println("$steps to reach $currentNodes")
}

fun createMap(directions: List<Node>): Map<String, Map<String, String>> =
    directions.associate { it.start to mapOf("L" to it.L, "R" to it.R) }


data class Node(val start: String, val L: String, val R: String) {

}

fun solvePartOneDay8(instructions: List<String>, directions: List<Node>) {
    var currentNode = directions.first { it.start == "AAA" }
    var currentInstruction = 0
    var steps = 0
    while (currentNode.start != "ZZZ") {
        print("$steps: ${currentNode.start} (${currentNode.L}, ${currentNode.R}) --> ${instructions[currentInstruction]} --> ")
        currentNode = determineNextNode(instructions[currentInstruction], currentNode, directions)
        print("${currentNode.start}\n")
        if (currentInstruction == instructions.size - 1) {
            println("Looping instructions at step $steps")
            currentInstruction = 0
        } else currentInstruction++
        steps++
    }
    println("$steps to reach $currentNode")
}

fun determineNextNode(instruction: String, currentNode: Node, directions: List<Node>): Node {
    return if (instruction == "L") {
        directions.filter { it.start == currentNode.L }.first()
    } else {
        directions.filter { it.start == currentNode.R }.first()
    }
}

fun main() {
//    val input = AocUtil.load("day8.test.input")
//    val input = AocUtil.load("day8.test2.input")
    val input = AocUtil.load("day8.input")
    val instructions = input.lines()[0].toCharArray().map { it.toString() }
    val directions: List<Node> = input.lines().subList(2, input.lines().size).map {
        val start = it.split("=")[0]!!.trim()
        val destination = it.split("=")[1]!!.trim().replace("(", "").replace(")", "").split(", ")
        Node(start, destination[0], destination[1])
    }
    solvePartOneDay8(instructions, directions)

}

data class Node(val start: String, val L: String, val R: String) {

}

fun solvePartOneDay8(instructions: List<String>, directions: List<Node>) {
    var currentNode = directions.first{it.start=="AAA"}
    var currentInstruction = 0
    var steps = 0
    while (currentNode.start != "ZZZ") {
        print("$steps: ${currentNode.start} (${currentNode.L}, ${currentNode.R}) --> ${instructions[currentInstruction]} --> ")
        if (instructions[currentInstruction] == "L") {
            currentNode = directions.filter { it.start == currentNode.L }.first()
        } else {
            currentNode = directions.filter { it.start == currentNode.R }.first()
        }
        print("${currentNode.start}\n")
        if (currentInstruction == instructions.size - 1) {
            println("Looping instructions at step $steps")
            currentInstruction = 0
        } else currentInstruction++
        steps++
    }
    println("$steps to reach $currentNode")
}


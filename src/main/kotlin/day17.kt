import kotlin.reflect.KClass
import kotlin.streams.asStream
import kotlin.streams.toList
import kotlin.system.exitProcess

val input = AocUtil.load("day17.test.input")
val grid = readGridArray(input.lines())
val maxX = grid[0].size - 1
val maxY = grid.size - 1
fun main() {

    //1 Mark all nodes unvisited. Create a set of all the unvisited nodes called the unvisited set.


    var x = 0
    var y = 0

    //2 Assign to every node a tentative distance value: set it to zero for our initial node and to infinity for all
    // other nodes. During the run of the algorithm, the tentative distance of a node v is the length of the shortest
    // path discovered so far between the node v and the starting node. Since initially no path is known to any other
    // vertex than the source itself (which is a path of length zero), all other tentative distances are initially
    // set to infinity. Set the initial node as current.[17]
    grid[0][0].tentativeDistance = 0

    while (true) {
        //3 For the current node, consider all of its unvisited neighbors and calculate their tentative distances
        // through the current node. Compare the newly calculated tentative distance to the one currently assigned
        // to the neighbor and assign it the smaller one. For example, if the current node A is marked with a distance
        // of 6, and the edge connecting it with a neighbor B has length 2, then the distance to B through A will be 6 + 2 = 8.
        // If B was previously marked with a distance greater than 8 then change it to 8.
        // Otherwise, the current value will be kept.
        val unvisitedNeighbours: List<CityBlock> =
            getNeighbours(y, x).map { grid[it.first][it.second] }.filter { !it.visited }

        unvisitedNeighbours.forEach { block ->
            block.tentativeDistance = grid[y][x].tentativeDistance + block.heatLoss
        }

        //4 When we are done considering all of the unvisited neighbors of the current node, mark the current node as
        // visited and remove it from the unvisited set. A visited node will never be checked again (this is valid and optimal
        // in connection with the behavior in step 6.: that the next nodes to visit will always be in the order of
        // 'smallest distance from initial node first' so any visits after would have a greater distance).
        grid[y][x].visited = true

        //5 If the destination node has been marked visited (when planning a route between two specific nodes)
        // or if the smallest tentative distance among the nodes in the unvisited set is infinity (when planning a
        // complete traversal; occurs when there is no connection between the initial node and remaining unvisited nodes),
        // then stop. The algorithm has finished.
        if (grid[y][x].visited && x == 12 && y == 12) {
            println("Reached $y, $x")
            println(grid)
            exitProcess(1)
        }
        if (grid.flatten().filter { !it.visited }.minBy { it.tentativeDistance }.tentativeDistance == Int.MAX_VALUE) {
            println(grid)
            println("No moves left")
            exitProcess(1)
        }

        //6 Otherwise, select the unvisited node that is marked with the smallest tentative distance,
        // set it as the new current node, and go back to step 3.
        val cheapest = getNeighbours(y, x).filter { !grid[it.first][it.second].visited }
            .minByOrNull { grid[it.first][it.second].tentativeDistance }
        if (cheapest == null) {
            println(grid.flatten().filter { it.visited })
            println("No more moves left")
            exitProcess(1)
        }


        y = cheapest!!.first
        x = cheapest!!.second
        grid.print()
    }
}


fun getNeighbours(currentY: Int, currentX: Int): List<Pair<Int, Int>> {
    val neighbours: MutableList<Pair<Int, Int>> = mutableListOf()

    //up
    if (currentY - 1 > 0) {
        neighbours += (currentY - 1) to currentX
    }
    //down
    if (currentY + 1 <= maxY) {
        neighbours += (currentY + 1) to currentX
    }
    //left
    if (currentX - 1 >= 0) {
        neighbours += currentY to (currentX - 1)
    }
    //right
    if (currentX + 1 <= maxX) {
        neighbours += currentY to (currentX + 1)
    }
    return neighbours
}

private fun readGridArray(lines: List<String>): List<List<CityBlock>> {
    var cityBlocks: MutableList<MutableList<CityBlock>> = mutableListOf()
    lines.mapIndexed { index, l ->
        var line: MutableList<CityBlock> = mutableListOf()
        line += getCityBlocksFromLine(index, l)
        cityBlocks += line
    }
    return cityBlocks
}

data class CityBlock(val heatLoss: Int, var tentativeDistance: Int = Int.MAX_VALUE, var visited: Boolean = false) {
    fun toColorString(): String = when {
        visited -> "\u001b[31m" + this.heatLoss + "\u001b[0m"
//        heatLoss == 0 -> "\u001b[40m" + this.heatLoss + "\u001b[0m"
//        heatLoss == 1 -> "\u001b[42m" + this.heatLoss + "\u001b[0m"
//        heatLoss == 2 -> "\u001b[43m" + this.heatLoss + "\u001b[0m"
//        heatLoss == 3 -> "\u001b[44m" + this.heatLoss + "\u001b[0m"
//        heatLoss == 4 -> "\u001b[45m" + this.heatLoss + "\u001b[0m"
//        heatLoss == 5 -> "\u001b[46m" + this.heatLoss + "\u001b[0m"
//        heatLoss == 6 -> "\u001b[33m" + this.heatLoss + "\u001b[0m"
//        heatLoss == 7 -> "\u001b[43m" + this.heatLoss + "\u001b[0m"
//        heatLoss == 8 -> "\u001b[31m" + this.heatLoss + "\u001b[0m"
//        heatLoss == 9 -> "\u001b[47m" + this.heatLoss + "\u001b[0m"
        else -> "."
    }
}


private fun getCityBlocksFromLine(index: Int, line: String): List<CityBlock> {
    val matches = Regex("([0-9])").findAll(line)
    return matches.asStream().map { match ->
        CityBlock(
            match.value.toInt()
        )
    }.toList()
}

private fun List<List<CityBlock>>.print(visited: List<Point> = listOf()) {

    this.forEachIndexed { i, line ->
        print("${i.toString().padStart(3, '0')}: ")
        for (block in line) {
            print(block.toColorString())
        }
        println()
    }
}

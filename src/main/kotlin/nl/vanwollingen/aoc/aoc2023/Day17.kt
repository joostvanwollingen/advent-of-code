package nl.vanwollingen.aoc.aoc2023

import nl.vanwollingen.aoc.util.grid.Point
import nl.vanwollingen.aoc.util.PuzzleInputUtil
import nl.vanwollingen.aoc.util.grid.getManhattanNeighbours
import kotlin.streams.asStream



fun main() {
    val input = PuzzleInputUtil.load("2023/day17.test.input")
    val cityBlock = readGrid(input.lines())

    val adjacencyListGraph = AdjacencyList<CityBlock>()

    cityBlock.forEach { block ->
        adjacencyListGraph.createVertex(block)
    }

    cityBlock.forEach { block ->
        val vertex: Vertex<CityBlock> = adjacencyListGraph.getVertex(block)!!

        val neighbours = cityBlock.filter { otherBlock ->
            block.location.getManhattanNeighbours().contains(otherBlock.location)
        }

        neighbours.forEach {
            adjacencyListGraph.addDirectedEdge(
                vertex, adjacencyListGraph.getVertex(it)!!, it.heatLoss.toDouble()
            )
        }
    }
    println(adjacencyListGraph)


    val start = adjacencyListGraph.getVertex(cityBlock.first())!!
    val end = adjacencyListGraph.getVertex(cityBlock.last())!!
    val result = findShortestPath(
        adjacencyListGraph.adjacencyMap.keys,
        adjacencyListGraph.adjacencyMap.values.flatten().toList(),
        start,
        end
    )
    println(result)
    val shorte = result.shortestPath(start, end)
    println(shorte.sumOf { it.data.heatLoss })
}

fun findShortestPath(
    nodes: MutableSet<Vertex<CityBlock>>,
    edges: List<Edge<CityBlock>>,
    source: Vertex<CityBlock>,
    target: Vertex<CityBlock>
): ShortestPathResult {

    // Note: this implementation uses similar variable names as the algorithm given do.
    // We found it more important to align with the algorithm than to use possibly more sensible naming.

    val dist = mutableMapOf<Vertex<CityBlock>, Int>()
    val prev = mutableMapOf<Vertex<CityBlock>, Vertex<CityBlock>?>()
    val q = nodes

    q.forEach { v ->
        dist[v] = Integer.MAX_VALUE
        prev[v] = null
    }
    dist[source] = 0

    while (q.isNotEmpty()) {
        val u = q.minByOrNull { dist[it] ?: 0 }
        q.remove(u)

        if (u == target) {
            break // Found shortest path to target
        }
        edges
            .filter { it.source == u }
            .forEach { edge ->
                val v = edge.destination
                val alt = (dist[u] ?: 0) + edge.weight!!.toInt()
                if (alt < (dist[v] ?: 0)) {
                    dist[v] = alt
                    prev[v] = u
                }
            }
    }

    return ShortestPathResult(prev, dist, source, target)
}

class ShortestPathResult(
    val prev: Map<Vertex<CityBlock>, Vertex<CityBlock>?>,
    val dist: Map<Vertex<CityBlock>, Int>,
    val source: Vertex<CityBlock>,
    val target: Vertex<CityBlock>
) {

    fun shortestPath(
        from: Vertex<CityBlock> = source,
        to: Vertex<CityBlock> = target,
        list: List<Vertex<CityBlock>> = emptyList()
    ): List<Vertex<CityBlock>> {
        val last = prev[to] ?: return if (from == to) {
            list + to
        } else {
            emptyList()
        }
        return shortestPath(from, last, list) + to
    }

    fun shortestDistance(): Int? {
        val shortest = dist[target]
        if (shortest == Integer.MAX_VALUE) {
            return null
        }
        return shortest
    }
}

data class CityBlock(val heatLoss: Int, val location: Point) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CityBlock

        if (heatLoss != other.heatLoss) return false
        if (location != other.location) return false

        return true
    }

    override fun hashCode(): Int {
        var result = heatLoss
        result = 31 * result + location.hashCode()
        return result
    }
}

data class Vertex<T>(val index: Int, val data: T)
data class Edge<T>(val source: Vertex<T>, val destination: Vertex<T>, val weight: Double? = null)

class AdjacencyList<T> {
    val adjacencyMap = mutableMapOf<Vertex<T>, ArrayList<Edge<T>>>()

    fun getVertex(data: T): Vertex<T>? {
        return adjacencyMap.filter { it.key.data == data }.keys.first()
    }

    fun createVertex(data: T): Vertex<T> {
        val vertex = Vertex(adjacencyMap.count(), data)
        adjacencyMap[vertex] = arrayListOf()
        return vertex
    }

    fun addDirectedEdge(source: Vertex<T>, destination: Vertex<T>, weight: Double? = 0.0) {
        val edge = Edge(source, destination, weight)
        adjacencyMap[source]?.add(edge)
    }

    override fun toString(): String {
        return buildString {
            adjacencyMap.forEach { (vertex, edges) ->
                val edgeString = edges.joinToString { it.destination.data.toString() }
                append("${vertex.data} -> [$edgeString]\n")
            }
        }
    }

    fun findShortestRoute(start: Vertex<T>, end: Vertex<T>) {
        var current = start
        val visited: MutableSet<Vertex<T>> = mutableSetOf()

        while (current != end) {
            visited += current
            current = adjacencyMap[current]!!.filter { edge -> !visited.contains(edge.destination) }
                .minBy { it.weight!! }.destination
        }
        println(visited)
    }
}

private fun readGrid(lines: List<String>): List<CityBlock> {
    val cityBlocks: MutableList<CityBlock> = mutableListOf()
    lines.mapIndexed { index, line ->
        cityBlocks += getCityBlocksFromLine(index, line)
    }
    return cityBlocks
}


private fun getCityBlocksFromLine(index: Int, line: String): List<CityBlock> {
    val matches = Regex("([0-9])").findAll(line)
    return matches.asStream().map { match ->
        CityBlock(
            match.value.toInt(), Point(index + 1, match.range.first + 1)
        )
    }.toList()
}


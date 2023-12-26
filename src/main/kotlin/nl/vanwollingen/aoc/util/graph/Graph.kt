package nl.vanwollingen.aoc.util.graph

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
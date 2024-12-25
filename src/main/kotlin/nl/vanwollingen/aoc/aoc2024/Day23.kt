package nl.vanwollingen.aoc.aoc2024

import nl.vanwollingen.aoc.util.Puzzle

fun main() = Day23.solve()

object Day23 : Puzzle(exampleInput = false) {

    private val connections = parseInput()

    override fun parseInput(): Map<String, Set<String>> {
        val connectionMap = mutableMapOf<String, MutableSet<String>>()

        extractPairs().forEach { (from, to) ->
            val fromConnections = connectionMap.getOrDefault(from, mutableSetOf())
            fromConnections.add(to)
            connectionMap[from] = fromConnections.sorted().toMutableSet()

            val toConnections = connectionMap.getOrDefault(to, mutableSetOf())
            toConnections.add(from)
            connectionMap[to] = toConnections.sorted().toMutableSet()
        }
        return connectionMap
    }

    private fun extractPairs(): List<Pair<String, String>> {
        val connections = input.lines().map {
            val (from, to) = it.split("-")
            from to to
        }
        return connections
    }

    override fun part1(): Any {
        val trips: MutableSet<Triple<String, String, String>> = mutableSetOf()
        val withT = connections.keys.filter { it.startsWith("t") }

        withT.forEach { computer ->
            val computerConnections = connections[computer] ?: throw Error("fail")
            computerConnections.forEach { conn ->
                val newConnections = connections[conn] ?: throw Error("fail")
                val sharedConnections = newConnections.intersect(computerConnections)
                sharedConnections.forEach { s ->
                    val (one, two, three) = listOf(computer, conn, s).sorted()
                    trips.add(Triple(one, two, three))
                }
            }
        }
        return trips.size
    }


    override fun part2(): Any {
        val networks = mutableSetOf<Set<String>>()

        connections.keys.forEach { computer ->
            val network = mutableSetOf(computer)
            connections[computer]!!.forEach {
                if (isConnectedToAll(network + it)) network.add(it)
            }
            networks.add(network)
        }

        return networks.maxBy { it.size }.sorted().joinToString(",")
    }

    private fun isConnectedToAll(set: Set<String>): Boolean {
        set.forEach { c ->
            set.forEach inner@{ d ->
                if (d == c) return@inner
                if (!connections[c]!!.contains(d)) return false
            }
        }
        return true
    }
}
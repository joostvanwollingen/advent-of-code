package nl.vanwollingen.aoc.aoc2015

import nl.vanwollingen.aoc.util.Puzzle

fun main() {
    val d9 = Day09(2015, 9)
    d9.solvePart1()
//    d9.solvePart2()
}

class Day09(year: Int, day: Int) : Puzzle(year, day) {
    val locations: MutableSet<String> = input.lines().map { line -> line.split(" to ")[0] }.distinct().plus("Arbre").toMutableSet()
    val dist = Array(locations.size) { DoubleArray(locations.size) }
    val next = Array(locations.size) { IntArray(locations.size) }
    override fun solvePart1() {

        for (i in 0 until next.size) {
            for (j in 0 until next.size) {
                if (i != j) next[i][j] = j + 1
            }
        }

        input.lines().forEach { line ->
            val start = line.split(" to ")[0]
            val end = line.split(" to ")[1].split(" = ")[0]
            val cost = line.split(" to ")[1].split(" = ")[1]

            val startIndex = locations.indexOf(start)
            val endIndex = locations.indexOf(end)
            dist[endIndex][startIndex] = cost.toDouble()
            dist[startIndex][endIndex] = cost.toDouble()
        }

        for (k in locations.indices) {
            for (i in locations.indices) {
                for (j in locations.indices) {
                    if (dist[i][j] > dist[i][k] + dist[k][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j]
                        next[i][j] = next[i][k]
                    }
                }
            }
        }

        printResult(dist, next)
        printTable()


    }

    private fun printResult(dist: Array<DoubleArray>, next: Array<IntArray>) {
        var u: Int
        var v: Int
        var path: String
        println("pair     dist    path")
        for (i in 0 until next.size) {
            for (j in 0 until next.size) {
                if (i != j) {
                    u = i + 1
                    v = j + 1
                    path = ("%d -> %d    %2d     %s").format(u, v, dist[i][j].toInt(), u)
                    do {
                        u = next[u - 1][v - 1]
                        path += " -> " + u
                    } while (u != v)
                    println(path)
                }
            }
        }
    }

    fun printTable() {
        print("\t\t                     ")
        locations.forEach { print("${it.subSequence(0, 3).padStart(21, ' ')}\t") }
        println("")
        for (i in locations.indices) {
            print("${locations.toList()[i].padStart(21, ' ')}:\t")
            for (j in locations.indices) {
                print("${dist[i][j].toString().padStart(21, ' ')}\t")
            }
            println("")
        }
    }

    override fun solvePart2() {
        TODO("Not yet implemented")
    }

}
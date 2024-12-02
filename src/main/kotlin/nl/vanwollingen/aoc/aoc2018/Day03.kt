package nl.vanwollingen.aoc.aoc2018

import nl.vanwollingen.aoc.util.Puzzle

fun main() {
    val d3 = Day03()
    d3.solvePart1()
    d3.solvePart2()
}

class Day03 : Puzzle() {

    private val claims: List<Claim> = parseInput()

    override fun part1() {
        val fabric: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()

        claims.forEach { claim ->
            val (x, y) = claim.location
            for (i in x..<x + claim.width) {
                for (j in y..<y + claim.height) {
                    fabric.merge(i to j, 1) { k, v ->
                        k + v
                    }
                }
            }
        }
        log(fabric.count { f -> f.value >= 2 })
    }

    override fun part2() {
       val fabric: MutableMap<Pair<Int, Int>, MutableList<Int>> = mutableMapOf()

        claims.forEach { claim ->
            val (x, y) = claim.location
            for (i in x..<x + claim.width) {
                for (j in y..<y + claim.height) {
                    val current:MutableList<Int> = fabric.getOrDefault(i to j, mutableListOf())
                    current.add(claim.id)
                    fabric[i to j] = current
                }
            }
        }

        val doubles = fabric.filterValues { it.size > 1 }.values.flatten().toSet()

        println(fabric.values.flatten().minus(doubles).first())
    }

    override fun parseInput(): List<Claim> {
        return input.lines().map { l ->
            parseClaims(l)
        }
    }

    private fun parseClaims(l: String): Claim {
        val regex = Regex("#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)")
        val matches = regex.find(l)
        requireNotNull(matches)
        return Claim(
            matches.groupValues[1].toInt(),
            matches.groupValues[2].toInt() + 1 to matches.groupValues[3].toInt() + 1,
            matches.groupValues[4].toInt(),
            matches.groupValues[5].toInt()
        )
    }

    data class Claim(val id: Int, val location: Pair<Int, Int>, val width: Int, val height: Int)
}
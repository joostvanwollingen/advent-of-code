package nl.vanwollingen.aoc.aoc2015

import nl.vanwollingen.aoc.util.Puzzle
import nl.vanwollingen.aoc.util.combinatorics.combinations
import kotlin.streams.asStream

fun main() {
    val d24 = Day24(2015, 24)
    d24.test()
//    d24.solvePart1()
}

class Day24(year: Int, day: Int) : Puzzle(year, day) {

    val packages = parseInput()
    val equalThirdWeight = packages.sum() / 3

    override fun parseInput() = input.lines().map { it.toInt() }.toList()
    fun test() {
        val packages = listOf(1, 2, 3, 4, 5, 7, 8, 9, 10, 11)
        val equalThirdWeight = packages.sum() / 3

        val possibleThirds: MutableList<List<Int>> = mutableListOf()

        var smallestFirstCompartment = Int.MAX_VALUE
        var lowestQuantumEntanglement = Long.MAX_VALUE

        for (i in 2..<packages.size) {
            possibleThirds += packages.combinations(i).asStream().parallel().filter { c -> c.sum() == equalThirdWeight }.toList()
        }

        possibleThirds.combinations(3).asStream().parallel().forEach { c ->
            val allPackagesUsed = c.sumOf { it.size } == packages.size
            val first = c[0].toSet()
            val second = c[1].toSet()
            val third = c[2].toSet()
            val noIntersection = first.intersect(second).isEmpty() && second.intersect(third).isEmpty() && first.intersect(third).isEmpty()

            if (allPackagesUsed && noIntersection) {
                val minFirstCompartment = c.sortedBy { it.size }[0].size
                val quantumEntanglement = c[0].reduce { acc, i -> acc * i }
                if (minFirstCompartment <= smallestFirstCompartment) {
                    if (quantumEntanglement <= lowestQuantumEntanglement) {
                        smallestFirstCompartment = minFirstCompartment
                        lowestQuantumEntanglement = quantumEntanglement.toLong()
                    }
                }
            }
        }
        println(smallestFirstCompartment)
        println(lowestQuantumEntanglement)
    }


    override fun solvePart1() {
        println(packages)
        println(equalThirdWeight)
    }

    override fun solvePart2() {
        TODO("Not yet implemented")
    }
}
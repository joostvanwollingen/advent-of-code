package nl.vanwollingen.aoc.aoc2015

import nl.vanwollingen.aoc.util.Puzzle

fun main() {
    val d13 = Day13()
    d13.solvePart1()
    d13.solvePart2()
}

class Day13() : Puzzle() {

    var seatings: Map<String, Map<String, Int>> = parseInput()
    override fun parseInput(): Map<String, Map<String, Int>> {
        val seats: MutableMap<String, MutableMap<String, Int>> = mutableMapOf()
        input.lines().forEach { line ->
            val name = line.split(" ").first()
            val other = line.dropLast(1).split(" ").last()
            val positive = line.contains(" gain ")
            val units = Regex("\\d+").findAll(line).toList().first().value.toInt() * if (positive) 1 else -1
            if (seats[name] == null) seats[name] = mutableMapOf()
            seats[name] = seats[name]!!.plus(other to units).toMutableMap()
        }
        return seats
    }


    override fun part1() {
        val options: MutableMap<List<String>, Int> = mutableMapOf()
        for (i in seatings.keys) {
            for (k in seatings.keys) {
                for (j in seatings.keys) {
                    for (l in seatings.keys) {
                        for (m in seatings.keys) {
                            for (n in seatings.keys) {
                                for (o in seatings.keys) {
                                    for (p in seatings.keys) {
                                        val list = listOf(i, k, j, l, m, n, o, p)
                                        if (list.distinct().size == 8) {
                                            options += list to getScore(list, seatings)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        println("${options.maxBy { it.value }}")
    }

    override fun part2() {
        val adjustedSeatings: MutableMap<String, Map<String, Int>> = mutableMapOf()

        seatings.forEach { s ->
            val added = s.value.toMutableMap()
            added["Myself"] = 0
            adjustedSeatings[s.key] = added
        }

        adjustedSeatings["Myself"] = mapOf("Alice" to 0, "Bob" to 0, "Frank" to 0, "Carol" to 0, "Eric" to 0, "David" to 0, "George" to 0, "Mallory" to 0)

        val options: MutableMap<List<String>, Int> = mutableMapOf()
        for (i in adjustedSeatings.keys) {
            for (k in adjustedSeatings.keys) {
                for (j in adjustedSeatings.keys) {
                    for (l in adjustedSeatings.keys) {
                        for (m in adjustedSeatings.keys) {
                            for (n in adjustedSeatings.keys) {
                                for (o in adjustedSeatings.keys) {
                                    for (p in adjustedSeatings.keys) {
                                        for (q in adjustedSeatings.keys) {
                                            val list = listOf(i, k, j, l, m, n, o, p, q)
                                            if (list.distinct().size == 9) {
                                                options += list to getScore(list, adjustedSeatings)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        println("${options.maxBy { it.value }}")
    }

    private fun getScore(names: List<String>, seats: Map<String, Map<String, Int>>): Int {
        var scored = 0
        names.windowed(2, 1) { seat ->
            scored += seats[seat.first()]!![seat.last()]!!
            scored += seats[seat.last()]!![seat.first()]!!
        }
        scored += seats[names[names.lastIndex]]!![names[0]]!!
        scored += seats[names[0]]!![names[names.lastIndex]]!!

        return scored
    }
}
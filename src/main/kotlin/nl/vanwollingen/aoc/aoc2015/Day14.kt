package nl.vanwollingen.aoc.aoc2015

import nl.vanwollingen.aoc.util.Puzzle

fun main() {
    val d14 = Day14(2015, 14)
    d14.solvePart1()
    d14.solvePart2()
}

class Day14(year: Int, day: Int) : Puzzle(year, day) {

    private val reindeers = parseInput()

    override fun solvePart1() {
        println(reindeers.map { reindeer -> reindeer.name to reindeer.distance(2503) }.maxBy { it.second })
    }

    override fun solvePart2() {
        val scores: MutableMap<String, Int> = mutableMapOf()
        reindeers.forEach { scores[it.name] = 0 }

        for (i in 1..2503) {
            val raceLead = reindeers.map { reindeer -> reindeer.name to reindeer.distance(i) }.groupBy { it.second }
            raceLead.maxBy { it.key }.value.forEach { r ->
                scores[r.first] = scores[r.first]!! + 1
            }
        }

        println(scores.maxBy { it.value })
    }

    override fun parseInput(): List<Reindeer> = input.lines().map { line ->
        Reindeer.fromString(line)
    }

    data class Reindeer(val name: String, val speed: Int, val stamina: Int, val rest: Int) {

        fun distance(duration: Int): Int {
            if (duration <= stamina) return duration * speed

            var racing = true
            var nextRest = stamina
            var nextRace = 0
            var distanceTraveled = 0

            for (i in 1..duration) {
                if (racing) distanceTraveled += speed

                if (nextRace == i) {
                    racing = true
                    nextRest = i + stamina
                }
                if (nextRest == i) {
                    racing = false
                    nextRace = i + rest
                }
            }

            return distanceTraveled
        }

        companion object {
            fun fromString(line: String): Reindeer {
                val groups = Regex("(.+) can fly (\\d+) km/s for (\\d+) seconds, but then must rest for (\\d+) seconds.").findAll(line).toList().first().groupValues
                return Reindeer(groups[1], groups[2].toInt(), groups[3].toInt(), groups[4].toInt())
            }
        }
    }
}
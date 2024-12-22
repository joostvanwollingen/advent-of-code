package nl.vanwollingen.aoc.aoc2024

import nl.vanwollingen.aoc.util.Puzzle

fun main() = Day22.solve()

object Day22 : Puzzle(exampleInput = false) {
    private val secrets = input.lines().map { it.toLong() }

    override fun part1() = secrets.sumOf {
        var secret = Secret(it)
        repeat(2000) {
            secret = secret.evolve()
        }
        secret.value
    }


    override fun part2(): Any {
        val totalScores = mutableMapOf<List<Long>, Long>()

        secrets.forEach { secret ->
            var s = Secret(secret)
            val consecutiveChanges = mutableMapOf<List<Long>, Long>()
            var list = mutableListOf<Long>()

            repeat(2000) {
                s.previousPrice?.let {
                    list.add(s.price - s.previousPrice!!)
                    if (list.size == 4) {
                        if (consecutiveChanges[list] == null) {
                            consecutiveChanges[list] = s.price
                        }
                        list = list.drop(1).toMutableList()
                    }
                }
                s = s.evolve()
            }
            consecutiveChanges.forEach { (key, value) ->
                totalScores[key] = totalScores.getOrDefault(key, 0) + value
            }
        }

        return totalScores.maxBy { it.value }.value
    }

    class Secret(val value: Long, val previousPrice: Long? = null) {

        val price: Long
            get() = ("$value".last() - '0'.code).toLong()

        fun evolve(): Secret {
            var new = prune(mix(value, (value * 64)))
            new = prune(mix(new, (new / 32)))
            new = prune(mix(new, (new * 2048)))
            return Secret(new, price)
        }

        private fun mix(given: Long, secret: Long) = given xor secret
        private fun prune(secret: Long) = secret % 16777216

        override fun toString(): String {
            return "$value"
        }
    }
}
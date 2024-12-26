package nl.vanwollingen.aoc.aoc2020

import nl.vanwollingen.aoc.util.Puzzle

fun main() = Day02.solve()

object Day02 : Puzzle(exampleInput = false) {

    private val policyAndPasswords = parseInput()

    override fun part1() =
        policyAndPasswords.sumOf { (policy, password) ->
            if (policy.isValid(password)) 1 else 0L
        }

    override fun part2() =
        policyAndPasswords.sumOf { (policy, password) ->
            if (policy.isValid(password, false)) 1 else 0L
        }

    data class Policy(val min: Int, val max: Int, val char: String) {
        fun isValid(password: String, oldPolicy: Boolean = true): Boolean {
            if (oldPolicy) {
                val matches = char.toRegex().findAll(password).toList()
                return matches.size in min..max
            }

            val first = password[min - 1].toString()
            val second = password[max - 1].toString()
            return first == char && second != char || first != char && second == char
        }
    }

    override fun parseInput() = input.lines().map { line ->
        val (policy, password) = line.split(": ")
        val (count, char) = policy.split(" ")
        val (min, max) = count.split("-")
        Policy(min.toInt(), max.toInt(), char) to password
    }
}
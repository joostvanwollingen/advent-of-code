package nl.vanwollingen.aoc.aoc2024

import nl.vanwollingen.aoc.util.Puzzle

fun main() = Day07.solve()

object Day07 : Puzzle(exampleInput = false, printDebug = false) {

    private val equations = parseInput()
    private val operators = listOf('+', '*', '|')

    override fun parseInput(): List<Pair<ULong, List<ULong>>> = input.lines().map { line ->
        val (testValue, numbersToSplit) = line.split(": ")
        val numbers = numbersToSplit.split(" ").map { it.toULong() }
        testValue.toULong() to numbers
    }.toList()

    override fun part1(): ULong =
        equations
            .filter { (k, v) -> canBeOperated(true, k, v) }
            .also { log("Valid entries: ${it.size}") }
            .sumOf { (k, _) -> k }

    override fun part2(): ULong =
        equations
            .filter { (k, v) -> canBeOperated(false, k, v) }
            .also { log("Valid entries: ${it.size}") }
            .sumOf { (k, _) -> k }

    private fun canBeOperated(partOne: Boolean, testValue: ULong, numbers: List<ULong>): Boolean {
        val activeOperators = if (partOne) operators.minus('|') else operators
        val operatorCombinationList: List<List<Char>> =
            generatePermutations(numbers.size - 1, activeOperators).map { it.toCharArray().toList() }

        operatorCombinationList.forEach { combination ->
            val operationResult: ULong = numbers.reduceIndexed { i, acc, b ->
                if (acc > testValue) return@forEach
                val operator = combination[i - 1]
                when (operator) {
                    '+' -> {
                        (acc + b)
                    }

                    '*' -> {
                        (acc * b)
                    }

                    '|' -> {
                        "$acc$b".toULong()
                    }

                    else -> {
                        throw Error("fail")
                    }
                }
            }

            if (operationResult == testValue) {
                return true
            }
        }
        return false
    }

    private fun generatePermutations(length: Int, options: List<Char>): List<String> {
        if (length == 0) return listOf("")
        val smallerPermutations = generatePermutations(length - 1, options)
        return smallerPermutations.flatMap { perm ->
            options.map { option -> perm + option }
        }
    }
}
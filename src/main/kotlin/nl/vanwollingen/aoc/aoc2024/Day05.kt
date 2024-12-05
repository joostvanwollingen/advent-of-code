package nl.vanwollingen.aoc.aoc2024

import nl.vanwollingen.aoc.util.Puzzle

fun main() = Day05.solve()

object Day05 : Puzzle(exampleInput = false, output = true) {

    val data: Pair<Map<Int, List<Int>>, List<List<Int>>> = parseInput()

    override fun parseInput(): Pair<Map<Int, List<Int>>, List<List<Int>>> {
        val (rules, updates) = input.split("\n\n")
        val ruleMap = rules.lines().map {
            val (page, before) = it.split("|")
            page.toInt() to before.toInt()
        }.groupBy(keySelector = { it.first }, valueTransform = { it.second })

        return ruleMap to updates.lines().map { it.split(",") }.map { it.map { it.toInt() } }
    }


    override fun part1(): Int {
        val (rules, updates) = data

        val validUpdates = updates.filter { update ->
            update.respectsTheRules(rules)
        }

        return validUpdates.sumOf { it[it.size / 2] }
    }

    override fun part2(): Int {
        val (rules, updates) = data

        val invalidUpdates = updates.filterNot { update ->
            update.respectsTheRules(rules)
        }

        val sorted = invalidUpdates.map {
            it.sortedWith { a, b ->
                when {
                    rules[b]?.contains(a) == true -> -1 // a must come before b
                    rules[a]?.contains(b) == true -> 1  // b must come before a
                    else -> 0 // no specific order required
                }
            }
        }

        return sorted.sumOf { it[it.size / 2] }
    }

    private fun List<Int>.respectsTheRules(ruleMap: Map<Int, List<Int>>): Boolean {
        val seen = mutableSetOf<Int>()
        this.forEach { entry ->
            val shouldBeBefore = ruleMap[entry]
            shouldBeBefore?.let {
                if (shouldBeBefore.intersect(seen).isNotEmpty()) {
                    return false
                }
            }
            seen += entry
        }
        return true
    }
}
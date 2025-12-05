package nl.vanwollingen.aoc.aoc2025

import nl.vanwollingen.aoc.util.Puzzle

fun main() = Day05.solve()

object Day05 : Puzzle(true, false) {

    private val ingredients = parseInput()

    override fun parseInput(): Pair<List<LongRange>, List<Long>> {
        val (ranges, ingredients) = input.split("\n\n").map { it.lines() }
        val freshIngredientRanges = ranges.map { it.split("-")[0].toLong()..it.split("-")[1].toLong() }
        val ingredientIds = ingredients.map { it.trim().toLong() }
        return freshIngredientRanges to ingredientIds
    }

    override fun part1(): Int {
        val (freshIngredientRanges, ingredientIds) = ingredients
        var freshIngredients = 0
        for (ingredient in ingredientIds) {
            if (freshIngredientRanges.any { it.contains(ingredient) }) freshIngredients++
        }
        return freshIngredients
    }

    override fun part2() = totalUniqueCount(ingredients.first)

    private fun totalUniqueCount(ranges: List<LongRange>): Long {
        if (ranges.isEmpty()) return 0

        val sorted = ranges.sortedBy { it.first }

        var mergedStart = sorted[0].first
        var mergedEnd   = sorted[0].last
        var total = 0L

        for (i in 1 until sorted.size) {
            val r = sorted[i]

            if (r.first <= mergedEnd + 1) {
                // Overlapping or touching → merge
                mergedEnd = maxOf(mergedEnd, r.last)
            } else {
                // No overlap → add previous segment and start a new one
                total += (mergedEnd - mergedStart + 1)
                mergedStart = r.first
                mergedEnd = r.last
            }
        }

        // Add final segment
        total += (mergedEnd - mergedStart + 1)

        return total
    }
}

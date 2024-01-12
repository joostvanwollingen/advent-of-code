package nl.vanwollingen.aoc.aoc2015

import nl.vanwollingen.aoc.util.Puzzle
import kotlin.math.max

fun main() {
    val d15 = Day15()
    d15.part1()
    d15.part2()
}

class Day15() : Puzzle() {

    private val ingredients = parseInput()
    private val sugar = ingredients.first { it.name == "Sugar" }
    private val candy = ingredients.first { it.name == "Candy" }
    private val chocolate = ingredients.first { it.name == "Chocolate" }
    private val sprinkles = ingredients.first { it.name == "Sprinkles" }
    override fun part1() {
        var highest = 0L
        for (i in 100 downTo 0) {
            for (j in 100 downTo 0) {
                for (k in 100 downTo 0) {
                    for (l in 100 downTo 0) {
                        if (i + j + k + l == 100) {
                            val score = getScore(sugar to i, candy to j, chocolate to k, sprinkles to l)
                            highest = if (highest > score) highest else score
                        }
                    }
                }
            }
        }
        println(highest)
    }

    override fun part2() {
        var highest = 0L
        for (i in 100 downTo 0) {
            for (j in 100 downTo 0) {
                for (k in 100 downTo 0) {
                    for (l in 100 downTo 0) {
                        if (i + j + k + l == 100 && getCalories(sugar to i, candy to j, chocolate to k, sprinkles to l) == 500L) {
                            val score = getScore(sugar to i, candy to j, chocolate to k, sprinkles to l)
                            highest = if (highest > score) highest else score
                        }
                    }
                }
            }
        }
        println(highest)
    }

    private fun getScore(vararg ingredients: Pair<Ingredient, Int>): Long {
        val capacity = max(0, ingredients.sumOf { it.first.capacity.toLong() * it.second })
        val durability = max(0, ingredients.sumOf { it.first.durability.toLong() * it.second })
        val flavor = max(0, ingredients.sumOf { it.first.flavor.toLong() * it.second })
        val texture = max(0, ingredients.sumOf { it.first.texture.toLong() * it.second })

        return capacity * durability * flavor * texture
    }

    private fun getCalories(vararg ingredients: Pair<Ingredient, Int>): Long = ingredients.sumOf { it.first.calories.toLong() * it.second }

    override fun parseInput(): List<Ingredient> = input.lines().map { line ->
        Ingredient.fromString(line)
    }

    data class Ingredient(
            val name: String,
            val capacity: Int,
            val durability: Int,
            val flavor: Int,
            val texture: Int,
            val calories: Int,
    ) {
        companion object {
            fun fromString(line: String): Ingredient {
                val properties = Regex("(.*): capacity (-?\\d+), durability (-?\\d+), flavor (-?\\d+), texture (-?\\d+), calories (-?\\d+)").findAll(line).toList().first().groupValues
                return Ingredient(properties[1], properties[2].toInt(), properties[3].toInt(), properties[4].toInt(), properties[5].toInt(), properties[6].toInt())
            }
        }
    }
}
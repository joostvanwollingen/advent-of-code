package nl.vanwollingen.aoc.aoc2023

import nl.vanwollingen.aoc.util.PuzzleInputUtil
import java.time.Instant
import java.util.*

fun main() {
    val d4 = Day4()
    d4.solvePart1()
    d4.solvePart2()
}

class Day4 {
    private val scratchCards = PuzzleInputUtil.parse("day4.input", Scratchcard::class.java).toList()

    fun solvePart1() {
        val points = scratchCards.sumOf { it.points }
        println(points)
    }

    fun solvePart2() {
        println("${Date.from(Instant.now())}")
        val finalCards: List<Scratchcard> = getFinalCards(scratchCards, emptyList(), scratchCards)
        println(finalCards.size)
        println("${Date.from(Instant.now())}")
    }

    private fun getFinalCards(allCards: List<Scratchcard>, finalCards: List<Scratchcard>, copiedCards: List<Scratchcard>): List<Scratchcard> {
        if (copiedCards.isNotEmpty()) {
            val newFinalcards: List<Scratchcard> = finalCards.plus(copiedCards)
            val copiesToMake = copiedCards.flatMap { card -> card.awardsCopiesOf }
            val cardCopies = copiesToMake.map { copyId -> allCards.filter { card -> card.id == copyId } }.flatten()
            return getFinalCards(allCards, newFinalcards, cardCopies)
        }
        return finalCards
    }

    data class Scratchcard(val input: String) {
        val id = Regex("Card\\W*(\\d*):").find(input)!!.groupValues[1].toInt()
        private val winningNumbers: List<String> = Regex("Card\\W*\\d*:(.*)").find(input)!!.groupValues[1].split("|")[0].strip().split(" ").filter { it.isNotEmpty() }
        private val numbers: List<String> = Regex("Card\\W*\\d*:(.*)").find(input)!!.groupValues[1].split("|")[1].strip().split(" ").filter { it.isNotEmpty() }
        private val numberOfMatches = winningNumbers.intersect(numbers.toSet()).size
        val points = getPoints(numberOfMatches)
        val awardsCopiesOf = getCopyIds(id, numberOfMatches)

        private fun getCopyIds(id: Int, numberOfMatches: Int): List<Int> = if (numberOfMatches != 0) id.plus(1).rangeTo(id + numberOfMatches).map { it } else emptyList()

        private fun getPoints(numberOfMatches: Int): Int = when (numberOfMatches) {
            0 -> 0
            1 -> 1
            else -> Math.pow(2.0, numberOfMatches - 1.toDouble()).toInt()
        }

        override fun toString(): String {
            return "Scratchcard(id='$id', winningNumbers=$winningNumbers, numbers=$numbers)"
        }
    }
}
package nl.vanwollingen.aoc.aoc2023

import nl.vanwollingen.aoc.util.AocUtil
import java.time.Instant
import java.util.*

fun main() {
    val scratchCards = AocUtil.parse("day4.input", Scratchcard2::class.java) as List<Scratchcard2>
    println("${Date.from(Instant.now())}")
    val finalCards: List<Scratchcard2> = getFinalCards(scratchCards, emptyList(), scratchCards)
    println(finalCards.size)
    println("${Date.from(Instant.now())}")
}

fun getFinalCards(
    allCards: List<Scratchcard2>, finalCards: List<Scratchcard2>, copiedCards: List<Scratchcard2>
): List<Scratchcard2> {
    if (copiedCards.isNotEmpty()) {
        val newFinalcards: List<Scratchcard2> = finalCards.plus(copiedCards)
        val copiesToMake = copiedCards.flatMap { card -> card.awardsCopiesOf }
        val copiedCards = copiesToMake.map { copyId -> allCards.filter { card -> card.id == copyId } }.flatten()
        return getFinalCards(allCards, newFinalcards, copiedCards)
    }
    return finalCards
}

data class Scratchcard2(val input: String) {
    val id = Regex("Card\\W*(\\d*):").find(input)!!.groupValues[1].toInt()
    val winningNumbers: List<String> =
        Regex("Card\\W*\\d*:(.*)").find(input)!!.groupValues[1].split("|")[0].strip().split(" ")
            .filter { it.isNotEmpty() }
    val numbers: List<String> = Regex("Card\\W*\\d*:(.*)").find(input)!!.groupValues[1].split("|")[1].strip().split(" ")
        .filter { it.isNotEmpty() }
    val hasMatch: Boolean = winningNumbers.intersect(numbers).isNotEmpty()
    val numberOfMatches = winningNumbers.intersect(numbers).size
    val points = getPoints(numberOfMatches)
    val awardsCopiesOf = getCopyIds(id, numberOfMatches)

    private fun getCopyIds(id: Int, numberOfMatches: Int): List<Int> =
        if (numberOfMatches != 0) id.plus(1).rangeTo(id + numberOfMatches).map { it } else emptyList()

    private fun getPoints(numberOfMatches: Int): Int = when (numberOfMatches) {
        0 -> 0
        1 -> 1
        else -> Math.pow(2.0, numberOfMatches - 1.toDouble()).toInt()
    }

    override fun toString(): String {
        return "Scratchcard(id='$id', winningNumbers=$winningNumbers, numbers=$numbers)"
    }
}
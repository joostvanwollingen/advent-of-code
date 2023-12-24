package nl.vanwollingen.aoc.aoc2023

import nl.vanwollingen.aoc.util.PuzzleInputUtil

fun main() {
    val input1 = PuzzleInputUtil.parse("2023/day7.input") {
        Day7.Hand(it, false)
    }.toList()

    val input2 = PuzzleInputUtil.parse("2023/day7.input") {
        Day7.Hand(it, true)
    }.toList()

    Day7().solvePart1(input1)
    Day7().solvePart1(input2)
}

class Day7 {
    fun solvePart1(cards: List<Hand>) {
        val sortedCards = cards.sorted()
        val totalWinnings = sortedCards.mapIndexed { index, hand -> hand to (index + 1) * hand.bid }
        totalWinnings.forEachIndexed { i, t ->
            println("${(i + 1).toString().padStart(4)}\t ${
                t.second.toString().padStart(6)
            }\t ${t.first.type}\t ${t.first.cards[0]}${t.first.cards[1]}${t.first.cards[2]}${t.first.cards[3]}${t.first.cards[4]}\t ${t.first.bid}")
        }
        println(totalWinnings.sumOf { it.second })
    }

    enum class HandType {
        HighCard, OnePair, TwoPair, ThreeOfAKind, FullHouse, FourOfAKind, FiveOfAKind,
    }

    class Hand(input: String, private val jacksEnabled: Boolean = false) : Comparable<Hand> {

        private val split = input.split(" ")
        val cards = split[0].toCharArray().map { Card(it.toString(), jacksEnabled) }
        val bid = split[1].toLong()
        private val counts = getCardCount(cards)
        val type: HandType = getType(counts, jacksEnabled)

        private fun getType(cardCount: Map<String, Int>, jacksEnabled: Boolean): HandType {
            if (jacksEnabled) {
                return if (cardCount["J"] == null || cardCount["J"] == 5) {
                    defaultCardType(cardCount)
                } else {
                    val newCardCount = cardCount.toMutableMap()
                    val notJCount = cardCount.filter { it.key != "J" }.maxBy { it.value }
                    val jCount = cardCount["J"] ?: 0
                    newCardCount.remove("J")
                    newCardCount[notJCount.key] = newCardCount[notJCount.key]!! + jCount
                    defaultCardType(newCardCount)
                }
            } else {
                return defaultCardType(cardCount)
            }
        }

        private fun defaultCardType(cardCount: Map<String, Int>): HandType {
            if (cardCount.filter { entry -> entry.value == 5 }.isNotEmpty()) return HandType.FiveOfAKind
            if (cardCount.filter { entry -> entry.value == 4 }.isNotEmpty()) return HandType.FourOfAKind
            if (cardCount.filter { entry -> entry.value == 3 }.isNotEmpty() && cardCount.filter { entry -> entry.value == 2 }.isNotEmpty()) return HandType.FullHouse
            if (cardCount.filter { entry -> entry.value == 3 }.isNotEmpty()) return HandType.ThreeOfAKind ///but can't be because FH above .filter { entry -> entry.value != 2 }
            if (cardCount.filter { entry -> entry.value == 2 }.size == 2) return HandType.TwoPair
            if (cardCount.filter { entry -> entry.value == 2 }.isNotEmpty()) return HandType.OnePair
            if (cardCount.filter { entry -> entry.value == 1 }.isNotEmpty()) return HandType.HighCard
            throw Exception("Card type failure $cardCount")
        }

        private fun getCardCount(cards: List<Card>) = cards.groupingBy { it.face }.eachCount()

        override fun compareTo(other: Hand): Int = when (this.type) {
            other.type -> compareRank(this, other)
            else -> this.type compareTo other.type

        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Hand

            if (type != other.type) return false
            if (compareRank(this, other) != 0) return false
            return true
        }

        private fun compareRank(hand: Hand, other: Hand): Int {
            for (i in 0..4) {
                if (hand.cards[i] == other.cards[i]) continue
                if (hand.cards[i] < other.cards[i]) return -1
                if (hand.cards[i] > other.cards[i]) return 1
            }
            return 0
        }
    }

    class Card(val face: String, jacksEnabled: Boolean) : Comparable<Card> {
        val points: Int = when (face) {
            "A" -> 14
            "K" -> 13
            "Q" -> 12
            "T" -> 10
            "9" -> 9
            "8" -> 8
            "7" -> 7
            "6" -> 6
            "5" -> 5
            "4" -> 4
            "3" -> 3
            "2" -> 2
            "J" -> if (jacksEnabled) 1 else 11
            else -> throw Exception("unknown $face")
        }

        override fun compareTo(other: Card): Int = when (this.points) {
            other.points -> 0
            else -> this.points compareTo other.points
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Card

            return points == other.points
        }

        override fun toString(): String {
            return face
        }
    }
}
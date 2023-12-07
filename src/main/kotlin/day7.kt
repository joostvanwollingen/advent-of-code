fun main() {
    val input = AocUtil.parse("day7.input", Hand::class.java).toList()
//    val input = AocUtil.parse("day7.test.input", Hand::class.java).toList()
    solveDay7PartOne(input)
//    val t = Hand("QQQJA 483")

}

fun solveDay7PartOne(cards: List<Hand>) {
    val sortedCards = cards.sorted()
    val totalWinnings = sortedCards.mapIndexed { index, hand -> hand to (index + 1) * hand.bid }
    totalWinnings.forEachIndexed{ i,t->println("${(i+1).toString().padStart(4)}\t ${t.second.toString().padStart(6)}\t ${t.first.type}\t ${t.first.cards[0]}${t.first.cards[1]}${t.first.cards[2]}${t.first.cards[3]}${t.first.cards[4]}\t ${t.first.bid}") }
    println(totalWinnings.sumOf { it.second })
}

enum class CardType(i: Int) {
    HighCard(2), OnePair(3), TwoPair(4), ThreeOfAKind(5), FullHouse(6), FourOfAKind(7), FiveOfAKind(8),
}

val rankComparator = Comparator<CardType> { r1, r2 ->
    r1.ordinal - r2.ordinal
}

class Hand(input: String) : Comparable<Hand> {

    private val split = input.split(" ")
    val cards = split[0].toCharArray().map { Card(it.toString()) }
    val bid = split[1].toLong()
    val counts = getCardCount(cards)
    val type: CardType = getType(counts)

    private fun getType(cardCount: Map<String, Int>): CardType {
        if (cardCount["J"] == null) {
            return defaultCardType(cardCount)
        } else {
            val defaultCardType = defaultCardType(cardCount)
            val notJCount = cardCount.filter { it.key != "J" }
            val maxOther = if(notJCount.isNotEmpty()) notJCount.maxBy { it.value }.value else 0
            val jCardType = when (cardCount["J"]) { //QQQJA 483
                5 -> CardType.FiveOfAKind //0
                4 -> CardType.FiveOfAKind //1
                3 -> if (maxOther == 2) CardType.FiveOfAKind else CardType.FourOfAKind //QQJJJ AKJJJ
                2 -> if (maxOther == 3) CardType.FiveOfAKind else if (maxOther == 2) CardType.FourOfAKind else CardType.ThreeOfAKind //QQQJJ AKJJ2
                1 -> if (maxOther == 4) CardType.FiveOfAKind else if (maxOther == 3) CardType.FourOfAKind else if (maxOther == 2) CardType.FourOfAKind else CardType.OnePair //QQQJJ AKJJ2
                else -> CardType.OnePair
            }
            if (defaultCardType > jCardType) return defaultCardType else return jCardType
        }
        throw Exception("card type fail")
    }

    private fun defaultCardType(cardCount: Map<String, Int>): CardType {
        if (cardCount.filter { entry -> entry.value == 5 }.isNotEmpty()) return CardType.FiveOfAKind
        if (cardCount.filter { entry -> entry.value == 4 }.isNotEmpty()) return CardType.FourOfAKind
        if (cardCount.filter { entry -> entry.value == 3 }
                .isNotEmpty() && cardCount.filter { entry -> entry.value == 2 }.isNotEmpty()) return CardType.FullHouse
        if (cardCount.filter { entry -> entry.value == 3 }
                .isNotEmpty()) return CardType.ThreeOfAKind ///but can't be because FH above .filter { entry -> entry.value != 2 }
        if (cardCount.filter { entry -> entry.value == 2 }.size == 2) return CardType.TwoPair
        if (cardCount.filter { entry -> entry.value == 2 }.isNotEmpty()) return CardType.OnePair
        if (cardCount.filter { entry -> entry.value == 1 }.isNotEmpty()) return CardType.HighCard
        throw Exception("Card type failure $cardCount")
    }

    private fun getCardCount(cards: List<Card>) = cards.groupingBy { it.face }.eachCount()


    override fun toString(): String {
        return "Hand(cards=${cards}, bid=$bid)"
    }

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


    override fun hashCode(): Int {
        return type.hashCode()
    }
}

private fun compareRank(hand: Hand, other: Hand): Int {
    for (i in 0..4) {
        if (hand.cards[i] == other.cards[i]) continue
        if (hand.cards[i] < other.cards[i]) return -1
        if (hand.cards[i] > other.cards[i]) return 1
    }
    return 0
}


class Card(val face: String) : Comparable<Card> {
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
        "J" -> 1
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

        if (points != other.points) return false

        return true
    }

    override fun hashCode(): Int {
        return points
    }

    override fun toString(): String {
        return "$face"
    }
}

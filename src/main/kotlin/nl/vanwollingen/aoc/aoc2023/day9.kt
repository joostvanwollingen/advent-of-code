package nl.vanwollingen.aoc.aoc2023

import nl.vanwollingen.aoc.util.AocUtil

fun main() {
//    val input = nl.vanwollingen.aoc.util.AocUtil.parse("day9.test.input", History::fromString).toList()
    val input = AocUtil.parse("day9.input", History::fromString).toList()
//    println(input[1])
//    println(input[0].extraPolate())
//    println(input[1])
//    println(input[1].extraPolate())
//    input[1].history.forEach( ::println )
//    println(input[1].extraPolate())
//    println(input[1].intraPolate())
//    println(input.map { it.extraPolate() }.sum())
    println(input.map { it.intraPolate() }.sum())
}

class History(val values: List<Long>) {
    val history
        get() = getFullHistory()

    companion object {
        fun fromString(input: String): History {
            return History(input.split(" ").map { it.toLong() })
        }
    }

    override fun toString(): String {
        return "History(values=$values)"
    }

    fun getNext(): History {
        val res: MutableList<Long> = values.foldIndexed(mutableListOf()) { i, r, v ->
            if (i != 0) {
                r += values[i] - values[i - 1]
            }
            r
        }
        return History(res)
    }

    private fun getFullHistory(): List<History> {
        val res = mutableListOf(getNext())
        while (!res.last().isAllZeros()) {
            res += res.last().getNext()
        }
        return res
    }

    fun isAllZeros(): Boolean = values.all { it == 0L }

    fun extraPolate(): Long {
        var prev: Long = 0L
        history.reversed().windowed(2, 1) {
            prev += it.last().values.last()
        }
        return prev + values.last()
    }

    fun intraPolate(): Long {
        var prev: Long = 0L
        history.reversed().windowed(2, 1) {
            prev = it.last().values.first() - prev
        }
        return values.first() - prev
    }
}
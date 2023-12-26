package nl.vanwollingen.aoc.aoc2023

import nl.vanwollingen.aoc.util.PuzzleInputUtil

fun main() {
    val input = PuzzleInputUtil.parse("2023/day9.input", Day09.History::fromString).toList()
    println(input.sumOf { it.extraPolate() })
    println(input.sumOf { it.intraPolate() })
}

class Day09 {

    class History(private val values: List<Long>) {
        private val history
            get() = getFullHistory()

        companion object {
            fun fromString(input: String): History {
                return History(input.split(" ").map { it.toLong() })
            }
        }

        override fun toString(): String {
            return "History(values=$values)"
        }

        private fun getNext(): History {
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

        private fun isAllZeros(): Boolean = values.all { it == 0L }

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
}
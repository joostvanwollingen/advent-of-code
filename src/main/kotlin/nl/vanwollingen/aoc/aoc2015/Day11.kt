package nl.vanwollingen.aoc.aoc2015

import nl.vanwollingen.aoc.util.Puzzle

fun main() {
    val d11 = Day11(2015, 11)
    d11.solvePart1()
    d11.solvePart2()
}

class Day11(year: Int, day: Int) : Puzzle(year, day) {
    override fun solvePart1() {
        val p = Password(input)
        p.next()
        println(p.password)
    }

    override fun solvePart2() {
        val p = Password(input)
        p.next().next()
        println(p.password)
    }

    data class Password(var password: String) {

        private fun isValid() = hasStraight() && hasNoIOL() && hasPairs()

        private fun hasPairs(): Boolean {
            val chars = password.toCharArray()
            var prevChar: Char? = null
            val pairFound: MutableList<Char> = mutableListOf()
            for (char in chars) {
                if (prevChar == char && !pairFound.contains(char)) {
                    pairFound += char
                }
                prevChar = char
            }
            return pairFound.size >= 2
        }

        private fun hasNoIOL(): Boolean = !password.contains(Regex("[iol]"))

        private fun hasStraight(): Boolean {
            val chars = password.toCharArray()
            var prevChar: Char? = null
            var count = 0
            for (char in chars) {
                if (prevChar == char - 1) count++ else count = 1
                if (count == 3) return true
                prevChar = char
            }
            return false
        }

        fun next(): Password {
            var chars = password.toCharArray()
            var valid = false
            while (!valid) {
                chars = increase(chars, chars.lastIndex)
                password = chars.joinToString("")
                valid = isValid()
            }
            return this
        }

        private fun increase(charss: CharArray, index: Int): CharArray {
            var chars = charss
            val currChar = chars[index]
            if (currChar == 'z') {
                chars[index] = 'a'
                if (index != 0) chars = increase(chars, index - 1)
            } else {
                chars[index]++
            }
            return chars
        }

        override fun toString(): String {
            return password
        }
    }
}
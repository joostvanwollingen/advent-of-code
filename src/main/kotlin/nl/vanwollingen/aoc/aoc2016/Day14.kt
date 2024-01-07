package nl.vanwollingen.aoc.aoc2016

import nl.vanwollingen.aoc.util.Puzzle
import nl.vanwollingen.aoc.util.hashing.md5

fun main() {
    val d14 = Day14()
    d14.solvePart1()
    d14.solvePart2()
}

class Day14(output: Boolean = false) : Puzzle(output) {
    override fun solvePart1() {
        findOneTimePad() { i -> md5(i) }
    }

    override fun solvePart2() {
        findOneTimePad() { i -> md5(i, 2016) }
    }

    private fun findOneTimePad(hashingFunction: (input: String) -> String) {
        val pendingKeys: MutableList<Key> = mutableListOf()
        val confirmedKeys: MutableList<Key> = mutableListOf()

        val triple = Regex("(.)\\1\\1")
        val quintuple = Regex("(.)\\1\\1\\1\\1")

        var index = 0

        while (confirmedKeys.size < 64) {
            val hash = hashingFunction("$input$index")

            val tripleMatches = triple.findAll(hash).toList()
            val quintupleMatches = quintuple.findAll(hash).toList()

            if (quintupleMatches.isNotEmpty()) {
                val pending = pendingKeys.filter { k -> k.confirmedAt == -1 && k.repeatingCharacter == quintupleMatches[0].groupValues[1] && index <= k.foundAt + 1000 }
                pending.forEach { k ->
                    k.confirmedAt = index
                    confirmedKeys += k
                    pendingKeys.remove(k)
                    debug("Confirmed key at $index (${confirmedKeys.size}) ${k.foundAt} ${k.foundAt - index}")
                }
            }

            if (tripleMatches.isNotEmpty()) {
                debug("Found potential key at $index ${pendingKeys.size} $hash")
                pendingKeys += Key(hash, foundAt = index, repeatingCharacter = tripleMatches[0].groupValues[1])
            }
            index++
        }
        log(confirmedKeys.sortedBy { it.foundAt }[63])
    }

    data class Key(val value: String, val foundAt: Int, var confirmedAt: Int = -1, val repeatingCharacter: String)

    private fun md5(input: String, times: Int): String {
        var hash = md5(input)
        for (i in 1..times) {
            hash = md5(hash)
        }
        return hash
    }

    fun check(index: Int) = md5("$input$index")
}
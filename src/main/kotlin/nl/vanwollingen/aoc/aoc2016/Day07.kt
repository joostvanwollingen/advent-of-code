package nl.vanwollingen.aoc.aoc2016

import nl.vanwollingen.aoc.util.Puzzle

fun main() {
    val d7 = Day07()
    d7.solvePart1()
    d7.solvePart2()
}

class Day07(output: Boolean = false) : Puzzle(output) {

    override fun solvePart1() {
        log(input.lines().count { supportsTLS(it) })
    }

    override fun solvePart2() {
        log(input.lines().count { supportsSSL(it) })
    }

    private fun supportsSSL(address: String): Boolean {

        var abas: MutableList<String> = mutableListOf()
        var babs: MutableList<String> = mutableListOf()
        var previous: CharSequence? = null
        var openHypernet = false
        address.windowed(2, 1) {
            if (it[0] == '[') openHypernet = true
            if (it[0] == ']') openHypernet = false
            if (previous != null) {
                if (previous!![0] == it[1]) {
                    if (openHypernet) babs += "$previous${it[1]}" else abas += "$previous${it[1]}"
                }
            }

            previous = it
        }
        abas = abas.filterNot { it.contains('[') || it.contains(']') }.toMutableList()
        babs = babs.filterNot { it.contains('[') || it.contains(']') }.toMutableList()
        if (abas.isEmpty() || babs.isEmpty()) return false

        return abas.any { aba -> babs.any { bab -> bab[0] == aba[1] && bab[1] == aba[0] && bab[1] == aba[2] } }
    }

    private fun supportsTLS(address: String): Boolean {
        val hypernets = Regex("\\[.+?]")
        val invalidHypernet = Regex("\\[.*([a-z])([a-z])\\2\\1.*?]")
        val hasAbbaSequence = Regex("([a-z])([a-z])\\2\\1")

        val hypernetsFound = hypernets.findAll(address).toList().map { it.value }
        if (hypernetsFound.any { invalidHypernet.containsMatchIn(it) }) return false

        val matches = hasAbbaSequence.findAll(address).toList()
        if (matches.isEmpty()) return false
        return matches.any { m ->
            m.groupValues[1] != m.groupValues[2]
        }
    }
}
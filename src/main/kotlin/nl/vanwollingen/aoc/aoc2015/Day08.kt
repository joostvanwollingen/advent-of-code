package nl.vanwollingen.aoc.aoc2015

import nl.vanwollingen.aoc.util.Puzzle

fun main() {
    val d8 = Day08()
    d8.solvePart1()
    d8.solvePart2()
}

class Day08() : Puzzle() {
    override fun solvePart1() {
        var code = 0
        var mem = 0

        input.lines().forEach { line ->
            code += line.replace(Regex("\\{2}"), "[").length

            mem += line.replace(Regex("""\\x[0-9a-fA-F]{2}"""), "A").removePrefix(""""""").removeSuffix(""""""").replace("\\\"", "[").replace("""\\""", """\""").length
        }
        println(code - mem)
    }

    override fun solvePart2() {
        var code = 0
        var enc = 0

        input.lines().forEach { line ->
            enc += line.replace("""\""", """\\""").replace(""""""", """\"""").length + 2

            code += line.replace(Regex("\\{2}"), "[").length
        }
        println(enc - code)
    }
}
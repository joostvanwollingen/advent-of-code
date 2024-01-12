package nl.vanwollingen.aoc.aoc2015

import nl.vanwollingen.aoc.util.Puzzle

fun main() {
    val d7 = Day07()
    d7.part1()
    d7.part2()
}

class Day07() : Puzzle() {
    override fun part1() {
        val signals: MutableMap<String, Int> = mutableMapOf()
        input.lines().sortedBy { it.takeLast(2) }.drop(1).forEach { c -> //sorted by target alphabetically, removing the first instruction of lx -> a
            processSignal(c, signals)
        }
        processSignal("lx -> a", signals)
        println(signals["a"])
    }

    override fun part2() {
        val signals: MutableMap<String, Int> = mutableMapOf()
        val input: MutableList<String> = input.lines().sortedBy { it.takeLast(2) }.drop(1).toMutableList()
        input.add("lx -> a")
        input[input.indexOf("19138 -> b")] = "16076 -> b"
        input.forEach { c ->
            processSignal(c, signals)
        }
        println(signals["a"])
    }

    enum class InstructionType { NOT, AND, RSHIFT, LSHIFT, SIGNAL, OR }

    private fun determineType(c: String): InstructionType = when {
        c.contains("NOT ") -> InstructionType.NOT
        c.contains(" AND ") -> InstructionType.AND
        c.contains(" LSHIFT ") -> InstructionType.LSHIFT
        c.contains(" RSHIFT ") -> InstructionType.RSHIFT
        c.contains(" OR ") -> InstructionType.OR
        else -> InstructionType.SIGNAL
    }

    private fun processSignal(c: String, signals: MutableMap<String, Int>) {
        when (determineType(c)) {
            InstructionType.SIGNAL -> {
                val (signal, target) = c.split(" -> ")
                signals[target] = if (signals[signal] != null) signals[signal]!! else signal.toInt()
            }

            InstructionType.NOT -> {
                val (not, target) = c.split(" -> ")
                signals[target] = 65535 - signals[not.drop(4)]!!
            }

            InstructionType.AND -> {
                val (and, target) = c.split(" -> ")
                val (left, right) = and.split(" AND ")
                signals[target] = if (left != "1") signals[left]!! and signals[right]!! else 1 and signals[right]!!
            }

            InstructionType.OR -> {
                val (or, target) = c.split(" -> ")
                val (left, right) = or.split(" OR ")
                signals[target] = signals[left]!! or signals[right]!!
            }

            InstructionType.LSHIFT -> {
                val (lshift, target) = c.split(" -> ")
                val (left, right) = lshift.split(" LSHIFT ")
                signals[target] = signals[left]!! shl right.toInt()
            }

            InstructionType.RSHIFT -> {
                val (rshift, target) = c.split(" -> ")
                val (left, right) = rshift.split(" RSHIFT ")
                signals[target] = signals[left]!! shr right.toInt()
            }
        }
    }
}
package nl.vanwollingen.aoc.aoc2023

import nl.vanwollingen.aoc.util.grid.Point
import nl.vanwollingen.aoc.util.PuzzleInputUtil
import java.util.*
import kotlin.streams.asStream

fun main() {
//    val input = nl.vanwollingen.aoc.util.AocUtil.load("day14.test.input")
    val input = PuzzleInputUtil.load("2023/day14.input")
    var reflectorDish = input.lines()

    var cycle = 0L
    while (cycle < 1000000000) {
        reflectorDish = transposeToRows(reflectorDish.joinToString("\n").columns().map { rollTheRocks(it) }) //north
        reflectorDish = reflectorDish.map { rollTheRocks(it) } //west
        reflectorDish =
            transposeToRows(reflectorDish.joinToString("\n").columns().map { rollTheRocks(it, true) }) //south
        reflectorDish = reflectorDish.map { rollTheRocks(it, true) } //east
        if (cycle % 100000 == 0L) {
            println("cycle: $cycle - reflectorDish ${reflectorDish.hashCode()}")
        }
        cycle++
    }
    val grid =
        readGrid(reflectorDish).groupBy { PointKey(it.l.y, it.l.x) }.mapValues { it.value.first() }.toMutableMap()
    grid.values.toList().print()
    grid.calcWeights()
}

private fun transposeToRows(sorted: List<String>): MutableList<String> {
    var lines = mutableListOf<String>()
    var line = ""
    for (char in 0 until sorted.maxOf { it.length }) {
        for (i in sorted.indices) {
            line += sorted[i][char]
            if (i == sorted.size - 1) {
                lines += line
                line = ""
            }
        }
    }
    return lines
}

private fun MutableMap<PointKey, Rock>.calcWeights(): Long {
    val minY = this.values.minBy { it.l.y }.l.y
    val maxY = this.values.maxBy { it.l.y }.l.y
    var score = 0L
    val groupedByLine = this.values.groupBy { it.l.y }
    groupedByLine.forEach { line ->
        val stones = line.value.count { it.s == "O" }
        val linePoints = maxY - (line.key - 1)
        val lineScore = stones * linePoints
        if (stones > 0) {
            score += lineScore
        }
    }
    println(score)
    return score
}

private fun readGrid(lines: List<String>): List<Rock> {
    val rocks: MutableList<Rock> = mutableListOf()
    lines.mapIndexed { index, line ->
        rocks += getRockFromLine(index, line)
    }
    return rocks
}

private data class Rock(val s: String, val l: Point) {
    var id: Int = 0
    fun toColorString(): String = when (s) {
        "." -> "\u001b[0m" + this.s + "\u001b[0m"
        "#" -> "\u001b[43m" + this.s + "\u001b[0m"
        "O" -> "\u001b[32m" + this.s + "\u001b[0m"
        else -> s
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Rock

        if (s != other.s) return false
        if (l != other.l) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = s.hashCode()
        result = 31 * result + l.hashCode()
        result = 31 * result + id
        return result
    }

}

private fun getRockFromLine(index: Int, line: String): List<Rock> {
    val matches = Regex("(#|\\.|O)").findAll(line)
    return matches.asStream().map { match ->
        Rock(
            match.value, Point(index + 1, match.range.first + 1)
        )
    }.toList()
}

private fun List<Rock>.print() {
    val groupedByLine = this.groupBy { it.l.y }
    groupedByLine.forEach { line ->
        val sortedLine = line.value.sortedBy { it.l.y }
        print("${line.key.toString().padStart(3, '0')}: ")
        for (rock in sortedLine) {
            print(rock.toColorString())
        }
        println()
    }
}

private fun rollTheRocks(input: String, reversed: Boolean = false): String {
    val line = if (!reversed) input else input.reversed()
    val q: Queue<Char> = LinkedList<Char>(line.toCharArray().asList())

    var appendString = ""
    var holdString = ""
    var nextElement = ' '

    while (q.peek() != null) {
        nextElement = q.peek()
        when (nextElement) {
            'O' -> {
                appendString += nextElement
            }

            '.' -> {
                holdString += nextElement
            }

            '#' -> {
                appendString += holdString
                holdString = ""
                appendString += nextElement
            }
        }
        q.remove()
    }
    return if (!reversed) appendString + holdString else (appendString + holdString).reversed()
}
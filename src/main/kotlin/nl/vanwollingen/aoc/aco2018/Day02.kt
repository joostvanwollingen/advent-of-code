package nl.vanwollingen.aoc.aco2018

import nl.vanwollingen.aoc.util.Puzzle

fun main() {
    val d2 = Day02()
    d2.solvePart1()
    d2.solvePart2()
}

class Day02 : Puzzle() {

    override fun part1() {
        val parsedBoxIds = input.lines().map { parseBoxId(it) }
        val two = parsedBoxIds.count { it.two > 0 }
        val three = parsedBoxIds.count { it.three > 0 }
        log(two * three)
    }

    override fun part2() {
        val boxIds = input.lines()
        for (i in 0..boxIds.size) {
            for (j in i + 1..<boxIds.size) {
                val result = compareBoxIds(boxIds[i], boxIds[j])
                if (result.differences == 1) {
                    log(result.similar)
                }
            }
        }
    }

    private fun compareBoxIds(firstBox: String, secondBox: String): BoxIdComparisonResult {
        var differences = 0
        var similar = ""
        firstBox.forEachIndexed { i, c ->
            if (secondBox[i] == c) {
                similar += c
            } else {
                differences += 1
            }
            if (differences > 1) return@forEachIndexed
        }
        return BoxIdComparisonResult(similar, differences)
    }

    data class BoxIdComparisonResult(val similar: String, val differences: Int)

    data class BoxIdParseResult(val two: Int, val three: Int)

    private fun parseBoxId(boxId: String): BoxIdParseResult {
        val charMap: MutableMap<Char, Int> = mutableMapOf()
        boxId.forEach { c ->
            charMap.merge(c, 1) { i, v -> v + i }
        }
        val two = charMap.count { c -> c.value == 2 }
        val three = charMap.count { c -> c.value == 3 }
        return BoxIdParseResult(two, three)
    }
}
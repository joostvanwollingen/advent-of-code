package nl.vanwollingen.aoc.aoc2024

import nl.vanwollingen.aoc.util.Puzzle

fun main() = Day25.solve()

object Day25 : Puzzle(exampleInput = false) {

    override fun parseInput(): Pair<MutableList<List<Int>>, MutableList<List<Int>>> {
        val keyOrLockStrings = input.split("\n\n")
        val locks: MutableList<List<Int>> = mutableListOf()
        val keys: MutableList<List<Int>> = mutableListOf()
        var i = 0
        keyOrLockStrings.map { item ->
            var lines = item.lines()
            val isLock = lines.first() == "#####"
            val heights = MutableList(5) { 0 }
            if (!isLock) lines = item.lines().reversed()
            lines = lines.drop(1)
            lines.forEach { line ->
                if (line[0] == '#') heights[0] += 1
                if (line[1] == '#') heights[1] += 1
                if (line[2] == '#') heights[2] += 1
                if (line[3] == '#') heights[3] += 1
                if (line[4] == '#') heights[4] += 1
            }
            if (isLock) {
                locks.add(heights)
            } else {
                keys.add(heights)
            }
        }
        return keys to locks
    }

    override fun part1(): Any {
        val (keys, locks) = parseInput()
        var keyFitsLock = 0
        keys.forEachIndexed { keyIndex, key ->
            locks.forEachIndexed lock@ { lockIndex, lock ->
                val keyInLock = listOf(
                    key[0] + lock[0],
                    key[1] + lock[1],
                    key[2] + lock[2],
                    key[3] + lock[3],
                    key[4] + lock[4],
                )
                if(keyInLock.any { it > 5 }) return@lock
                keyFitsLock += 1
            }
        }
        return keyFitsLock
    }

    override fun part2(): Any {
        TODO("Not yet implemented")
    }
}
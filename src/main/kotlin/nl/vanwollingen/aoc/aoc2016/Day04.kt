package nl.vanwollingen.aoc.aoc2016

import nl.vanwollingen.aoc.util.Puzzle

fun main() {
    val d4 = Day04()
    d4.solvePart1()
    d4.solvePart2()
}

class Day04(output: Boolean = false) : Puzzle(output) {

    private val rooms = parseInput()
    override fun parseInput(): List<Room> = input.lines().map { Room.fromString(it) }

    override fun part1() {
        val result = getValidRooms()
        log(result)
    }

    private fun getValidRooms(): Int {
        var sumOfSectorIds = 0
        rooms.forEach { r ->
            val charCount = getCharCount(r.name)
            var valid = true
            var tieCheck = 0

            debug(r)
            debug(charCount)

            for (i in 0..<r.checksum.toList().size) {
                if (charCount[i].first == r.checksum[i]) {
                    continue
                } else {
                    valid = false
                    val currentChecksumSubject = charCount.firstOrNull { c -> c.first == r.checksum[i] }
                    val charactersWithSameCount = charCount.filter { c -> c.second == currentChecksumSubject?.second }.sortedBy { it.first }

                    if (charactersWithSameCount.isNotEmpty() || charactersWithSameCount.size != 1) {
                        break
                    }

                    if (charactersWithSameCount[tieCheck].first == r.checksum[i]) {
                        valid = true
                        tieCheck++
                        if (r.checksum.lastIndex == i) {
                            debug("Room ${r.sectorId} is valid")
                            break
                        }
                    }

                    if (!valid) break
                }
            }
            if (valid) {
                debug("Room ${r.sectorId} is valid")
                sumOfSectorIds += r.sectorId
            }
        }
        return sumOfSectorIds
    }


    override fun part2() {
        log(rooms.first { it.decrypt().contains("north") }.sectorId)
    }

    private fun getCharCount(input: String) = input.replace("-", "").map { c -> c to input.count { it == c } }.distinct().toList().sortedWith(compareByDescending<Pair<Char, Int>> { it.second }.thenBy { it.first })

    data class Room(val name: String, val sectorId: Int, val checksum: String) {

        fun decrypt(): String {
            val result: MutableList<Char> = mutableListOf()
            for (i in name.indices) {
                var current: Char = name[i]
                var steps = 0

                if (current == '-') {
                    result += current; continue
                }

                while (steps < sectorId) {
                    if (current == 'z') {
                        current = 'a'
                        steps++
                    } else {
                        current += 1
                        steps++
                    }
                }
                result += current
            }
            return result.joinToString("")
        }

        companion object {
            fun fromString(input: String): Room {
                val (full, name, sector, checksum) = Regex("^(.*)-(\\d+)\\[([a-z]+)]").findAll(input).first().groupValues
                return Room(name, sector.toInt(), checksum)
            }
        }
    }
}
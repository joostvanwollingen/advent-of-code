package nl.vanwollingen.aoc.aoc2024

import nl.vanwollingen.aoc.util.Puzzle
import java.util.LinkedList

fun main() = Day09.solve()

object Day09 : Puzzle(exampleInput = false, printDebug = true) {

    override fun parseInput(): Map<Int, FileAndFreeSpace> {
        var id = 0
        val result = mutableMapOf<Int, FileAndFreeSpace>()
        input.windowed(2, 2, true) {
            val size = "${it[0]}".toInt()
            val freeSpace = "${it.getOrElse(1) { '0' }}".toInt()
            result.put(
                key = id, value = FileAndFreeSpace(
                    id = id++,
                    size = size,
                    freeSpace = freeSpace,
                )
            )
        }
        return result
    }

    override fun part1(): Long {
        val files = parseInput()
        val blockMap: MutableMap<Int, Int> = generateBlockMap(files)
        val freeSpace = LinkedList(blockMap.values.mapIndexedNotNull { i, c -> if (c == -1) i else null })
        val freeSpaceEnd = freeSpace.size
        var allocated = 0

        for (entry in files.entries.reversed()) {
            if (allocated == freeSpaceEnd) break
            val (_, file) = entry
            while (freeSpace.peek() != null && file.allocated < file.size) {
                if (allocated == freeSpaceEnd) break
                val freeIndex = freeSpace.poll()
                file.allocated += 1
                blockMap[freeIndex] = file.id
                while (blockMap[blockMap.size - allocated - 1] == -1) {
                    allocated++
                }
                blockMap[blockMap.size - allocated - 1] = -1
                allocated++
            }
        }

        return getCheckSum(blockMap)
    }


    override fun part2(): Any {
        val files = parseInput()

        val blockMap: MutableMap<Int, Int> = generateBlockMap(files)
        val fileMap: Map<Int, List<Int>> = blockMap.entries.groupBy({ it.value }, { it.key })
        var freeSpace = findSequentialIndices(blockMap, -1)

        for (entry in files.entries.reversed()) {
            val (_, file) = entry
            val toAllocate = file.size

            val allocationPossible = freeSpace
                .filter { it.key >= toAllocate }

            if (allocationPossible.isEmpty()) continue

            val indexThatCouldFit = allocationPossible.minBy { v -> v.value.minBy { it.min() }.min() }.key
            val target = freeSpace[indexThatCouldFit]!!.first()

            val firstIndex = target.min()
            val lastIndex = firstIndex + toAllocate - 1

            if (firstIndex > fileMap[file.id]!!.min()) continue

            for (i in firstIndex..lastIndex) {
                blockMap[i] = file.id
            }
            fileMap[file.id]?.forEach { blockMap[it] = -1 }
            freeSpace = findSequentialIndices(blockMap, -1)

        }
        return getCheckSum(blockMap)
    }

    private fun findSequentialIndices(map: Map<Int, Int>, targetValue: Int): MutableMap<Int, List<MutableList<Int>>> {
        val result = mutableListOf<MutableList<Int>>()
        val currentGroup = mutableListOf<Int>()

        for ((key, value) in map) {
            if (value == targetValue) {
                currentGroup.add(key) // Add to the current group
            } else if (currentGroup.isNotEmpty()) {
                result.add(currentGroup.toMutableList()) // Save the group and reset
                currentGroup.clear()
            }
        }

        if (currentGroup.isNotEmpty()) {
            result.add(currentGroup.toMutableList()) // Add the last group if needed
        }

        return result.groupBy { it.size }.toSortedMap()
    }

    private fun generateBlockMap(files: Map<Int, FileAndFreeSpace>): MutableMap<Int, Int> {
        var index = 0
        val blockMap: MutableMap<Int, Int> = mutableMapOf()
        for (entry in files) {
            var toAdd = 0
            while (toAdd < entry.value.size) {
                blockMap[index++] = entry.key
                toAdd++
            }

            var empty = 0
            while (empty < entry.value.freeSpace) {
                blockMap[index++] = -1
                empty++
            }
        }
        return blockMap
    }

    private fun getCheckSum(map: MutableMap<Int, Int>): Long {
        var sum = 0L
        map.forEach {
            if (it.value != -1) {
                sum += it.key * it.value
            }
        }
        return sum
    }

    data class FileAndFreeSpace(val id: Int, val size: Int, var freeSpace: Int, var allocated: Int = 0)
}
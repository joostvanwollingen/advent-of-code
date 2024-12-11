package nl.vanwollingen.aoc.aoc2024

import nl.vanwollingen.aoc.util.Puzzle
import java.util.LinkedList
import kotlin.math.max
import kotlin.math.sign

fun main() = Day11.solve()

object Day11 : Puzzle(exampleInput = false, printDebug = true) {

    override fun part1(): Long {
        val stones = LinkedList(input.split(" ").map { it.toLong() })
        return blink(stones, 25)
    }

    override fun part2(): Int {
        val initialStones = input.split(" ").map { it.toLong() }

        return 0
    }


//    private fun buildCache(initialStones: List<Long>, depth: Int = 1): Map<Long, Map<Int, Long>> {
//        val cache = mutableMapOf<Long, MutableMap<Int, Int>>()
//        for (stone in initialStones) {
//            val l = cache.getOrDefault(stone, mutableMapOf())
//            for (d in 1..depth ) {
//                l[d] = blink(stone).size
//            }
//            cache[stone] = l
//        }
//        return cache
//    }

    private fun blink(stones: List<Long>, repeat: Int, memoMap: MutableMap<Long, MutableMap<Int, Long>>): Long {
        if (stones.size > 1) throw Error("Fail")
        val stone = stones.first()
        val result = memoMap[stone]?.get(repeat)
        if (result != null) return result

        var stoneList = LinkedList(stones)
        repeat(repeat) {
            stoneList = LinkedList(blink(stoneList))
        }
        return stoneList.size.toLong()
    }

    private fun blink(stones: List<Long>, repeat: Int): Long {
        var stoneList = LinkedList(stones)
        repeat(repeat) {
            stoneList = LinkedList(blink(stoneList))
        }
        return stoneList.size.toLong()
    }

    private fun blink(stones: List<Long>): List<Long> {
        val newList = LinkedList(stones)
        return blink(newList)
    }

    private fun blink(stones: LinkedList<Long>): List<Long> {
        val newList = LinkedList<Long>()
        var stone = stones.poll()
        while (stone != null) {
            newList.addAll(blink(stone))
            stone = stones.poll()
        }
        return newList
    }

    private fun blink(stone: Long): List<Long> {
        return when {
            stone == 0L -> listOf(1)
            isLengthEven(stone) -> {
                val s = "$stone"
                val l = s.length / 2
                val left = s.substring(0, l).toLong()
                val right = s.substring(l, s.length).toLong()
                listOf(left, right)
            }

            else -> listOf(stone * 2024)
        }
    }

    private fun isLengthEven(value: Long): Boolean {
        var number = value
        var digitCount = 0

        // Handle negative numbers
        if (number < 0) {
            number = -number
        }

        while (number > 0) {
            digitCount++
            number /= 10
        }

        return digitCount % 2 == 0
    }
}
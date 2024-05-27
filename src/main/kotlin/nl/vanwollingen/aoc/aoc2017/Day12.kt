package nl.vanwollingen.aoc.aoc2017

import nl.vanwollingen.aoc.util.Puzzle
import java.util.LinkedList
import java.util.Queue

fun main() {
    val d12 = Day12()
    d12.solvePart1()
}

class Day12(output: Boolean = false) : Puzzle(output) {

    private val pipesMap: MutableMap<Int, Set<Int>> = mutableMapOf()
    private val startPipe = 0

    override fun parseInput() = input.lines().map { l -> Pipe.fromString(l) }
        .forEach { p ->
            val k = pipesMap.getOrPut(p.from) { p.to }
            if (k != p.to) {
                pipesMap[p.from] = k.plus(p.to)
            }

            p.to.forEach { t ->
                val d = pipesMap.getOrPut(t) { setOf(p.from) }
                if (d != setOf(p.from)) {
                    pipesMap[t] = d.plus(p.from)
                }
            }
        }

    override fun part1() {
        parseInput()
        val visited: MutableSet<Int> = mutableSetOf(startPipe)
        val q: Queue<Int> = LinkedList()
        q.add(startPipe)

        while (q.isNotEmpty()) {
            val v = q.poll()
            visited.add(v)

            val newPipes = pipesMap.getOrDefault(v, emptySet()).minus(visited)

            q.addAll(newPipes)
        }

        log(visited.size)
    }

    override fun part2() {
        TODO("Not yet implemented")
    }

    data class Pipe(val from: Int, val to: MutableSet<Int>) {
        companion object {
            fun fromString(l: String): Pipe {
                val s = l.split(" <-> ")
                return Pipe(s.first().toInt(), s.last().split(",").map { it.strip().toInt() }.toMutableSet())
            }
        }
    }
}
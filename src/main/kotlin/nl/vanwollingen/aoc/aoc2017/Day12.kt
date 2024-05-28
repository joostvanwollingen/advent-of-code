package nl.vanwollingen.aoc.aoc2017

import nl.vanwollingen.aoc.util.Puzzle
import java.util.LinkedList
import java.util.Queue

fun main() {
    val d12 = Day12()
    d12.parseInput()
    d12.solvePart1()
    d12.solvePart2()
}

class Day12(output: Boolean = false) : Puzzle(output) {

    private val pipesMap: MutableMap<Int, Set<Int>> = mutableMapOf()
    private val startPipe = 0

    override fun parseInput() = input.lines().map { l -> Pipe.fromString(l) }.forEach { p ->
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
        val visited = getGroupForPipe(startPipe, pipesMap)
        log(visited.size)
    }

    override fun part2() {
        val allGroups = pipesMap.map { getGroupForPipe(it.key, pipesMap) }.distinct()
        log(allGroups.size)
    }

    private fun getGroupForPipe(pipe: Int, pipesMap: MutableMap<Int, Set<Int>>): MutableSet<Int> {
        val visited: MutableSet<Int> = mutableSetOf(pipe)
        val q: Queue<Int> = LinkedList()
        q.add(pipe)

        while (q.isNotEmpty()) {
            val v = q.poll()
            visited.add(v)

            val newPipes = pipesMap.getOrDefault(v, emptySet()).minus(visited)

            q.addAll(newPipes)
        }
        return visited
    }

    data class Pipe(val from: Int, val to: MutableSet<Int>) {
        companion object {
            fun fromString(l: String): Pipe {
                val s = l.split(" <-> ")
                return Pipe(s.first().toInt(), s.last().split(",").map { it.trim().toInt() }.toMutableSet())
            }
        }
    }
}
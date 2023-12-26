package nl.vanwollingen.aoc.aoc2023

import nl.vanwollingen.aoc.util.PuzzleInputUtil

fun main() {
    val input = PuzzleInputUtil.load("2023/day25.test.input")
    val d25 = Day25()
    d25.solvePart1(input)
}

class Day25 {
    fun solvePart1(input: String) {
        val componentSets = input.lines().map { line ->
            buildSet {
                this.add(line.split(": ")[0])
                this.addAll(line.split(": ")[1].split(" "))
            }
        }

        val components = componentSets.flatMap { set ->
            set.map { component ->
                Day25.Component(component, set.minus(component).toMutableSet())
            }
        }

        val names = components.map { it.name }.distinct()
        val merged = names.map { name ->
            val named = components.filter { it.name == name }
            val firstNamed = named.first()
            firstNamed.merge(named)
        }
        merged.sortedBy { it.connectionCount }.forEach { println(it) }
        merged.forEach { m ->
            m.connections.forEach { c ->
                println("${m.name} $c")
            }
        }

    }

    class Component(val name: String, connections: MutableSet<String> = mutableSetOf()) {
        val connections = connections.sorted()
        val connectionCount = connections.size
        fun merge(other: Component): Component {
            if (name != other.name) throw Exception("Trying to merge $name and ${other.name}")
            return Component(name, connections.plus(other.connections).toMutableSet())
        }

        fun merge(others: List<Component>): Component {
            return others.fold(this) { acc, component -> acc.merge(component) }
        }

        override fun toString(): String {
            return "Component(name='$name', connections=$connections ($connectionCount))"
        }

    }

}
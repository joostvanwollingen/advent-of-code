package nl.vanwollingen.aoc.aoc2024

import nl.vanwollingen.aoc.util.Puzzle
import nl.vanwollingen.aoc.util.combinatorics.combinations

fun main() = Day24.solve()

object Day24 : Puzzle(exampleInput = false) {

    private var wires = getWires()
    private val gates: List<Gate> = input.split("\n\n")[1].lines().map { Gate.fromString(it) }

    override fun part1(): Any {
        val zWires = gates.mapNotNull { if (it.output.startsWith("z")) it.output else null }

        while (!wires.keys.containsAll(zWires)) {
            gates.forEach { gate ->
                gate.resolve()?.let { wires[gate.output] = it }
            }
        }
        return zWires.wiresToNumber()
    }

    override fun part2(): Any {
        wires = getWires()
        val xWires = wires.mapNotNull { if (it.key.startsWith("x")) it.key else null }
        val yWires = wires.mapNotNull { if (it.key.startsWith("y")) it.key else null }
        val zWires = gates.mapNotNull { if (it.output.startsWith("z")) it.output else null }
        val target = xWires.wiresToNumber() + yWires.wiresToNumber()

        wires = getWires()

        while (!wires.keys.containsAll(zWires)) {
            gates.forEach { gate ->
                gate.resolve()?.let { wires[gate.output] = it }
            }
        }

        val targetBinary = target.toString(2)
        log("y: ${yWires.getWireValues()}")
        log("x: ${xWires.getWireValues()}")
        log("z: ${zWires.getWireValues()}")
        log("t: ${targetBinary}")


        var i = 0
        var registers = zWires.sorted().reversed().map { it to (wires[it] to targetBinary[i++]) }
        var faultyRegisters = registers.filter { it.second.first != it.second.second.toString().toInt() }

        faultyRegisters.forEach {
            log("$it ${gates.filter { g -> g.output == it.first }}")
        }

        log("")

        wires = getWires()

        val swap = mapOf(
//            "bla" to "bla"

            //z07, z18, z19
            "hch" to "z07",
            "z07" to "hch",

            //z35-41, z41
            "wmn" to "cmk",
            "cmk" to "wmn",

            //z44
//            "tsb" to "mdq",// -> fails
//            "mdq" to "tsb", //-> fails for other numbers


            //z08, z11
            "cbj" to "z08",
            "z08" to "cbj"

        )
        while (!wires.keys.containsAll(zWires)) {
            gates.forEach { gate ->

                gate.resolve()?.let {
                    val outputGate = if (swap[gate.output] != null) swap[gate.output]!! else gate.output
                    wires[outputGate] = it
                }
            }
        }

        i = 0
        registers = zWires.sorted().reversed().map { it to (wires[it] to targetBinary[i++]) }
            .filter { it.second.first != it.second.second.toString().toInt() }



        log("")
        log("$target")
        log(zWires.wiresToNumber())
        log(target == zWires.wiresToNumber())

        log("y: ${yWires.getWireValues()}")
        log("x: ${xWires.getWireValues()}")
        log("z: ${zWires.getWireValues()}")
        log("t: ${targetBinary}")

        registers.forEach {
            log("$it ${gates.filter { g -> g.output == it.first }}")
        }

        return listOf(
            "hch",
            "z07",
            "wmn",
            "cmk",
            "tsb",
            "mdq",
            "cbj",
            "z08"
        ).sorted().joinToString(",")
    }

    private fun List<String>.wiresToNumber() = this.getWireValues()
        .toLong(2)

    private fun List<String>.getWireValues() = this.map { it to wires[it] }
        .sortedBy { it.first }
        .reversed()
        .map { it.second }
        .joinToString("")

    data class Gate(val left: String, val right: String, val type: String, val output: String) {

        fun resolve(): Int? {
            val left = wires.getOrDefault(left, null)
            val right = wires.getOrDefault(right, null)

            if (left == null || right == null) return null

            return when (type) {
                "AND" -> {
                    if (left == 1 && right == 1) 1 else 0
                }

                "OR" -> {
                    if (left == 1 || right == 1) 1 else 0
                }

                "XOR" -> {
                    if (left != right) 1 else 0
                }

                else -> throw Error("Unknown type")
            }
        }

        companion object {
            fun fromString(input: String): Gate {
                val (gateData, output) = input.split(" -> ")
                val (left, type, right) = gateData.split(" ")
                return Gate(left, right, type, output)
            }
        }
    }

    private fun getWires() =
        input.split("\n\n")[0].lines().associate {
            val (wire, value) = it.split(": ")
            wire to value.toInt()
        }.toMutableMap()
}
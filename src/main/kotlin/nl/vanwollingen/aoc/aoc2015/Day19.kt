package nl.vanwollingen.aoc.aoc2015

import nl.vanwollingen.aoc.util.Puzzle

fun main() {
    val d19 = Day19()
    d19.part1()
//    d19.solvePart2()
}


class Day19() : Puzzle() {

    private val moleculeReplacements = parseInput()
    override fun parseInput(): Molecule {
        val replacements = input.split("\n\n")[0].lines().map { it.split(" => ")[0] to it.split(" => ")[1] }
        val calibration = input.split("\n\n")[1]
        return Molecule(calibration, replacements)
    }

    override fun part1() {
        val results = mutableSetOf<String>()
        moleculeReplacements.replacements.forEach { rep ->
            val regex = Regex(rep.first)
            val matches = regex.findAll(moleculeReplacements.calibration)
            matches.toList().forEach { match ->
                results += moleculeReplacements.calibration.replaceRange(match.groups[0]!!.range, rep.second)
            }
        }
        println(results)
        println(results.size)
    }

    override fun part2() {
        val molecules = listOf("e")

        val testMolecule = Molecule("HOH", listOf(
                "e" to "H",
                "e" to "O",
                "H" to "HO",
                "H" to "OH",
                "O" to "HH",
        ))

        val steps = findTargetMolecule(moleculeReplacements.calibration, moleculeReplacements.replacements.map { Regex(it.first) to it.second }, molecules, 0)
        println(steps)
    }

    private fun findTargetMolecule(target: String, replacements: List<Pair<Regex, String>>, molecules: List<String>, count: Int): Int {
        if (molecules.contains(target)) return count
        val newMolecules = molecules.flatMap { molecule -> getPossibleReplacements(molecule, replacements) }
        println("$count: $molecules => $newMolecules")
        return findTargetMolecule(target, replacements, newMolecules.distinct(), count + 1)
    }

    private fun getPossibleReplacements(molecule: String, replacements: List<Pair<Regex, String>>): List<String> {
        var result: MutableList<String> = mutableListOf()

        for (r in replacements) {
            val matches = r.first.findAll(molecule).toList()
            matches.forEach { match ->
                result += molecule.replaceRange(match.groups[0]!!.range, r.second)
            }
        }
        return result
    }


    data class Molecule(val calibration: String, val replacements: List<Pair<String, String>>)
}
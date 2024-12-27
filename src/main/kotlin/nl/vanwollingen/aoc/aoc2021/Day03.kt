package nl.vanwollingen.aoc.aoc2021

import nl.vanwollingen.aoc.util.Puzzle

fun main() = Day03.solve()

object Day03 : Puzzle(exampleInput = false) {

    override fun part1(): Any {
        val gammaRate = determineGammaRate(input.lines())
        val epsilonRate = gammaRate.map { if (it == 1) 0 else 1 }
        val powerConsumption = gammaRate.toDecimal() * epsilonRate.toDecimal()

        return powerConsumption
    }

    override fun part2(): Any {
        val oxygenGeneratorRating = getOtherRating(input.lines(), true).first()
        val co2ScrubberRating = getOtherRating(input.lines(), false).first()
        val lifesupportRating = oxygenGeneratorRating.toLong(2) * co2ScrubberRating.toLong(2)
        return lifesupportRating
    }

    private fun getOtherRating(input: List<String>, equals: Boolean): List<String> {
        var reports = input
        for (i in input.indices) {
            reports = reports.filter {
                if (equals) {
                    determineGammaRate(
                        reports, i, i + 1
                    )[i] == it[i].toString().toInt()
                } else {
                    determineGammaRate(
                        reports, i, i + 1
                    )[i] != it[i].toString().toInt()
                }
            }
            if (reports.size == 1) return reports
        }
        return reports
    }

    private fun determineGammaRate(
        reports: List<String>, start: Int = 0, end: Int = reports.first().length
    ): MutableList<Int> {
        val reportLength = reports.first().length
        val numberOfReports = reports.size
        val gammaRate = List(reportLength) { 0 }.toMutableList()

        reports.forEach { line ->
            for (i in start..<end) {
                gammaRate[i] = gammaRate[i] + charToInt(line, i)
            }
        }

        for (i in start..<end) {
            gammaRate[i] = if (gammaRate[i] / numberOfReports.toDouble() >= 0.5) {
                1
            } else {
                0
            }
        }
        return gammaRate
    }

    private fun List<Int>.toDecimal() = this.joinToString("").toLong(2)
    private fun charToInt(line: String, position: Int) = line[position].toString().toInt()
}
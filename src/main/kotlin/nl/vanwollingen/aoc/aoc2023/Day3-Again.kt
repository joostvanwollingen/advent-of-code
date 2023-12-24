package nl.vanwollingen.aoc.aoc2023

import nl.vanwollingen.aoc.grid.Point
import nl.vanwollingen.aoc.grid.getSurroundingPoints
import nl.vanwollingen.aoc.util.PuzzleInputUtil
import kotlin.streams.asStream

fun main() {
    val d3 = Day3Again()
    d3.solvePart1()
    d3.solvePart2()
}

class Day3Again {
    val lines = PuzzleInputUtil.load("2023/day3.input").lines()
    val grid = readGrid(lines)
    private val partNumbers: kotlin.collections.List<PartNumber> = grid.first
    private val symbols: kotlin.collections.List<Symbol> = grid.second

    fun solvePart1(): List<Pair<PartNumber, List<Symbol>>> {
        //Part 1
        val partNumbersAdjecentToSymbols: List<Pair<PartNumber, List<Symbol>>> = partNumbers.map { part ->
            val partIsAdjecent: List<Point> = part.location.getSurroundingPoints()
            val symbolLocations: List<Point> = symbols.map { symbol -> symbol.location }
            val isValidPartNumber = partIsAdjecent.intersect(symbolLocations)
            part to symbols.filter { symbol -> isValidPartNumber.contains(symbol.location) }
        }.filter {
            it.second.isNotEmpty()
        }
        println(partNumbersAdjecentToSymbols.sumOf { it.first.number })
        return partNumbersAdjecentToSymbols
    }

    fun solvePart2() {
        //Part 2
        val partNumbersAdjecentToSymbols = solvePart1()
        val grouped = partNumbersAdjecentToSymbols.groupBy { part -> part.second }.filter { symbol -> symbol.value.size == 2 }
        println(grouped.values.sumOf { part -> part.first().first.number * part.last().first.number })
    }

    private fun readGrid(lines: List<String>): Pair<List<PartNumber>, List<Symbol>> {
        val partNumbers: MutableList<PartNumber> = mutableListOf()
        val symbols: MutableList<Symbol> = mutableListOf()
        lines.mapIndexed { index, line ->
            partNumbers += getPartNumbersFromLine(index, line)
            symbols += getSymbolsFromLine(index, line)
        }
        return Pair(partNumbers, symbols)
    }

    private fun getSymbolsFromLine(index: Int, line: String): List<Symbol> {
        val matches = Regex("([*#+\$=&%/@-])").findAll(line)
        return matches.asStream().map { match ->
            Symbol(match.value, Point(index + 1, match.range.first + 1))
        }.toList()
    }

    private fun getPartNumbersFromLine(index: Int, line: String): List<PartNumber> {
        val matches = Regex("([0-9]+)").findAll(line)
        return matches.asStream().map { match ->
            PartNumber(match.value.toInt(), match.range.map { location -> Point(index + 1, location + 1) })
        }.toList()
    }

    data class Symbol(val symbol: String, val location: Point)
    data class PartNumber(val number: Int, val location: List<Point>)
}
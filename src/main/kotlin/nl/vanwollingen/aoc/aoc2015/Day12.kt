package nl.vanwollingen.aoc.aoc2015

import nl.vanwollingen.aoc.util.Puzzle

fun main() {
    val d12 = Day12()
    d12.part1()
    d12.part2() //TODO: gives the wrong solution
}

class Day12() : Puzzle() {
    override fun part1() {
        println(Regex("-?\\d+").findAll(input).toList().sumOf { it.value.toInt() })
    }

    override fun part2() {
        val sum: MutableList<Int> = mutableListOf()
        val currentsum: MutableList<Int> = mutableListOf()
        var number = ""
        var negate = false
        var inRed = ""
        var inObject = false
        for (char in "[1,{\"c\":\"red\",\"b\":2},3]") {
            if (char == '{') {
                inObject = true
                if (inRed != "red") {
                    sum += currentsum
                    currentsum.clear()
                }
            }
            if (inObject && char == 'r' && inRed == "") inRed = "r"
            if (inObject && char == 'e' && inRed == "r") inRed = "re"
            if (inObject && char == 'd' && inRed == "re") {
                inRed = "red"
                currentsum.clear()
            }
            if (char == '}') {
                if (inRed != "red") {
                    sum += currentsum
                    currentsum.clear()
                }
                inRed = ""
                inObject = false
            }

            if (char.isDigit()) number += char
            if (char == '-') negate = true
            if (number.isNotEmpty() && !char.isDigit() && inRed != "red") {
                println("Adding $number ($negate)")
                currentsum += number.toInt() * if (!negate) 1 else -1
                number = ""
                negate = false
            }
        }
        if (number.isNotEmpty() && inRed != "red") {
            currentsum += number.toInt() * if (!negate) 1 else -1
            sum += currentsum
        }
        println(sum)
        println(sum.sum())

    }
}
package nl.vanwollingen.aoc.aoc2015

import nl.vanwollingen.aoc.util.Puzzle

fun main() {
    val d16 = Day16()
    d16.solvePart1()
    d16.solvePart2()
}

class Day16() : Puzzle() {

    private val aunties = parseInput()
    private val children = 3
    private val cats = 7
    private val samoyeds = 2
    private val pomeranians = 3
    private val akitas = 0
    private val vizslas = 0
    private val goldfish = 5
    private val trees = 3
    private val cars = 2
    private val perfumes = 1

    override fun solvePart1() {
        println(aunties.first { auntie ->
            (auntie.children == -1 || auntie.children == children) && (auntie.cats == -1 || auntie.cats == cats) && (auntie.samoyeds == -1 || auntie.samoyeds == samoyeds) && (auntie.pomeranians == -1 || auntie.pomeranians == pomeranians) && (auntie.akitas == -1 || auntie.akitas == akitas) && (auntie.vizslas == -1 || auntie.vizslas == vizslas) && (auntie.goldfish == -1 || auntie.goldfish == goldfish) && (auntie.trees == -1 || auntie.trees == trees) && (auntie.cars == -1 || auntie.cars == cars) && (auntie.perfumes == -1 || auntie.perfumes == perfumes)
        })
    }

    override fun solvePart2() {
        println(aunties.first { auntie ->
            (auntie.children == -1 || auntie.children == children) && (auntie.cats == -1 || auntie.cats > cats) && (auntie.samoyeds == -1 || auntie.samoyeds == samoyeds) && (auntie.pomeranians == -1 || auntie.pomeranians < pomeranians) && (auntie.akitas == -1 || auntie.akitas == akitas) && (auntie.vizslas == -1 || auntie.vizslas == vizslas) && (auntie.goldfish == -1 || auntie.goldfish < goldfish) && (auntie.trees == -1 || auntie.trees > trees) && (auntie.cars == -1 || auntie.cars == cars) && (auntie.perfumes == -1 || auntie.perfumes == perfumes)
        })
    }

    override fun parseInput(): List<AuntSue> = input.lines().map { line -> AuntSue.fromString(line) }

    data class AuntSue(
            val number: Int,
            val children: Int,
            val cats: Int,
            val samoyeds: Int,
            val pomeranians: Int,
            val akitas: Int,
            val vizslas: Int,
            val goldfish: Int,
            val trees: Int,
            val cars: Int,
            val perfumes: Int,
    ) {
        companion object {
            fun fromString(line: String): AuntSue {
                val sueNumber = getFromRegex("Sue (\\d+):", line)
                val children = getFromRegex("children: (\\d+)", line)
                val cars = getFromRegex("cars: (\\d+)", line)
                val vizslas = getFromRegex("vizslas: (\\d+)", line)
                val goldfish = getFromRegex("goldfish: (\\d+)", line)
                val akitas = getFromRegex("akitas: (\\d+)", line)
                val samoyeds = getFromRegex("samoyeds: (\\d+)", line)
                val perfumes = getFromRegex("perfumes: (\\d+)", line)
                val trees = getFromRegex("trees: (\\d+)", line)
                val pomeranians = getFromRegex("pomeranians: (\\d+)", line)
                val cats = getFromRegex("cats: (\\d+)", line)

                return AuntSue(
                        number = sueNumber,
                        children = children,
                        cars = cars,
                        vizslas = vizslas,
                        goldfish = goldfish,
                        akitas = akitas,
                        samoyeds = samoyeds,
                        perfumes = perfumes,
                        trees = trees,
                        pomeranians = pomeranians,
                        cats = cats,
                )
            }

            private fun getFromRegex(regex: String, line: String) = (Regex(regex).find(line).takeIf { it != null }?.let { it.groups[1]?.value?.toInt() }
                    ?: -1)
        }
    }
}
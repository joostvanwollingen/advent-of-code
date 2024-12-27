package nl.vanwollingen.aoc.aoc2020

import nl.vanwollingen.aoc.util.Puzzle

fun main() = Day04.solve()

object Day04 : Puzzle(exampleInput = false) {

    private val passports = input.split("\n\n").map { Passport.fromString(it) }

    override fun part1() =
        passports.count { it.isValid() }

    override fun part2() =
        passports.count { it.isValid() && it.automaticValidation() }


    data class Passport(
        val byr: String?,
        val iyr: String?,
        val eyr: String?,
        val hgt: String?,
        val hcl: String?,
        val ecl: String?,
        val pid: String?,
        val cid: String?,
    ) {

        fun isValid() = byr != null &&
                iyr != null &&
                eyr != null &&
                hgt != null &&
                hcl != null &&
                ecl != null &&
                pid != null

        fun automaticValidation(): Boolean {
            return byr!!.toInt() >= 1920 && byr.toInt() <= 2002
                    && iyr!!.toInt() >= 2010 && iyr.toInt() <= 2020
                    && eyr!!.toInt() >= 2020 && eyr.toInt() <= 2030
                    && validHeight(hgt!!)
                    && hcl!!.matches(Regex("#[0-9a-f]{6}"))
                    && ecl!! in listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
                    && pid!!.matches(Regex("\\d{9}"))
        }

        fun validHeight(height: String): Boolean {
            if (height.endsWith("cm")) {
                val cm = height.dropLast(2).toInt()
                return cm >= 150 && cm <= 193
            } else if (height.endsWith("in")) {
                val inch = height.dropLast(2).toInt()
                return inch >= 59 && inch <= 76
            }
            return false
        }

        companion object {
            fun fromString(input: String): Passport {
                val kv = input.lines().flatMap { line ->
                    val kv = line.split(" ")
                    kv.map {
                        val split = it.split(":")
                        split[0] to split[1]
                    }
                }.associate { it.first to it.second }
                return Passport(
                    byr = kv["byr"],
                    iyr = kv["iyr"],
                    eyr = kv["eyr"],
                    hgt = kv["hgt"],
                    hcl = kv["hcl"],
                    ecl = kv["ecl"],
                    pid = kv["pid"],
                    cid = kv["cid"]
                )
            }
        }
    }
}


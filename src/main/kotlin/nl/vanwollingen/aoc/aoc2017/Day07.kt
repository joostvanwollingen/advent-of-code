package nl.vanwollingen.aoc.aoc2017

import nl.vanwollingen.aoc.util.Puzzle
import kotlin.system.measureTimeMillis

fun main() {
    val d7 = Day07()
    d7.solvePart1()
//    d7.test()
    d7.solvePart2() //TODO fix part 2
}

class Day07(output: Boolean = false) : Puzzle(output) {

    private val disks = parseInput(input)

    fun parseInput(input: String): MutableMap<String, Disk> =
        input
            .lines()
            .map { line -> Disk.fromString(line) }
            .let { populateDiskMap(it) }

    fun test() {
        val inf = """pbga (66)
xhth (57)
ebii (61)
havc (66)
ktlj (57)
fwft (72) -> ktlj, cntj, xhth
qoyq (66)
padx (45) -> pbga, havc, qoyq
tknk (41) -> ugml, padx, fwft
jptl (61)
ugml (68) -> gyxo, ebii, jptl
gyxo (61)
cntj (57)"""

        val disks = parseInput(inf)
        val firstDisk = disks[disks.filterValues { it.parent == null }.keys.first()]!!
        for (carry in firstDisk.carries) {
            log("$carry ${carriesWeight(disks[carry]!!, disks, 0L)}")
        }

//        val byLevel = disks.values.groupBy { disk ->
//            disk.level to disk.parent
//        }
//
//        val maxLevel = disks.values.maxOf { it.level!! }
//        for (l in maxLevel downTo 0) {
//            val branches = byLevel.filter { l == it.key.first!! }.values
//
//            branches.forEach { branch ->
//
//                var currentWeights = true
//                var previousWeight: Int? = null
//
//                branch.forEach { branchDisk ->
//                    log("${branchDisk.name} (${branchDisk.weight})")
//                    if (previousWeight != null && currentWeights == true) {
//                        if (previousWeight != branchDisk.weight) currentWeights = false
//                    }
//                    previousWeight = branchDisk.weight
//
//                }
//                log("\t ${branch[0].parent} ${currentWeights} $previousWeight")
//            }
//        }
    }

    private fun populateLevels(disks: MutableMap<String, Disk>): MutableMap<String, Disk> {
        var currentDisks = disks.filterValues { it.parent == null }.values.toList()
        var level = 0

        while (currentDisks.isNotEmpty()) {
            val newCurrentDisks = mutableListOf<Disk>()
            currentDisks.forEach {
                it.level = level
                it.carries.forEach { carry ->
                    newCurrentDisks += disks[carry]!!
                }
            }
            level++
            currentDisks = newCurrentDisks
        }
        return disks
    }

    override fun part1() {
        log(disks.filterValues { it.parent == null }.values.first().name)
    }

    override fun part2() {
        val maxLevel = disks.values.maxOf { it.level!! }

        disks.values.forEach { disk ->
            logDiskDownTree(disk, 0)
        }
    }

    private fun logDiskDownTree(disk: Disk, weight: Long) {
        measureTimeMillis {  }
        val weight = disk.weight + weight
        if(disk.parent!="hmvwl" && disk.parent!=null) {
            log("${disk.name} (${disk.weight}) -> ", false)
            logDiskDownTree(disks[disk.parent]!!, weight)
        } else {
            log("${disk.name} (${disk.weight}) : $weight")
        }


    }

    fun carriesWeight(firstDisk: Disk, disks: MutableMap<String, Disk>, weight: Long): Long {
        var carries = firstDisk.carries.map { disks[it] }
        if (carries.isEmpty()) return weight
        var carriesWeight = mutableMapOf<String, Long>()
        carries.forEach { disk ->
            carriesWeight[disk!!.name] = carriesWeight(disk, disks, weight + disk.weight)
        }
        return carriesWeight.values.sum() + firstDisk.weight
    }

    private fun populateDiskMap(disks: List<Disk>): MutableMap<String, Disk> {
        val diskMap = disks.associateBy { it.name }.toMutableMap()
        disks.forEach { disk ->
            if (disk.carries.isNotEmpty()) {
                disk.carries.forEach { carry ->
                    diskMap[carry]!!.parent = disk.name
                }
            }
        }
        return populateLevels(diskMap)
    }

    data class Disk(
        val name: String, val weight: Int, val carries: List<String>, var parent: String? = null, var level: Int? = null
    ) {
        companion object {
            fun fromString(input: String): Disk {
                if (input.contains("->")) {
                    val (nameWeight, carries) = input.split(" -> ")
                    val carriesList = carries.split(", ").toList()
                    val name = nameWeight.split(" ")[0]
                    val weight = nameWeight.split(" ")[1].drop(1).dropLast(1).toInt()
                    return Disk(name, weight, carriesList)
                } else {
                    val name = input.split(" ")[0]
                    val weight = input.split(" ")[1].drop(1).dropLast(1).toInt()
                    return Disk(name, weight, emptyList())
                }
            }
        }
    }
}
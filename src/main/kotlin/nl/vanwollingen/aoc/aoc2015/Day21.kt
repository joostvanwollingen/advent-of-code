package nl.vanwollingen.aoc.aoc2015

import nl.vanwollingen.aoc.util.Puzzle

fun main() {
    val d21 = Day21(2015, 21)
    d21.solvePart1()
    d21.solvePart2()
}

class Day21(year: Int, day: Int) : Puzzle(year, day) {
    override fun solvePart1() {
        val fights = generateCharactersAndEquipment().map { character -> character to fight(character, boss()) }
        println(fights.filter { fight -> fight.second }.minBy { fight -> fight.first.goldSpent })
    }

    override fun solvePart2() {
        val fights = generateCharactersAndEquipment().map { character -> character to fight(character, boss()) }
        println(fights.filter { fight -> !fight.second }.maxBy { fight -> fight.first.goldSpent })
    }

    private fun boss() = Character("Boss", 109, 8, 2)

    private fun generateCharactersAndEquipment(): Sequence<Character> = sequence {
        weapons.forEach { weapon ->
            armors.forEach { armor ->
                for (i in 0..rings.size) {
                    for (j in i + 1..<rings.size) {
                        val damage = weapon.damage + rings[i].damage + rings[j].damage
                        val armorScore = armor.armor + rings[i].armor + rings[j].armor
                        val costs = weapon.cost + armor.cost + rings[i].cost + rings[j].cost
                        val name = "${weapon.name} | ${armor.name} | ${rings[i].name} | ${rings[j].name}"
                        yield(Character(name, 100, damage, armorScore, costs))
                    }
                }
            }
        }
    }


    private fun fight(player: Character, boss: Character, output: Boolean = false): Boolean {
        var playersTurn = true
        while (player.hitpoints > 0 && boss.hitpoints > 0) {
            if (playersTurn) {
                boss.hitpoints -= (player.damage - boss.armor).coerceAtLeast(1)
                playersTurn = false
                if (output) println("The player deals ${player.damage}-${boss.armor} = ${(player.damage - boss.armor).coerceAtLeast(1)} damage; the boss goes down to ${boss.hitpoints} hit points.")
            } else {
                player.hitpoints -= (boss.damage - player.armor).coerceAtLeast(1)
                playersTurn = true
                if (output) println("The boss deals ${boss.damage}-${player.armor} = ${(boss.damage - player.armor).coerceAtLeast(1)} damage; the player goes down to ${player.hitpoints} hit points.")
            }
        }
        val winner = if (player.hitpoints <= 0) boss else player
        if (output) println("Winner: ${winner.name}")
        return winner == player
    }

    private data class Character(val name: String, var hitpoints: Int, val damage: Int, val armor: Int, val goldSpent: Int = 0)
    private data class Item(val name: String, val cost: Int, val damage: Int, val armor: Int)

    private val weapons: List<Item> = listOf(
            Item("Dagger", 8, 4, 0),
            Item("Shortsword", 10, 5, 0),
            Item("Warhammer", 25, 6, 0),
            Item("Longsword", 40, 7, 0),
            Item("Greataxe", 74, 8, 0),
    )

    private val armors: List<Item> = listOf(
            Item("No armor", 0, 0, 0),
            Item("Leather", 13, 0, 1),
            Item("Chainmail", 31, 0, 2),
            Item("Splintmail", 53, 0, 3),
            Item("Bandedmail", 75, 0, 4),
            Item("Platemail", 102, 0, 5),
    )

    private val rings: List<Item> = listOf(
            Item("No ring 1", 0, 0, 0),
            Item("Damage +1", 25, 1, 0),
            Item("Damage +2", 50, 2, 0),
            Item("Damage +3", 100, 3, 0),
            Item("Defense +1", 20, 0, 1),
            Item("Defense +2", 40, 0, 2),
            Item("Defense +3", 80, 0, 3),
            Item("No ring 2", 0, 0, 0),
    )
}
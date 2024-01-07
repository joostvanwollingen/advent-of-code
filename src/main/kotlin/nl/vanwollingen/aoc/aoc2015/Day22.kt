package nl.vanwollingen.aoc.aoc2015

import nl.vanwollingen.aoc.util.Puzzle
import nl.vanwollingen.aoc.aoc2015.Day22.Effect.EffectType
import nl.vanwollingen.aoc.aoc2015.Day22.Spell.SpellType
import kotlin.system.exitProcess

fun main() {
    val d22 = Day22()
    d22.solvePart1()
}

class Day22() : Puzzle() {

    override fun solvePart1() {
        val magicMissileFirst = mutableListOf(Spell.of(SpellType.MAGIC_MISSILE))
        val shieldFirst = mutableListOf(Spell.of(SpellType.SHIELD))
        val poisonFirt = mutableListOf(Spell.of(SpellType.POISON))
        val drainFirst = mutableListOf(Spell.of(SpellType.DRAIN))
        val rechargeFirst = mutableListOf(Spell.of(SpellType.RECHARGE))

        var spells = mutableListOf(magicMissileFirst, shieldFirst, poisonFirt, drainFirst, rechargeFirst)
        var win = false
        while (win == false) {
            spells.forEach { spell ->
                try {
                    val player = Character("Player", 50, 0, 0, 500, spell)
                    val boss = Character("Boss", 58, 9)
                    win = fight(player, boss)
                    if (win) {
                        println("Won with $player")
                        exitProcess(1)
                    }
                } catch (e: NoSuchElementException) {
                    log("Player died because of no spells left")
                }
            }
            spells = getSpells(spells)
        }
    }

    private fun getSpells(lists: MutableList<MutableList<Spell>>): MutableList<MutableList<Spell>> {
        var newLists: MutableList<MutableList<Spell>> = mutableListOf()
        lists.forEach { l ->
            var newList = mutableListOf<MutableList<Spell>>()
            newList += (l + Spell.of(SpellType.MAGIC_MISSILE)).toMutableList()
            newList += (l + Spell.of(SpellType.DRAIN)).toMutableList()
            newList += (l + Spell.of(SpellType.POISON)).toMutableList()
            newList += (l + Spell.of(SpellType.SHIELD)).toMutableList()
            newList += (l + Spell.of(SpellType.RECHARGE)).toMutableList()
            newLists += newList
        }
        return newLists
    }

    override fun solvePart2() {
        TODO("Not yet implemented")
    }

    private fun fight(player: Character, boss: Character): Boolean {
        var playersTurn = true
        var turn = 0
        val effects: MutableSet<Effect> = mutableSetOf()

        while (player.hitpoints > 0 && boss.hitpoints > 0) {
            turn++
            log("\uD83C\uDFAF ======== Turn $turn ======== \uD83C\uDFAF")
            val magicArmor = effects.firstOrNull { it.type == EffectType.SHIELD }?.armor ?: 0
            effects.forEach { e -> e.apply(player, boss) }
            if (playersTurn) {
                if (player.mana < 53) return false
                player.castSpell(boss, effects).also { if (it != null) effects += it }
                playersTurn = false
                log("The player deals ${player.damage} damage; the boss goes down to ${boss.hitpoints} hit points.")
            } else {
                player.hitpoints -= (boss.damage - magicArmor).coerceAtLeast(1)
                playersTurn = true
                log("The boss deals ${boss.damage}-$magicArmor = ${(boss.damage - magicArmor).coerceAtLeast(1)} damage; the player goes down to ${player.hitpoints} hit points.")
            }
            effects.removeIf { it.duration == 0 }
        }
        val winner = if (player.hitpoints <= 0) boss else player
        log("Winner: ${winner.name}")
        return winner == player
    }

    data class Spell(val type: SpellType, val cost: Int, val damage: Int = 0, val healing: Int = 0, val effect: Effect? = null) {
        companion object {
            fun of(type: SpellType): Spell {
                return when (type) {
                    SpellType.MAGIC_MISSILE -> Spell(SpellType.MAGIC_MISSILE, 53, 4)

                    SpellType.DRAIN -> Spell(SpellType.DRAIN, 73, 2, 2)

                    SpellType.SHIELD -> Spell(SpellType.SHIELD, 113, effect = Effect.of(EffectType.SHIELD))

                    SpellType.POISON -> Spell(SpellType.POISON, 173, effect = Effect.of(EffectType.POISON))

                    SpellType.RECHARGE -> Spell(SpellType.RECHARGE, 229, effect = Effect.of(EffectType.RECHARGE))
                }
            }
        }

        enum class SpellType {
            MAGIC_MISSILE, DRAIN, SHIELD, POISON, RECHARGE
        }
    }

    data class Effect(val type: EffectType, var duration: Int = 0, val damage: Int = 0, val healing: Int = 0, val mana: Int = 0, val armor: Int = 0) {
        enum class EffectType {
            SHIELD, POISON, RECHARGE
        }

        fun apply(player: Character, boss: Character) {
            if (duration > 0) {
                duration--
                player.mana += mana
                boss.hitpoints -= damage
                log("Applied effect $type: hp +$healing, mana+$mana. Boss damaged for :$damage")
            }
        }

        companion object {
            fun of(type: EffectType): Effect = when (type) {
                EffectType.SHIELD -> Effect(EffectType.SHIELD, duration = 6, armor = 7)
                EffectType.POISON -> Effect(EffectType.POISON, duration = 6, damage = 3)
                EffectType.RECHARGE -> Effect(EffectType.RECHARGE, duration = 5, mana = 101)
            }
        }
    }

    data class Character(val name: String, var hitpoints: Int, var damage: Int, var armor: Int = 0, var mana: Int = 0, val spells: List<Spell> = emptyList()) {
        var manaSpent = 0
        val spellsIterator = spells.iterator()
        fun castSpell(boss: Character, effects: Set<Effect>): Effect? {
            var spell = spellsIterator.next()
            while (effects.any { it.type == spell.effect?.type } || mana < spell.cost) {
                log("Can't cast $spell, because effect is already active or insufficient mana")
                spell = spellsIterator.next()
            }
            log("Player cast spell: ${spell.type}")
            mana -= spell.cost
            manaSpent += spell.cost

            when (spell.type) {
                SpellType.MAGIC_MISSILE -> {
                    boss.hitpoints -= spell.damage
                }

                SpellType.DRAIN -> {
                    boss.hitpoints -= spell.damage
                    hitpoints += spell.healing
                }

                SpellType.SHIELD -> {
                    return Effect.of(EffectType.SHIELD)
                }

                SpellType.POISON -> {
                    return Effect.of(EffectType.POISON)
                }

                SpellType.RECHARGE -> {
                    return Effect.of(EffectType.RECHARGE)
                }
            }
            return null
        }

        override fun toString(): String {
            return "Character(name='$name', hitpoints=$hitpoints, damage=$damage, armor=$armor, mana=$mana, spellSequence=$spells, manaSpent=$manaSpent)"
        }
    }
}
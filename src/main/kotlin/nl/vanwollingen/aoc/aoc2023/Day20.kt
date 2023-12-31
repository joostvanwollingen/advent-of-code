package nl.vanwollingen.aoc.aoc2023

import nl.vanwollingen.aoc.util.PuzzleInputUtil
import nl.vanwollingen.aoc.util.quickmaths.mansnothot.bigshaq.findLcm
import java.util.*
import kotlin.system.exitProcess

fun main() {
    Day20().solvePart1(1000)
    Day20().solvePart1(4007)
}

class Day20() {

    fun solvePart1(requiredButtonPresses:Int = 4) {
        val pulseModules: List<PulseModule> = PuzzleInputUtil.parse("2023/day20.input", PulseModule::fromString).toList()

        val conjunctions: List<Conjunction> =
                pulseModules.filter { it.type == PulseModule.PulseModuleType.CONJUNCTION } as List<Conjunction>
        conjunctions.forEach { it.loadInputModules(pulseModules) }
        println(pulseModules)

        val button = Button("button", type = PulseModule.PulseModuleType.BUTTON)
        val q: Queue<Pulse> = LinkedList()

        val processedPulses: MutableList<Pulse> = mutableListOf()
        var buttonPresses = 0L
        var conjunctionHighWhen: MutableMap<String, Long> = mutableMapOf()
        while (buttonPresses < requiredButtonPresses) {
            buttonPresses++
            q.addAll(button.push())
            while (q.isNotEmpty()) {
                val currentPulse = q.remove()
                processedPulses += currentPulse

                val highConjunctions =
                        conjunctions.map { it to it.inputModules.values.all { value -> value == Day20.PulseType.HIGH } }
                if (highConjunctions.any { it.second }) {
                    highConjunctions.filter { it.second }.forEach {
                        if (conjunctionHighWhen[it.first.name] == null) conjunctionHighWhen[it.first.name] = buttonPresses
                    }
                    if (conjunctionHighWhen.size == 8) {
                        println(findLcm(conjunctionHighWhen.values.toList()))
//                        println(conjunctionHighWhen)
                        exitProcess(1)
                    }
                }

                if (currentPulse.destination != "rx") {
                    val processingModule = pulseModules.firstOrNull { it.name == currentPulse.destination }
                    if (processingModule == null) {
//                        println("module is null $currentPulse");
                        break
                    }
                    q.addAll(processingModule.receivePulse(currentPulse))
                }
            }
        }
//        println("button: $buttonPresses")
        val low: Long = processedPulses.count { it.type == Day20.PulseType.LOW }.toLong()
        val high: Long = processedPulses.count { it.type == Day20.PulseType.HIGH }.toLong()
        println("low: $low high: $high mul: ${low * high}")
    }
    interface PulseModule {
        val type: PulseModuleType

        enum class PulseModuleType {
            BROADCASTER, CONJUNCTION, FLIPFLOP, BUTTON
        }

        val name: String
        val destinationModules: List<String>
        fun receivePulse(pulse: Pulse): List<Pulse>

        companion object {
            fun fromString(input: String): PulseModule = when (input[0]) {
                '%' -> FlipFlop(
                    input.subSequence(1, 3).toString().trim(),
                    input.split(" -> ")[1].split(",").map { it.trim() },
                    PulseModuleType.FLIPFLOP
                )

                '&' -> Conjunction(
                    input.subSequence(1, 4).toString().trim(),
                    input.split(" -> ")[1].split(",").map { it.trim() },
                    PulseModuleType.CONJUNCTION
                )

                'b' -> Broadcaster(
                    "broadcaster", input.split(" -> ")[1].split(",").map { it.trim() }, PulseModuleType.BROADCASTER
                )

                else -> throw Exception("Failed to map to PulseModule: $input")
            }
        }
    }

    class Broadcaster(
        override val name: String,
        override val destinationModules: List<String>,
        override val type: PulseModule.PulseModuleType
    ) : PulseModule {
        override fun receivePulse(pulse: Pulse): List<Pulse> = destinationModules.map { Pulse(pulse.type, name, it) }
        override fun toString(): String {
            return "Broadcaster(name='$name', destinationModules=$destinationModules)"
        }
    }

    class FlipFlop(
        override val name: String,
        override val destinationModules: List<String>,
        override val type: PulseModule.PulseModuleType
    ) : PulseModule {
        var state = ModuleState.OFF
        override fun receivePulse(pulse: Pulse): List<Pulse> {
            val response: MutableList<Pulse> = mutableListOf()
            if (pulse.type == PulseType.HIGH) return emptyList()
            if (pulse.type == PulseType.LOW) {
                response += if (state == ModuleState.OFF) {
                    destinationModules.map { Pulse(PulseType.HIGH, name, it) }
                } else {
                    destinationModules.map { Pulse(PulseType.LOW, name, it) }
                }
                flipState()
            }
            return response
        }

        private fun flipState() {
            state = if (state == ModuleState.OFF) ModuleState.ON else ModuleState.OFF
        }

        override fun toString(): String {
            return "FlipFlop(name='$name', destinationModules=$destinationModules, state=$state)"
        }

        enum class ModuleState {
            ON, OFF
        }
    }

    class Conjunction(
        override val name: String,
        override val destinationModules: List<String>,
        override val type: PulseModule.PulseModuleType
    ) : PulseModule {
        var inputModules: MutableMap<String, PulseType> = mutableMapOf()

        fun loadInputModules(modules: List<PulseModule>) {
            modules.filter { mod -> mod.destinationModules.contains(name) }
                .forEach { inputModules[it.name] = PulseType.LOW }
        }

        override fun receivePulse(pulse: Pulse): List<Pulse> {
            if (inputModules[pulse.source] == null) throw Exception("Pulse received: $pulse. Source not registered as input module")
            inputModules[pulse.source] = pulse.type

            return if (inputModules.all { it.value == PulseType.HIGH }) {
                destinationModules.map { Pulse(PulseType.LOW, name, it) }
            } else {
                destinationModules.map { Pulse(PulseType.HIGH, name, it) }
            }
        }

        override fun toString(): String {
            return "Conjunction(name='$name', destinationModules=$destinationModules, inputModules=$inputModules)"
        }
    }

    class Button(
        override val name: String,
        override val destinationModules: List<String> = listOf("broadcaster"),
        override val type: PulseModule.PulseModuleType
    ) : PulseModule {
        override fun receivePulse(pulse: Pulse): List<Pulse> {
            return emptyList()
        }

        fun push() = listOf(Pulse(PulseType.LOW, name, "broadcaster"))

        override fun toString(): String {
            return "Button(name='$name', destinationModules=$destinationModules)"
        }

    }

    enum class PulseType {
        HIGH, LOW
    }

    data class Pulse(val type: PulseType, val source: String, val destination: String)
}
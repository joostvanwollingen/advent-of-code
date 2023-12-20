import java.util.*

fun main() {
    val pulseModules: List<Day20.PulseModule> =
//        AocUtil.parse("day20.test.input", Day20.PulseModule::fromString).toList()
        AocUtil.parse("day20.test2.input", Day20.PulseModule::fromString).toList()

    val conjunctions: List<Day20.Conjunction> =
        pulseModules.filter { it.type == Day20.PulseModule.PulseModuleType.CONJUNCTION } as List<Day20.Conjunction>
    conjunctions.forEach { it.loadInputModules(pulseModules) }
    println(pulseModules)

    val button = Day20.Button("button", type = Day20.PulseModule.PulseModuleType.BUTTON)
    val q: Queue<Day20.Pulse> = LinkedList()

    val processedPulses: MutableList<Day20.Pulse> = mutableListOf()
    var buttonPresses = 0
    while (buttonPresses < 1000) {
        buttonPresses++
        q.addAll(button.push())
        while (q.isNotEmpty()) {
            val currentPulse = q.remove()
            processedPulses += currentPulse
            if (currentPulse.destination != "output") {
                q.addAll(pulseModules.first { it.name == currentPulse.destination }.receivePulse(currentPulse))
            }
        }
    }
//    println(processedPulses)
    println(processedPulses.count { it.type == Day20.PulseType.LOW })
    println(processedPulses.count { it.type == Day20.PulseType.HIGH })
}


class Day20() {
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
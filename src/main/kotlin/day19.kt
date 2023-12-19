class Day19() {
//    val input = AocUtil.load("day19.test.input")

        val input = AocUtil.load("day19.input")
    val workflows = input.split("\n\n")[0].split("\n").map { Workflow.fromString(it) }
    val parts = input.split("\n\n")[1].split("\n").map { MachinePart.fromString(it) }


    fun solve1() {
        println(workflows)
        println(parts)

        var currentWorkflow = workflows.first { it.name == "in" }
        var accepted: MutableMap<MachinePart, RuleResult> = mutableMapOf()

        parts@ for (p in parts) {
            var result: RuleResult? = RuleResult(nextWorkflow = "in")

            while (result?.accept != true && result?.reject != true) {
                currentWorkflow =
                    if (result?.nextWorkflow != "A" && result?.nextWorkflow != "R") workflows.first { it.name == result!!.nextWorkflow } else workflows.first { it.name == "in" }
                for (i in currentWorkflow.rules.indices) {
                    result = currentWorkflow.rules[i].evaluate(p)
                    if (result.evaluation == true) {
                        println("part $p rule ${currentWorkflow.rules[i]} result $result")
                        println("Next: ${result.nextWorkflow}")
                        if (result.accept == true) {
                            println("part accepted: $p")
                            accepted.put(p, result)
                            continue@parts
                        }
                        if (result.reject == true) {
                            println("part rejected: $p")
                            continue@parts
                        }
                        break
                    }
                }
            }
        }
        println(accepted)
        println(accepted.keys.sumOf { it.a + it.m + it.s + it.x })
    }

    data class MachinePart(val x: Int, val m: Int, val a: Int, val s: Int) {
        companion object {
            fun fromString(input: String): MachinePart {
                val xmas = input.drop(1).dropLast(1).split(",")
                return MachinePart(
                    xmas[0].split("=")[1].toInt(),
                    xmas[1].split("=")[1].toInt(),
                    xmas[2].split("=")[1].toInt(),
                    xmas[3].split("=")[1].toInt()
                )
            }
        }
    }


    class Workflow(val name: String, val rules: List<Rule>) {

        companion object {
            fun fromString(input: String): Workflow {
                val name = input.split("{")[0]
                val rulesString: List<String> = Regex("\\{(.*)}").find(input)!!.value.drop(1).dropLast(1).split(",")
                val rules: List<Rule> = rulesString.map { rule ->

                    if (rule[0] == 'A') {
                        Rule(nextWorkflow = "A", accept = true)
                    } else if (rule[0] == 'R') {
                        Rule(nextWorkflow = "R", reject = true)
                    } else {
                        val semiIndex = rule.indexOf(':')
                        if (semiIndex > 0) {
                            Rule(
                                rule[0].toString(),
                                rule[1].toString(),
                                rule.subSequence(2, semiIndex).toString().toInt(),
                                rule.subSequence(semiIndex + 1, rule.length).toString(),
                                accept = if (rule.subSequence(semiIndex + 1, rule.length)
                                        .toString() == "A"
                                ) true else null,
                                reject = if (rule.subSequence(semiIndex + 1, rule.length)
                                        .toString() == "R"
                                ) true else null,
                            )
                        } else {
                            Rule(nextWorkflow = rule)
                        }
                    }
                }
                return Workflow(name, rules)
            }
        }

        override fun toString(): String {
            return "Workflow(name='$name', rules=$rules)"
        }
    }

    data class Rule(
        val field: String? = null,
        val operator: String? = null,
        val value: Int? = null,
        val nextWorkflow: String? = null,
        val accept: Boolean? = null,
        val reject: Boolean? = null,
    ) {
        fun evaluate(machinePart: MachinePart): RuleResult {
            if (field != null) {
                val fieldValue = when (field) {
                    "x" -> machinePart.x
                    "m" -> machinePart.m
                    "a" -> machinePart.a
                    "s" -> machinePart.s
                    else -> throw Exception("Not able to match field $field")
                }

                val result = if (operator == ">") fieldValue > value!! else fieldValue < value!!

                val nextWorkflow = if (result) nextWorkflow else null
                val accepted = accept == true
                val rejected = reject == true

                return RuleResult(result, accepted, rejected, nextWorkflow)
            }
            return RuleResult(
                evaluation = if (nextWorkflow == "R" || nextWorkflow == "A") true else null,
                nextWorkflow = nextWorkflow,
                reject = reject,
                accept = accept
            )
        }
    }
}

data class RuleResult(
    val evaluation: Boolean? = null, val accept: Boolean? = null, val reject: Boolean? = null, val nextWorkflow: String?
)

fun main() {
    Day19().solve1()
}
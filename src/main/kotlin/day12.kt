import java.util.*

fun main() {
//    val input = AocUtil.load("day12.test.input")
    val input = AocUtil.load("day12.input")
    val springConditions: List<SpringConditions> = input.lines().map {
        SpringConditions.fromString(it)
    }

    val q = springConditions.sumOf { s ->
        val possibilities = replaceQuestionmark(s.record.lineSequence(), s.record.count { it == '?' }).toList()
        possibilities.count { checkValidity(it, s.sections) }
    }
    println(q)
}

fun checkValidity(s: String, list: List<Int>): Boolean {
    val split = s.split(".")
    val lengths = split.filter { it.isNotBlank() }.map { it.length }
    return lengths == list
}

private tailrec fun replaceQuestionmark(lines: Sequence<String>, count: Int): Sequence<String> {
    if (count > 0) {
        return replaceQuestionmark(
            lines.flatMap {
                listOf(
                    it.replaceFirst("?", "."), it.replaceFirst("?", "#")
                )
            }, count - 1
        )
    }
    return lines
}

data class SpringConditions(val record: String, val sections: List<Int>) {
    companion object {
        fun fromString(input: String): SpringConditions {
            val (record, sections) = input.split(" ")

            return SpringConditions(record, sections.split(",").map { it.toInt() })
        }
    }
}
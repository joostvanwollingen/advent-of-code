fun main() {
//    val input = AocUtil.load("day15.test.input")
    val input = AocUtil.load("day15.test2.input")

    val toBeHashed = input.split(",")
    println(toBeHashed.sumOf { hashString(it) })
}

private fun hashString(input: String): Long =
    input.toCharArray().fold(0L) { acc, char -> (acc + char.code).times(17).rem(256) }

fun main() {
    val input1 = AocUtil.load("day6.input")
    val input = AocUtil.load("day6.2.input")
//    val input = AocUtil.load("day6.test.input")
    solveOne(input1)
    solveTwo(input)
}

fun solveOne(input: String) {
    val times = input.lines().first().split(":")[1].trim().split(" ").filter { it.isNotEmpty() }
    val distance = input.lines().last().split(":")[1].trim().split(" ").filter { it.isNotEmpty() }
    val races = times.zip(distance).map { r -> Race(r.first.toInt(), r.second.toInt()) }
    println(races.map { race -> race.numberOfWinningOutcomes }.reduce(Int::times))
}

fun solveTwo(input: String) {
    val times = input.lines().first().split(":")[1].trim().split(" ").filter { it.isNotEmpty() }
    val distance = input.lines().last().split(":")[1].trim().split(" ").filter { it.isNotEmpty() }
    val races = times.zip(distance).map { r -> RaceD(r.first.toLong(), r.second.toLong()) }
    println(races[0].calc())
}

data class RaceD(val raceDuration: Long, val previousRecord: Long) {
    val distanceToWin = this.previousRecord + 1
    fun calc(): Long {
        var results = 0L
        for (i in 0..raceDuration) {
                if((raceDuration-i)*i >= distanceToWin) {
                    results = results.plus(1)
            }
        }
        return results
    }

    override fun toString(): String {
        return "RaceD(raceDuration=$raceDuration, previousRecord=$previousRecord, distanceToWin=$distanceToWin)"
    }

}

data class Race(val raceDuration: Int, val previousRecord: Int) {
    val distanceToWin = this.previousRecord + 1
    val possibleOutcomes = calc()
    val winningOutcomes = possibleOutcomes.filter { outcome -> outcome.value.second >= distanceToWin }
    val numberOfWinningOutcomes = winningOutcomes.size

    private fun calc(): Map<Int, Pair<Int, Int>> {
        val results: MutableMap<Int, Pair<Int, Int>> = mutableMapOf()
        for (i in 0..raceDuration) {
            results.put(i, Pair(i, (raceDuration - i) * i))
        }
        return results
    }

    override fun toString(): String {
        return "Race(raceDuration=$raceDuration, previousRecord=$previousRecord, distanceToWin=$distanceToWin, possibleOutcomes=$possibleOutcomes, winningOutcomes=$winningOutcomes, numberOfWinningOutcomes=$numberOfWinningOutcomes)"
    }

}

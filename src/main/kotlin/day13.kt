fun main() {
//    val input = AocUtil.load("day13.test.input")
    val input = AocUtil.load("day13.input")
    val reflections = input.reader().readText().split("\n\n")

    var columnReflectionIndex: Int = 0
    var rowReflectionIndex: Int = 0
    var leftOf = 0L
    var aboveOf = 0L

    reflections.forEach { ref ->
        println("Reflection: ${reflections.indexOf(ref)}")
        columnReflectionIndex = getReflectionIndex(ref.columns())
        rowReflectionIndex = getReflectionIndex(ref.lines(), "row")
        leftOf += columnReflectionIndex
        aboveOf += rowReflectionIndex
    }

    println("")
    println("Summary")
    println("leftOf: $leftOf")
    println("aboveOf: $aboveOf")
    println("=> ${(100*aboveOf) + leftOf}")
}

fun String.columns(): List<String> {
    val ret = mutableMapOf<Int, String>()
    this.lines().mapIndexed { index, line ->
        for (s in line.indices) {
            ret[s] = (ret[s] ?: "") + line[s].toString()
        }
    }
    return ret.values.toList()
}

fun getReflectionIndex(input: List<String>, v:String="column"): Int {
    var step = 0
    var columnReflectionIndex: Int = 0

    input.windowed(2, 1) {
        step++
        if (it.first() == it.last()) {
            println("Same $v $it at step $step")
            if (verifyRealReflection(input, step)) {
                println("Reflection was real")
                columnReflectionIndex = step
            }
            return@windowed
        }
    }
    return columnReflectionIndex
}

fun verifyRealReflection(input: List<String>, step: Int): Boolean {
    val left = input.subList(0, step)
    val right = input.subList(step, input.size)
    val zipped = left.reversed().zip(right)
    return zipped.all { it.first == it.second }
}

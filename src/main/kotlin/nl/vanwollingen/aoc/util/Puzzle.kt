package nl.vanwollingen.aoc.util

abstract class Puzzle(output: Boolean = false) {

    val input by lazy {
        val year = this::class.java.packageName.split(".").last().substring(3)
        val day = this::class.simpleName!!.substring(3)
        PuzzleInputUtil.load("$year/Day${day.toString().padStart(2, '0')}.input")
    }

    open fun parseInput(): Any {
        TODO("Not yet implemented")
    }

    abstract fun solvePart1()
    abstract fun solvePart2()

    init {
        showOutput = output
    }

    companion object {
        var showOutput = false
        fun log(message: Any, linebreak: Boolean = true) {
            print("${message}${if (linebreak) "\n" else ""}")
        }

        fun debug(message: Any) {
            if (showOutput) println(message.toString())
        }
    }
}
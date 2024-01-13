package nl.vanwollingen.aoc.util

import nl.vanwollingen.aoc.util.exceptions.TargetStateReachedException
import java.time.Instant
import java.time.temporal.ChronoUnit

abstract class Puzzle(output: Boolean = false) {

    val input by lazy {
        val year = this::class.java.packageName.split(".").last().substring(3)
        val day = this::class.simpleName!!.substring(3)
        PuzzleInputUtil.load("$year/Day${day.toString().padStart(2, '0')}.input")
    }

    open fun parseInput(): Any {
        TODO("Not yet implemented")
    }

    abstract fun part1()
    abstract fun part2()

    fun solvePart1() {
        runTimed { part1() }
    }

    fun solvePart2() {
        runTimed { part2() }
    }

    private fun runTimed(job: () -> Unit) {
        val start = Instant.now()
        try {
            job()
        } catch (e: TargetStateReachedException) {
            log("Target state was reached")
        } finally {
            val end = Instant.now()
            log("Completed in ${start.until(end, ChronoUnit.MILLIS)} ms")
        }
    }

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
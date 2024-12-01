package nl.vanwollingen.aoc.util

import nl.vanwollingen.aoc.util.exceptions.TargetStateReachedException
import java.time.Instant
import java.time.temporal.ChronoUnit

abstract class Puzzle(output: Boolean = false, exampleInput: Boolean = false) {

    val input by lazy {
        val year = this::class.java.packageName.split(".").last().substring(3)
        val day = this::class.simpleName!!.substring(3)
        PuzzleInputUtil.load(
            "$year/Day${day.toString().padStart(2, '0')}${
                if (exampleInput) {
                    ".example"
                } else {
                    ""
                }
            }.input"
        )
    }

    open fun parseInput(): Any {
        TODO("Not yet implemented")
    }

    abstract fun part1()
    abstract fun part2()

    fun solvePart1() = runTimed { part1() }

    fun solvePart2() = runTimed { part2() }

    private fun runTimed(job: () -> Unit): Long {
        var end: Instant = Instant.MAX
        val start = Instant.now()
        try {
            job()
            end = Instant.now()
        } catch (e: TargetStateReachedException) {
            debug("Target state was reached")
        } finally {
            log("Completed in ${start.until(end, ChronoUnit.MICROS)} μs")
        }
        return start.until(end, ChronoUnit.MICROS)
    }

    init {
        debug = output
    }

    companion object {
        var debug = false
        fun log(message: Any, linebreak: Boolean = true) {
            print("${message}${if (linebreak) "\n" else ""}")
        }

        fun debug(message: Any, linebreak: Boolean = true) {
            if (debug) print("${message}${if (linebreak) "\n" else ""}")
        }

        fun debug(message: String? = null, func: () -> Unit) {
            if (debug) {
                message?.let { println(it) }
                func()
            }
        }
    }
}
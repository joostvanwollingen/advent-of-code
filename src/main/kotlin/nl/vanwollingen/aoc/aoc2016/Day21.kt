package nl.vanwollingen.aoc.aoc2016

import nl.vanwollingen.aoc.util.Puzzle
import java.util.*

fun main() {
    val d21 = Day21()
    d21.solvePart1()
    d21.solvePart2()
}

class Day21(output: Boolean = false) : Puzzle(output) {

    override fun part1() {
        var scrambleInput = "abcdefgh".toCharArray()

        for (i in 0..<input.lines().size) {
            debug(input.lines()[i])
            scrambleInput = doOperation(input.lines()[i], scrambleInput)
            debug(scrambleInput.joinToString(""))
        }

        log(scrambleInput.joinToString(""))
    }

    override fun part2() {
        var unscrambleInput = "fbgdceah".toCharArray()

        for (i in 0..<input.lines().size) {
            debug(input.lines().reversed()[i])
            unscrambleInput = doOperation(input.lines().reversed()[i], unscrambleInput, true)
            debug(unscrambleInput.joinToString(""))
        }

        log(unscrambleInput.joinToString(""))
    }

    fun doOperation(operation: String, input: CharArray, reversed: Boolean = false): CharArray {
        val rotate = Regex("(rotate) (left|right) (\\d)")
        val swap = Regex("(swap) (position|letter) (.) with (position|letter) (.)")
        val rotateLetter = Regex("rotate based on position of letter ([a-z])")
        val reverse = Regex("(reverse) positions (\\d) through (\\d)")
        val move = Regex("(move) position (\\d) to position (\\d)")

        return if (!reversed) determineOperationRegular(operation, rotateLetter, input, rotate, swap, reverse, move)
        else return determineOperationReversed(operation, rotateLetter, input, rotate, swap, reverse, move)
    }

    private fun determineOperationReversed(
        operation: String,
        rotateLetter: Regex,
        input: CharArray,
        rotate: Regex,
        swap: Regex,
        reverse: Regex,
        move: Regex
    ): CharArray {
        if (operation.contains("rotate")) {
            if (operation.contains("based")) {
                val matches = rotateLetter.findAll(operation).toList().first().groupValues
                return reverseRotate(input, matches[1].single())
            }
            val matches = rotate.findAll(operation).toList().first().groupValues
            return rotate(input, if (matches[2] == "right") -1 else 1, matches[3].toInt())
        }

        if (operation.contains("swap")) {
            val matches = swap.findAll(operation).toList().first().groupValues
            return if (matches[2] == "position") {
                swapPosition(input, matches[3].toInt(), matches[5].toInt())
            } else {
                swapLetter(input, matches[3].single(), matches[5].single())
            }
        }

        if (operation.contains("reverse")) {
            val matches = reverse.findAll(operation).toList().first().groupValues
            return reversePositions(input, matches[2].toInt(), matches[3].toInt())
        }

        if (operation.contains("move")) {
            val matches = move.findAll(operation).toList().first().groupValues
            return movePosition(input, matches[3].toInt(), matches[2].toInt())
        }
        throw Exception("Failed to map operation")
    }

    private fun reverseRotate(input: CharArray, single: Char): CharArray {
        val rotations = when (input.indexOf(single)) {
            1 -> 1
            3 -> 2
            5 -> 3
            7 -> 4
            2 -> 6
            4 -> 7
            6 -> 8
            0 -> 9
            else -> throw Exception("")
        }

        return rotate(input, -1, rotations)
    }

    private fun determineOperationRegular(
        operation: String,
        rotateLetter: Regex,
        input: CharArray,
        rotate: Regex,
        swap: Regex,
        reverse: Regex,
        move: Regex
    ): CharArray {
        if (operation.contains("rotate")) {
            if (operation.contains("based")) {
                val matches = rotateLetter.findAll(operation).toList().first().groupValues
                return rotate(input, matches[1].single())
            }
            val matches = rotate.findAll(operation).toList().first().groupValues
            return rotate(input, if (matches[2] == "right") 1 else -1, matches[3].toInt())
        }

        if (operation.contains("swap")) {
            val matches = swap.findAll(operation).toList().first().groupValues
            return if (matches[2] == "position") {
                swapPosition(input, matches[3].toInt(), matches[5].toInt())
            } else {
                swapLetter(input, matches[3].single(), matches[5].single())
            }
        }

        if (operation.contains("reverse")) {
            val matches = reverse.findAll(operation).toList().first().groupValues
            return reversePositions(input, matches[2].toInt(), matches[3].toInt())
        }

        if (operation.contains("move")) {
            val matches = move.findAll(operation).toList().first().groupValues
            return movePosition(input, matches[2].toInt(), matches[3].toInt())
        }
        throw Exception("Failed to map operation")
    }

    private fun swapPosition(input: CharArray, x: Int, y: Int): CharArray {
        val xChar = input[x]
        val yChar = input[y]

        input[x] = yChar
        input[y] = xChar

        return input
    }

    private fun swapLetter(input: CharArray, letterA: Char, letterB: Char): CharArray {
        val aCharIndex = input.indexOf(letterA)
        val bCharIndex = input.indexOf(letterB)

        val aChar = input[input.indexOf(letterA)]
        val bChar = input[input.indexOf(letterB)]

        input[aCharIndex] = bChar
        input[bCharIndex] = aChar

        return input
    }

    private fun reversePositions(input: CharArray, start: Int, end: Int): CharArray {
        var endIndex = end
        if (end >= input.size) endIndex = input.size - 1
        val reverse = input.slice(start..endIndex).reversed()
        return (input.slice(0..<start) + reverse + input.slice(end + 1..<input.size)).toCharArray()
    }

    private fun rotate(input: CharArray, direction: Int, steps: Int): CharArray {
        val q: Deque<Char> = LinkedList()
        q.addAll(input.toList())
        for (i in 1..steps) {
            if (direction == -1) {
                q.addLast(q.pollFirst())
            } else {
                q.addFirst(q.pollLast())
            }
        }
        return q.toCharArray()
    }

    private fun movePosition(input: CharArray, from: Int, to: Int): CharArray {
        val charToMove = input[from]
        var returnValue = (input.slice(0..<from) + input.slice(from + 1..<input.size)).toMutableList()

        if (to > returnValue.size - 1) {
            returnValue.add(charToMove)
        } else if (to == 0) {
            returnValue = mutableListOf(charToMove).plus(returnValue).toMutableList()
        } else {
            returnValue =
                (returnValue.slice(0..<to) + charToMove + returnValue.slice(to..<returnValue.size)).toMutableList()
        }
        return returnValue.toCharArray()
    }

    private fun rotate(input: CharArray, letter: Char): CharArray {
        var rotations = 1
        val indexOf = input.indexOf(letter)
        rotations += input.indexOf(letter)
        if (indexOf >= 4) rotations++
        return rotate(input, 1, rotations)
    }
}
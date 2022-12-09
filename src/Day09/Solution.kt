package Day09

import readInput
import kotlin.math.abs

/**
 * Initial state:
    ......
    ......
    ......
    ......
    H.....
 */
fun main() {

    fun Boolean.toInt() = if (this) 1 else 0

    fun parseInstructions(input: List<String>) : List<Pair<Char, Int>> {
        return input.map { line ->
            line.split(" ").let { Pair(it.first().first(), it.last().toInt()) }
        }
    }

    fun moveHead(from: Pair<Int, Int>, direction: Char) : Pair<Int, Int> {
        return when (direction) {
            'R' -> Pair(from.first + 1, from.second)
            'D' -> Pair(from.first, from.second + 1)
            'L' -> Pair(from.first - 1, from.second)
            'U' -> Pair(from.first, from.second - 1)
            else -> throw IllegalArgumentException("Illegal direction: $direction")
        }
    }

    fun isTouching(head: Pair<Int, Int>, tail: Pair<Int, Int>) : Boolean {
        return abs(head.first - tail.first) <= 1 &&
            abs(head.second - tail.second) <= 1
    }

    fun moveTail(tail: Pair<Int, Int>, head: Pair<Int, Int>) : Pair<Int, Int> {
        if (isTouching(tail, head)) return tail
        var newX: Int = tail.first
        var newY: Int = tail.second

        // UGLY BUT WORKS (:

        // X
        if (abs(head.first - tail.first) > 1) {
            newX = tail.first + if (tail.first > head.first) -1 else 1

            if (abs(head.second - tail.second) > 1) {
                newY = tail.second + if (tail.second > head.second) -1 else 1
            } else {
                newY = head.second
            }
            return Pair(newX, newY)
        }

        // Y
        if (abs(head.second - tail.second) > 1) {
            newY = tail.second + if (tail.second > head.second) -1 else 1

            if (abs(head.first - tail.first) > 1) {
                newX = tail.first + if (tail.first > head.first) -1 else 1
            } else {
                newX = head.first
            }
            return Pair(newX, newY)
        }
        return Pair(newX, newY)
    }

    fun printField(
        positions: List<Pair<Int, Int>>,
        xMin: Int? = null,
        xMax: Int? = null,
        yMin: Int? = null,
        yMax: Int? = null,
    ) {
        val xMin = xMin ?: positions.minOf { it.first }
        val xMax = xMax ?: positions.maxOf { it.first }
        val yMin = yMin ?: positions.minOf { it.second }
        val yMax = yMax ?: positions.maxOf { it.second }
        for (y in yMin .. yMax) {
            for (x in xMin .. xMax) {
                if (positions.first() == Pair(x,y)) {
                    print("H")
                } else if (positions.contains(Pair(x,y))) {
                    print(positions.indexOfFirst { it == Pair(x,y) })
                } else {
                    print(".")
                }
            }
            print("\n")
        }
        println("")
    }

    fun part2(input: List<String>, tails: Int): Int {
        val instructions = parseInstructions(input)
        val positions = mutableListOf<Pair<Int, Int>>()
        repeat(tails+1) { positions.add(Pair(0,0)) } // first is head, rest is tail
        val visitedFields = mutableSetOf(positions.last())
        instructions.forEach { instruction ->
            repeat(instruction.second) {
                positions[0] = moveHead(positions.first(), instruction.first)
                for (i in 1 until positions.size) {
                    val newTail = moveTail(positions[i], positions[i-1])
                    positions[i] = newTail
                }
                visitedFields.add(positions.last())
            }
        }
//        printField(positions)
//        printField(visitedFields.toList())
        return visitedFields.count()
    }

    fun part1(input: List<String>): Int {
        return part2(input, 1)
    }

    val testInput = readInput("Day09/TestInput")
    val testInput2 = readInput("Day09/TestInput2")
    val input = readInput("Day09/Input")

    println("\n=== Part 1 - Test Input ===")
    println(part1(testInput))
    println("\n=== Part 1 - Final Input ===")
    println(part1(input))

    println("\n=== Part 2 - Test Input ===")
    println(part2(testInput, 9))
    println("\n=== Part 2 - Test Input 2 ===")
    println(part2(testInput2, 9))
    println("\n=== Part 2 - Final Input ===")
    println(part2(input, 9))
}

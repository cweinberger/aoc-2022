package Day13

import readInput
import toInt
import java.util.Scanner

object Solution {

    fun getNextBrackets(line: String) : IntRange {
        var start = -1
        var end = -1
        for (idx in line.indices) {
            if (line[idx] == '[') {
                start = idx
                break
            }
        }

        var nestedCount = 0
        for (idx in line.indices) {
            when (line[idx]) {
                '[' -> nestedCount++
                ']' -> {
                    nestedCount--
                    if (nestedCount == 0) {
                        end = idx
                        break
                    }
                }
            }
        }
        return IntRange(start, end)
    }

    fun parseStringToLists(line: String) : List<Any> {
        println("Parse: $line")
        val range = getNextBrackets(line)
        val start = range.first
        val end = range.last
        println("-- start: $start, end: $end")
        return if (start == -1) {
            line
                .split(',')
                .mapNotNull { it.ifEmpty { null } }
                .map { it.toInt() }
                .toList()
        } else {
            val contentStart = start + 1
            val contentEnd = end - 1
            val result = parseStringToLists(line.substring(contentStart .. contentEnd))
            val remainder = line.substring(end + 1 until line.length)
            if (remainder.isNotEmpty()) {
                return listOf(result, parseStringToLists(remainder))
            } else {
                return result
            }
        }
    }

    fun comparePair(pair: Pair<String, String>) : Int {

        return -1
    }

    fun parseInput(input: List<String>) : List<Pair<Any, Any>> {
        return input
            .chunked(3) { Pair(it[0], it[1]) }
            .map { pair ->
                val p = Pair(
                    parseStringToLists(pair.first),
                    parseStringToLists(pair.second),
                )
                println("Pair: $p")
                p
            }
    }

    fun compare(pair: Pair<Any, Any>): Int {
        return 1
    }

    fun part1(input: List<String>) : Int {
        val parsedInput = parseInput(input)
        return parsedInput.mapIndexedNotNull { index, pair ->
            if (compare(pair) == 1) index + 1 else null
        }.sum()
    }

    fun part2(input: List<String>) : Int {
        return 0
    }
}

fun main() {
    val testInput = readInput("Day13/TestInput")
    val input = readInput("Day13/Input")

    println("\n=== Part 1 - Test Input ===")
    println(Solution.part1(testInput))
//    println("\n=== Part 1 - Final Input ===")
//    println(Solution.part1(input))
//
//    println("\n=== Part 2 - Test Input ===")
//    println(Solution.part2(testInput))
//    println("\n=== Part 2 - Final Input ===")
//    println(Solution.part2(input))
}

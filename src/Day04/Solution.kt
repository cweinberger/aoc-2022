package Day04

import readInput

fun main() {

    fun parseLine(input: String) : Pair<IntRange, IntRange> {
        return input
            .split(",")
            .map { assignmentString ->
                assignmentString.split("-")
                    .map { it.toInt() }
                    .let { assigmentList ->
                        IntRange(assigmentList.first(), assigmentList.last())
                    }
            }
            .let {
                Pair(it.first(), it.last())
            }
    }

    fun IntRange.contains(other: IntRange) : Boolean {
        return (this.contains(other.first) && this.contains(other.last))
    }

    fun IntRange.overlaps(other: IntRange) : Boolean {
        return (this.contains(other.first) || this.contains(other.last))
    }

    fun part1(input: List<String>): Int {
        println("Evaluating ${input.count()} assignment pairs")
        return input.map { assigmentString ->
            parseLine(assigmentString)
                .let {
                    if (it.first.contains(it.second) || it.second.contains(it.first)) { 1 } else { 0 }
                }
        }.sum()
    }

    fun part2(input: List<String>): Int {
        println("Evaluating ${input.count()} assignment pairs")
        return input.map { assigmentString ->
            parseLine(assigmentString)
                .let {
                    if (it.first.overlaps(it.second) || it.second.overlaps(it.first)) { 1 } else { 0 }
                }
        }.sum()
    }

    val testInput = readInput("Day04/TestInput")
    val input = readInput("Day04/Input")

    println("=== Part 1 - Test Input ===")
    println(part1(testInput))
    println("=== Part 1 - Final Input ===")
    println(part1(input))

    println("=== Part 2 - Test Input ===")
    println(part2(testInput))
    println("=== Part 2 - Final Input ===")
    println(part2(input))
}

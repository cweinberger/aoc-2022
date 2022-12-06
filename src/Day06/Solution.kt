package Day06

import readInput

fun main() {

    fun checkMarker(line: String, size: Int): Int {
        var idx: Int = 0
        while (idx < line.count()) {
            val packet = line.subSequence(idx, idx+size)
            if (packet.count() == packet.toSet().count()) { break }
            idx++
        }
        return idx+size
    }

    fun part1(input: List<String>): List<Int> {
        return input.map { line ->
            checkMarker(line, 4)
        }
    }

    fun part2(input: List<String>): List<Int> {
        return input.map { line ->
            checkMarker(line, 14)
        }
    }

    val testInput = readInput("Day06/TestInput")
    val input = readInput("Day06/Input")

    println("=== Part 1 - Test Input ===")
    println(part1(testInput))
    println("=== Part 1 - Final Input ===")
    println(part1(input))

    println("=== Part 2 - Test Input ===")
    println(part2(testInput))
    println("=== Part 2 - Final Input ===")
    println(part2(input))
}

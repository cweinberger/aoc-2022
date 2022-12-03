package Day03

import readInput

fun main() {

    fun String.getCompartments() : Pair<String, String> {
        return Pair(
            this.slice(0 until this.length/2),
            this.slice(this.length/2 until this.length)
        )
    }

    fun Pair<String, String>.getDuplicateCharacter() : Char {
        return this.first
            .first { this.second.contains(it) }
    }

    /**
     * Priorities:
     * a-z = 1-26
     * A-Z = 27-52
     *
     * Char codes:
     * a-z = 97-...
     * A-Z = 65-...
     */
    fun Char.toPriority() : Int {
        return when (this.code) {
            in 'a'.code .. 'z'.code -> (this.code - 96)
            in 'A'.code .. 'Z'.code -> (this.code - 38)
            else -> throw IllegalArgumentException("Unexpected character '$this'")
        }
    }

    fun List<String>.getDuplicateCharacter() : Char {
        return this.first().first { character ->
            this
                .slice(1 until this.size)
                .map { it.contains(character) }
                .all { it }
        }
    }

    fun part1(input: List<String>): Int {
        println("Evaluating ${input.count()} rucksacks")
        return input.map { rucksack ->
            rucksack
                .getCompartments()
                .getDuplicateCharacter()
                .toPriority()
        }.sum()
    }

    fun part2(input: List<String>): Int {
        println("Evaluating ${input.count()} rucksacks")
        return input.chunked(3) { rucksacks ->
            rucksacks.getDuplicateCharacter()
                .toPriority()
        }.sum()
    }

    val testInput = readInput("Day03/TestInput")
    val input = readInput("Day03/Input")

    println("=== Part 1 - Test Input ===")
    println(part1(testInput))
    println("=== Part 1 - Final Input ===")
    println(part1(input))

    println("=== Part 2 - Test Input ===")
    println(part2(testInput))
    println("=== Part 2 - Final Input ===")
    println(part2(input))
}

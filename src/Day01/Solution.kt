package Day01

import readInput

fun main() {

    fun totalCaloriesByElve(input: List<String>): List<Int> {
        return input
            .joinToString("#")
            .split("##")
            .map { values ->
                values
                    .split("#")
                    .sumOf { it.toInt() }
            }
            .sortedDescending()
    }

    fun part1(input: List<String>): Int {
        return totalCaloriesByElve(input).first()
    }

    fun part2(input: List<String>): Int {
        return totalCaloriesByElve(input).subList(0,3).sum()
    }

    val input = readInput("Day01/Input")
    println(part1(input))
    println(part2(input))
}

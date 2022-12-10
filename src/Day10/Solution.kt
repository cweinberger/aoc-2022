package Day10

import readInput
import kotlin.math.abs

object Day10 {

    fun runInstructions(instructions: List<String>, registerValue: Int, maxCycles: Int) : List<Int> {
        val register = mutableListOf<Int>()
        var instructionIndex = 0
        while (register.size <= maxCycles) {
            val lastValue = register.lastOrNull() ?: registerValue
            when (val line = instructions[instructionIndex % instructions.size]) {
                "noop" -> register.add(lastValue)
                else -> {
                    val instruction = line.split(" ")
                    register.add(lastValue)
                    register.add(lastValue + instruction.last().toInt())
                }
            }
            instructionIndex++
        }
        return register.subList(0, maxCycles)
    }

    fun getSignalStrength(register: List<Int>, cycle: Int): Int {
        val value = register[cycle-2]
        val result = value * cycle
        println("getSignalStrength: $cycle, $value => $result")
        return result
    }

    fun part1(input: List<String>): Int {
        val cycles = listOf(20, 60, 100, 140, 180, 220)
        val register = runInstructions(input, 1, cycles.last())
        return cycles.sumOf {
            getSignalStrength(register, it)
        }
    }

    fun part2(input: List<String>) {
        val cycles = listOf(40, 80, 120, 160, 200, 240)
        val register = runInstructions(input, 1, cycles.last())

        val rows = mutableListOf<String>()
        cycles.forEach { cycle ->
            val row = MutableList(40) { '.'}
            for(idx in cycle-39 .. cycle) {
                val registerValue = register[idx-1]
                val crtIndex = idx%40
                if (abs(crtIndex-registerValue) < 2) {
                    row[crtIndex] = '#'
                } else {
                    row[crtIndex] = '.'
                }
            }
            rows.add(String(row.toCharArray()))
        }
        rows.forEach { println(it) }
    }
}

fun main() {

    val testInput = readInput("Day10/TestInput")
    val input = readInput("Day10/Input")

    println("\n=== Part 1 - Test Input 2 ===")
    println(Day10.part1(testInput))
    println("\n=== Part 1 - Final Input ===")
    println(Day10.part1(input))

    println("\n=== Part 2 - Test Input ===")
    println(Day10.part2(testInput))
    println("\n=== Part 2 - Final Input ===")
    println(Day10.part2(input))
}

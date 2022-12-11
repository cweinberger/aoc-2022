package Day11

import readInput

object Solution {

    data class Monkey(
        var startingItems: MutableList<ULong>,
        val operation: Char,
        val operationValue: String,
        val testDivisibleBy: ULong,
        val monkeyTrue: Int,
        val monkeyFalse: Int,
        var activities: ULong = 0UL,
    ) {
        override fun toString(): String {
            return """
                  Starting items: ${startingItems.joinToString(", ")}
                  Operation: new = $operation
                  Test: divisible by $testDivisibleBy
                    If true: throw to monkey $monkeyTrue
                    If false: throw to monkey $monkeyFalse
            """.trimIndent()
        }
    }

    fun parseInput(input: List<String>): List<Monkey> {
        val monkeys = mutableListOf<Monkey>()
        input.chunked(7).forEach { monkeyInput ->
            val startingItems = monkeyInput[1].split(": ").last().split(", ").map { it.toULong() }
            val operation = monkeyInput[2].split(" = ").last().split(" ")[1].first()
            val operationValue = monkeyInput[2].split(" = ").last().split(" ")[2]
            val testDivisibleBy = monkeyInput[3].split(" by ").last().toULong()
            val monkeyTrue = monkeyInput[4].split(" monkey ").last().toInt()
            val monkeyFalse = monkeyInput[5].split(" monkey ").last().toInt()
            monkeys.add(
                Monkey(
                    startingItems = startingItems.toMutableList(),
                    operation = operation,
                    operationValue = operationValue,
                    testDivisibleBy = testDivisibleBy,
                    monkeyTrue = monkeyTrue,
                    monkeyFalse = monkeyFalse
                )
            )
        }
        return monkeys
    }

    fun applyOperation(item: ULong, operation: Char, operationValue: String, divideWorryLevelBy: Int?) : ULong {
        var newValue = item
        val opValue = if (operationValue == "old") item else operationValue.toULong()
        when (operation) {
            '*' -> newValue = item * opValue
            '+' -> newValue = item + opValue
        }
        return (if(divideWorryLevelBy == null) newValue else (newValue/divideWorryLevelBy.toULong()))
    }

    fun runRound(monkeys: List<Monkey>, divideWorryLevelBy: Int? = null, superModulo: ULong? = null): List<Monkey> {
        monkeys.forEach { monkey ->
            monkey.startingItems.forEach { item ->
                var worryLevel = applyOperation(item, monkey.operation, monkey.operationValue, divideWorryLevelBy)
                superModulo?.let { worryLevel %= it }
                if(worryLevel % monkey.testDivisibleBy == 0UL) {
                    monkeys[monkey.monkeyTrue].startingItems.add(worryLevel)
                } else {
                    monkeys[monkey.monkeyFalse].startingItems.add(worryLevel)
                }
            }
            monkey.activities += monkey.startingItems.count().toULong()
            monkey.startingItems = mutableListOf()
        }
        return monkeys
    }

    fun part1(input: List<String>) : ULong {
        val monkeys = parseInput(input)
        repeat(20) {
            runRound(monkeys, 3)
        }
        monkeys.forEach { println(""); println(it) }
        return monkeys
            .sortedByDescending { it.activities }
            .take(2)
            .let { it.first().activities * it.last().activities }
    }

    fun part2(input: List<String>) : ULong {
        val monkeys = parseInput(input)
        repeat(10000) {
            // congruence https://en.wikipedia.org/wiki/Modular_arithmetic
            val superModulo = monkeys
                .map { it.testDivisibleBy }
                .reduce{ acc, elem -> acc * elem }

            runRound(monkeys, null, superModulo)
        }
        monkeys.forEach { println(it.activities) }
        return monkeys
            .sortedByDescending { it.activities }
            .take(2)
            .let { it.first().activities * it.last().activities }
    }
}

fun main() {

    val testInput = readInput("Day11/TestInput")
    val input = readInput("Day11/Input")

    println("\n=== Part 1 - Test Input ===")
    println(Solution.part1(testInput))
    println("\n=== Part 1 - Final Input ===")
    println(Solution.part1(input))

    println("\n=== Part 2 - Test Input ===")
    println(Solution.part2(testInput))
    println("\n=== Part 2 - Final Input ===")
    println(Solution.part2(input))
}

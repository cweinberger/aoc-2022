package Day05

import readInput

fun main() {

    fun parseSlot(slot: String) : Char? {
        return slot
            .replace("[", "")
            .replace("]", "")
            .trim()
            .firstOrNull()
    }

    fun parseContainerLine(line: String) : MutableList<Char?> {
        return line.chunked(4) { slot ->
            parseSlot(slot.toString())
        }.toMutableList()
    }

    fun <T>List<List<T>>.transpose(): MutableList<MutableList<T>> {
        val transpose = mutableListOf<MutableList<T>>()
        repeat(this.first().size) { transpose.add(mutableListOf()) }
        this.forEach { row ->
            for (i in row.indices) {
                val box = row[i]
                if (box != null) { transpose[i].add(row[i]) }
            }
        }
        return transpose
    }

    fun parseContainers(input: List<String>) : List<MutableList<Char?>> {
        val index = input.indexOfFirst { it.startsWith(" 1") }
        return input.subList(0, index)
            .reversed()
            .map { parseContainerLine(it) }
            .let { it.transpose() }
    }

    fun parseInstruction(instruction: String) : List<Int> {
        return Regex("[0-9]+")
            .findAll(instruction)
            .map { it.value.toInt() }
            .toList()
    }

    fun parseInstructions(input: List<String>) : List<List<Int>> {
        val index = input.indexOfFirst { it.startsWith("move") }
        return input
            .subList(index, input.size)
            .map { parseInstruction(it) }
    }

    fun processInstructions(
        instructions: List<List<Int>>,
        containers: List<MutableList<Char?>>,
        allAtOne: Boolean,
    ) : List<MutableList<Char?>> {
        instructions.forEach { instruction ->
            val amount = instruction[0]
            var from = containers[instruction[1]-1]
            val to = containers[instruction[2]-1]
            val boxesToMove = if (allAtOne) from.takeLast(amount) else from.takeLast(amount).reversed()
            to.addAll(boxesToMove)
            repeat(amount) { from.removeLast() }
        }
        return containers
    }

    fun part1(input: List<String>): String {
        val containers = parseContainers(input)
        val instructions = parseInstructions(input)
        println("Evaluating ${containers.count()} containers and ${instructions.count()} instructions.")
        return processInstructions(instructions, containers, false)
            .mapNotNull { it.lastOrNull() }
            .let { String(it.toCharArray()) }
    }

    fun part2(input: List<String>): String {
        val containers = parseContainers(input)
        val instructions = parseInstructions(input)
        println("Evaluating ${containers.count()} containers and ${instructions.count()} instructions.")
        return processInstructions(instructions, containers, true)
            .mapNotNull { it.lastOrNull() }
            .let { String(it.toCharArray()) }
    }

    val testInput = readInput("Day05/TestInput")
    val input = readInput("Day05/Input")

    println("=== Part 1 - Test Input ===")
    println(part1(testInput))
    println("=== Part 1 - Final Input ===")
    println(part1(input))

    println("=== Part 2 - Test Input ===")
    println(part2(testInput))
    println("=== Part 2 - Final Input ===")
    println(part2(input))
}

package Day14

import readInput
import toInt
import java.util.Scanner
import kotlin.math.abs
import kotlin.math.sign

object Solution {

    fun parseLine(line: String) : List<Pair<Int, Int>> {
        return line
            .split(" -> ")
            .map {
                it.split(',')
                    .map { it.toInt() }
            }
            .map {
                Pair(it.first(), it.last())
            }
    }

    fun getRange(instructions: List<List<Pair<Int, Int>>>): Pair<Pair<Int, Int>, Pair<Int, Int>> {
        val minX = instructions.flatten().minOf { it.first }
        val maxX = instructions.flatten().maxOf { it.first }
        val minY = instructions.flatten().minOf { it.second }
        val maxY = instructions.flatten().maxOf { it.second }
        return Pair(Pair(minX, maxX), Pair(minY, maxY))
    }

    fun normalizeInstructions(instructions: List<List<Pair<Int, Int>>>): List<List<Pair<Int, Int>>> {
        val xRange = getRange(instructions).first
        val xDiff = -xRange.first
        return instructions.map { path ->
            path.map { point ->
                Pair(
                    point.first + xDiff,
                    point.second
                )
            }
        }
    }

    fun createMap(instructions: List<List<Pair<Int, Int>>>, char: Char): Array<Array<Char>> {
        val dimensions = getRange(instructions)
        return Array(dimensions.second.second + 1) {
            Array(dimensions.first.second + 1) { char }
        }
    }

    fun printMap(map: Array<Array<Char>>) {
        println("+++ Printing Map of size ${map.first().size} x ${map.size} +++")
        map.forEach { row ->
            println(String(row.toCharArray()))
        }
    }

    fun drawPoint(x: Int, y: Int, char: Char, on: Array<Array<Char>>) {
        on[y][x] = char
    }

    fun drawLine(
        from: Pair<Int, Int>,
        to: Pair<Int, Int>,
        char: Char,
        on: Array<Array<Char>>
    ) {
        val diffX = from.first - to.first
        val diffY = from.second - to.second

        if (diffX != 0) {
            for (x in 0 .. abs(diffX)) {
                val signedX = from.first - x * diffX/abs(diffX)
                drawPoint(x = signedX, y = from.second, char = char, on = on)
            }
        } else if (diffY != 0) {
            for (y in 0 .. abs(diffY)) {
                val signedY = from.second - y * diffY/abs(diffY)
                drawPoint(x = from.first, y = signedY, char = char, on = on)
            }
        }
    }

    fun drawPath(
        path: List<Pair<Int, Int>>,
        char: Char,
        on: Array<Array<Char>>
    ) {
        var from = path.first()
        path.takeLast(path.size-1).forEach { to ->
            drawLine(from, to, char, on)
            from = to
        }
    }

    fun drawPaths(
        paths: List<List<Pair<Int, Int>>>,
        char: Char,
        on: Array<Array<Char>>
    ) {
        paths.forEach { path ->
            drawPath(path, char, on)
        }
    }

    fun emitSand(source: Pair<Int, Int>, on: Array<Array<Char>>) : Pair<Int, Int> {
        var sandPosition = source
        for(y in source.second until (on.size - 1)) {
            val belowLeft = on[y+1].getOrNull(sandPosition.first-1)
            val belowCenter = on[y+1].getOrNull(sandPosition.first)
            val belowRight = on[y+1].getOrNull(sandPosition.first+1)
            if (belowCenter == '.' || belowCenter == null) {
                sandPosition = Pair(sandPosition.first, y+1)
            } else if (belowLeft == '.' || belowLeft == null) {
                sandPosition = Pair(sandPosition.first-1, y+1)
            } else if (belowRight == '.' || belowRight == null) {
                sandPosition = Pair(sandPosition.first+1, y+1)
            } else {
                break
            }
        }
        return sandPosition
    }

    fun part1(input: List<String>) : Int {
        // parse instructions
        val instructions = input.map { parseLine(it) }
        // normalize
        val normalizedInstructions = normalizeInstructions(instructions)
        // create map
        val map = createMap(normalizedInstructions, '.')
        drawPaths(normalizedInstructions, '#', map)
        // draw sand source
        val normalizedXDiff = normalizedInstructions.first().first().first - instructions.first().first().first
        val sandSource = Pair(500+normalizedXDiff, 0)
        drawPoint(sandSource.first, sandSource.second, '+', map)
        printMap(map)
        // emit sand
        var counter = 0
        while(true) {
            val emittedSandPosition = emitSand(sandSource, map)
            if(emittedSandPosition.first < 0 || emittedSandPosition.first > map.first().size) {
                break
            }
            counter++
            drawPoint(emittedSandPosition.first, emittedSandPosition.second, 'o', map)
        }
        printMap(map)
        return counter
    }

    fun part2(input: List<String>) : Int {
        // parse instructions
        val instructions = input.map { parseLine(it) }

        // add bottom line
        val range = getRange(instructions)
        val yMax = range.second.second
        val newInstruction = listOf(
            Pair(range.first.first - (yMax), yMax + 2),
            Pair(range.first.second + (yMax), yMax + 2)
        )
        val updatedInstructions = instructions.toMutableList()
        updatedInstructions.add(newInstruction)

        // normalize
        val normalizedInstructions = normalizeInstructions(updatedInstructions)

        // create map
        val map = createMap(normalizedInstructions, '.')
        drawPaths(normalizedInstructions, '#', map)

        // draw sand source
        val normalizedXDiff = normalizedInstructions.first().first().first - instructions.first().first().first
        val sandSource = Pair(500+normalizedXDiff, 0)
        drawPoint(sandSource.first, sandSource.second, '+', map)
        printMap(map)

        // emit sand
        var counter = 0
        while(true) {
            val emittedSandPosition = emitSand(sandSource, map)
            counter++
            drawPoint(emittedSandPosition.first, emittedSandPosition.second, 'o', map)
            if(emittedSandPosition.first == sandSource.first && emittedSandPosition.second == sandSource.second) {
                break
            }
        }
        printMap(map)
        return counter
    }
}

fun main() {
    val testInput = readInput("Day14/TestInput")
    val input = readInput("Day14/Input")

    println("\n=== Part 1 - Test Input ===")
    println(Solution.part1(testInput))
    println("\n=== Part 1 - Final Input ===")
    println(Solution.part1(input))

    println("\n=== Part 2 - Test Input ===")
    println(Solution.part2(testInput))
    println("\n=== Part 2 - Final Input ===")
    println(Solution.part2(input))
}

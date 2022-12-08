package Day08

import readInput

fun main() {

    fun parseInput(input: List<String>) : List<List<Int>> {
        return input.map { line ->
            line.map { it.digitToInt() }
        }
    }

    fun isVisible1D(input: List<Int>, targetIndex: Int) : Boolean {
        if(targetIndex == 0 || targetIndex == input.size-1) return true
        val value = input[targetIndex]
        val listBefore = input.subList(0, targetIndex)
        val listAfter = input.subList(targetIndex+1, input.size)
        return listBefore.all { it < value } || listAfter.all { it < value }
    }

    fun isVisible2D(input: List<List<Int>>, targetRow: Int, targetCol: Int) : Boolean {
        val row = input[targetRow]
        val col = input.map { it[targetCol] }
        return isVisible1D(row, targetCol) || isVisible1D(col, targetRow)
    }

    fun Boolean.toInt() = if (this) 1 else 0

    fun getViewingDistance1D(input: List<Int>, targetIndex: Int) : Int {
        val value = input[targetIndex]
        val listBefore = input.subList(0, targetIndex)
        val listAfter = input.subList(targetIndex+1, input.size)
        val listBeforeClearView = listBefore.reversed().toMutableList().dropWhile { it < value }
        val listAfterClearView = listAfter.toMutableList().dropWhile { it < value }

        val distanceBefore = listBefore.count() - listBeforeClearView.count() + listBeforeClearView.isNotEmpty().toInt()
        val distanceAfter = listAfter.count() - listAfterClearView.count() + listAfterClearView.isNotEmpty().toInt()
        return distanceBefore * distanceAfter
    }

    fun getViewingDistance2D(input: List<List<Int>>, targetRow: Int, targetCol: Int) : Int {
        val row = input[targetRow]
        val col = input.map { it[targetCol] }
        return getViewingDistance1D(row, targetCol) * getViewingDistance1D(col, targetRow)
    }

    fun part1(input: List<String>): Int {
        val parsedInput = parseInput(input)
        return parsedInput
            .mapIndexed { rowIndex, row ->
                row.mapIndexed { colIndex, col ->
                    isVisible2D(parsedInput, rowIndex, colIndex)
                }
            }
            .flatten()
            .count { it }
    }

    fun part2(input: List<String>): Int {
        val parsedInput = parseInput(input)
        return parsedInput
            .mapIndexed { rowIndex, row ->
                row.mapIndexed { colIndex, col ->
                    getViewingDistance2D(parsedInput, rowIndex, colIndex)
                }
            }
            .flatten().maxOf { it }
    }

    val testInput = readInput("Day08/TestInput")
    val input = readInput("Day08/Input")

    println("\n=== Part 1 - Test Input ===")
    println(part1(testInput))
    println("\n=== Part 1 - Final Input ===")
    println(part1(input))

    println("\n=== Part 2 - Test Input ===")
    println(part2(testInput))
    println("\n=== Part 2 - Final Input ===")
    println(part2(input))

//    println("1,2: ${getViewingDistance2D(parseInput(testInput), 1, 2)}")
//    println("3,2: ${getViewingDistance2D(parseInput(testInput), 3, 2)}")
}

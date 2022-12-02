package Day02

import readInput

/**
 * Col 1
 *  A = Rock
 *  B = Paper
 *  C = Scissors
 *
 * Col 2
 *  X = Rock | Lose
 *  Y = Paper | Draw
 *  Z = Scissors | Win
 *
 *  Lose = 0 pts
 *  Draw = 3 pts
 *  Win = 6 pts
 *  Rock = 1 pts
 *  Paper = 2 pts
 *  Scissors = 3 pts
 */

enum class Hand(val value: Int) {
    ROCK(1),
    PAPER(2),
    SCISSORS(3);

    companion object {
        @Throws(IllegalArgumentException::class)
        fun withInput(input: String) : Hand {
            return when (input) {
                "A", "X" -> ROCK
                "B", "Y" -> PAPER
                "C", "Z" -> SCISSORS
                else -> throw IllegalArgumentException("Unexpected input: '$input'")
            }
        }
    }

    fun resultAgainst(other: Hand) : Result {
        return when (other) {
            this.getLosingHand() -> Result.WIN
            this.getWinningHand() -> Result.LOSE
            else -> Result.DRAW
        }
    }

    fun getLosingHand() : Hand {
        return when (this) {
            ROCK -> SCISSORS
            PAPER -> ROCK
            SCISSORS -> PAPER
        }
    }

    fun getWinningHand() : Hand {
        return when (this) {
            ROCK -> PAPER
            PAPER -> SCISSORS
            SCISSORS -> ROCK
        }
    }
}

enum class Result(val value: Int) {
    WIN(6),
    DRAW(3),
    LOSE(0);

    companion object {
        @Throws(IllegalArgumentException::class)
        fun withInput(input: String) : Result {
            return when (input) {
                "X" -> LOSE
                "Y" -> DRAW
                "Z" -> WIN
                else -> throw IllegalArgumentException("Unexpected input: '$input'")
            }
        }
    }
}

fun main() {

    fun parseMatchLineAsHands(input: String) : Pair<Hand, Hand> {
        return input
            .split(" ")
            .map { Hand.withInput(it) }
            .let { Pair(it.first(), it.last()) }
    }

    fun parseMatchLineAsHandAndResult(input: String) : Pair<Hand, Result> {
        return input
            .split(" ")
            .let { Pair(
                Hand.withInput(it.first()),
                Result.withInput(it.last())
            ) }
    }

    fun evaluateMatch(yourHand: Hand, otherHand: Hand): Int {
        return yourHand.value + yourHand.resultAgainst(otherHand).value
    }

    fun evaluateMatches(input: List<String>): List<Int> {
        println("Evaluating ${input.count()} matches")
        return input
            .map { matchStr ->
                parseMatchLineAsHands(matchStr)
                    .let { hands ->
                        evaluateMatch(hands.second, hands.first)
                    }
            }
    }

    fun part1(input: List<String>): Int {
        return evaluateMatches(input).sum()
    }

    fun part2(input: List<String>): Int {
        println("Evaluating ${input.count()} matches")
        return input
            .map { matchStr ->
                parseMatchLineAsHandAndResult(matchStr)
                    .let {
                        when (it.second) {
                            Result.WIN -> Pair(it.first, it.first.getWinningHand())
                            Result.DRAW -> Pair(it.first, it.first)
                            Result.LOSE -> Pair(it.first, it.first.getLosingHand())
                        }
                    }
                    .let { hands ->
                        evaluateMatch(hands.second, hands.first)
                    }
            }.sum()
    }

    val testInput = readInput("Day02/TestInput")
    val input = readInput("Day02/Input")

    println("=== Part 1 - Test Input ===")
    println(part1(testInput))
    println("=== Part 1 - Final Input ===")
    println(part1(input))

    println("=== Part 2 - Test Input ===")
    println(part2(testInput))
    println("=== Part 2 - Final Input ===")
    println(part2(input))
}

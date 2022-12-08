package Day07

import readInput
import kotlin.math.max

enum class ItemType {
    File, Directory
}

sealed class Item(
    open val name: String,
    open val parent: Item.Directory?,
    val type: ItemType
) {
    data class File(
        override val name: String,
        override val parent: Directory?,
        val size: Int
    ) : Item(name, parent, ItemType.File)

    data class Directory(
        override val name: String,
        override val parent: Directory?,
        val items: MutableList<Item>
    ) : Item(name, parent, ItemType.Directory)
}

enum class Command {
    ChangeDirectory,
    ListDirectory;

    companion object {
        fun withString(input: String): Command {
            return when (input) {
                "cd" -> ChangeDirectory
                "ls" -> ListDirectory
                else -> throw IllegalArgumentException("Unknown input for command '$input'")
            }
        }
    }
}

fun main() {

    fun parseCommand(input: String) : Pair<Command, String?> {
        return input
            .replace("$ ", "")
            .split(" ")
            .let {
                Pair(
                    Command.withString(it.first()),
                    if (it.size > 1) it.last() else null
                )
            }
    }

    fun parseDirectory(input: String, parent: Item.Directory) : Item.Directory {
        return Item.Directory(
            input.split(" ").last(),
            parent,
            mutableListOf()
        )
    }

    fun parseFile(input: String, parent: Item.Directory) : Item.File {
        return Item.File(
            input.split(" ").last(),
            parent,
            input.split(" ").first().toInt()
        )
    }

    fun createFileSystem(input: List<String>): Item.Directory {
        var root = Item.Directory("/", null, mutableListOf())
        var currentDir = root
        input.forEach { line ->
            if (line.startsWith("$")) {
                val cmd = parseCommand(line)
                when (cmd.first) {
                    Command.ListDirectory -> return@forEach
                    Command.ChangeDirectory -> {
                        currentDir = when (cmd.second) {
                            "/" -> root
                            ".." -> currentDir.parent ?: root
                            else -> currentDir.items
                                .first {
                                    it.type == ItemType.Directory &&
                                        it.name == cmd.second
                                } as Item.Directory
                        }
                    }
                }
            } else if (line.startsWith("dir")) {
                val dir = parseDirectory(line, currentDir)
                currentDir.items.add(dir)
            } else {
                val file = parseFile(line, currentDir)
                currentDir.items.add(file)
            }
        }
        return root
    }

    fun printDirectory(dir: Item.Directory, prefix: String = "") {
        dir.items.forEach { item ->
            when(item) {
                is Item.Directory -> {
                    println("$prefix - ${item.name}")
                    printDirectory(item, "$prefix  ")
                }
                is Item.File -> println("$prefix - ${item.name} (${item.size})")
            }
        }
    }

    fun getDirectorySize(dir: Item.Directory) : Int {
        return dir.items
            .filterIsInstance(Item.File::class.java)
            .sumOf { it.size }
            .plus(
                dir.items
                    .filterIsInstance(Item.Directory::class.java)
                    .sumOf { getDirectorySize(it) }
            )
    }

    fun findDirectories(
        root: Item.Directory,
        filter: (Item.Directory) -> Boolean
    ) : List<Item.Directory> {
        return root.items
            .filterIsInstance(Item.Directory::class.java)
            .mapNotNull {
                if (filter(it)) {
                    listOf(it) + findDirectories(it, filter)
                } else {
                    findDirectories(it, filter)
                }
            }
            .flatten()
    }

    fun part1(input: List<String>): Int {
        val fs = createFileSystem(input)
        return findDirectories(fs) { getDirectorySize(it) <= 100000 }
            .sumOf { getDirectorySize(it) }
    }

    fun part2(input: List<String>): Int {
        val fs = createFileSystem(input)
        val totalSize = getDirectorySize(fs)
        val requiredSpace = 30000000
        val diskSpace = 70000000
        val spaceToFreeUp = requiredSpace - (diskSpace - totalSize)
        println("Disk usage: $totalSize of $diskSpace\n" +
            "Space to free up: $spaceToFreeUp")
        return findDirectories(fs) { getDirectorySize(it) >= spaceToFreeUp }
            .map { getDirectorySize(it) }
            .minOf { it }
    }

    val testInput = readInput("Day07/TestInput")
    val input = readInput("Day07/Input")

    println("\n=== Part 1 - Test Input ===")
    println(part1(testInput))
    println("\n=== Part 1 - Final Input ===")
    println(part1(input))

    println("\n=== Part 2 - Test Input ===")
    println(part2(testInput))
    println("\n=== Part 2 - Final Input ===")
    println(part2(input))
}

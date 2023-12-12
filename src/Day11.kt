import kotlin.math.absoluteValue

data class Day11Point(val y: Int, val x: Int)

fun isBetween(inputVal: Int, val1: Int, val2: Int): Boolean {
    val start = minOf(val1, val2)
    val end = maxOf(val1, val2)

    return inputVal in start..end
}

fun solvePart1Day11(galaxies: List<Day11Point>, input: List<String>): Int {
    var total = 0
    val blankColList = getBlankColList(input)
    val blankRowList = getBlankRowList(input)

    for (i in 0..<galaxies.size) {
        val galaxy1 = galaxies[i]
        for (j in i+1..<galaxies.size) {
            val galaxy2 = galaxies[j]

            val yDiff = galaxy2.y - galaxy1.y
            val xDiff = galaxy2.x - galaxy1.x

            val rowAdds = blankRowList.filter { isBetween(it,galaxy1.y, galaxy2.y) }.size
            val colAdds = blankColList.filter { isBetween(it, galaxy1.x, galaxy2.x) }.size

            val subTotal = yDiff.absoluteValue + xDiff.absoluteValue + (rowAdds * 1) + (colAdds * 1)
            total += subTotal
        }
    }

    return total
}

fun solvePart2Day11(galaxies: List<Day11Point>, input: List<String>): Long {
    var total: Long = 0
    val blankColList = getBlankColList(input)
    val blankRowList = getBlankRowList(input)

    for (i in 0..<galaxies.size) {
        val galaxy1 = galaxies[i]
        for (j in i+1..<galaxies.size) {
            val galaxy2 = galaxies[j]

            val yDiff = galaxy2.y - galaxy1.y
            val xDiff = galaxy2.x - galaxy1.x

            val rowAdds = blankRowList.filter { isBetween(it,galaxy1.y, galaxy2.y) }.size
            val colAdds = blankColList.filter { isBetween(it, galaxy1.x, galaxy2.x) }.size

            val subTotal: Long = yDiff.absoluteValue.toLong() + xDiff.absoluteValue.toLong() +
                    (rowAdds.toLong() * 999999L) + (colAdds.toLong() * 999999L)
            total += subTotal
        }
    }

    return total
}

fun getBlankColList(input: List<String>): List<Int> {
    val blankColList = mutableListOf<Int>()
    for (i in 0..<input[0].length) {
        if (input.all{ it[i] == '.'}) {
            blankColList.add(i)
        }
    }
    return blankColList
}

fun getBlankRowList(input: List<String>): List<Int> {
    val blankRowList = mutableListOf<Int>()

    for (i in 0..<input.size) {
        if (input[i].length == input[i].filter { it == '.' }.length) {
            blankRowList.add(i)
        }
    }
    return blankRowList
}

fun findGalaxies(universe: List<String>): List<Day11Point> {
    var galaxies = mutableListOf<Day11Point>()

    for (y in 0..<universe.size) {
        for (x in 0..<universe[y].length) {
            if (universe[y][x] != '.') {
                val tmpPoint = Day11Point(y,x)
                galaxies.add(tmpPoint)
            }
        }
    }
    return galaxies
}

fun main() {
//    val input = readInput("Day11_test")
    val input = readInput("Day11_real")

    val galaxyList = findGalaxies(input)

    val firstResult = solvePart1Day11(galaxyList, input)
    val secondResult = solvePart2Day11(galaxyList, input)

    println("First Result: $firstResult")
    println("Second Result: $secondResult")
}
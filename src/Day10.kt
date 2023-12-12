import java.io.File

class startingPointException(message: String): Exception (message)

data class Day10Point(var x: Int, val y: Int, var pipe: Char, var fromDir: Char)

var part2Grid = mutableListOf<String>()

val EAST_WEST = '-'
val NORTH_SOUTH = '|'
val EAST_NORTH = 'L'
val WEST_NORTH = 'J'
val EAST_SOUTH = 'F'
val WEST_SOUTH = '7'

fun getStartingPoint(input: List<String>): Day10Point {
    var yCoord = 0
    var xCoord = 0
    var pipeChar = 'S'
    var connectors = mutableListOf<Char>()

    for (y in 0..<input.size) {
        for (x in 0..<input[y].length) {
            if (input[y][x] == 'S') {
                yCoord = y
                xCoord = x
                pipeChar = 'S'
            }
        }
    }

    var start = Day10Point(xCoord, yCoord, pipeChar, 'X')

    if (start.x != 0 && input[start.y][start.x-1] in listOf(EAST_WEST, EAST_NORTH, EAST_SOUTH)) connectors.add('W')
    if (start.x != input[start.y].length-1 && input[start.y][start.x+1] in listOf(EAST_WEST, WEST_NORTH, WEST_SOUTH)) connectors.add('E')
    if (start.y != 0 && input[start.y-1][start.x] in listOf(NORTH_SOUTH, WEST_SOUTH, EAST_SOUTH)) connectors.add('N')
    if (start.y != input.size-1 && input[start.y+1][start.x] in listOf(NORTH_SOUTH, WEST_NORTH, EAST_NORTH)) connectors.add('S')

    // Starting point should only have two valid connections that are non periods
    if (connectors.size != 2) throw startingPointException("Starting point had more than 2 connecting pipes")

    // Figure out what the S should be. This is needed for part 2.
    if (connectors.containsAll(listOf('E','W'))) {
        start.pipe = EAST_WEST
        start.fromDir = 'E'
        return start
    }
    if (connectors.containsAll(listOf('E','S'))) {
        start.pipe = EAST_SOUTH
        start.fromDir = 'E'
        return start
    }
    if (connectors.containsAll(listOf('E','N'))) {
        start.pipe = EAST_NORTH
        start.fromDir = 'E'
        return start
    }
    if (connectors.containsAll(listOf('W','S'))) {
        start.pipe = WEST_SOUTH
        start.fromDir = 'W'
        return start
    }
    if (connectors.containsAll(listOf('W','N'))) {
        start.pipe = WEST_NORTH
        start.fromDir = 'W'
        return start
    }
    if (connectors.containsAll(listOf('N','S'))) {
        start.pipe = NORTH_SOUTH
        start.fromDir = 'S'
        return start
    }

    // I don't ever expect to be here....but i don't feel like putting in an error
    return start
}

fun moveWest(point: Day10Point, input: List<String>): Day10Point = Day10Point(point.x-1, point.y, input[point.y][point.x-1], 'E')
fun moveEast(point: Day10Point, input: List<String>): Day10Point = Day10Point(point.x+1, point.y, input[point.y][point.x+1], 'W')
fun moveSouth(point: Day10Point, input: List<String>): Day10Point = Day10Point(point.x, point.y+1, input[point.y+1][point.x], 'N')
fun moveNorth(point: Day10Point, input: List<String>): Day10Point = Day10Point(point.x, point.y-1, input[point.y-1][point.x], 'S')

// Load a grid of just the pipes we traversed - everything else is "." so it's easier to figure out internal dots
fun logCurrentPointToGrid(curPoint: Day10Point) {
    var tmpString = StringBuilder(part2Grid[curPoint.y])
    tmpString.setCharAt(curPoint.x, curPoint.pipe)
    part2Grid[curPoint.y] = tmpString.toString()
}

fun solvePart1Day10(input: List<String>): Int {
    var isEnd = false
    var counter = -1
    var startPoint = getStartingPoint(input)
    var currentPoint = startPoint

    while (!isEnd) {
        counter += 1
        if (currentPoint.y == startPoint.y && currentPoint.x == startPoint.x && counter != 0) {
            break
        }
        logCurrentPointToGrid(currentPoint)

        if (currentPoint.fromDir == 'E') {
            when (currentPoint.pipe) {
                EAST_WEST -> {
                    currentPoint = moveWest(currentPoint, input)
                    continue
                }
                EAST_NORTH -> {
                    currentPoint = moveNorth(currentPoint, input)
                    continue
                }
                EAST_SOUTH -> {
                    currentPoint = moveSouth(currentPoint, input)
                    continue
                }
            }
        }
        if (currentPoint.fromDir == 'W') {
            when (currentPoint.pipe) {
                EAST_WEST -> {
                    currentPoint = moveEast(currentPoint, input)
                    continue
                }
                WEST_NORTH -> {
                    currentPoint = moveNorth(currentPoint, input)
                    continue
                }
                WEST_SOUTH -> {
                    currentPoint = moveSouth(currentPoint, input)
                    continue
                }
            }
        }
        if (currentPoint.fromDir == 'S') {
            when (currentPoint.pipe) {
                NORTH_SOUTH -> {
                    currentPoint = moveNorth(currentPoint, input)
                    continue
                }
                EAST_SOUTH -> {
                    currentPoint = moveEast(currentPoint, input)
                    continue
                }
                WEST_SOUTH -> {
                    currentPoint = moveWest(currentPoint, input)
                    continue
                }
            }
        }
        if (currentPoint.fromDir == 'N') {
            when (currentPoint.pipe) {
                NORTH_SOUTH -> {
                    currentPoint = moveSouth(currentPoint, input)
                    continue
                }
                EAST_NORTH -> {
                    currentPoint = moveEast(currentPoint, input)
                    continue
                }
                WEST_NORTH -> {
                    currentPoint = moveWest(currentPoint, input)
                    continue
                }
            }
        }
    }
    return counter / 2
}

fun solvePart2Day10(): Int {
    var total = 0
    for (y in part2Grid) {
        for (x in y.length-1 downTo 0) {
            var edgeCount = 0

            // It's a period so we need to see if it's in the polygon, else, skip it
            if (y[x] == '.') {
                // count the edges to the left - Raycasting algorithm
                for (z in 0..<x) {
                    val tmpChar = y[z]
                    if (tmpChar in listOf(NORTH_SOUTH, EAST_SOUTH, EAST_NORTH, WEST_NORTH, WEST_SOUTH)) edgeCount++
                }
            }
            if (edgeCount%2 == 1) {
                total ++
            }
        }
    }
    return total
}

fun loadPart2Grid(input: List<String>): Unit {
    for (rec in input) {
        var tmpString = ".".repeat(input[0].length)
        part2Grid.add(tmpString)
    }
}


fun main() {
//    val input = readInput("Day10_test")
//    val input = readInput("Day10_test2")
//    val input = readInput("Day10_test3")
    val input = readInput("Day10_test4")
//    val input = readInput("Day10_test5")
//    val input = readInput("Day10_real")

    loadPart2Grid(input)

    val firstResult = solvePart1Day10(input)
    val secondResult = solvePart2Day10()


    File("Day10output.txt").printWriter().use { out ->
        part2Grid.forEach {
            out.println(it)
        }
    }

    println("First Result: $firstResult")
    println("Second Result: $secondResult")
}
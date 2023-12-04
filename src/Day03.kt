data class Point(val x: Int, val y: Int)

data class partNumber(val partNumber: Int, val row: Int, val xStart: Int, val xEnd: Int)

fun checkHorizontal(input: List<String>, y: Int, Xstart: Int, Xend: Int): Boolean {
    val maxX = input[y].length - 1

    if (Xend != maxX) {
        val pointVal = input[y][Xend+1]
        val tmpString = pointVal.toString().replace(".","")
        if (Regex("\\p{Punct}").matches(tmpString)) return true
    }
    if (Xstart != 0) {
        val pointVal = input[y][Xstart-1]
        val tmpString = pointVal.toString().replace(".","")
        if (Regex("\\p{Punct}").matches(tmpString)) return true
    }
    return false
}

fun checkVertical(input: List<String>, y: Int, Xstart: Int, Xend: Int): Boolean {
    val maxY = input.size - 1
    val maxX = input[y].length - 1
    val startIndex = listOf(0,Xstart-1).max()
    val endIndex = listOf(maxX,Xend+1).min()

    if (y != maxY) {
        var tmpString = ""

        for (i in startIndex..endIndex) {
            tmpString += input[y+1][i]
        }
        // Periods are punctuation, so I need to delete them first - numbers won't trip it up
        val testString = tmpString.replace(".","")
        if (Regex("\\p{Punct}").containsMatchIn(testString)) return true
    }

    if (y != 0) {
        var tmpString = ""

        for (i in startIndex..endIndex) {
            tmpString += input[y-1][i]
        }
        // Periods are punctuation, so I need to delete them first - numbers won't trip it up
        val testString = tmpString.replace(".","")
        if (Regex("\\p{Punct}").containsMatchIn(testString)) return true
    }

    return false
}

fun isPartnumber(input: List<String>, y: Int, startIndex: Int, endIndex: Int): Boolean {
    if (checkVertical(input, y, startIndex, endIndex)) return true
    if (checkHorizontal(input, y, startIndex, endIndex)) return true

    return false
}

fun getPartValue(input: List<String>, y: Int, startIndex: Int, endIndex: Int): Int {
    // When given a row and start/end indexes, find out what number we've found by piecing the chars together
    var tmpString = ""

    for (i in startIndex..endIndex) {
        tmpString += input[y][i]
    }
    return tmpString.toInt()
}

fun solvePart1(input: List<String>, partNumbers: MutableList<partNumber>): Int {
    // This solves part 1 but also loads a list of part numbers needed for part2 to complete
    var startIndex: Int?
    var endIndex: Int?
    var total = 0

    for (y in 0..input.size-1) {
        startIndex = null
        endIndex = null
        val lineLength = input[y].length-1
//        val inputLength = input.size-1
        var partValue: Int

        for (x in 0..lineLength) {
            val pointVal = input[y][x]

            // Is it a digit, if so, we should see if it's the start or continuation of a number
            if (pointVal.isDigit()) {
                if (startIndex == null && x == lineLength) {
                    startIndex = x
                    endIndex = x

                    // If we just started a digit and it's the end of the line, it's a single digit
                    partValue = getPartValue(input, y, startIndex, endIndex)
                    if (isPartnumber(input, y, startIndex, endIndex)) {
                        total += partValue
                        val tmpPart = partNumber(partValue, y, startIndex, endIndex)
                        partNumbers.add(tmpPart)
                    }
                    continue
                } else if (startIndex == null) {
                    // found the start of a part number and we're not at the end of the string
                    startIndex = x
                    continue
                } else if (startIndex != null && x == lineLength) {
                    endIndex = x

                    // We'd already started a number, but we're at the end of the string - it stops here
                    partValue = getPartValue(input, y, startIndex, endIndex)
                    if (isPartnumber(input, y, startIndex, endIndex)) {
                        total += partValue
                        val tmpPart = partNumber(partValue, y, startIndex, endIndex)
                        partNumbers.add(tmpPart)
                    }
                    continue
                } else if (startIndex != null && x != lineLength) {
                    // found a digit, we're not at the end and we've already started a number, keep checking for end
                    continue
                }
            } else {
                // It isn't a digit but let's see if we finished a number
                if (startIndex != null) {
                    // Not a digit but start index is populated, means we just found the end of a number
                    endIndex = x - 1

                    partValue = getPartValue(input, y, startIndex, endIndex)
                    if (isPartnumber(input, y, startIndex, endIndex)) {
                        total += partValue
                        val tmpPart = partNumber(partValue, y, startIndex, endIndex)
                        partNumbers.add(tmpPart)
                    }
                    // finished a number, reset so we can find other numbers on this line
                    startIndex = null
                    endIndex = null
                    continue
                }
            }
        }
    }
    return total
}

fun solvePart2(symbolList: List<Point>, partsList: List<partNumber>): Int {
    var totalGearNumber = 0
    for (symbol in symbolList) {
        val adjacentParts = mutableListOf<Int>()

        // Loop through parts list
        for (part in partsList) {
            if (part.row in symbol.y-1..symbol.y+1 && symbol.x in part.xStart-1..part.xEnd+1) {
                //check to see if part number is adjacent to symbol
                adjacentParts.add(part.partNumber)
            }
        }
        if (adjacentParts.size == 2) {
            totalGearNumber += (adjacentParts[0] * adjacentParts[1])
        }
    }
    return totalGearNumber
}

fun loadSymbols(input: List<String>): List<Point> {
    val symbolList = mutableListOf<Point>()

    // Loop over 2D array and find coordinates for symbols
    for (y in 0..input.size-1) {
        for (x in 0..input[y].length-1) {
            val pointValue = input[y][x]
            if (pointValue == "*".single()) {
                val tmpPoint = Point(x, y)
                symbolList.add(tmpPoint)
            }
        }
    }
    return symbolList
}

fun main() {
//    val input = readInput("Day03_test")
    val input = readInput("Day03_real")

    // Find the coordinates for all the symbols and then find coordinates for all the part numbers
    val symbolList = loadSymbols(input)
    val partNumberList = mutableListOf<partNumber>()

    val firstResult = solvePart1(input, partNumberList)
    val secondResult = solvePart2(symbolList, partNumberList)

    println("First Result: $firstResult")
    println("Second Result: $secondResult")
}
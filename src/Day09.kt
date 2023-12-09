// Easiest to just build an override into this function - very simple logic change
// from part 1 to part 2
fun getSequence(input: List<Int>, isPart2: Boolean): Int {
    val tmpSequence = mutableListOf<Int>()

    if (input.size != input.filter { it == 0 }.size) {

        for (i in 0..<input.size-1) {
            tmpSequence.add(input[i+1] - input[i])
        }

        if (isPart2) {
            return input[0] - getSequence(tmpSequence, isPart2)
        } else {
            return input[input.size - 1] + getSequence(tmpSequence, isPart2)
        }
    } else {
        return 0
    }
}

fun loadSequences(input: List<String>): MutableList<List<Int>> {
    var tmpList: MutableList<List<Int>> = mutableListOf()
    for (rec in input) {
        var strList = rec.split("\\s+".toRegex())
        var intList = strList.map{it.toInt()}
        tmpList.add(intList)
    }

    return tmpList
}
fun solvePart1Day09(input: MutableList<List<Int>>): Int {
    var total = 0

    for (rec in input) {
        total += getSequence(rec, false)
    }

    return total
}

fun solvePart2Day09(input: MutableList<List<Int>>): Int {
    var total = 0

    for (rec in input) {
        total += getSequence(rec, true)
    }

    return total
}

fun main() {
//    val input = readInput("Day09_test")
    val input = readInput("Day09_real")

    val seqList = loadSequences(input)

    val firstResult = solvePart1Day09(seqList)
    val secondResult = solvePart2Day09(seqList)

    println("First Result: $firstResult")
    println("Second Result: $secondResult")
}
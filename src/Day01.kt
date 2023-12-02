var numMap: Map<String, Int> = mapOf(
    "one" to 1,
    "two" to 2,
    "three" to 3,
    "four" to 4,
    "five" to 5,
    "six" to 6,
    "seven" to 7,
    "eight" to 8,
    "nine" to 9)

fun main() {
    fun part1(input: List<String>): Int {
        var total = 0
        var numLine: String
        var lineTotal: String

        for (line in input) {
            numLine = Regex("[a-z]").replace(line,"")

            lineTotal = numLine[0].toString() + numLine[numLine.length-1].toString()
            total += lineTotal.toInt()
        }

        return total
    }

    fun stringToNum(stringNum: String): Int {
        for (key in numMap.keys) {
            if (stringNum.contains(key)) {
                return numMap.getValue(key)
            }
        }
        return 0
    }

    fun part2(input: List<String>): Int {
        var total = 0
        var firstNum: Int = 0
        var secondNum: Int = 0

        for (line in input) {
//            println(line)
            for (x in 0..<line.length) {

                if (line[x].isDigit()) {
                    firstNum = line[x].toString().toInt()
                    break
                } else {
                    val tmp = stringToNum(line.take(x+1))
                    if (numMap.values.contains(tmp)) {
                        firstNum = tmp
                        break
                    }
                }
            }
//            println("First Number is: ${firstNum}")

            for (x in line.length-1 downTo 0) {
                if (line[x].isDigit()) {
                    secondNum = line[x].toString().toInt()
                    break
                } else {
                    val tmp = stringToNum(line.takeLast(line.length-x))
                    if (numMap.values.contains(tmp)) {
                        secondNum = tmp
                        break
                    }
                }
            }
//            println("Second Number is: ${secondNum}")
            total += firstNum.toString().plus(secondNum.toString()).toInt()
        }
        return total
    }

//    val input = readInput("Day01_test")
    val input = readInput("Day01_real")
    println("Part 1 Answer: ${part1(input)}")
//    val input2 = readInput("Day01_test2")
    println("Part 2 Answer: ${part2(input)}")
}

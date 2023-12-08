data class Network(val instructions: String, val instLength: Int, val network: MutableMap<String, List<String>>)

// Blatantly stole this off the interwebs :)
fun findLCM(a: Long, b: Long): Long {
    val larger = if (a > b) a else b
    val maxLcm: Long = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == 0L && lcm % b == 0L) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}

fun getSteps(startNode: String, net: Network, isPart2: Boolean): Int {
    var counter = -1
    var instrLetter = ""
    var node = startNode

    // I hate having to put a bifurcation in logic based on the part...but this the easiest thing to do
    // so..i've got an isPart2 to help me differentiate whether i'm looking for ZZZ or XXZ
    while (true) {
        counter ++

        // mod allows us to loop back through instruction set if we get to the end
        instrLetter = net.instructions[counter%net.instLength].toString()

        if (instrLetter == "L") {
            node = net.network.getValue(node)[0]
        } else {
            node = net.network.getValue(node)[1]
        }

        if (isPart2) {
            if (node[2] == "Z".single()) {
                break
            }
        } else {
            if (node == "ZZZ") {
                break
            }
        }
    }
    // + 1 because i started at 0 to line up with array parsing
    return counter + 1
}

fun solvePart1Day08(net: Network): Int = getSteps("AAA", net, false)

fun solvePart2Day08(net: Network): Long {
    // Get the number of steps to get to a Z for each starting point that has an A - then find the
    // lowest common denominator among all of those numbers
    var nodeSteps = mutableListOf<Long>()
    var tmpLCD: Long = 1

    for (key in net.network.keys) {
        if (key[2] == "A".single()) {
            nodeSteps.add(getSteps(key, net, true).toLong())
        }
    }

    for (i in nodeSteps) {
        tmpLCD = findLCM(tmpLCD,i)
    }
    return tmpLCD
}

fun loadNetwork(input: List<String>): Network {
    val instruction = input[0].trim()

    val map = mutableMapOf<String, List<String>>()

    for (x in 2..input.size-1) {
        val tmp = input[x].split("\\s+".toRegex())
        val key = tmp[0].trim()
        val dest1 = tmp[2].replace("(","").replace(",","")
        val dest2 = tmp[3].trim().replace(")","")
        map[key] = listOf(dest1, dest2)
    }

    val tmpNetwork = Network(instruction, instruction.length, map)

    return tmpNetwork
}

fun main() {
//    val input = readInput("Day08_test")
    val input = readInput("Day08_real")

    val network = loadNetwork(input)

    val firstResult = solvePart1Day08(network)
    val secondResult = solvePart2Day08(network)

    println("First Result: $firstResult")
    println("Second Result: $secondResult")
}
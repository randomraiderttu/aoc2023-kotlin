class BoatRace(val time: Long, val distance: Long) {
    var waysToWin: Long = 0
    init {
        for (x in 0..time) {
            val tmpDistance = x * (time - x)
            if (tmpDistance > distance) {
                waysToWin += 1
            }
        }
    }
}

fun solvePart1Day06(input: List<BoatRace>): Long {
    var total: Long = 1
    for (race in input) {
        total *= race.waysToWin
    }
    return total
}

fun solvePart2Day06(input: List<String>): Long {
    val timeList = input[0].split("\\s+".toRegex())
    val distanceList = input[1].split("\\s+".toRegex())
    var strTime: String = ""
    var strDistance: String = ""
    var time: Long = 0
    var distance: Long = 0
    var stopPoint: Long = 0

    for (x in 1..<timeList.size) {
        strTime += timeList[x]
        strDistance += distanceList[x]
    }
    time = strTime.toLong()
    distance = strDistance.toLong()

    for (x in 0..time) {
        val tmpDistance = x * (time - x)
        if (tmpDistance > distance) {
            stopPoint = x
            break
        }
    }
    return (time+1) - (2*stopPoint)
}

fun loadRaceParams(input: List<String>): List<BoatRace> {
    val raceList = mutableListOf<BoatRace>()
    val timeList = input[0].split("\\s+".toRegex())
    val distanceList = input[1].split("\\s+".toRegex())

    for (x in 1..<timeList.size) {
        val tmpBoatRace = BoatRace(timeList[x].toLong(), distanceList[x].toLong())
        raceList.add(tmpBoatRace)
    }
    return raceList
}

fun main() {
//    val input = readInput("Day06_test")
    val input = readInput("Day06_real")

    val races = loadRaceParams(input)

    val firstResult = solvePart1Day06(races)
    val secondResult = solvePart2Day06(input)

    println("First Result: $firstResult")
    println("Second Result: $secondResult")
}
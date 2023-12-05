data class gameCard
    (val winningNumbers: List<String>,
     val gameCard: List<String>,
     val gameCardMatchNum: Int,
     var gameCardInstances: Int = 1)
fun power(baseVal: Int, exponentVal: Int): Long {
    var exponent = exponentVal
    var result: Long = 1

    while (exponent != 0) {
        result *= baseVal.toLong()
        --exponent
    }
    return result
}
fun solvePart1Day04(input: List<String>): Long {
    var total:Long = 0

    for (game in input) {
        val gameCard = game.split(":")[1].trim()
        val winningNumList = gameCard.split("|")[0].trim().split("\\s+". toRegex())
        val cardNumList = gameCard.split("|")[1].trim().split("\\s+". toRegex())
        val matchNum = winningNumList.intersect(cardNumList.toSet())

        if (matchNum.isNotEmpty()) {
            total += power(2, matchNum.size - 1)
        }
    }
    return total
}

fun solvePart2Day04(input: List<String>): Int {
    val gameMap = mutableMapOf<Int, gameCard>()
    var total = 0

    // create map referenced by game card number and then game card details
    for (game in input) {
        val gameCardNumber = game.split(":")[0].split("\\s+".toRegex())[1].toInt()
        val gameCard = game.split(":")[1].trim()
        val winningNumList = gameCard.split("|")[0].trim().split("\\s+". toRegex())
        val cardNumList = gameCard.split("|")[1].trim().split("\\s+". toRegex())
        val matchNum = winningNumList.intersect(cardNumList.toSet()).size
        val tmpCard = gameCard(winningNumList,cardNumList,matchNum)
        gameMap.put(gameCardNumber,tmpCard)
    }
    val mapSize = gameMap.size

    for (x in 1..mapSize) {
        val currentCard = gameMap.getValue(x)

        // If current card has matches, we need to create dupes. How many you create depends
        // on how many game card instances there are. Instead of adding rows, we just keep track
        // of the number of that particular card
        if (currentCard.gameCardMatchNum > 0) {
            for (i in x+1..x+currentCard.gameCardMatchNum) {
                val tmpCard = gameMap.getValue(i)
                tmpCard.gameCardInstances += currentCard.gameCardInstances
                // I HATE this. Apparently you can't manipulate the object while it's in the map
                // you have to remove it and replace the map value - ugh. Should be a damn pointer - what's the issue
                gameMap.remove(i)
                gameMap.put(i, tmpCard)
            }
        }
    }

    // Add them up
    gameMap.forEach{ entry ->
        total += entry.value.gameCardInstances
    }
    return total
}

fun main() {
//    val input = readInput("Day04_test")
    val input = readInput("Day04_real")

    val firstResult = solvePart1Day04(input)
    val secondResult = solvePart2Day04(input)

    println("First Result: $firstResult")
    println("Second Result: $secondResult")
}
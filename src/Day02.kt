class game(val red: Int = 0, val blue: Int = 0, val green: Int = 0)

class cubeGame(gameID: Int, gameList: List<game>) {
    val gameID = gameID
    val gameList = gameList

    var maxRed = 0
    var maxBlue = 0
    var maxGreen = 0
    var total = 0
    init {
        // figure out the max values across each game for a set of games
        for (game in gameList) {
            if (game.red > maxRed) maxRed = game.red
            if (game.blue > maxBlue) maxBlue = game.blue
            if (game.green > maxGreen) maxGreen = game.green

        }

        total = maxRed * maxBlue * maxGreen
        // this is just used to print for troubleshooting
//        println("My game id is: ${gameID}")
//        for (game in gameList) {
//            println("    Red: ${game.red} Blue: ${game.blue} Green: ${game.green}")
//        }
//        println ("      Max Values: ${maxRed}-Red ${maxBlue}-Blue ${maxGreen}-Green")
    }

    fun isValidGame(red: Int, blue: Int, green: Int): Boolean = (red >= maxRed && blue >= maxBlue && green >= maxGreen)
}


fun createGame(input: String): game {
    var red = 0
    var green = 0
    var blue = 0

    for (entry in input.split(",")) {
        val color = entry.trim().split(" ").get(1)
        val value = entry.trim().split(" ").get(0).toInt()

        if (color.equals("blue")) {
            blue = value
        }
        if (color.equals("red")) {
            red = value
        }
        if (color.equals("green")) {
            green = value
        }
    }

    return game(red, blue, green)
}
fun solvePart1(input: List<cubeGame>): Int {
    val red = 12
    val green = 13
    val blue = 14
    var total = 0

    for (game in input) {
        if (game.isValidGame(red, blue, green)) {
//            println("Game ID ${game.gameID} is valid")
            total += game.gameID
        }
//        else {
//            println("Game ID ${game.gameID} is not valid")
//        }
    }
    return total
}

fun solvePart2(input: List<cubeGame>): Int {
    var total = 0
    for (game in input) {
        total += game.total
    }
    return total
}

fun getGameID(gameIDString: String): Int = gameIDString.split(" ").get(1).toInt()

fun main() {
    var gameList = mutableListOf<cubeGame>()

//    val input = readInput("Day02_test")
    val input = readInput("Day02_real")

    // Load the list of games - the cubeGame class has all the individuals games and max values
    // Should have put this in its own function to clean up main a bit...but...whatever
    for (game in input) {
//        println(game)
        val tmp = game.split(":").toTypedArray()
        val tmpGameList = tmp[1].split(";").toTypedArray()
        val games = mutableListOf<game>()
        for (turn in tmpGameList) {
            val tmpGame = createGame(turn)
            games.add(tmpGame)
        }

        var tmpGame = cubeGame(getGameID(tmp[0]),games)
        gameList.add(tmpGame)
    }

    val firstResult = solvePart1(gameList)
    val secondResult = solvePart2(gameList)

    println("First Result: ${firstResult}")
    println("Second Result: ${secondResult}")
}
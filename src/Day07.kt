import kotlin.collections.HashMap

var cardRank1: Map<Char, Int> = mapOf(
    "A".single() to 13,
    "K".single() to 12,
    "Q".single() to 11,
    "J".single() to 10,
    "T".single() to 9,
    "9".single() to 8,
    "8".single() to 7,
    "7".single() to 6,
    "6".single() to 5,
    "5".single() to 4,
    "4".single() to 3,
    "3".single() to 2,
    "2".single() to 1)

var cardRank2: Map<Char, Int> = mapOf(
    "A".single() to 13,
    "K".single() to 12,
    "Q".single() to 11,
    "J".single() to 0,
    "T".single() to 9,
    "9".single() to 8,
    "8".single() to 7,
    "7".single() to 6,
    "6".single() to 5,
    "5".single() to 4,
    "4".single() to 3,
    "3".single() to 2,
    "2".single() to 1)

class Hand(val cardHand: String, val bid: Int, var rank: Int = 0) {
    var handType = 0
    init {
        handType = getHandType(cardHand)
    }

    fun adjHandType(): Int {
        var jokerCount = 0
        for (x in 0..<cardHand.length) {
            if (cardHand[x] == "J".single()) jokerCount++
        }

        // 7 = 5kind; 6 = 4kind; 5 = full house; 4 = 3kind; 3 = two pair; 2 = one pair; 1 = high card
        when (jokerCount) {
            5 -> return 7 // 5 jokers is still 5 of a kind
            4 -> return 7 // 4 jokers creates 5 of a kind
            3 -> {
                if (handType == 5) return 7 // full house will become 5 of a kind
                if (handType == 4) return 6 // three of a kind w/ jokers would become 4 of a kind
            }

            2 -> {
                if (handType == 5) return 7 // full house with double jokers becomes 5 of a kind
                if (handType == 3) return 6 // two pair (with two being Jokers) becomes 4 of a kind
                if (handType == 2) return 4 // a single pair of jokers would become 3 of a kind of something
            }

            1 -> {
                if (handType == 6) return 7 // 4 of a kind would become 5 of a kind
                if (handType == 4) return 6 // 3 of a kind would become 4 of a kind
                if (handType == 3) return 5 // two pair and one joker would create a boat
                if (handType == 2) return 4 // a pair and a joke creates 3 of a kind
                if (handType == 1) return 2 // high card hand with a joker becomes a pair of something
            }
        }

        return handType // joker count was 0, handtype remains the same
    }
}

fun solvePart1Day07(hands: MutableList<Hand>): Int {
    val n = hands.size
    var total = 0

    for (i in 0 until n - 1) {
        for (j in 0 until n - i - 1) {
            if (compareHands(hands[j], hands[j + 1], false) == 1) {
                // Swap the elements
                val temp = hands[j]
                hands[j] = hands[j + 1]
                hands[j + 1] = temp
            }
        }
    }
    for (x in 0..hands.size-1) {
        hands[x].rank = x+1
        total += hands[x].bid * hands[x].rank
    }
    return total
}

fun getHandMap(input: String): HashMap<Char, Int> {
    val hashMap = HashMap<Char, Int>()
    for (char in input) {
        if (hashMap.containsKey(char)) {
            hashMap[char] = hashMap[char]!! + 1
        } else {
            hashMap[char] = 1
        }
    }
    return hashMap
}

fun getHandType(hand: String): Int {
    var map = getHandMap(hand)

    if (map.size == 1) return 7 // It's 5 of a kind
    if (map.size == 2) {
        map.forEach {
            if (it.value == 4) return 6 // It's four of a kind
        }
        // map size of 2 and it isn't 4 of a kind means it's a boat
        return 5
    }
    if (map.size == 3) {
        map.forEach{
            if (it.value == 3) return 4 // map size of 3 with 3 matches is 3 of a kind
        }
        // map size of 3 and there isn't a card with three means two pair
        return 3
    }
    if (map.size == 4) return 2 // pair

    return 1 // means the map.size is 5 and we're looking at high card

}

fun compareHands(hand1: Hand, hand2: Hand, isSecondPart: Boolean): Int {
    // Return 1 if hand1 is greater than hand 2
    // Return 0 if they are equal
    // Return -1 if hand2 is greater than hand1
    var hand1HandType = hand1.handType
    var hand2HandType = hand2.handType
    var cardMapper = cardRank1

    if (isSecondPart) {
        hand1HandType = hand1.adjHandType()
        hand2HandType = hand2.adjHandType()
        cardMapper = cardRank2
    }

    if (hand1HandType == hand2HandType) {
        for (i in 0..5) {
            if (cardMapper.getValue(hand1.cardHand[i]) > cardMapper.getValue(hand2.cardHand[i])) {
                return 1
            } else if (cardMapper.getValue(hand1.cardHand[i]) < cardMapper.getValue(hand2.cardHand[i])) {
                return -1
            }
        }
        return 0
    }
    if (hand1HandType > hand2HandType) {
        return 1
    }
    if (hand1HandType < hand2HandType) {
        return -1
    }
    return 0
}

fun solvePart2Day07(hands: MutableList<Hand>): Int {
    val n = hands.size
    var total = 0

    for (i in 0 until n - 1) {
        for (j in 0 until n - i - 1) {
            if (compareHands(hands[j], hands[j + 1], true) == 1) {
                // Swap the elements
                val temp = hands[j]
                hands[j] = hands[j + 1]
                hands[j + 1] = temp
            }
        }
    }
    for (x in 0..hands.size-1) {
        hands[x].rank = x+1
        total += hands[x].bid * hands[x].rank
    }
    return total
}

fun loadHands(input: List<String>): MutableList<Hand> {
    var handList = mutableListOf<Hand>()
    for (rec in input) {
        val tmpList = rec.split("\\s+".toRegex())
        var tmpHand = Hand(tmpList[0], tmpList[1].toInt())
        handList.add(tmpHand)
    }
    return handList
}

fun main() {
//    val input = readInput("Day07_test")
    val input = readInput("Day07_real")

    var hands = loadHands(input)

    val firstResult = solvePart1Day07(hands)
    val secondResult = solvePart2Day07(hands)

    println("First Result: $firstResult")
    println("Second Result: $secondResult")
}
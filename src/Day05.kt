import kotlin.math.min
import kotlin.math.max

data class Instruction(val destStart: Long, val sourceStart: Long, val rangeVal: Long)

class Mapping(val instructions: List<Instruction>) {

    fun getDestFromSource(source: Long): Long {
        for (rec in instructions) {
            if (source in rec.sourceStart..< rec.sourceStart+rec.rangeVal) {
                return (source - rec.sourceStart) + rec.destStart
            }
        }
        // Things that don't match - the dest and source are same
        return source
    }

    fun getSourceFromDest(dest: Long): Long {
        for (rec in instructions) {
            if (dest in rec.destStart..<rec.destStart+rec.rangeVal) {
                return (dest - rec.destStart) + rec.sourceStart
            }
        }
        // Things that don't match - the dest and source are same
        return dest
    }
}

data class Seed(var seed: Long, var soil: Long = 0, var fertilizer: Long = 0, var water: Long = 0, var light: Long = 0,
    var temp: Long = 0, var humidity: Long = 0, var location: Long = 0)

data class Mappings(
    val seeds: List<Seed>,
    val seedToSoil: Mapping,
    val soilToFert: Mapping,
    val fertToWater: Mapping,
    val waterToLight: Mapping,
    val lightToTemp: Mapping,
    val tempToHumidity: Mapping,
    val humidityToLoc: Mapping)

fun solvePart1Day05(mappings: Mappings): Long {
    for (seed in mappings.seeds) {
        seed.soil = mappings.seedToSoil.getDestFromSource(seed.seed)
        seed.fertilizer = mappings.soilToFert.getDestFromSource(seed.soil)
        seed.water = mappings.fertToWater.getDestFromSource(seed.fertilizer)
        seed.light = mappings.waterToLight.getDestFromSource(seed.water)
        seed.temp = mappings.lightToTemp.getDestFromSource(seed.light)
        seed.humidity = mappings.tempToHumidity.getDestFromSource(seed.temp)
        seed.location = mappings.humidityToLoc.getDestFromSource(seed.humidity)
    }

    var lowest = mappings.seeds[0].location
    for (seed in mappings.seeds) {
        lowest = min(lowest, seed.location)
    }
    return lowest
}

fun solvePart2Day05(mappings: Mappings): Long {
    var tmpSeedVal: Long
    val seedListSize = mappings.seeds.size
    var seedRange: Long

    // 0 is the lowest location you can get, so start there and backtrack to seed numbers going up
    // if you backtrack and check to see if that is in one of the seed ranges, then you found a seed
    // that was provided in the input and that's the lowest location
    // Just picked a huge number to run up to and ended up getting it sub-second just under 8million
    for (x in 0..<10000000000) {
        var tmpseed = Seed(0)
        tmpseed.humidity = mappings.humidityToLoc.getSourceFromDest(x.toLong())
        tmpseed.temp = mappings.tempToHumidity.getSourceFromDest(tmpseed.humidity)
        tmpseed.light = mappings.lightToTemp.getSourceFromDest(tmpseed.temp)
        tmpseed.water = mappings.waterToLight.getSourceFromDest(tmpseed.light)
        tmpseed.fertilizer = mappings.fertToWater.getSourceFromDest(tmpseed.water)
        tmpseed.soil = mappings.soilToFert.getSourceFromDest(tmpseed.fertilizer)
        tmpSeedVal = mappings.seedToSoil.getSourceFromDest(tmpseed.soil)

        for (y in 0..<seedListSize step 2) {
            seedRange = mappings.seeds[y].seed + mappings.seeds[y + 1].seed

            if (tmpSeedVal in mappings.seeds[y].seed..<seedRange) {
                return x.toLong()
            }
        }
    }
    return "-1".toLong()
}

fun loadMaps(input: List<String>): Mappings {
    var seeds = mutableListOf<Seed>()
    var is_seed_to_soil = false
    var is_soil_to_fert = false
    var is_fert_to_water = false
    var is_water_to_light = false
    var is_light_to_temp = false
    var is_temp_to_humidity = false
    var is_humidity_to_loc = false

    var seedToSoilList = mutableListOf<Instruction>()
    var soilToFertList = mutableListOf<Instruction>()
    var fertToWaterList = mutableListOf<Instruction>()
    var waterToLightList = mutableListOf<Instruction>()
    var lightToTempList = mutableListOf<Instruction>()
    var tempToHumidList = mutableListOf<Instruction>()
    var humidToLocList = mutableListOf<Instruction>()


    for (rec in input) {
        val section = rec.take(5)

        // Short circuit this - if it's a blank line, move on
        if (section == "") continue

        when(section) {
            "seeds" -> {
                for (seed in rec.split(":")[1].trim().split("\\s+".toRegex())) {
                    var tmpSeed = Seed(seed.toLong())
                    seeds.add(tmpSeed)
                }
            }
            "seed-" -> {
                is_seed_to_soil = true
                continue
            }
            "soil-" -> {
                is_soil_to_fert = true
                is_seed_to_soil = false
                continue
            }
            "ferti" -> {
                is_fert_to_water = true
                is_soil_to_fert = false
                continue
            }
            "water" -> {
                is_water_to_light = true
                is_fert_to_water = false
                continue
            }
            "light" -> {
                is_light_to_temp = true
                is_water_to_light = false
                continue
            }
            "tempe" -> {
                is_temp_to_humidity = true
                is_light_to_temp = false
                continue
            }
            "humid" -> {
                is_humidity_to_loc = true
                is_temp_to_humidity = false
                continue
            }
        }

        if (is_seed_to_soil) {
            val text = rec.split("\\s+".toRegex())
            val tmpInstruction = Instruction(text[0].toLong(), text[1].toLong(), text[2].toLong())
            seedToSoilList.add(tmpInstruction)
        }
        if (is_soil_to_fert) {
            val text = rec.split("\\s+".toRegex())
            val tmpInstruction = Instruction(text[0].toLong(), text[1].toLong(), text[2].toLong())
            soilToFertList.add(tmpInstruction)
        }
        if (is_fert_to_water) {
            val text = rec.split("\\s+".toRegex())
            val tmpInstruction = Instruction(text[0].toLong(), text[1].toLong(), text[2].toLong())
            fertToWaterList.add(tmpInstruction)
        }
        if (is_water_to_light) {
            val text = rec.split("\\s+".toRegex())
            val tmpInstruction = Instruction(text[0].toLong(), text[1].toLong(), text[2].toLong())
            waterToLightList.add(tmpInstruction)
        }
        if (is_light_to_temp) {
            val text = rec.split("\\s+".toRegex())
            val tmpInstruction = Instruction(text[0].toLong(), text[1].toLong(), text[2].toLong())
            lightToTempList.add(tmpInstruction)
        }
        if (is_temp_to_humidity) {
            val text = rec.split("\\s+".toRegex())
            val tmpInstruction = Instruction(text[0].toLong(), text[1].toLong(), text[2].toLong())
            tempToHumidList.add(tmpInstruction)
        }
        if (is_humidity_to_loc) {
            val text = rec.split("\\s+".toRegex())
            val tmpInstruction = Instruction(text[0].toLong(), text[1].toLong(), text[2].toLong())
            humidToLocList.add(tmpInstruction)
        }
    }
    var seedToSoil = Mapping(seedToSoilList)
    var soilToFert = Mapping(soilToFertList)
    var fertToWater = Mapping(fertToWaterList)
    var waterToLight = Mapping(waterToLightList)
    var lightToTemp = Mapping(lightToTempList)
    var tempToHumid = Mapping(tempToHumidList)
    var humidToLoc = Mapping(humidToLocList)

    var finalMappings = Mappings(seeds, seedToSoil, soilToFert, fertToWater, waterToLight, lightToTemp, tempToHumid, humidToLoc)

    return finalMappings
}

fun main() {
//    val input = readInput("Day05_test")
    val input = readInput("Day05_real")

    var mappings = loadMaps(input)
    val firstResult = solvePart1Day05(mappings)
    val secondResult = solvePart2Day05(mappings)

    println("First Result: $firstResult")
    println("Second Result: $secondResult")
}
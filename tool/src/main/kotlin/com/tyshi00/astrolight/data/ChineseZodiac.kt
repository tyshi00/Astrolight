package com.tyshi00.astrolight.data

/**
 * Chinese zodiac data based on traditional Chinese astrology.
 * The 12-year animal cycle combined with the 5-element cycle
 * creates a 60-year grand cycle (Sexagenary cycle).
 */
object ChineseZodiac {

    enum class CElement(val label: String) {
        WOOD("Wood"), FIRE("Fire"), EARTH("Earth"), METAL("Metal"), WATER("Water")
    }

    data class Animal(
        val name: String,
        val emoji: String,
        val traits: List<String>,
        val strengths: List<String>,
        val weaknesses: List<String>,
        val trineGroup: Int, // 1-4, animals in same group are natural allies
        val clashAnimal: String, // the animal that directly opposes
        val bestWith: List<String>,
        val worstWith: List<String>,
        val luckyNumbers: List<Int>,
        val luckyColors: List<String>,
    )

    val animals = listOf(
        Animal(
            name = "Rat",
            emoji = "\uD83D\uDC00",
            traits = listOf("Quick-witted", "Resourceful", "Versatile", "Charming"),
            strengths = listOf("Adaptable", "Observant", "Sociable", "Sharp instincts"),
            weaknesses = listOf("Opportunistic", "Stubborn", "Critical", "Nervous"),
            trineGroup = 1,
            clashAnimal = "Horse",
            bestWith = listOf("Dragon", "Monkey", "Ox"),
            worstWith = listOf("Horse", "Goat"),
            luckyNumbers = listOf(2, 3),
            luckyColors = listOf("Blue", "Gold", "Green"),
        ),
        Animal(
            name = "Ox",
            emoji = "\uD83D\uDC02",
            traits = listOf("Diligent", "Dependable", "Strong", "Determined"),
            strengths = listOf("Honest", "Patient", "Methodical", "Loyal"),
            weaknesses = listOf("Stubborn", "Slow", "Conventional", "Poor communication"),
            trineGroup = 2,
            clashAnimal = "Goat",
            bestWith = listOf("Snake", "Rooster", "Rat"),
            worstWith = listOf("Goat", "Horse", "Dog"),
            luckyNumbers = listOf(1, 4),
            luckyColors = listOf("White", "Yellow", "Green"),
        ),
        Animal(
            name = "Tiger",
            emoji = "\uD83D\uDC05",
            traits = listOf("Brave", "Competitive", "Confident", "Unpredictable"),
            strengths = listOf("Courageous", "Enthusiastic", "Leadership", "Generous"),
            weaknesses = listOf("Reckless", "Impatient", "Aggressive", "Overindulgent"),
            trineGroup = 3,
            clashAnimal = "Monkey",
            bestWith = listOf("Horse", "Dog", "Pig"),
            worstWith = listOf("Monkey", "Snake"),
            luckyNumbers = listOf(1, 3, 4),
            luckyColors = listOf("Blue", "Gray", "Orange"),
        ),
        Animal(
            name = "Rabbit",
            emoji = "\uD83D\uDC07",
            traits = listOf("Gentle", "Elegant", "Alert", "Responsible"),
            strengths = listOf("Compassionate", "Artistic", "Diplomatic", "Sensitive"),
            weaknesses = listOf("Timid", "Hesitant", "Overly cautious", "Self-indulgent"),
            trineGroup = 4,
            clashAnimal = "Rooster",
            bestWith = listOf("Goat", "Pig", "Dog"),
            worstWith = listOf("Rooster", "Dragon"),
            luckyNumbers = listOf(3, 4, 6),
            luckyColors = listOf("Red", "Pink", "Purple"),
        ),
        Animal(
            name = "Dragon",
            emoji = "\uD83D\uDC09",
            traits = listOf("Confident", "Ambitious", "Intelligent", "Enthusiastic"),
            strengths = listOf("Energetic", "Fearless", "Charismatic", "Lucky"),
            weaknesses = listOf("Arrogant", "Impatient", "Tactless", "Inflexible"),
            trineGroup = 1,
            clashAnimal = "Dog",
            bestWith = listOf("Rat", "Monkey", "Rooster"),
            worstWith = listOf("Dog", "Rabbit"),
            luckyNumbers = listOf(1, 6, 7),
            luckyColors = listOf("Gold", "Silver", "Gray"),
        ),
        Animal(
            name = "Snake",
            emoji = "\uD83D\uDC0D",
            traits = listOf("Intuitive", "Wise", "Elegant", "Decisive"),
            strengths = listOf("Perceptive", "Determined", "Sophisticated", "Calm"),
            weaknesses = listOf("Suspicious", "Jealous", "Secretive", "Materialistic"),
            trineGroup = 2,
            clashAnimal = "Pig",
            bestWith = listOf("Ox", "Rooster", "Dragon"),
            worstWith = listOf("Pig", "Tiger"),
            luckyNumbers = listOf(2, 8, 9),
            luckyColors = listOf("Black", "Red", "Yellow"),
        ),
        Animal(
            name = "Horse",
            emoji = "\uD83D\uDC0E",
            traits = listOf("Energetic", "Active", "Animated", "Warm-hearted"),
            strengths = listOf("Independent", "Quick-minded", "Cheerful", "Talented"),
            weaknesses = listOf("Impatient", "Wasteful", "Self-centered", "Hot-headed"),
            trineGroup = 3,
            clashAnimal = "Rat",
            bestWith = listOf("Tiger", "Dog", "Goat"),
            worstWith = listOf("Rat", "Ox"),
            luckyNumbers = listOf(2, 3, 7),
            luckyColors = listOf("Yellow", "Red", "Green"),
        ),
        Animal(
            name = "Goat",
            emoji = "\uD83D\uDC11",
            traits = listOf("Calm", "Gentle", "Sympathetic", "Creative"),
            strengths = listOf("Artistic", "Kind", "Elegant", "Nurturing"),
            weaknesses = listOf("Indecisive", "Pessimistic", "Over-sensitive", "Dependent"),
            trineGroup = 4,
            clashAnimal = "Ox",
            bestWith = listOf("Rabbit", "Pig", "Horse"),
            worstWith = listOf("Ox", "Rat", "Dog"),
            luckyNumbers = listOf(2, 7),
            luckyColors = listOf("Brown", "Red", "Purple"),
        ),
        Animal(
            name = "Monkey",
            emoji = "\uD83D\uDC12",
            traits = listOf("Clever", "Curious", "Mischievous", "Inventive"),
            strengths = listOf("Intelligent", "Versatile", "Sociable", "Witty"),
            weaknesses = listOf("Deceitful", "Arrogant", "Restless", "Sly"),
            trineGroup = 1,
            clashAnimal = "Tiger",
            bestWith = listOf("Rat", "Dragon", "Snake"),
            worstWith = listOf("Tiger", "Pig"),
            luckyNumbers = listOf(4, 9),
            luckyColors = listOf("White", "Blue", "Gold"),
        ),
        Animal(
            name = "Rooster",
            emoji = "\uD83D\uDC13",
            traits = listOf("Observant", "Hardworking", "Honest", "Courageous"),
            strengths = listOf("Capable", "Punctual", "Resourceful", "Confident"),
            weaknesses = listOf("Vain", "Blunt", "Critical", "Impatient"),
            trineGroup = 2,
            clashAnimal = "Rabbit",
            bestWith = listOf("Ox", "Snake", "Dragon"),
            worstWith = listOf("Rabbit", "Dog"),
            luckyNumbers = listOf(5, 7, 8),
            luckyColors = listOf("Gold", "Brown", "Yellow"),
        ),
        Animal(
            name = "Dog",
            emoji = "\uD83D\uDC15",
            traits = listOf("Loyal", "Honest", "Amiable", "Prudent"),
            strengths = listOf("Faithful", "Courageous", "Reliable", "Warm"),
            weaknesses = listOf("Anxious", "Stubborn", "Cynical", "Sensitive"),
            trineGroup = 3,
            clashAnimal = "Dragon",
            bestWith = listOf("Tiger", "Horse", "Rabbit"),
            worstWith = listOf("Dragon", "Ox", "Goat"),
            luckyNumbers = listOf(3, 4, 9),
            luckyColors = listOf("Red", "Green", "Purple"),
        ),
        Animal(
            name = "Pig",
            emoji = "\uD83D\uDC16",
            traits = listOf("Compassionate", "Generous", "Diligent", "Easygoing"),
            strengths = listOf("Sincere", "Tolerant", "Optimistic", "Responsible"),
            weaknesses = listOf("Naive", "Gullible", "Materialistic", "Lazy"),
            trineGroup = 4,
            clashAnimal = "Snake",
            bestWith = listOf("Rabbit", "Goat", "Tiger"),
            worstWith = listOf("Snake", "Monkey"),
            luckyNumbers = listOf(2, 5, 8),
            luckyColors = listOf("Yellow", "Gray", "Brown"),
        ),
    )

    /**
     * Element modifiers describe how the element colors the animal's personality.
     */
    val elementDescriptions = mapOf(
        CElement.WOOD to "Generous, cooperative, and idealistic. Wood adds warmth and expansiveness.",
        CElement.FIRE to "Dynamic, passionate, and assertive. Fire amplifies energy and leadership.",
        CElement.EARTH to "Grounded, practical, and reliable. Earth brings stability and patience.",
        CElement.METAL to "Determined, disciplined, and resolute. Metal sharpens focus and ambition.",
        CElement.WATER to "Flexible, intuitive, and persuasive. Water deepens wisdom and adaptability.",
    )

    fun getAnimalByName(name: String): Animal? =
        animals.find { it.name.equals(name, ignoreCase = true) }

    /**
     * Get the Chinese zodiac animal for a given birth year.
     * The cycle starts with Rat and repeats every 12 years.
     * Reference: 2020 was Year of the Rat.
     */
    fun animalForYear(year: Int): Animal {
        val index = ((year - 2020) % 12 + 12) % 12
        return animals[index]
    }

    /**
     * Get the element for a given birth year.
     * Elements follow a 2-year pattern within the 10-year Heavenly Stems cycle.
     * Reference: 2020-2021 = Metal, 2022-2023 = Water, 2024-2025 = Wood, etc.
     */
    fun elementForYear(year: Int): CElement {
        // The heavenly stems cycle: Wood, Fire, Earth, Metal, Water
        // Each element covers 2 consecutive years
        // 2024 = Wood Dragon, 2025 = Wood Snake
        // 2026 = Fire Horse, 2027 = Fire Goat
        val stemIndex = ((year - 2024) % 10 + 10) % 10
        return when (stemIndex / 2) {
            0 -> CElement.WOOD
            1 -> CElement.FIRE
            2 -> CElement.EARTH
            3 -> CElement.METAL
            4 -> CElement.WATER
            else -> CElement.WOOD
        }
    }

    /**
     * Compatibility between two animals based on trine groups and clash pairs.
     */
    fun compatibility(a: Animal, b: Animal): String = when {
        a.name == b.name -> "Same sign: deep understanding but may amplify shared weaknesses"
        a.trineGroup == b.trineGroup -> "Trine allies: natural harmony and mutual support"
        a.bestWith.contains(b.name) || b.bestWith.contains(a.name) -> "Favorable pairing with complementary strengths"
        a.clashAnimal == b.name -> "Direct clash: opposing natures require significant compromise"
        a.worstWith.contains(b.name) || b.worstWith.contains(a.name) -> "Challenging pairing, needs patience and understanding"
        else -> "Neutral pairing with room to grow together"
    }
}

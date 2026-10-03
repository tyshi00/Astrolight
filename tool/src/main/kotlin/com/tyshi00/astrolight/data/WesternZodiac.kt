package com.tyshi00.astrolight.data

/**
 * Western zodiac sign data based on established astrological tradition.
 * Each sign includes element, modality, ruling planet, traits,
 * compatible signs, and lucky attributes.
 */
object WesternZodiac {

    enum class Element(val label: String) {
        FIRE("Fire"), EARTH("Earth"), AIR("Air"), WATER("Water")
    }

    enum class Modality(val label: String) {
        CARDINAL("Cardinal"), FIXED("Fixed"), MUTABLE("Mutable")
    }

    data class Sign(
        val name: String,
        val symbol: String,
        val dateRange: String,
        val element: Element,
        val modality: Modality,
        val rulingPlanet: String,
        val traits: List<String>,
        val strengths: List<String>,
        val weaknesses: List<String>,
        val compatibleSigns: List<String>,
        val challengingSigns: List<String>,
        val luckyNumbers: List<Int>,
        val luckyColor: String,
        val luckyDay: String,
    ) {
        val apiName: String get() = name.lowercase()
    }

    val signs = listOf(
        Sign(
            name = "Aries",
            symbol = "\u2648",
            dateRange = "Mar 21 - Apr 19",
            element = Element.FIRE,
            modality = Modality.CARDINAL,
            rulingPlanet = "Mars",
            traits = listOf("Bold", "Ambitious", "Energetic", "Pioneering", "Confident"),
            strengths = listOf("Courageous", "Determined", "Optimistic", "Passionate"),
            weaknesses = listOf("Impatient", "Impulsive", "Short-tempered", "Competitive"),
            compatibleSigns = listOf("Leo", "Sagittarius", "Gemini", "Aquarius"),
            challengingSigns = listOf("Cancer", "Capricorn"),
            luckyNumbers = listOf(1, 8, 17),
            luckyColor = "Red",
            luckyDay = "Tuesday",
        ),
        Sign(
            name = "Taurus",
            symbol = "\u2649",
            dateRange = "Apr 20 - May 20",
            element = Element.EARTH,
            modality = Modality.FIXED,
            rulingPlanet = "Venus",
            traits = listOf("Reliable", "Patient", "Practical", "Devoted", "Sensual"),
            strengths = listOf("Dependable", "Persistent", "Loyal", "Generous"),
            weaknesses = listOf("Stubborn", "Possessive", "Materialistic", "Resistant to change"),
            compatibleSigns = listOf("Virgo", "Capricorn", "Cancer", "Pisces"),
            challengingSigns = listOf("Leo", "Aquarius"),
            luckyNumbers = listOf(2, 6, 9),
            luckyColor = "Green",
            luckyDay = "Friday",
        ),
        Sign(
            name = "Gemini",
            symbol = "\u264A",
            dateRange = "May 21 - Jun 20",
            element = Element.AIR,
            modality = Modality.MUTABLE,
            rulingPlanet = "Mercury",
            traits = listOf("Versatile", "Curious", "Witty", "Expressive", "Social"),
            strengths = listOf("Adaptable", "Quick-witted", "Communicative", "Intellectual"),
            weaknesses = listOf("Indecisive", "Inconsistent", "Superficial", "Restless"),
            compatibleSigns = listOf("Libra", "Aquarius", "Aries", "Leo"),
            challengingSigns = listOf("Virgo", "Pisces"),
            luckyNumbers = listOf(5, 7, 14),
            luckyColor = "Yellow",
            luckyDay = "Wednesday",
        ),
        Sign(
            name = "Cancer",
            symbol = "\u264B",
            dateRange = "Jun 21 - Jul 22",
            element = Element.WATER,
            modality = Modality.CARDINAL,
            rulingPlanet = "Moon",
            traits = listOf("Nurturing", "Intuitive", "Protective", "Sentimental", "Loyal"),
            strengths = listOf("Compassionate", "Tenacious", "Imaginative", "Persuasive"),
            weaknesses = listOf("Moody", "Clingy", "Overly sensitive", "Suspicious"),
            compatibleSigns = listOf("Scorpio", "Pisces", "Taurus", "Virgo"),
            challengingSigns = listOf("Aries", "Libra"),
            luckyNumbers = listOf(2, 7, 11),
            luckyColor = "Silver",
            luckyDay = "Monday",
        ),
        Sign(
            name = "Leo",
            symbol = "\u264C",
            dateRange = "Jul 23 - Aug 22",
            element = Element.FIRE,
            modality = Modality.FIXED,
            rulingPlanet = "Sun",
            traits = listOf("Charismatic", "Generous", "Warm", "Creative", "Dramatic"),
            strengths = listOf("Confident", "Loyal", "Ambitious", "Encouraging"),
            weaknesses = listOf("Arrogant", "Stubborn", "Self-centered", "Inflexible"),
            compatibleSigns = listOf("Aries", "Sagittarius", "Gemini", "Libra"),
            challengingSigns = listOf("Taurus", "Scorpio"),
            luckyNumbers = listOf(1, 3, 10),
            luckyColor = "Gold",
            luckyDay = "Sunday",
        ),
        Sign(
            name = "Virgo",
            symbol = "\u264D",
            dateRange = "Aug 23 - Sep 22",
            element = Element.EARTH,
            modality = Modality.MUTABLE,
            rulingPlanet = "Mercury",
            traits = listOf("Analytical", "Practical", "Diligent", "Modest", "Helpful"),
            strengths = listOf("Detail-oriented", "Hardworking", "Reliable", "Thoughtful"),
            weaknesses = listOf("Overcritical", "Worrying", "Perfectionistic", "Shy"),
            compatibleSigns = listOf("Taurus", "Capricorn", "Cancer", "Scorpio"),
            challengingSigns = listOf("Gemini", "Sagittarius"),
            luckyNumbers = listOf(5, 14, 23),
            luckyColor = "Navy blue",
            luckyDay = "Wednesday",
        ),
        Sign(
            name = "Libra",
            symbol = "\u264E",
            dateRange = "Sep 23 - Oct 22",
            element = Element.AIR,
            modality = Modality.CARDINAL,
            rulingPlanet = "Venus",
            traits = listOf("Diplomatic", "Gracious", "Fair-minded", "Social", "Harmonious"),
            strengths = listOf("Charming", "Cooperative", "Idealistic", "Balanced"),
            weaknesses = listOf("Indecisive", "Avoidant", "People-pleasing", "Vain"),
            compatibleSigns = listOf("Gemini", "Aquarius", "Leo", "Sagittarius"),
            challengingSigns = listOf("Cancer", "Capricorn"),
            luckyNumbers = listOf(4, 6, 13),
            luckyColor = "Pink",
            luckyDay = "Friday",
        ),
        Sign(
            name = "Scorpio",
            symbol = "\u264F",
            dateRange = "Oct 23 - Nov 21",
            element = Element.WATER,
            modality = Modality.FIXED,
            rulingPlanet = "Pluto",
            traits = listOf("Intense", "Passionate", "Resourceful", "Determined", "Magnetic"),
            strengths = listOf("Brave", "Focused", "Loyal", "Strategic"),
            weaknesses = listOf("Jealous", "Secretive", "Resentful", "Controlling"),
            compatibleSigns = listOf("Cancer", "Pisces", "Virgo", "Capricorn"),
            challengingSigns = listOf("Leo", "Aquarius"),
            luckyNumbers = listOf(8, 11, 18),
            luckyColor = "Crimson",
            luckyDay = "Tuesday",
        ),
        Sign(
            name = "Sagittarius",
            symbol = "\u2650",
            dateRange = "Nov 22 - Dec 21",
            element = Element.FIRE,
            modality = Modality.MUTABLE,
            rulingPlanet = "Jupiter",
            traits = listOf("Adventurous", "Optimistic", "Philosophical", "Honest", "Free-spirited"),
            strengths = listOf("Generous", "Idealistic", "Humorous", "Energetic"),
            weaknesses = listOf("Tactless", "Impatient", "Overconfident", "Careless"),
            compatibleSigns = listOf("Aries", "Leo", "Libra", "Aquarius"),
            challengingSigns = listOf("Virgo", "Pisces"),
            luckyNumbers = listOf(3, 7, 9),
            luckyColor = "Purple",
            luckyDay = "Thursday",
        ),
        Sign(
            name = "Capricorn",
            symbol = "\u2651",
            dateRange = "Dec 22 - Jan 19",
            element = Element.EARTH,
            modality = Modality.CARDINAL,
            rulingPlanet = "Saturn",
            traits = listOf("Disciplined", "Ambitious", "Responsible", "Practical", "Tenacious"),
            strengths = listOf("Patient", "Strategic", "Self-reliant", "Hardworking"),
            weaknesses = listOf("Pessimistic", "Rigid", "Unforgiving", "Condescending"),
            compatibleSigns = listOf("Taurus", "Virgo", "Scorpio", "Pisces"),
            challengingSigns = listOf("Aries", "Libra"),
            luckyNumbers = listOf(4, 8, 13),
            luckyColor = "Brown",
            luckyDay = "Saturday",
        ),
        Sign(
            name = "Aquarius",
            symbol = "\u2652",
            dateRange = "Jan 20 - Feb 18",
            element = Element.AIR,
            modality = Modality.FIXED,
            rulingPlanet = "Uranus",
            traits = listOf("Independent", "Innovative", "Humanitarian", "Original", "Intellectual"),
            strengths = listOf("Progressive", "Visionary", "Open-minded", "Inventive"),
            weaknesses = listOf("Detached", "Unpredictable", "Contrarian", "Aloof"),
            compatibleSigns = listOf("Gemini", "Libra", "Aries", "Sagittarius"),
            challengingSigns = listOf("Taurus", "Scorpio"),
            luckyNumbers = listOf(4, 7, 11),
            luckyColor = "Electric blue",
            luckyDay = "Saturday",
        ),
        Sign(
            name = "Pisces",
            symbol = "\u2653",
            dateRange = "Feb 19 - Mar 20",
            element = Element.WATER,
            modality = Modality.MUTABLE,
            rulingPlanet = "Neptune",
            traits = listOf("Empathetic", "Artistic", "Intuitive", "Dreamy", "Gentle"),
            strengths = listOf("Compassionate", "Creative", "Wise", "Musical"),
            weaknesses = listOf("Escapist", "Overly trusting", "Fearful", "Melancholic"),
            compatibleSigns = listOf("Cancer", "Scorpio", "Taurus", "Capricorn"),
            challengingSigns = listOf("Gemini", "Sagittarius"),
            luckyNumbers = listOf(3, 9, 12),
            luckyColor = "Sea green",
            luckyDay = "Thursday",
        ),
    )

    fun getByName(name: String): Sign? =
        signs.find { it.name.equals(name, ignoreCase = true) }

    /**
     * Element compatibility: Fire+Air harmonize, Earth+Water harmonize.
     * Same element is strong. Cross-harmony is neutral. Opposing is challenging.
     */
    fun elementCompatibility(a: Element, b: Element): String = when {
        a == b -> "Strong natural bond"
        (a == Element.FIRE && b == Element.AIR) || (a == Element.AIR && b == Element.FIRE) -> "Energizing and stimulating"
        (a == Element.EARTH && b == Element.WATER) || (a == Element.WATER && b == Element.EARTH) -> "Grounding and nurturing"
        (a == Element.FIRE && b == Element.WATER) || (a == Element.WATER && b == Element.FIRE) -> "Challenging but transformative"
        (a == Element.EARTH && b == Element.AIR) || (a == Element.AIR && b == Element.EARTH) -> "Different wavelengths, needs effort"
        (a == Element.FIRE && b == Element.EARTH) || (a == Element.EARTH && b == Element.FIRE) -> "Can build or burn, depends on patience"
        (a == Element.AIR && b == Element.WATER) || (a == Element.WATER && b == Element.AIR) -> "Misty connection, creative but elusive"
        else -> "Neutral"
    }
}

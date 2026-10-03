package com.tyshi00.astrolight.data

/**
 * Determines Western sun sign and Chinese zodiac from a date of birth.
 */
object ZodiacCalculator {

    /**
     * Get Western zodiac sign from month and day.
     */
    fun westernSign(month: Int, day: Int): WesternZodiac.Sign {
        val index = when {
            (month == 3 && day >= 21) || (month == 4 && day <= 19) -> 0   // Aries
            (month == 4 && day >= 20) || (month == 5 && day <= 20) -> 1   // Taurus
            (month == 5 && day >= 21) || (month == 6 && day <= 20) -> 2   // Gemini
            (month == 6 && day >= 21) || (month == 7 && day <= 22) -> 3   // Cancer
            (month == 7 && day >= 23) || (month == 8 && day <= 22) -> 4   // Leo
            (month == 8 && day >= 23) || (month == 9 && day <= 22) -> 5   // Virgo
            (month == 9 && day >= 23) || (month == 10 && day <= 22) -> 6  // Libra
            (month == 10 && day >= 23) || (month == 11 && day <= 21) -> 7 // Scorpio
            (month == 11 && day >= 22) || (month == 12 && day <= 21) -> 8 // Sagittarius
            (month == 12 && day >= 22) || (month == 1 && day <= 19) -> 9  // Capricorn
            (month == 1 && day >= 20) || (month == 2 && day <= 18) -> 10  // Aquarius
            (month == 2 && day >= 19) || (month == 3 && day <= 20) -> 11  // Pisces
            else -> 0
        }
        return WesternZodiac.signs[index]
    }

    /**
     * Get Chinese zodiac animal and element from birth year.
     */
    fun chineseAnimal(year: Int): ChineseZodiac.Animal = ChineseZodiac.animalForYear(year)
    fun chineseElement(year: Int): ChineseZodiac.CElement = ChineseZodiac.elementForYear(year)

    /**
     * Full profile from a date of birth.
     */
    data class ZodiacProfile(
        val westernSign: WesternZodiac.Sign,
        val chineseAnimal: ChineseZodiac.Animal,
        val chineseElement: ChineseZodiac.CElement,
        val chineseElementDescription: String,
        val chineseFullName: String, // e.g. "Water Tiger"
    )

    fun profile(year: Int, month: Int, day: Int): ZodiacProfile {
        val western = westernSign(month, day)
        val animal = chineseAnimal(year)
        val element = chineseElement(year)
        val elementDesc = ChineseZodiac.elementDescriptions[element] ?: ""
        return ZodiacProfile(
            westernSign = western,
            chineseAnimal = animal,
            chineseElement = element,
            chineseElementDescription = elementDesc,
            chineseFullName = "${element.label} ${animal.name}",
        )
    }

    /**
     * Compatibility report between two profiles.
     */
    data class CompatibilityReport(
        val westernCompatibility: String,
        val elementCompatibility: String,
        val chineseCompatibility: String,
        val overallVibes: String,
    )

    fun compatibility(a: ZodiacProfile, b: ZodiacProfile): CompatibilityReport {
        val westernCompat = if (a.westernSign.compatibleSigns.contains(b.westernSign.name)) {
            "${a.westernSign.name} and ${b.westernSign.name} are naturally compatible"
        } else if (a.westernSign.challengingSigns.contains(b.westernSign.name)) {
            "${a.westernSign.name} and ${b.westernSign.name} face natural tension, growth through challenge"
        } else {
            "${a.westernSign.name} and ${b.westernSign.name} have a neutral dynamic with potential"
        }

        val elementCompat = WesternZodiac.elementCompatibility(
            a.westernSign.element, b.westernSign.element,
        )

        val chineseCompat = ChineseZodiac.compatibility(a.chineseAnimal, b.chineseAnimal)

        // Simple overall summary
        val positiveWestern = a.westernSign.compatibleSigns.contains(b.westernSign.name)
        val positiveChinese = a.chineseAnimal.bestWith.contains(b.chineseAnimal.name) ||
            a.chineseAnimal.trineGroup == b.chineseAnimal.trineGroup
        val negativeWestern = a.westernSign.challengingSigns.contains(b.westernSign.name)
        val negativeChinese = a.chineseAnimal.clashAnimal == b.chineseAnimal.name

        val overall = when {
            positiveWestern && positiveChinese -> "Both systems agree: a naturally strong connection"
            positiveWestern && negativeChinese -> "Western stars align, but Chinese signs suggest different rhythms"
            negativeWestern && positiveChinese -> "Chinese harmony balances Western tension: an interesting mix"
            negativeWestern && negativeChinese -> "Both systems see challenge: rewarding if both invest effort"
            positiveWestern -> "Western compatibility is strong, Chinese signs are neutral"
            positiveChinese -> "Chinese compatibility is strong, Western signs are neutral"
            else -> "A relationship with room to define its own path"
        }

        return CompatibilityReport(
            westernCompatibility = westernCompat,
            elementCompatibility = elementCompat,
            chineseCompatibility = chineseCompat,
            overallVibes = overall,
        )
    }
}

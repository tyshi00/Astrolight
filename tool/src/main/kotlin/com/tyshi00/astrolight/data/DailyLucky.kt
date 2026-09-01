package com.tyshi00.astrolight.data

import java.util.Calendar
import java.util.TimeZone
import kotlin.math.abs

/**
 * Generates daily-changing lucky attributes for a zodiac sign.
 * Uses a hash of the date and sign name to deterministically pick
 * from a pool of colors, numbers, and compatible signs.
 * Same sign + same day = same results for everyone.
 * New day = new values.
 */
object DailyLucky {

    private val colorPool = listOf(
        "Red", "Blue", "Green", "Gold", "Silver", "Purple", "White",
        "Orange", "Pink", "Turquoise", "Coral", "Lavender", "Ivory",
        "Emerald", "Crimson", "Amber", "Teal", "Indigo", "Peach", "Sage",
    )

    data class DailyData(
        val luckyNumber: Int,
        val luckyColor: String,
        val supportingSign: String,
    )

    /**
     * Generate today's lucky data for a given sign.
     */
    fun forToday(sign: WesternZodiac.Sign): DailyData {
        val tz = TimeZone.getDefault()
        val cal = Calendar.getInstance(tz)
        val dayOfYear = cal.get(Calendar.DAY_OF_YEAR)
        val year = cal.get(Calendar.YEAR)

        // Create a deterministic seed from date + sign
        val seed = (year * 367 + dayOfYear) * 31 + sign.name.hashCode()
        val hash = abs(seed)

        // Lucky number: pick from the sign's own lucky numbers + a daily offset
        val baseNumber = sign.luckyNumbers[hash % sign.luckyNumbers.size]
        val dailyNumber = baseNumber + (hash / 7 % 10)

        // Lucky color: rotate through the full pool
        val colorIndex = hash % colorPool.size
        val luckyColor = colorPool[colorIndex]

        // Supporting sign: pick from compatible signs, rotating daily
        val supportIndex = (hash / 13) % sign.compatibleSigns.size
        val supportingSign = sign.compatibleSigns[supportIndex]

        return DailyData(
            luckyNumber = dailyNumber,
            luckyColor = luckyColor,
            supportingSign = supportingSign,
        )
    }
}

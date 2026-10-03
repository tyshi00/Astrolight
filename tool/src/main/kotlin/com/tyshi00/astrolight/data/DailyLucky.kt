package com.tyshi00.astrolight.data

import java.util.Calendar
import java.util.TimeZone
import kotlin.math.abs

/**
 * Generates daily-changing lucky attributes for a zodiac sign.
 * Uses a hash of the date, sign name, and birth date to deterministically
 * pick from a pool of colors, numbers, and compatible signs.
 * Same person + same day = same results.
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

    /** Safe modulo that always returns a non-negative result. */
    private fun safeMod(value: Long, mod: Int): Int {
        val result = (value % mod).toInt()
        return if (result < 0) result + mod else result
    }

    /**
     * Mix bits so related inputs produce unrelated outputs.
     * Ensures different signs/DOBs on the same day get independent values
     * for each attribute (color, number, sign).
     */
    private fun mix(value: Long, salt: Long): Long {
        var h = value xor salt
        h = (h xor (h ushr 16)) * 0x45D9F3BL
        h = (h xor (h ushr 16)) * 0x45D9F3BL
        h = h xor (h ushr 16)
        return if (h == Long.MIN_VALUE) Long.MAX_VALUE else abs(h)
    }

    fun forToday(sign: WesternZodiac.Sign, birthYear: Int = 0, birthMonth: Int = 0, birthDay: Int = 0): DailyData {
        val tz = TimeZone.getDefault()
        val cal = Calendar.getInstance(tz)
        val dayOfYear = cal.get(Calendar.DAY_OF_YEAR).toLong()
        val year = cal.get(Calendar.YEAR).toLong()

        val dobSalt = (birthYear * 13L + birthMonth * 7L + birthDay * 3L)
        val baseSeed = (year * 367L + dayOfYear) * 31L + sign.name.hashCode().toLong() + dobSalt

        // Each attribute uses a different salt so they vary independently
        val numberHash = mix(baseSeed, 0x12345678ABCDEF0L)
        val colorHash = mix(baseSeed, 0x7EDCBA9876543210L)
        val signHash = mix(baseSeed, 0x5A5A5A5A5A5A5A5AL)

        val baseNumber = sign.luckyNumbers[safeMod(numberHash, sign.luckyNumbers.size)]
        val dailyNumber = baseNumber + safeMod(numberHash ushr 16, 10)

        val luckyColor = colorPool[safeMod(colorHash, colorPool.size)]

        val supportingSign = sign.compatibleSigns[safeMod(signHash, sign.compatibleSigns.size)]

        return DailyData(
            luckyNumber = dailyNumber,
            luckyColor = luckyColor,
            supportingSign = supportingSign,
        )
    }
}

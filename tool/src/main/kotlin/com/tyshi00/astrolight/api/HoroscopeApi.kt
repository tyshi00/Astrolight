package com.tyshi00.astrolight.api

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess

private const val TAG = "HoroscopeApi"

class HoroscopeApi {

    private val client = HttpClient(OkHttp)

    private val signNumbers = mapOf(
        "aries" to 1, "taurus" to 2, "gemini" to 3, "cancer" to 4,
        "leo" to 5, "virgo" to 6, "libra" to 7, "scorpio" to 8,
        "sagittarius" to 9, "capricorn" to 10, "aquarius" to 11, "pisces" to 12,
    )

    data class HoroscopeResult(
        val text: String,
        val period: String,
    )

    suspend fun fetchDaily(sign: String): Result<HoroscopeResult> {
        val num = signNumbers[sign.lowercase()] ?: return Result.failure(IllegalArgumentException("Unknown sign: $sign"))
        val url = "https://www.horoscope.com/us/horoscopes/general/horoscope-general-daily-today.aspx?sign=$num"
        return fetchAndParse(url, "daily")
    }

    suspend fun fetchWeekly(sign: String): Result<HoroscopeResult> {
        val num = signNumbers[sign.lowercase()] ?: return Result.failure(IllegalArgumentException("Unknown sign: $sign"))
        val url = "https://www.horoscope.com/us/horoscopes/general/horoscope-general-weekly.aspx?sign=$num"
        return fetchAndParse(url, "weekly")
    }

    suspend fun fetchMonthly(sign: String): Result<HoroscopeResult> {
        val num = signNumbers[sign.lowercase()] ?: return Result.failure(IllegalArgumentException("Unknown sign: $sign"))
        val url = "https://www.horoscope.com/us/horoscopes/general/horoscope-general-monthly.aspx?sign=$num"
        return fetchAndParse(url, "monthly")
    }

    private suspend fun fetchAndParse(url: String, period: String): Result<HoroscopeResult> = runCatching {
        Log.d(TAG, "Fetching $period: $url")

        val response = client.get(url) {
            headers {
                append("User-Agent", "Mozilla/5.0 (Linux; Android 14) AppleWebKit/537.36")
                append("Cache-Control", "no-cache, no-store")
            }
        }

        if (!response.status.isSuccess()) throw IllegalStateException("HTTP ${response.status.value}")

        val html = response.bodyAsText()
        val text = extractFromMainHoroscope(html)

        if (text.isBlank()) throw IllegalStateException("Could not parse $period horoscope text")

        Log.d(TAG, "Parsed ($period): ${text.take(120)}...")
        HoroscopeResult(text = text, period = period)
    }

    private fun extractFromMainHoroscope(html: String): String {
        val mainStart = html.indexOf("main-horoscope")
        if (mainStart < 0) return fallbackExtract(html)

        val pStart = html.indexOf("<p", mainStart)
        if (pStart < 0) return fallbackExtract(html)

        val pTagClose = html.indexOf(">", pStart)
        if (pTagClose < 0) return fallbackExtract(html)

        val pEnd = html.indexOf("</p>", pTagClose)
        if (pEnd < 0) return fallbackExtract(html)

        var text = stripHtmlTags(html.substring(pTagClose + 1, pEnd))
        text = stripDatePrefix(text)
        return text.trim()
    }

    private fun fallbackExtract(html: String): String {
        val ogPattern = Regex("""content="([^"]{50,})"[^>]*property="og:description"""")
        val ogMatch = ogPattern.find(html)
        if (ogMatch != null) return stripDatePrefix(ogMatch.groupValues[1].trim())

        val ogPattern2 = Regex("""property="og:description"\s+content="([^"]{50,})"""")
        val ogMatch2 = ogPattern2.find(html)
        if (ogMatch2 != null) return stripDatePrefix(ogMatch2.groupValues[1].trim())

        return ""
    }

    private fun stripHtmlTags(html: String): String {
        return html.replace(Regex("<[^>]*>"), "")
            .replace("&amp;", "&")
            .replace("&nbsp;", " ")
            .replace("&quot;", "\"")
            .replace("&#039;", "'")
            .replace("&apos;", "'")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace(Regex("\\s+"), " ")
            .trim()
    }

    private fun stripDatePrefix(text: String): String {
        // Find the LAST 4-digit year in the first 40 characters.
        // Weekly format has a date range: "Aug 24, 2026 - Aug 30, 2026 - text"
        // We need to skip past ALL dates, not just the first one.
        var lastYearEnd = -1
        val searchLimit = text.length.coerceAtMost(45)
        for (i in 0..(searchLimit - 4)) {
            if (text[i].isDigit() && i + 3 < text.length &&
                text[i + 1].isDigit() && text[i + 2].isDigit() && text[i + 3].isDigit()
            ) {
                lastYearEnd = i + 4
            }
        }
        if (lastYearEnd > 0 && lastYearEnd < searchLimit) {
            // Skip past the year and any non-letter characters after it
            var j = lastYearEnd
            while (j < text.length && !text[j].isLetter()) {
                j++
            }
            if (j < text.length) {
                return text.substring(j).trim()
            }
        }
        return text.trim()
    }

    fun close() = client.close()
}

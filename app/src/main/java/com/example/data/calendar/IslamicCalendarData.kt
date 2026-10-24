package com.example.data.calendar

import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class IslamicDate(
    val day: Int,
    val monthNumber: Int,
    val monthName: String,
    val year: Int
) {
    fun formatted(): String = "$day $monthName $year AH"
}

data class IslamicEvent(
    val title: String,
    val hijriDate: String,
    val estimatedGregorianDate: String,
    val description: String,
    val isFastingRecommended: Boolean = false
)

object IslamicCalendarData {

    val HIJRI_MONTHS = listOf(
        "Muharram", "Safar", "Rabi' al-Awwal", "Rabi' al-Thani",
        "Jumada al-Awwal", "Jumada al-Thani", "Rajab", "Sha'ban",
        "Ramadan", "Shawwal", "Dhu al-Qi'dah", "Dhu al-Hijjah"
    )

    /**
     * Converts Gregorian LocalDate to estimated Umm al-Qura / Tabular Hijri date.
     */
    fun getEstimatedHijriDate(date: LocalDate = LocalDate.now()): IslamicDate {
        // Base epoch reference: 1 Muharram 1446 AH ~ July 7, 2024
        val epochHijriYear = 1446
        val epochGregorian = LocalDate.of(2024, 7, 7)
        val daysDiff = ChronoUnit.DAYS.between(epochGregorian, date).toInt()

        // Average synodic lunar month length ~ 29.530588 days
        var totalHijriDays = daysDiff
        var year = epochHijriYear

        val daysInYear = 354
        while (totalHijriDays >= 355) {
            val leapYear = isHijriLeapYear(year)
            val yDays = if (leapYear) 355 else 354
            if (totalHijriDays >= yDays) {
                totalHijriDays -= yDays
                year++
            } else {
                break
            }
        }
        while (totalHijriDays < 0) {
            year--
            val leapYear = isHijriLeapYear(year)
            val yDays = if (leapYear) 355 else 354
            totalHijriDays += yDays
        }

        var month = 1
        for (m in 1..12) {
            val daysInMonth = getDaysInHijriMonth(year, m)
            if (totalHijriDays >= daysInMonth) {
                totalHijriDays -= daysInMonth
                month++
            } else {
                break
            }
        }
        val day = (totalHijriDays + 1).coerceIn(1, 30)
        val safeMonth = month.coerceIn(1, 12)
        val monthName = HIJRI_MONTHS[safeMonth - 1]

        return IslamicDate(day, safeMonth, monthName, year)
    }

    private fun isHijriLeapYear(year: Int): Boolean {
        // 30-year cycle has leap years in years: 2, 5, 7, 10, 13, 16, 18, 21, 24, 26, 29
        val cycle = year % 30
        return cycle in listOf(2, 5, 7, 10, 13, 16, 18, 21, 24, 26, 29)
    }

    private fun getDaysInHijriMonth(year: Int, month: Int): Int {
        return if (month % 2 == 1) {
            30
        } else if (month == 12 && isHijriLeapYear(year)) {
            30
        } else {
            29
        }
    }

    val MAJOR_EVENTS = listOf(
        IslamicEvent(
            title = "Islamic New Year (1st Muharram)",
            hijriDate = "1 Muharram",
            estimatedGregorianDate = "Start of the Hijri Calendar year",
            description = "Commemorates the Hijrah (migration) of the Prophet Muhammad (ﷺ) from Makkah to Madinah."
        ),
        IslamicEvent(
            title = "Day of Ashura",
            hijriDate = "10 Muharram",
            estimatedGregorianDate = "10th of Muharram",
            description = "The day Allah delivered Prophet Musa (AS) and the Children of Israel from Pharaoh. Fasting this day expiates sins of the previous year.",
            isFastingRecommended = true
        ),
        IslamicEvent(
            title = "Mawlid an-Nabi",
            hijriDate = "12 Rabi' al-Awwal",
            estimatedGregorianDate = "Month of Rabi' al-Awwal",
            description = "Remembrance of the birth and blessed character of Prophet Muhammad (ﷺ)."
        ),
        IslamicEvent(
            title = "Isra and Mi'raj",
            hijriDate = "27 Rajab",
            estimatedGregorianDate = "Month of Rajab",
            description = "The miraculous Night Journey from Makkah to Jerusalem and Ascension through the seven heavens, where the 5 daily prayers were gifted."
        ),
        IslamicEvent(
            title = "Mid-Sha'ban (Shab-e-Barat)",
            hijriDate = "15 Sha'ban",
            estimatedGregorianDate = "Month of Sha'ban",
            description = "A night of seeking forgiveness and preparing hearts for the arrival of holy Ramadan."
        ),
        IslamicEvent(
            title = "First Day of Ramadan",
            hijriDate = "1 Ramadan",
            estimatedGregorianDate = "Start of Ramadan",
            description = "The holy month of fasting, Quran revelation, repentance, and intensive charity.",
            isFastingRecommended = true
        ),
        IslamicEvent(
            title = "Laylat al-Qadr (The Night of Power)",
            hijriDate = "Odd nights of last 10 days of Ramadan (21st, 23rd, 25th, 27th, 29th)",
            estimatedGregorianDate = "Last 10 Nights of Ramadan",
            description = "Better than 1,000 months of continuous worship. The night the Holy Quran was sent down to the lowest heaven."
        ),
        IslamicEvent(
            title = "Eid al-Fitr",
            hijriDate = "1 Shawwal",
            estimatedGregorianDate = "End of Ramadan",
            description = "Festival of breaking the fast, thanksgiving, family joy, and distributing Zakat al-Fitr."
        ),
        IslamicEvent(
            title = "First Ten Days of Dhu al-Hijjah",
            hijriDate = "1 - 10 Dhu al-Hijjah",
            estimatedGregorianDate = "Month of Hajj",
            description = "The most beloved ten days of the year to Allah for good deeds, fasting, Takbeer, and remembrance.",
            isFastingRecommended = true
        ),
        IslamicEvent(
            title = "Day of Arafah",
            hijriDate = "9 Dhu al-Hijjah",
            estimatedGregorianDate = "Day before Eid al-Adha",
            description = "The pinnacle of Hajj pilgrimage. Fasting on this day for non-pilgrims expiates the sins of the preceding and coming year.",
            isFastingRecommended = true
        ),
        IslamicEvent(
            title = "Eid al-Adha",
            hijriDate = "10 Dhu al-Hijjah",
            estimatedGregorianDate = "Festival of Sacrifice",
            description = "Festival commemorating the supreme devotion and obedience of Prophet Ibrahim (AS) and Ismail (AS)."
        )
    )
}

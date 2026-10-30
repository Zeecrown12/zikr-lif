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
    val isWhiteDay: Boolean get() = day in 13..15
}

enum class HijriConvention(
    val id: String,
    val displayName: String,
    val description: String,
    val defaultOffsetDays: Int
) {
    UMM_AL_QURA(
        id = "umm_al_qura",
        displayName = "Umm al-Qura (Saudi / Astronomical)",
        description = "Official calendar of Saudi Arabia based on calculated astronomical crescent birth.",
        defaultOffsetDays = 0
    ),
    NIGERIA_MOONSIGHTING(
        id = "nigeria_moonsighting",
        displayName = "Nigeria Moon Sighting Committee",
        description = "Follows naked-eye crescent moon sighting declarations by Sultanate Council of Sokoto.",
        defaultOffsetDays = 0
    ),
    GLOBAL_CALCULATION(
        id = "global_calculation",
        displayName = "Global Sighting / Tabular",
        description = "Standard lunar visibility calculation with manual +/- adjustment.",
        defaultOffsetDays = 0
    );

    companion object {
        fun fromId(id: String): HijriConvention =
            entries.find { it.id.equals(id, ignoreCase = true) } ?: UMM_AL_QURA
    }
}

data class IslamicEvent(
    val title: String,
    val hijriDate: String,
    val estimatedGregorianDate: String,
    val description: String,
    val isFastingRecommended: Boolean = false,
    val monthNumber: Int = 0,
    val dayNumber: Int = 0,
    val isDisputedOrLocalSighting: Boolean = false
)

object IslamicCalendarData {

    val HIJRI_MONTHS = listOf(
        "Muharram", "Safar", "Rabi' al-Awwal", "Rabi' al-Thani",
        "Jumada al-Awwal", "Jumada al-Thani", "Rajab", "Sha'ban",
        "Ramadan", "Shawwal", "Dhu al-Qi'dah", "Dhu al-Hijjah"
    )

    /**
     * Converts Gregorian LocalDate to Hijri date with user-selected offset.
     */
    fun getEstimatedHijriDate(date: LocalDate = LocalDate.now(), offsetDays: Int = 0): IslamicDate {
        // Base epoch reference: 1 Muharram 1446 AH ~ July 7, 2024
        val epochHijriYear = 1446
        val epochGregorian = LocalDate.of(2024, 7, 7)
        val adjustedDate = date.plusDays(offsetDays.toLong())
        val daysDiff = ChronoUnit.DAYS.between(epochGregorian, adjustedDate).toInt()

        var totalHijriDays = daysDiff
        var year = epochHijriYear

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
        val cycle = year % 30
        return cycle in listOf(2, 5, 7, 10, 13, 16, 18, 21, 24, 26, 29)
    }

    fun getDaysInHijriMonth(year: Int, month: Int): Int {
        return if (month % 2 == 1) {
            30
        } else if (month == 12 && isHijriLeapYear(year)) {
            30
        } else {
            29
        }
    }

    /**
     * Check if a specific Hijri date is an Islamic observance.
     */
    fun getEventsForHijriDay(month: Int, day: Int): List<IslamicEvent> {
        val events = mutableListOf<IslamicEvent>()

        // White days (13, 14, 15)
        if (day in 13..15 && month != 9) { // Not in Ramadan because Ramadan has obligatory fasting all month
            events.add(
                IslamicEvent(
                    title = "Ayyam al-Beed (White Days)",
                    hijriDate = "$day ${HIJRI_MONTHS[month - 1]}",
                    estimatedGregorianDate = "Middle of the lunar month",
                    description = "Sunnah to fast the three bright nights (13th, 14th, 15th) of every Islamic month. Prophet Muhammad (ﷺ) said: 'Fasting three days of each month is equivalent to fasting a lifetime.' (Bukhari)",
                    isFastingRecommended = true,
                    monthNumber = month,
                    dayNumber = day
                )
            )
        }

        // Matching specific major events
        for (event in MAJOR_EVENTS) {
            if (event.monthNumber == month && (event.dayNumber == day || (event.dayNumber == 0 && event.monthNumber == month))) {
                events.add(event)
            }
        }

        return events
    }

    val MAJOR_EVENTS = listOf(
        IslamicEvent(
            title = "Islamic New Year (1st Muharram)",
            hijriDate = "1 Muharram",
            estimatedGregorianDate = "Start of the Hijri Calendar year",
            description = "Commemorates the Hijrah (migration) of Prophet Muhammad (ﷺ) from Makkah to Madinah, establishing the first Islamic society based on brotherhood and faith.",
            monthNumber = 1,
            dayNumber = 1
        ),
        IslamicEvent(
            title = "Tasu'a (9th Muharram)",
            hijriDate = "9 Muharram",
            estimatedGregorianDate = "Day before Ashura",
            description = "Recommended day of fasting alongside the Day of Ashura. The Prophet (ﷺ) said: 'If I remain until next year, I will certainly fast the ninth day.' (Muslim)",
            isFastingRecommended = true,
            monthNumber = 1,
            dayNumber = 9
        ),
        IslamicEvent(
            title = "Day of Ashura",
            hijriDate = "10 Muharram",
            estimatedGregorianDate = "10th of Muharram",
            description = "The blessed day Allah delivered Prophet Musa (AS) and the Children of Israel from Pharaoh. Fasting on this day expiates the minor sins of the previous year. (Sahih Muslim)",
            isFastingRecommended = true,
            monthNumber = 1,
            dayNumber = 10
        ),
        IslamicEvent(
            title = "Mawlid an-Nabi",
            hijriDate = "12 Rabi' al-Awwal",
            estimatedGregorianDate = "Month of Rabi' al-Awwal",
            description = "Observed by many Muslims around the world to send blessings on Prophet Muhammad (ﷺ), study his seerah (life), and emulate his sublime character and compassion.",
            isDisputedOrLocalSighting = true,
            monthNumber = 3,
            dayNumber = 12
        ),
        IslamicEvent(
            title = "Isra and Mi'raj (The Night Journey)",
            hijriDate = "27 Rajab",
            estimatedGregorianDate = "Month of Rajab",
            description = "The miraculous Night Journey from the Sacred Mosque in Makkah to Al-Aqsa Mosque in Jerusalem and ascension through the seven heavens, where the five daily prayers were ordained.",
            monthNumber = 7,
            dayNumber = 27
        ),
        IslamicEvent(
            title = "Mid-Sha'ban (Shab-e-Barat)",
            hijriDate = "15 Sha'ban",
            estimatedGregorianDate = "Month of Sha'ban",
            description = "A night of seeking forgiveness, making abundant du'a, and spiritually preparing the heart for the blessed arrival of Ramadan.",
            monthNumber = 8,
            dayNumber = 15
        ),
        IslamicEvent(
            title = "First Day of Ramadan",
            hijriDate = "1 Ramadan",
            estimatedGregorianDate = "Beginning of the Holy Month",
            description = "The commencement of obligatory fasting from dawn to sunset, recitation of the Qur'an, Taraweeh prayers, and deep spiritual renewal.",
            isFastingRecommended = true,
            monthNumber = 9,
            dayNumber = 1
        ),
        IslamicEvent(
            title = "Laylat al-Qadr (Night of Power)",
            hijriDate = "Odd nights of last 10 days of Ramadan (21st, 23rd, 25th, 27th, 29th)",
            estimatedGregorianDate = "Last 10 Nights of Ramadan",
            description = "The pinnacle night of the entire year: 'The Night of Decree is better than a thousand months' (Surah Al-Qadr 97:3). Note: Its exact date is concealed by divine wisdom to encourage consistent worship across all the last ten odd nights.",
            monthNumber = 9,
            dayNumber = 27
        ),
        IslamicEvent(
            title = "Eid al-Fitr",
            hijriDate = "1 Shawwal",
            estimatedGregorianDate = "Conclusion of Ramadan",
            description = "The joyous Festival of Breaking the Fast. Begins with Salat al-Eid, thanksgiving to Allah, family visits, and distributing Zakat al-Fitr to the poor before the prayer.",
            monthNumber = 10,
            dayNumber = 1
        ),
        IslamicEvent(
            title = "Six Days of Shawwal",
            hijriDate = "2 - 30 Shawwal",
            estimatedGregorianDate = "Month of Shawwal",
            description = "Voluntary fasting of any six days during the month of Shawwal. The Prophet (ﷺ) said: 'Whoever fasts Ramadan and follows it with six days of Shawwal, it will be as if he fasted for a lifetime.' (Muslim)",
            isFastingRecommended = true,
            monthNumber = 10,
            dayNumber = 0
        ),
        IslamicEvent(
            title = "First Ten Days of Dhu al-Hijjah",
            hijriDate = "1 - 10 Dhu al-Hijjah",
            estimatedGregorianDate = "Sacred Month of Hajj",
            description = "The most virtuous ten days of the Islamic calendar. Prophet Muhammad (ﷺ) said: 'There are no days in which righteous deeds are more beloved to Allah than these ten days.' (Sahih Bukhari)",
            isFastingRecommended = true,
            monthNumber = 12,
            dayNumber = 1
        ),
        IslamicEvent(
            title = "Day of Arafah",
            hijriDate = "9 Dhu al-Hijjah",
            estimatedGregorianDate = "Day before Eid al-Adha",
            description = "The great pillar of the Hajj pilgrimage. For those not performing Hajj, fasting on this day expiates the sins of the previous year and the coming year. (Sahih Muslim)",
            isFastingRecommended = true,
            monthNumber = 12,
            dayNumber = 9
        ),
        IslamicEvent(
            title = "Eid al-Adha (Feast of Sacrifice)",
            hijriDate = "10 Dhu al-Hijjah",
            estimatedGregorianDate = "Major Islamic Festival",
            description = "Festival commemorating the unwavering faith and submission of Prophet Ibrahim (AS) and Ismail (AS). Marked by Eid prayer, takbeerat, and offering the sacrifice (Udhiyah/Qurbani).",
            monthNumber = 12,
            dayNumber = 10
        ),
        IslamicEvent(
            title = "Days of Tashreeq",
            hijriDate = "11, 12, 13 Dhu al-Hijjah",
            estimatedGregorianDate = "Three days following Eid al-Adha",
            description = "Days of eating, drinking, and remembering Allah. Fasting is prohibited during these festive days as pilgrims complete the rituals at Mina.",
            monthNumber = 12,
            dayNumber = 11
        )
    )
}

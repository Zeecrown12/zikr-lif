package com.example.data.prayer

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.*

enum class CalculationMethod(val displayName: String, val fajrAngle: Double, val ishaAngle: Double, val ishaMinutes: Int? = null) {
    MUSLIM_WORLD_LEAGUE("Muslim World League", 18.0, 17.0),
    ISNA("ISNA (North America)", 15.0, 15.0),
    EGYPT("Egyptian General Authority", 19.5, 17.5),
    UMM_AL_QURA("Umm Al-Qura (Makkah)", 18.5, 0.0, ishaMinutes = 90),
    KARACHI("Univ. of Islamic Sciences, Karachi", 18.0, 18.0)
}

enum class JuristicMethod(val displayName: String, val shadowFactor: Double) {
    STANDARD("Standard (Shafi'i, Maliki, Hanbali)", 1.0),
    HANAFI("Hanafi", 2.0)
}

data class CityLocation(
    val name: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val timeZoneOffsetHours: Double
)

data class PrayerTimes(
    val fajr: LocalTime,
    val sunrise: LocalTime,
    val dhuhr: LocalTime,
    val asr: LocalTime,
    val maghrib: LocalTime,
    val isha: LocalTime,
    val date: LocalDate = LocalDate.now()
) {
    fun formatted(time: LocalTime): String {
        return time.format(DateTimeFormatter.ofPattern("hh:mm a"))
    }

    fun getNextPrayer(currentTime: LocalTime = LocalTime.now()): Pair<String, LocalTime> {
        return when {
            currentTime.isBefore(fajr) -> "Fajr" to fajr
            currentTime.isBefore(sunrise) -> "Sunrise" to sunrise
            currentTime.isBefore(dhuhr) -> "Dhuhr" to dhuhr
            currentTime.isBefore(asr) -> "Asr" to asr
            currentTime.isBefore(maghrib) -> "Maghrib" to maghrib
            currentTime.isBefore(isha) -> "Isha" to isha
            else -> "Fajr (Tomorrow)" to fajr
        }
    }
}

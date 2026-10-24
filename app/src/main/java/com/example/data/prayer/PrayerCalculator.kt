package com.example.data.prayer

import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoField
import kotlin.math.*

object PrayerCalculator {

    val KAABA_LAT = 21.422487
    val KAABA_LNG = 39.826206

    val DEFAULT_CITIES = listOf(
        CityLocation("Makkah", "Saudi Arabia", 21.4225, 39.8262, 3.0),
        CityLocation("Madinah", "Saudi Arabia", 24.5247, 39.5692, 3.0),
        CityLocation("London", "United Kingdom", 51.5074, -0.1278, 1.0),
        CityLocation("New York", "United States", 40.7128, -74.0060, -4.0),
        CityLocation("Cairo", "Egypt", 30.0444, 31.2357, 3.0),
        CityLocation("Istanbul", "Turkey", 41.0082, 28.9784, 3.0),
        CityLocation("Dubai", "United Arab Emirates", 25.2048, 55.2708, 4.0),
        CityLocation("Jakarta", "Indonesia", -6.2088, 106.8456, 7.0),
        CityLocation("Kuala Lumpur", "Malaysia", 3.1390, 101.6869, 8.0),
        CityLocation("Toronto", "Canada", 43.6532, -79.3832, -4.0),
        CityLocation("Lagos", "Nigeria", 6.5244, 3.3792, 1.0),
        CityLocation("Sydney", "Australia", -33.8688, 151.2093, 11.0)
    )

    fun calculateTimes(
        latitude: Double,
        longitude: Double,
        timeZoneOffset: Double,
        date: LocalDate = LocalDate.now(),
        method: CalculationMethod = CalculationMethod.MUSLIM_WORLD_LEAGUE,
        juristicMethod: JuristicMethod = JuristicMethod.STANDARD
    ): PrayerTimes {
        val dayOfYear = date.get(ChronoField.DAY_OF_YEAR)

        // Solar Declination & Equation of Time calculation (standard astronomical formula)
        val gamma = 2.0 * Math.PI / 365.0 * (dayOfYear - 1)
        val eqTime = 229.18 * (0.000075 + 0.001868 * cos(gamma) - 0.032077 * sin(gamma) -
                0.014615 * cos(2 * gamma) - 0.040849 * sin(2 * gamma))
        val declination = 0.006918 - 0.399912 * cos(gamma) + 0.070257 * sin(gamma) -
                0.006758 * cos(2 * gamma) + 0.000907 * sin(2 * gamma) -
                0.002697 * cos(3 * gamma) + 0.00148 * sin(3 * gamma)

        val latRad = Math.toRadians(latitude)
        val declRad = declination

        // Solar Noon in hours
        val timeOffset = eqTime + 4.0 * longitude - 60.0 * timeZoneOffset
        val solarNoonHours = 12.0 - timeOffset / 60.0

        // Dhuhr is slightly after solar noon (typically +2 to +5 min for safety)
        val dhuhrHours = solarNoonHours + (2.0 / 60.0)

        // Sunrise & Sunset angle (sun center is at -0.8333 degrees below horizon due to refraction)
        val sunAltRad = Math.toRadians(-0.8333)
        val cosHA = (sin(sunAltRad) - sin(latRad) * sin(declRad)) / (cos(latRad) * cos(declRad))
        val haRad = acos(cosHA.coerceIn(-1.0, 1.0))
        val haHours = Math.toDegrees(haRad) / 15.0

        val sunriseHours = solarNoonHours - haHours
        val sunsetHours = solarNoonHours + haHours

        // Fajr angle calculation
        val fajrAltRad = Math.toRadians(-method.fajrAngle)
        val cosHAFajr = (sin(fajrAltRad) - sin(latRad) * sin(declRad)) / (cos(latRad) * cos(declRad))
        val haFajrRad = acos(cosHAFajr.coerceIn(-1.0, 1.0))
        val haFajrHours = Math.toDegrees(haFajrRad) / 15.0
        val fajrHours = solarNoonHours - haFajrHours

        // Asr angle calculation
        val asrAltRad = atan(1.0 / (juristicMethod.shadowFactor + tan(abs(latRad - declRad))))
        val cosHAAsr = (sin(asrAltRad) - sin(latRad) * sin(declRad)) / (cos(latRad) * cos(declRad))
        val haAsrRad = acos(cosHAAsr.coerceIn(-1.0, 1.0))
        val haAsrHours = Math.toDegrees(haAsrRad) / 15.0
        val asrHours = solarNoonHours + haAsrHours

        // Maghrib is identical to Sunset (plus 2 min for safety)
        val maghribHours = sunsetHours + (2.0 / 60.0)

        // Isha calculation
        val ishaHours = if (method.ishaMinutes != null) {
            maghribHours + (method.ishaMinutes / 60.0)
        } else {
            val ishaAltRad = Math.toRadians(-method.ishaAngle)
            val cosHAIsha = (sin(ishaAltRad) - sin(latRad) * sin(declRad)) / (cos(latRad) * cos(declRad))
            val haIshaRad = acos(cosHAIsha.coerceIn(-1.0, 1.0))
            val haIshaHours = Math.toDegrees(haIshaRad) / 15.0
            solarNoonHours + haIshaHours
        }

        return PrayerTimes(
            fajr = decimalToLocalTime(fajrHours),
            sunrise = decimalToLocalTime(sunriseHours),
            dhuhr = decimalToLocalTime(dhuhrHours),
            asr = decimalToLocalTime(asrHours),
            maghrib = decimalToLocalTime(maghribHours),
            isha = decimalToLocalTime(ishaHours),
            date = date
        )
    }

    private fun decimalToLocalTime(decimalHours: Double): LocalTime {
        var h = decimalHours
        while (h < 0) h += 24.0
        while (h >= 24) h -= 24.0
        val hours = h.toInt()
        val minutes = ((h - hours) * 60).toInt().coerceIn(0, 59)
        val seconds = ((((h - hours) * 60) - minutes) * 60).toInt().coerceIn(0, 59)
        return LocalTime.of(hours, minutes, seconds)
    }

    /**
     * Calculates Qibla direction (bearing in degrees 0..360 from true North)
     */
    fun calculateQiblaBearing(userLat: Double, userLng: Double): Float {
        val lat1 = Math.toRadians(userLat)
        val lng1 = Math.toRadians(userLng)
        val lat2 = Math.toRadians(KAABA_LAT)
        val lng2 = Math.toRadians(KAABA_LNG)

        val deltaLng = lng2 - lng1
        val y = sin(deltaLng) * cos(lat2)
        val x = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(deltaLng)
        var bearing = Math.toDegrees(atan2(y, x))
        if (bearing < 0) {
            bearing += 360.0
        }
        return bearing.toFloat()
    }

    /**
     * Calculates distance to Kaaba in kilometers
     */
    fun calculateDistanceToKaaba(userLat: Double, userLng: Double): Int {
        val earthRadiusKm = 6371.0
        val dLat = Math.toRadians(KAABA_LAT - userLat)
        val dLng = Math.toRadians(KAABA_LNG - userLng)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(userLat)) * cos(Math.toRadians(KAABA_LAT)) *
                sin(dLng / 2) * sin(dLng / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return (earthRadiusKm * c).roundToInt()
    }
}

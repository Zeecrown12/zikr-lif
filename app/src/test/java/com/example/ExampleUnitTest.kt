package com.example

import com.example.data.calendar.IslamicCalendarData
import com.example.data.prayer.CalculationMethod
import com.example.data.prayer.PrayerCalculator
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate

class ExampleUnitTest {
  @Test
  fun testPrayerCalculation() {
    // Makkah: 21.4225, 39.8262
    val times = PrayerCalculator.calculateTimes(
      latitude = 21.4225,
      longitude = 39.8262,
      timeZoneOffset = 3.0,
      date = LocalDate.of(2026, 10, 24),
      method = CalculationMethod.UMM_AL_QURA
    )
    assertNotNull(times.fajr)
    assertNotNull(times.dhuhr)
    assertNotNull(times.maghrib)
    assertTrue(times.fajr.isBefore(times.sunrise))
    assertTrue(times.sunrise.isBefore(times.dhuhr))
    assertTrue(times.dhuhr.isBefore(times.asr))
    assertTrue(times.asr.isBefore(times.maghrib))
    assertTrue(times.maghrib.isBefore(times.isha))
  }

  @Test
  fun testQiblaCalculation() {
    // From London (51.5, -0.12) to Makkah (21.42, 39.82)
    val bearing = PrayerCalculator.calculateQiblaBearing(51.5074, -0.1278)
    // Bearing from London to Makkah is approximately 118 degrees (East-Southeast)
    assertTrue("Bearing should be roughly between 110 and 125 degrees", bearing in 110f..125f)
  }

  @Test
  fun testHijriCalendar() {
    val hijriDate = IslamicCalendarData.getEstimatedHijriDate(LocalDate.of(2026, 10, 24))
    assertTrue("Year should be around 1448 AH", hijriDate.year in 1447..1449)
    assertTrue("Day must be valid", hijriDate.day in 1..30)
    assertTrue("Month must be valid", hijriDate.monthNumber in 1..12)
  }
}

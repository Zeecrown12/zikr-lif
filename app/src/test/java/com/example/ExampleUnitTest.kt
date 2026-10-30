package com.example

import com.example.data.calendar.IslamicCalendarData
import com.example.data.calendar.IslamicDate
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

  @Test
  fun testHijriOffsetAndWhiteDays() {
    val baseDate = LocalDate.of(2026, 10, 24)
    val standard = IslamicCalendarData.getEstimatedHijriDate(baseDate, offsetDays = 0)
    val plusOne = IslamicCalendarData.getEstimatedHijriDate(baseDate, offsetDays = 1)
    val minusOne = IslamicCalendarData.getEstimatedHijriDate(baseDate, offsetDays = -1)

    assertNotNull(standard)
    assertNotNull(plusOne)
    assertNotNull(minusOne)

    // White Days check on 13, 14, 15 of any Hijri month
    val whiteDay13 = IslamicDate(day = 13, monthNumber = 9, year = 1448, monthName = "Ramadan")
    val whiteDay14 = IslamicDate(day = 14, monthNumber = 9, year = 1448, monthName = "Ramadan")
    val whiteDay15 = IslamicDate(day = 15, monthNumber = 9, year = 1448, monthName = "Ramadan")
    val normalDay1 = IslamicDate(day = 1, monthNumber = 9, year = 1448, monthName = "Ramadan")

    assertTrue("Day 13 is a White Day", whiteDay13.isWhiteDay)
    assertTrue("Day 14 is a White Day", whiteDay14.isWhiteDay)
    assertTrue("Day 15 is a White Day", whiteDay15.isWhiteDay)
    assertFalse("Day 1 is not a White Day", normalDay1.isWhiteDay)
  }

  @Test
  fun testMajorIslamicEventsContainSacredDays() {
    val ramadan = IslamicCalendarData.MAJOR_EVENTS.find { it.title.contains("Ramadan", ignoreCase = true) }
    val arafah = IslamicCalendarData.MAJOR_EVENTS.find { it.title.contains("Arafah", ignoreCase = true) }
    val eidFitr = IslamicCalendarData.MAJOR_EVENTS.find { it.title.contains("Eid al-Fitr", ignoreCase = true) }
    val eidAdha = IslamicCalendarData.MAJOR_EVENTS.find { it.title.contains("Eid al-Adha", ignoreCase = true) }

    assertNotNull("Ramadan event must be defined", ramadan)
    assertNotNull("Day of Arafah must be defined", arafah)
    assertNotNull("Eid al-Fitr must be defined", eidFitr)
    assertNotNull("Eid al-Adha must be defined", eidAdha)
  }

  @Test
  fun testUpdateManagerVersioning() {
    val currentCode = com.example.data.update.UpdateManager.getCurrentVersionCode()
    val currentName = com.example.data.update.UpdateManager.getCurrentVersionName()
    val appId = com.example.data.update.UpdateManager.getApplicationId()

    assertEquals("Application ID must remain consistent", "com.aistudio.zikrlife.mxbq", appId)
    assertEquals(2, currentCode)
    assertEquals("1.1.0", currentName)
  }
}

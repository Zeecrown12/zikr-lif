package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.calendar.HijriConvention
import com.example.data.calendar.PersonalCalendarEntry
import com.example.data.prayer.CalculationMethod
import com.example.data.prayer.CityLocation
import com.example.data.prayer.JuristicMethod
import com.example.data.prayer.PrayerCalculator
import com.example.ui.theme.ArabicFontOption
import com.example.ui.theme.EnglishFontOption
import com.example.ui.theme.LineSpacingOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

class ZikrPreferencesRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("zikr_life_prefs", Context.MODE_PRIVATE)

    // Current City
    private val _currentCity = MutableStateFlow(loadCity())
    val currentCity: StateFlow<CityLocation> = _currentCity.asStateFlow()

    // Calculation Method
    private val _calculationMethod = MutableStateFlow(loadCalculationMethod())
    val calculationMethod: StateFlow<CalculationMethod> = _calculationMethod.asStateFlow()

    // Juristic Method (Asr)
    private val _juristicMethod = MutableStateFlow(loadJuristicMethod())
    val juristicMethod: StateFlow<JuristicMethod> = _juristicMethod.asStateFlow()

    // Quran & Arabic Font Size (sp)
    private val _quranFontSize = MutableStateFlow(prefs.getInt("quran_font_size", 26))
    val quranFontSize: StateFlow<Int> = _quranFontSize.asStateFlow()
    val arabicFontSize: StateFlow<Int> = _quranFontSize.asStateFlow()

    // Translation Font Size (sp)
    private val _translationFontSize = MutableStateFlow(prefs.getInt("translation_font_size", 16))
    val translationFontSize: StateFlow<Int> = _translationFontSize.asStateFlow()

    fun setArabicFontSize(size: Int) = setQuranFontSize(size)

    // Typography Options
    private val _arabicFont = MutableStateFlow(loadArabicFont())
    val arabicFont: StateFlow<ArabicFontOption> = _arabicFont.asStateFlow()

    private val _englishFont = MutableStateFlow(loadEnglishFont())
    val englishFont: StateFlow<EnglishFontOption> = _englishFont.asStateFlow()

    private val _lineSpacing = MutableStateFlow(loadLineSpacing())
    val lineSpacing: StateFlow<LineSpacingOption> = _lineSpacing.asStateFlow()

    // Calendar Options
    private val _hijriOffsetDays = MutableStateFlow(prefs.getInt("hijri_offset_days", 0))
    val hijriOffsetDays: StateFlow<Int> = _hijriOffsetDays.asStateFlow()

    private val _hijriConvention = MutableStateFlow(loadHijriConvention())
    val hijriConvention: StateFlow<HijriConvention> = _hijriConvention.asStateFlow()

    // Personal Calendar Tracking Entries
    private val _calendarEntries = MutableStateFlow(loadCalendarEntries())
    val calendarEntries: StateFlow<List<PersonalCalendarEntry>> = _calendarEntries.asStateFlow()

    // Bookmarked Surahs
    private val _bookmarkedSurahs = MutableStateFlow(loadBookmarks())
    val bookmarkedSurahs: StateFlow<Set<Int>> = _bookmarkedSurahs.asStateFlow()

    // Last Read Surah & Ayah
    private val _lastReadSurah = MutableStateFlow(prefs.getInt("last_read_surah", 1))
    val lastReadSurah: StateFlow<Int> = _lastReadSurah.asStateFlow()

    // Tasbih Lifetime Count
    private val _lifetimeTasbih = MutableStateFlow(prefs.getLong("lifetime_tasbih", 2150L))
    val lifetimeTasbih: StateFlow<Long> = _lifetimeTasbih.asStateFlow()

    // Haptic Feedback Enabled
    private val _hapticEnabled = MutableStateFlow(prefs.getBoolean("haptic_enabled", true))
    val hapticEnabled: StateFlow<Boolean> = _hapticEnabled.asStateFlow()

    // Completed Daily Habits for Today
    private val _todayHabits = MutableStateFlow(loadTodayHabits())
    val todayHabits: StateFlow<Set<String>> = _todayHabits.asStateFlow()

    // Missed fasts (Qadha) counter
    private val _missedFastsCount = MutableStateFlow(prefs.getInt("missed_fasts_count", 0))
    val missedFastsCount: StateFlow<Int> = _missedFastsCount.asStateFlow()

    // Fasted days in current month/year
    private val _fastedDates = MutableStateFlow(loadFastedDates())
    val fastedDates: StateFlow<Set<String>> = _fastedDates.asStateFlow()

    // Last Read Ayah
    private val _lastReadAyah = MutableStateFlow(prefs.getInt("last_read_ayah", 1))
    val lastReadAyah: StateFlow<Int> = _lastReadAyah.asStateFlow()

    // Bookmarked Ayahs (e.g. "2:255")
    private val _bookmarkedVerses = MutableStateFlow(loadBookmarkedVerses())
    val bookmarkedVerses: StateFlow<Set<String>> = _bookmarkedVerses.asStateFlow()

    // Hisnul Muslim Bookmarks & Favorites (Dua IDs)
    private val _hisnulBookmarks = MutableStateFlow(loadHisnulBookmarks())
    val hisnulBookmarks: StateFlow<Set<String>> = _hisnulBookmarks.asStateFlow()

    private val _hisnulFavorites = MutableStateFlow(loadHisnulFavorites())
    val hisnulFavorites: StateFlow<Set<String>> = _hisnulFavorites.asStateFlow()

    // Quran Reading Mode
    private val _quranReadingMode = MutableStateFlow(loadQuranReadingMode())
    val quranReadingMode: StateFlow<com.example.data.quran.QuranReadingMode> = _quranReadingMode.asStateFlow()

    // Dhikr Reading Mode
    private val _dhikrReadingMode = MutableStateFlow(loadDhikrReadingMode())
    val dhikrReadingMode: StateFlow<com.example.data.dhikr.DhikrReadingMode> = _dhikrReadingMode.asStateFlow()

    // Reminder Toggles
    private val _prayerReminders = MutableStateFlow(loadPrayerReminders())
    val prayerReminders: StateFlow<Map<String, Boolean>> = _prayerReminders.asStateFlow()

    // Typography Loaders & Setters
    private fun loadArabicFont(): ArabicFontOption {
        val id = prefs.getString("arabic_font_id", ArabicFontOption.UTHMANI.id) ?: ArabicFontOption.UTHMANI.id
        return ArabicFontOption.fromId(id)
    }

    fun setArabicFont(option: ArabicFontOption) {
        prefs.edit().putString("arabic_font_id", option.id).apply()
        _arabicFont.value = option
    }

    private fun loadEnglishFont(): EnglishFontOption {
        val id = prefs.getString("english_font_id", EnglishFontOption.NOTO_SANS.id) ?: EnglishFontOption.NOTO_SANS.id
        return EnglishFontOption.fromId(id)
    }

    fun setEnglishFont(option: EnglishFontOption) {
        prefs.edit().putString("english_font_id", option.id).apply()
        _englishFont.value = option
    }

    private fun loadLineSpacing(): LineSpacingOption {
        val id = prefs.getString("line_spacing_id", LineSpacingOption.NORMAL.id) ?: LineSpacingOption.NORMAL.id
        return LineSpacingOption.fromId(id)
    }

    fun setLineSpacing(option: LineSpacingOption) {
        prefs.edit().putString("line_spacing_id", option.id).apply()
        _lineSpacing.value = option
    }

    fun setTranslationFontSize(size: Int) {
        val safeSize = size.coerceIn(12, 28)
        prefs.edit().putInt("translation_font_size", safeSize).apply()
        _translationFontSize.value = safeSize
    }

    // Calendar Loaders & Setters
    private fun loadHijriConvention(): HijriConvention {
        val id = prefs.getString("hijri_convention_id", HijriConvention.UMM_AL_QURA.id) ?: HijriConvention.UMM_AL_QURA.id
        return HijriConvention.fromId(id)
    }

    fun setHijriConvention(convention: HijriConvention) {
        prefs.edit().putString("hijri_convention_id", convention.id).apply()
        _hijriConvention.value = convention
    }

    fun setHijriOffsetDays(offset: Int) {
        val safeOffset = offset.coerceIn(-3, 3)
        prefs.edit().putInt("hijri_offset_days", safeOffset).apply()
        _hijriOffsetDays.value = safeOffset
    }

    private fun loadCalendarEntries(): List<PersonalCalendarEntry> {
        val json = prefs.getString("personal_calendar_entries", null)
        return PersonalCalendarEntry.listFromJsonString(json)
    }

    fun addCalendarEntry(entry: PersonalCalendarEntry) {
        val current = _calendarEntries.value.toMutableList()
        current.removeAll { it.id == entry.id }
        current.add(0, entry)
        val json = PersonalCalendarEntry.listToJsonString(current)
        prefs.edit().putString("personal_calendar_entries", json).apply()
        _calendarEntries.value = current
    }

    fun updateCalendarEntry(entry: PersonalCalendarEntry) {
        addCalendarEntry(entry)
    }

    fun deleteCalendarEntry(id: String) {
        val current = _calendarEntries.value.toMutableList()
        current.removeAll { it.id == id }
        val json = PersonalCalendarEntry.listToJsonString(current)
        prefs.edit().putString("personal_calendar_entries", json).apply()
        _calendarEntries.value = current
    }

    private fun loadQuranReadingMode(): com.example.data.quran.QuranReadingMode {
        val name = prefs.getString("quran_reading_mode", com.example.data.quran.QuranReadingMode.FULL.name)
        return try {
            com.example.data.quran.QuranReadingMode.valueOf(name ?: com.example.data.quran.QuranReadingMode.FULL.name)
        } catch (e: Exception) {
            com.example.data.quran.QuranReadingMode.FULL
        }
    }

    fun setQuranReadingMode(mode: com.example.data.quran.QuranReadingMode) {
        prefs.edit().putString("quran_reading_mode", mode.name).apply()
        _quranReadingMode.value = mode
    }

    private fun loadDhikrReadingMode(): com.example.data.dhikr.DhikrReadingMode {
        val name = prefs.getString("dhikr_reading_mode", com.example.data.dhikr.DhikrReadingMode.FULL.name)
        return try {
            com.example.data.dhikr.DhikrReadingMode.valueOf(name ?: com.example.data.dhikr.DhikrReadingMode.FULL.name)
        } catch (e: Exception) {
            com.example.data.dhikr.DhikrReadingMode.FULL
        }
    }

    fun setDhikrReadingMode(mode: com.example.data.dhikr.DhikrReadingMode) {
        prefs.edit().putString("dhikr_reading_mode", mode.name).apply()
        _dhikrReadingMode.value = mode
    }

    private fun loadCity(): CityLocation {
        val name = prefs.getString("city_name", "Makkah") ?: "Makkah"
        val country = prefs.getString("city_country", "Saudi Arabia") ?: "Saudi Arabia"
        val lat = prefs.getFloat("city_lat", 21.4225f).toDouble()
        val lng = prefs.getFloat("city_lng", 39.8262f).toDouble()
        val tz = prefs.getFloat("city_tz", 3.0f).toDouble()
        return CityLocation(name, country, lat, lng, tz)
    }

    fun setCity(city: CityLocation) {
        prefs.edit()
            .putString("city_name", city.name)
            .putString("city_country", city.country)
            .putFloat("city_lat", city.latitude.toFloat())
            .putFloat("city_lng", city.longitude.toFloat())
            .putFloat("city_tz", city.timeZoneOffsetHours.toFloat())
            .apply()
        _currentCity.value = city
    }

    private fun loadCalculationMethod(): CalculationMethod {
        val name = prefs.getString("calc_method", CalculationMethod.MUSLIM_WORLD_LEAGUE.name)
        return try {
            CalculationMethod.valueOf(name ?: CalculationMethod.MUSLIM_WORLD_LEAGUE.name)
        } catch (e: Exception) {
            CalculationMethod.MUSLIM_WORLD_LEAGUE
        }
    }

    fun setCalculationMethod(method: CalculationMethod) {
        prefs.edit().putString("calc_method", method.name).apply()
        _calculationMethod.value = method
    }

    private fun loadJuristicMethod(): JuristicMethod {
        val name = prefs.getString("juristic_method", JuristicMethod.STANDARD.name)
        return try {
            JuristicMethod.valueOf(name ?: JuristicMethod.STANDARD.name)
        } catch (e: Exception) {
            JuristicMethod.STANDARD
        }
    }

    fun setJuristicMethod(method: JuristicMethod) {
        prefs.edit().putString("juristic_method", method.name).apply()
        _juristicMethod.value = method
    }

    fun setQuranFontSize(size: Int) {
        val safeSize = size.coerceIn(18, 38)
        prefs.edit().putInt("quran_font_size", safeSize).apply()
        _quranFontSize.value = safeSize
    }

    private fun loadBookmarks(): Set<Int> {
        val stringSet = prefs.getStringSet("quran_bookmarks", setOf("1", "67")) ?: emptySet()
        return stringSet.mapNotNull { it.toIntOrNull() }.toSet()
    }

    fun toggleBookmark(surahNumber: Int) {
        val current = _bookmarkedSurahs.value.toMutableSet()
        if (current.contains(surahNumber)) {
            current.remove(surahNumber)
        } else {
            current.add(surahNumber)
        }
        prefs.edit().putStringSet("quran_bookmarks", current.map { it.toString() }.toSet()).apply()
        _bookmarkedSurahs.value = current
    }

    fun setLastReadSurah(surahNumber: Int) {
        prefs.edit().putInt("last_read_surah", surahNumber).apply()
        _lastReadSurah.value = surahNumber
    }

    fun setLastReadPosition(surahNumber: Int, ayahNumber: Int) {
        prefs.edit()
            .putInt("last_read_surah", surahNumber)
            .putInt("last_read_ayah", ayahNumber)
            .apply()
        _lastReadSurah.value = surahNumber
        _lastReadAyah.value = ayahNumber
    }

    private fun loadBookmarkedVerses(): Set<String> {
        return prefs.getStringSet("quran_ayah_bookmarks", setOf("2:255", "18:10")) ?: emptySet()
    }

    fun toggleAyahBookmark(surahNumber: Int, ayahNumber: Int) {
        val key = "$surahNumber:$ayahNumber"
        val current = _bookmarkedVerses.value.toMutableSet()
        if (current.contains(key)) {
            current.remove(key)
        } else {
            current.add(key)
        }
        prefs.edit().putStringSet("quran_ayah_bookmarks", current).apply()
        _bookmarkedVerses.value = current
    }

    private fun loadHisnulBookmarks(): Set<String> {
        return prefs.getStringSet("hisnul_bookmarks", setOf("hisnul_wake_1", "hisnul_istikhara")) ?: emptySet()
    }

    fun toggleHisnulBookmark(id: String) {
        val current = _hisnulBookmarks.value.toMutableSet()
        if (current.contains(id)) {
            current.remove(id)
        } else {
            current.add(id)
        }
        prefs.edit().putStringSet("hisnul_bookmarks", current).apply()
        _hisnulBookmarks.value = current
    }

    private fun loadHisnulFavorites(): Set<String> {
        return prefs.getStringSet("hisnul_favorites", setOf("hisnul_istighfar_1", "hisnul_anxiety_1")) ?: emptySet()
    }

    fun toggleHisnulFavorite(id: String) {
        val current = _hisnulFavorites.value.toMutableSet()
        if (current.contains(id)) {
            current.remove(id)
        } else {
            current.add(id)
        }
        prefs.edit().putStringSet("hisnul_favorites", current).apply()
        _hisnulFavorites.value = current
    }

    fun addTasbihCount(count: Int) {
        val newTotal = _lifetimeTasbih.value + count
        prefs.edit().putLong("lifetime_tasbih", newTotal).apply()
        _lifetimeTasbih.value = newTotal
    }

    fun setHapticEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("haptic_enabled", enabled).apply()
        _hapticEnabled.value = enabled
    }

    private fun todayKey(): String = LocalDate.now().toString()

    private fun loadTodayHabits(): Set<String> {
        val savedDate = prefs.getString("habits_date", "")
        val today = todayKey()
        return if (savedDate == today) {
            prefs.getStringSet("habits_completed", emptySet()) ?: emptySet()
        } else {
            emptySet()
        }
    }

    fun toggleHabit(habitId: String) {
        val today = todayKey()
        val current = _todayHabits.value.toMutableSet()
        if (current.contains(habitId)) {
            current.remove(habitId)
        } else {
            current.add(habitId)
        }
        prefs.edit()
            .putString("habits_date", today)
            .putStringSet("habits_completed", current)
            .apply()
        _todayHabits.value = current
    }

    private fun loadFastedDates(): Set<String> {
        return prefs.getStringSet("fasted_dates", emptySet()) ?: emptySet()
    }

    fun toggleFastedDate(date: LocalDate) {
        val key = date.toString()
        val current = _fastedDates.value.toMutableSet()
        if (current.contains(key)) {
            current.remove(key)
        } else {
            current.add(key)
        }
        prefs.edit().putStringSet("fasted_dates", current).apply()
        _fastedDates.value = current
    }

    fun setMissedFasts(count: Int) {
        val safeCount = count.coerceAtLeast(0)
        prefs.edit().putInt("missed_fasts_count", safeCount).apply()
        _missedFastsCount.value = safeCount
    }

    private fun loadPrayerReminders(): Map<String, Boolean> {
        val defaultMap = mutableMapOf(
            "Fajr" to true,
            "Dhuhr" to true,
            "Asr" to true,
            "Maghrib" to true,
            "Isha" to true,
            "MorningAdhkar" to true,
            "EveningAdhkar" to true,
            "FridayKahf" to true,
            "MondaysThursdaysFasting" to false,
            "SuhoorIftar" to true
        )
        for (key in defaultMap.keys.toList()) {
            defaultMap[key] = prefs.getBoolean("rem_$key", defaultMap[key] ?: true)
        }
        return defaultMap
    }

    fun toggleReminder(key: String, enabled: Boolean) {
        prefs.edit().putBoolean("rem_$key", enabled).apply()
        val map = _prayerReminders.value.toMutableMap()
        map[key] = enabled
        _prayerReminders.value = map
    }
}

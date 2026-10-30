package com.example.data.calendar

import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

enum class CalendarEntryCategory(val displayName: String, val iconName: String) {
    WORSHIP_GOAL("Worship Goal", "Favorite"),
    QURAN_MILESTONE("Qur'an Milestone", "MenuBook"),
    SUNNAH_FAST("Sunnah Fast", "WbSunny"),
    FAMILY_DUA("Family & Du'a", "Groups"),
    PERSONAL_NOTE("Personal Note", "Edit");

    companion object {
        fun fromName(name: String): CalendarEntryCategory =
            entries.find { it.name.equals(name, ignoreCase = true) } ?: PERSONAL_NOTE
    }
}

data class PersonalCalendarEntry(
    val id: String = UUID.randomUUID().toString(),
    val dateIso: String, // "YYYY-MM-DD"
    val title: String,
    val note: String = "",
    val category: CalendarEntryCategory = CalendarEntryCategory.WORSHIP_GOAL,
    val hasReminder: Boolean = false,
    val reminderHour: Int = 8,
    val reminderMinute: Int = 0,
    val timestampCreated: Long = System.currentTimeMillis()
) {
    fun toJson(): JSONObject {
        return JSONObject().apply {
            put("id", id)
            put("dateIso", dateIso)
            put("title", title)
            put("note", note)
            put("category", category.name)
            put("hasReminder", hasReminder)
            put("reminderHour", reminderHour)
            put("reminderMinute", reminderMinute)
            put("timestampCreated", timestampCreated)
        }
    }

    companion object {
        fun fromJson(json: JSONObject): PersonalCalendarEntry {
            return PersonalCalendarEntry(
                id = json.optString("id", UUID.randomUUID().toString()),
                dateIso = json.optString("dateIso", ""),
                title = json.optString("title", ""),
                note = json.optString("note", ""),
                category = CalendarEntryCategory.fromName(json.optString("category", CalendarEntryCategory.WORSHIP_GOAL.name)),
                hasReminder = json.optBoolean("hasReminder", false),
                reminderHour = json.optInt("reminderHour", 8),
                reminderMinute = json.optInt("reminderMinute", 0),
                timestampCreated = json.optLong("timestampCreated", System.currentTimeMillis())
            )
        }

        fun listToJsonString(list: List<PersonalCalendarEntry>): String {
            val array = JSONArray()
            list.forEach { array.put(it.toJson()) }
            return array.toString()
        }

        fun listFromJsonString(jsonString: String?): List<PersonalCalendarEntry> {
            if (jsonString.isNullOrEmpty()) return emptyList()
            return try {
                val array = JSONArray(jsonString)
                val list = mutableListOf<PersonalCalendarEntry>()
                for (i in 0 until array.length()) {
                    val obj = array.optJSONObject(i)
                    if (obj != null) {
                        list.add(fromJson(obj))
                    }
                }
                list.sortedByDescending { it.dateIso }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}

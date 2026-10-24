package com.example.data.habits

data class HabitDefinition(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: String,
    val iconName: String,
    val hadithInspiration: String
)

object HabitData {
    val HABITS = listOf(
        HabitDefinition(
            id = "habit_fajr",
            title = "Fajr Prayer",
            subtitle = "Pray on time and commence the day in Allah's protection",
            category = "Salah",
            iconName = "WbTwilight",
            hadithInspiration = "Whoever prays the dawn prayer is in the protection of Allah. (Sahih Muslim)"
        ),
        HabitDefinition(
            id = "habit_dhuhr",
            title = "Dhuhr Prayer",
            subtitle = "Midday remembrance and pause for the soul",
            category = "Salah",
            iconName = "WbSunny",
            hadithInspiration = "This is an hour when the gates of heaven are opened. (Tirmidhi)"
        ),
        HabitDefinition(
            id = "habit_asr",
            title = "Asr Prayer",
            subtitle = "The middle prayer of tranquility",
            category = "Salah",
            iconName = "FilterDrama",
            hadithInspiration = "Whoever prays the two cool prayers (Fajr and Asr) will enter Paradise. (Bukhari)"
        ),
        HabitDefinition(
            id = "habit_maghrib",
            title = "Maghrib Prayer",
            subtitle = "Sunset gratitude and evening reflection",
            category = "Salah",
            iconName = "NightsStay",
            hadithInspiration = "Pray promptly as the twilight begins."
        ),
        HabitDefinition(
            id = "habit_isha",
            title = "Isha Prayer",
            subtitle = "Concluding the night with peaceful prostration",
            category = "Salah",
            iconName = "Bedtime",
            hadithInspiration = "Whoever prays Isha in congregation, it is as if he prayed half the night. (Muslim)"
        ),
        HabitDefinition(
            id = "habit_quran",
            title = "Daily Quran Reading",
            subtitle = "15 minutes or 1 page of divine revelation",
            category = "Quran",
            iconName = "MenuBook",
            hadithInspiration = "The best of you are those who learn the Quran and teach it. (Bukhari)"
        ),
        HabitDefinition(
            id = "habit_morning_adhkar",
            title = "Morning Adhkar",
            subtitle = "Fortress of remembrance at dawn",
            category = "Remembrance",
            iconName = "Favorite",
            hadithInspiration = "And remember your Lord within yourself in humility and in fear... at morning and evening. (7:205)"
        ),
        HabitDefinition(
            id = "habit_evening_adhkar",
            title = "Evening Adhkar",
            subtitle = "Protection and peace as night falls",
            category = "Remembrance",
            iconName = "Shield",
            hadithInspiration = "Remembrance of Allah is the greatest shield against anxiety."
        ),
        HabitDefinition(
            id = "habit_istighfar",
            title = "100x Istighfar",
            subtitle = "Astaghfirullah — seeking forgiveness and relief",
            category = "Dhikr",
            iconName = "Loop",
            hadithInspiration = "Whoever persists in seeking forgiveness, Allah will make a way out for him from every tight situation. (Abu Dawood)"
        ),
        HabitDefinition(
            id = "habit_salawat",
            title = "100x Salawat",
            subtitle = "Blessings upon the Beloved Prophet Muhammad (ﷺ)",
            category = "Dhikr",
            iconName = "Spa",
            hadithInspiration = "Whoever sends blessings upon me once, Allah will send blessings upon him ten times. (Muslim)"
        ),
        HabitDefinition(
            id = "habit_learning",
            title = "Islamic Learning & Reflection",
            subtitle = "Read a Hadith, tafsir, or beneficial article",
            category = "Knowledge",
            iconName = "Lightbulb",
            hadithInspiration = "Whoever travels a path in search of knowledge, Allah will make easy for him the path to Paradise. (Muslim)"
        ),
        HabitDefinition(
            id = "habit_sadaqah",
            title = "Sadaqah or Act of Kindness",
            subtitle = "Charity, a warm smile, or helping someone",
            category = "Character",
            iconName = "VolunteerActivism",
            hadithInspiration = "Every good deed is a form of charity. Your smile in the face of your brother is charity. (Tirmidhi)"
        )
    )

    val ENCOURAGING_QUOTES = listOf(
        "Take up good deeds only as much as you are able, for the best of deeds are those done consistently, even if small. — Prophet Muhammad (ﷺ)",
        "So remember Me; I will remember you. And be grateful to Me and do not deny Me. — Quran (2:152)",
        "Allah does not burden a soul beyond that it can bear. Be gentle with yourself and begin anew with every breath. — Quran (2:286)",
        "Verily, in the remembrance of Allah do hearts find rest. — Quran (13:28)",
        "Do not despair of the mercy of Allah; indeed, Allah forgives all sins. Truly He is the Oft-Forgiving, Most Merciful. — Quran (39:53)"
    )
}

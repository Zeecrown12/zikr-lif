package com.example.data.ramadan

data class RamadanDayPlan(
    val dayNumber: Int,
    val juzToRead: Int,
    val keySurah: String,
    val theme: String,
    val dailyDua: String,
    val duaMeaning: String,
    val charityGoal: String
)

object RamadanData {

    val RAMADAN_30_DAYS = (1..30).map { day ->
        val isLastTen = day >= 21
        val theme = when {
            day <= 10 -> "First 10 Days: Days of Mercy (Rahmah)"
            day <= 20 -> "Second 10 Days: Days of Forgiveness (Maghfirah)"
            else -> "Last 10 Nights: Seeking Laylatul Qadr & Freedom from the Fire"
        }
        val dailyDua = if (isLastTen) {
            "اللَّهُمَّ إِنَّكَ عَفُوٌّ تُحِبُّ الْعَفْوَ فَاعْفُ عَنِّي"
        } else if (day <= 10) {
            "رَبِّ اغْفِرْ وَارْحَمْ وَأَنْتَ خَيْرُ الرَّاحِمِينَ"
        } else {
            "أَسْتَغْفِرُ اللَّهَ الْعَظِيمَ وَأَتُوبُ إِلَيْهِ"
        }
        val meaning = if (isLastTen) {
            "O Allah, You are Most Forgiving and love to forgive, so forgive me."
        } else if (day <= 10) {
            "My Lord, forgive and have mercy, for You are the best of the merciful."
        } else {
            "I seek forgiveness from Allah the Magnificent and repent to Him."
        }
        val charity = when (day % 5) {
            0 -> "Sponsor an Iftar or feed a fasting person"
            1 -> "Support an orphan or widow in need"
            2 -> "Provide clean water (Sadaqah Jariyah)"
            3 -> "Give secret charity without drawing attention"
            else -> "Send a gift or meal to family or neighbours"
        }
        RamadanDayPlan(
            dayNumber = day,
            juzToRead = day,
            keySurah = "Juz $day",
            theme = theme,
            dailyDua = dailyDua,
            duaMeaning = meaning,
            charityGoal = charity
        )
    }

    val LAST_TEN_NIGHTS_TIPS = listOf(
        "Seek Laylat al-Qadr on the odd nights (21st, 23rd, 25th, 27th, 29th).",
        "Offer Tahajjud / Qiyam al-Layl prayer in the quiet pre-dawn hours.",
        "Abundantly recite: 'Allahumma innaka 'Afuwwun tuhibbul-'afwa fa'fu 'anni'.",
        "Make heartfelt sincere du'a during Suhoor when Allah descends to the lowest heaven.",
        "Give consistent nightly charity so your deed falls on the blessed night better than 1,000 months (83+ years of worship!)."
    )
}

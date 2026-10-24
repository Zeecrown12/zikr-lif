package com.example.data.quran

import android.content.Context
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

enum class QuranReadingMode(val displayName: String) {
    ARABIC_ONLY("Arabic Only"),
    ARABIC_TRANSLATION("Arabic + Translation"),
    ARABIC_TRANSLITERATION("Arabic + Transliteration"),
    FULL("Arabic, Transliteration & Translation"),
    CONTINUOUS("Continuous Reading")
}

data class Ayah(
    val numberInSurah: Int,
    val globalNumber: Int = 0,
    val arabic: String,
    val transliteration: String,
    val translation: String,
    val audioUrl: String = ""
)

data class Surah(
    val number: Int,
    val nameArabic: String,
    val nameEnglish: String,
    val englishMeaning: String,
    val totalVerses: Int,
    val revelationType: String, // "Meccan" or "Medinan"
    val verses: List<Ayah> = emptyList(),
    val audioUrl: String = "https://server8.mp3quran.net/afs/${String.format("%03d", number)}.mp3"
)

object QuranData {

    private val surahCache = mutableMapOf<Int, Surah>()

    val SURAH_METADATA_LIST = listOf(
        Surah(1, "الفاتحة", "Al-Fatihah", "The Opening", 7, "Meccan", verses = AlFatihahVerses),
        Surah(2, "البقرة", "Al-Baqarah", "The Cow", 286, "Medinan"),
        Surah(3, "آل عمران", "Ali 'Imran", "Family of Imran", 200, "Medinan"),
        Surah(4, "النساء", "An-Nisa", "The Women", 176, "Medinan"),
        Surah(5, "المائدة", "Al-Ma'idah", "The Table Spread", 120, "Medinan"),
        Surah(6, "الأنعام", "Al-An'am", "The Cattle", 165, "Meccan"),
        Surah(7, "الأعراف", "Al-A'raf", "The Heights", 206, "Meccan"),
        Surah(8, "الأنفال", "Al-Anfal", "The Spoils of War", 75, "Medinan"),
        Surah(9, "التوبة", "At-Tawbah", "The Repentance", 129, "Medinan"),
        Surah(10, "يونس", "Yunus", "Jonah", 109, "Meccan"),
        Surah(11, "هود", "Hud", "Hud", 123, "Meccan"),
        Surah(12, "يوسف", "Yusuf", "Joseph", 111, "Meccan"),
        Surah(13, "الرعد", "Ar-Ra'd", "The Thunder", 43, "Medinan"),
        Surah(14, "إبراهيم", "Ibrahim", "Abraham", 52, "Meccan"),
        Surah(15, "الحجر", "Al-Hijr", "The Rocky Tract", 99, "Meccan"),
        Surah(16, "النحل", "An-Nahl", "The Bee", 128, "Meccan"),
        Surah(17, "الإسراء", "Al-Isra", "The Night Journey", 111, "Meccan"),
        Surah(18, "الكهف", "Al-Kahf", "The Cave", 110, "Meccan"),
        Surah(19, "مريم", "Maryam", "Mary", 98, "Meccan"),
        Surah(20, "طه", "Ta-Ha", "Ta-Ha", 135, "Meccan"),
        Surah(21, "الأنبياء", "Al-Anbiya", "The Prophets", 112, "Meccan"),
        Surah(22, "الحج", "Al-Hajj", "The Pilgrimage", 78, "Medinan"),
        Surah(23, "المؤمنون", "Al-Mu'minun", "The Believers", 118, "Meccan"),
        Surah(24, "النور", "An-Nur", "The Light", 64, "Medinan"),
        Surah(25, "الفرقان", "Al-Furqan", "The Criterion", 77, "Meccan"),
        Surah(26, "الشعراء", "Ash-Shu'ara", "The Poets", 227, "Meccan"),
        Surah(27, "النمل", "An-Naml", "The Ant", 93, "Meccan"),
        Surah(28, "القصص", "Al-Qasas", "The Stories", 88, "Meccan"),
        Surah(29, "العنكبوت", "Al-'Ankabut", "The Spider", 69, "Meccan"),
        Surah(30, "الروم", "Ar-Rum", "The Romans", 60, "Meccan"),
        Surah(31, "لقمان", "Luqman", "Luqman", 34, "Meccan"),
        Surah(32, "السجدة", "As-Sajdah", "The Prostration", 30, "Meccan"),
        Surah(33, "الأحزاب", "Al-Ahzab", "The Combined Forces", 73, "Medinan"),
        Surah(34, "سبأ", "Saba", "Sheba", 54, "Meccan"),
        Surah(35, "فاطر", "Fatir", "Originator", 45, "Meccan"),
        Surah(36, "يس", "Ya-Sin", "Ya-Sin", 83, "Meccan"),
        Surah(37, "الصافات", "As-Saffat", "Those who set the Ranks", 182, "Meccan"),
        Surah(38, "ص", "Sad", "The Letter Sad", 88, "Meccan"),
        Surah(39, "الزمر", "Az-Zumar", "The Troops", 75, "Meccan"),
        Surah(40, "غافر", "Ghafir", "The Forgiver", 85, "Meccan"),
        Surah(41, "فصلت", "Fussilat", "Explained in Detail", 54, "Meccan"),
        Surah(42, "الشورى", "Ash-Shura", "The Consultation", 53, "Meccan"),
        Surah(43, "الزخرف", "Az-Zukhruf", "The Ornaments of Gold", 89, "Meccan"),
        Surah(44, "الدخان", "Ad-Dukhan", "The Smoke", 59, "Meccan"),
        Surah(45, "الجاثية", "Al-Jathiyah", "The Crouching", 37, "Meccan"),
        Surah(46, "الأحقاف", "Al-Ahqaf", "The Wind-Curved Sandhills", 35, "Meccan"),
        Surah(47, "محمد", "Muhammad", "Muhammad", 38, "Medinan"),
        Surah(48, "الفتح", "Al-Fath", "The Victory", 29, "Medinan"),
        Surah(49, "الحجرات", "Al-Hujurat", "The Rooms", 18, "Medinan"),
        Surah(50, "ق", "Qaf", "The Letter Qaf", 45, "Meccan"),
        Surah(51, "الذاريات", "Adh-Dhariyat", "The Winnowing Winds", 60, "Meccan"),
        Surah(52, "الطور", "At-Tur", "The Mount", 49, "Meccan"),
        Surah(53, "النجم", "An-Najm", "The Star", 62, "Meccan"),
        Surah(54, "القمر", "Al-Qamar", "The Moon", 55, "Meccan"),
        Surah(55, "الرحمن", "Ar-Rahman", "The Beneficent", 78, "Medinan"),
        Surah(56, "الواقعة", "Al-Waqi'ah", "The Inevitable", 96, "Meccan"),
        Surah(57, "الحديد", "Al-Hadid", "The Iron", 29, "Medinan"),
        Surah(58, "المجادلة", "Al-Mujadila", "The Pleading Woman", 22, "Medinan"),
        Surah(59, "الحشر", "Al-Hashr", "The Exile", 24, "Medinan"),
        Surah(60, "الممتحنة", "Al-Mumtahanah", "She that is to be examined", 13, "Medinan"),
        Surah(61, "الصف", "As-Saff", "The Ranks", 14, "Medinan"),
        Surah(62, "الجمعة", "Al-Jumu'ah", "The Congregation (Friday)", 11, "Medinan"),
        Surah(63, "المنافقون", "Al-Munafiqun", "The Hypocrites", 11, "Medinan"),
        Surah(64, "التغابن", "At-Taghabun", "The Mutual Disillusion", 18, "Medinan"),
        Surah(65, "الطلاق", "At-Talaq", "The Divorce", 12, "Medinan"),
        Surah(66, "التحريم", "At-Tahrim", "The Prohibition", 12, "Medinan"),
        Surah(67, "الملك", "Al-Mulk", "The Sovereignty", 30, "Meccan"),
        Surah(68, "القلم", "Al-Qalam", "The Pen", 52, "Meccan"),
        Surah(69, "الحاقة", "Al-Haqqah", "The Reality", 52, "Meccan"),
        Surah(70, "المعارج", "Al-Ma'arij", "The Ascending Stairways", 44, "Meccan"),
        Surah(71, "نوح", "Nuh", "Noah", 28, "Meccan"),
        Surah(72, "الجن", "Al-Jinn", "The Jinn", 28, "Meccan"),
        Surah(73, "المزمل", "Al-Muzzammil", "The Enshrouded One", 20, "Meccan"),
        Surah(74, "المدثر", "Al-Muddaththir", "The Cloaked One", 56, "Meccan"),
        Surah(75, "القيامة", "Al-Qiyamah", "The Resurrection", 40, "Meccan"),
        Surah(76, "الإنسان", "Al-Insan", "The Human", 31, "Medinan"),
        Surah(77, "المرسلات", "Al-Mursalat", "The Emissaries", 50, "Meccan"),
        Surah(78, "النبأ", "An-Naba", "The Tidings", 40, "Meccan"),
        Surah(79, "النازعات", "An-Nazi'at", "Those who drag forth", 46, "Meccan"),
        Surah(80, "عبس", "'Abasa", "He Frowned", 42, "Meccan"),
        Surah(81, "التكوير", "At-Takwir", "The Overthrowing", 29, "Meccan"),
        Surah(82, "الانفطار", "Al-Infitar", "The Cleaving", 19, "Meccan"),
        Surah(83, "المطففين", "Al-Mutaffifin", "The Defrauding", 36, "Meccan"),
        Surah(84, "الانشقاق", "Al-Inshiqaq", "The Splitting Asunder", 25, "Meccan"),
        Surah(85, "البروج", "Al-Buruj", "The Mansions of the Stars", 22, "Meccan"),
        Surah(86, "الطارق", "At-Tariq", "The Morning Star", 17, "Meccan"),
        Surah(87, "الأعلى", "Al-A'la", "The Most High", 19, "Meccan"),
        Surah(88, "الغاشية", "Al-Ghashiyah", "The Overwhelming", 26, "Meccan"),
        Surah(89, "الفجر", "Al-Fajr", "The Dawn", 30, "Meccan"),
        Surah(90, "البلد", "Al-Balad", "The City", 20, "Meccan"),
        Surah(91, "الشمس", "Ash-Shams", "The Sun", 15, "Meccan"),
        Surah(92, "الليل", "Al-Layl", "The Night", 21, "Meccan"),
        Surah(93, "الضحى", "Ad-Duha", "The Morning Hours", 11, "Meccan"),
        Surah(94, "الشرح", "Ash-Sharh", "The Relief", 8, "Meccan"),
        Surah(95, "التين", "At-Tin", "The Fig", 8, "Meccan"),
        Surah(96, "العلق", "Al-'Alaq", "The Clot", 19, "Meccan"),
        Surah(97, "القدر", "Al-Qadr", "The Night of Decree", 5, "Meccan"),
        Surah(98, "البينة", "Al-Bayyinah", "The Clear Evidence", 8, "Medinan"),
        Surah(99, "الزلزلة", "Az-Zalzalah", "The Earthquake", 8, "Medinan"),
        Surah(100, "العاديات", "Al-'Adiyat", "The Courser", 11, "Meccan"),
        Surah(101, "القارعة", "Al-Qari'ah", "The Calamity", 11, "Meccan"),
        Surah(102, "التكاثر", "At-Takathur", "The Rivalry in World Increase", 8, "Meccan"),
        Surah(103, "العصر", "Al-'Asr", "The Declining Day", 3, "Meccan"),
        Surah(104, "الهمزة", "Al-Humazah", "The Traducer", 9, "Meccan"),
        Surah(105, "الفيل", "Al-Fil", "The Elephant", 5, "Meccan"),
        Surah(106, "قريش", "Quraysh", "Quraysh", 4, "Meccan"),
        Surah(107, "الماعون", "Al-Ma'un", "Small Kindnesses", 7, "Meccan"),
        Surah(108, "الكوثر", "Al-Kawthar", "The Abundance", 3, "Meccan"),
        Surah(109, "الكافرون", "Al-Kafirun", "The Disbelievers", 6, "Meccan"),
        Surah(110, "النصر", "An-Nasr", "The Help", 3, "Medinan"),
        Surah(111, "المسد", "Al-Masad", "The Palm Fiber", 5, "Meccan"),
        Surah(112, "الإخلاص", "Al-Ikhlas", "The Sincerity", 4, "Meccan"),
        Surah(113, "الفلق", "Al-Falaq", "The Daybreak", 5, "Meccan"),
        Surah(114, "الناس", "An-Nas", "Mankind", 6, "Meccan")
    )

    fun getSurahByNumber(number: Int): Surah? {
        return SURAH_METADATA_LIST.find { it.number == number }
    }

    /**
     * Loads the complete Surah with all verses from local assets.
     * Caches in memory for fast performance.
     */
    fun loadSurah(context: Context, surahNumber: Int): Surah {
        surahCache[surahNumber]?.let { return it }

        try {
            val inputStream = context.assets.open("quran/surah_$surahNumber.json")
            val reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
            val jsonString = reader.use { it.readText() }
            val obj = JSONObject(jsonString)

            val number = obj.getInt("number")
            val nameArabic = obj.getString("nameArabic")
            val nameEnglish = obj.getString("nameEnglish")
            val englishMeaning = obj.getString("englishMeaning")
            val totalVerses = obj.getInt("totalVerses")
            val revelationType = obj.getString("revelationType")
            val audioUrl = obj.optString("audioUrl", "https://server8.mp3quran.net/afs/${String.format("%03d", number)}.mp3")

            val ayahsArray = obj.getJSONArray("ayahs")
            val ayahsList = ArrayList<Ayah>(ayahsArray.length())
            for (i in 0 until ayahsArray.length()) {
                val aObj = ayahsArray.getJSONObject(i)
                ayahsList.add(
                    Ayah(
                        numberInSurah = aObj.getInt("numberInSurah"),
                        globalNumber = aObj.optInt("globalNumber", 0),
                        arabic = aObj.getString("arabic"),
                        translation = aObj.getString("translation"),
                        transliteration = aObj.getString("transliteration"),
                        audioUrl = aObj.optString("audioUrl", "")
                    )
                )
            }

            val loadedSurah = Surah(
                number = number,
                nameArabic = nameArabic,
                nameEnglish = nameEnglish,
                englishMeaning = englishMeaning,
                totalVerses = totalVerses,
                revelationType = revelationType,
                verses = ayahsList,
                audioUrl = audioUrl
            )
            surahCache[surahNumber] = loadedSurah
            return loadedSurah
        } catch (e: Exception) {
            val fallback = getSurahByNumber(surahNumber) ?: Surah(
                number = surahNumber,
                nameArabic = "سورة",
                nameEnglish = "Surah $surahNumber",
                englishMeaning = "The Chapter",
                totalVerses = 7,
                revelationType = "Meccan",
                verses = AlFatihahVerses
            )
            return fallback
        }
    }
}

val AlFatihahVerses = listOf(
    Ayah(1, 1, "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ", "Bismillaahir Rahmaanir Raheem", "In the name of Allah, the Entirely Merciful, the Especially Merciful.", "https://cdn.islamic.network/quran/audio/128/ar.alafasy/1.mp3"),
    Ayah(2, 2, "ٱلْحَمْدُ لِلَّهِ رَبِّ ٱلْعَٰلَمِينَ", "Alhamdu lillaahi Rabbil 'aalameen", "[All] praise is [due] to Allah, Lord of the worlds -", "https://cdn.islamic.network/quran/audio/128/ar.alafasy/2.mp3"),
    Ayah(3, 3, "ٱلرَّحْمَٰنِ ٱلرَّحِيمِ", "Ar-Rahmaanir-Raheem", "The Entirely Merciful, the Especially Merciful,", "https://cdn.islamic.network/quran/audio/128/ar.alafasy/3.mp3"),
    Ayah(4, 4, "مَٰلِكِ يَوْمِ ٱلدِّينِ", "Maaliki Yawmid-Deen", "Sovereign of the Day of Recompense.", "https://cdn.islamic.network/quran/audio/128/ar.alafasy/4.mp3"),
    Ayah(5, 5, "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ", "Iyyaaka na'budu wa lyyaaka nasta'een", "It is You we worship and You we ask for help.", "https://cdn.islamic.network/quran/audio/128/ar.alafasy/5.mp3"),
    Ayah(6, 6, "ٱهْدِنَا ٱلصِّرَٰطَ ٱلْمُسْتَقِيمَ", "Ihdinas-Siraatal-Mustaqeem", "Guide us to the straight path -", "https://cdn.islamic.network/quran/audio/128/ar.alafasy/6.mp3"),
    Ayah(7, 7, "صِرَٰطَ ٱلَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ ٱلْمَغْضُوبِ عَلَيْهِمْ وَلَا ٱلضَّآلِّينَ", "Siraatal-lazeena an'amta 'alaihim ghayril-maghdoobi 'alaihim wa lad-daaalleen", "The path of those upon whom You have bestowed favor, not of those who have evoked [Your] anger or of those who are astray.", "https://cdn.islamic.network/quran/audio/128/ar.alafasy/7.mp3")
)

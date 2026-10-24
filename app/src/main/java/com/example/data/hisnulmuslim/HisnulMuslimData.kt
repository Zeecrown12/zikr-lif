package com.example.data.hisnulmuslim

enum class HisnulCategory(val displayName: String, val iconName: String) {
    ALL("All Duas", "MenuBook"),
    DAILY_ROUTINE("Daily & Sleep", "WbTwilight"),
    PRAYER_ABLUTION("Prayer & Wudu", "Mosque"),
    MORNING_EVENING("Morning & Evening", "NightsStay"),
    FOOD_TRAVEL("Food & Travel", "DirectionsCar"),
    TRIALS_PROTECTION("Distress & Protection", "Shield"),
    SICKNESS_HEALING("Sickness & Ruqyah", "Healing"),
    FAMILY_SOCIAL("Family & Social", "FamilyRestroom"),
    REPENTANCE_PRAISE("Praise & Repentance", "Favorite")
}

data class HisnulChapter(
    val id: Int,
    val titleEnglish: String,
    val titleArabic: String,
    val category: HisnulCategory,
    val description: String = ""
)

data class HisnulDua(
    val id: String,
    val chapterId: Int,
    val chapterTitle: String,
    val occasion: String,
    val title: String,
    val arabic: String,
    val transliteration: String,
    val translation: String,
    val reference: String,
    val repetition: String = "1x",
    val audioUrl: String = ""
)

object HisnulMuslimData {

    val CHAPTERS = listOf(
        HisnulChapter(1, "When Waking Up", "أذكار الاستيقاظ من النوم", HisnulCategory.DAILY_ROUTINE, "Supplications upon opening one's eyes and waking from sleep"),
        HisnulChapter(2, "Putting on Clothes", "دعاء لبس الثوب", HisnulCategory.DAILY_ROUTINE, "Supplication when wearing everyday or new garments"),
        HisnulChapter(3, "Entering & Leaving Restroom", "دعاء دخول الخلاء والخروج منه", HisnulCategory.DAILY_ROUTINE, "Sunnah manners and protection when entering and leaving the lavatory"),
        HisnulChapter(4, "Before & After Wudu (Ablution)", "الذكر قبل الوضوء وبعد الفراغ منه", HisnulCategory.PRAYER_ABLUTION, "Spiritual purification before prayer"),
        HisnulChapter(5, "Leaving & Entering the House", "الذكر عند الخروج من المنزل ودخوله", HisnulCategory.DAILY_ROUTINE, "Entrusting affairs to Allah when departing or returning home"),
        HisnulChapter(6, "Mosque: Going, Entering & Leaving", "دعاء الذهاب إلى المسجد ودخوله والخروج منه", HisnulCategory.PRAYER_ABLUTION, "Asking for light and divine mercy in the house of Allah"),
        HisnulChapter(7, "Opening Duas of Prayer (Istiftah)", "دعاء الاستفتاح في الصلاة", HisnulCategory.PRAYER_ABLUTION, "Commencing the formal salah after Takbeeratul Ihram"),
        HisnulChapter(8, "During Ruku' (Bowing)", "دعاء الركوع والرفع منه", HisnulCategory.PRAYER_ABLUTION, "Exalting Allah's majesty while bowing in humility"),
        HisnulChapter(9, "During Sujood (Prostration)", "دعاء السجود والجلسة بين السجدتين", HisnulCategory.PRAYER_ABLUTION, "The closest a servant is to their Lord"),
        HisnulChapter(10, "Tashahhud & Durood on Prophet", "التشهد والصلاة على النبي ﷺ", HisnulCategory.PRAYER_ABLUTION, "Honoring Allah and sending blessings on the Messenger"),
        HisnulChapter(11, "Before Salam & After Prayer", "الدعاء قبل السلام والأذكار بعده", HisnulCategory.PRAYER_ABLUTION, "Protection from trials and standard post-prayer adhkar"),
        HisnulChapter(12, "Seeking Guidance (Istikharah)", "دعاء صلاة الاستخارة", HisnulCategory.PRAYER_ABLUTION, "Consulting Allah in crucial decisions and matters of life"),
        HisnulChapter(13, "Morning & Evening Remembrance", "أذكار الصباح والمساء", HisnulCategory.MORNING_EVENING, "Daily fortress of protection throughout day and night"),
        HisnulChapter(14, "Before Sleeping & Restless Night", "أذكار النوم والفزع منه", HisnulCategory.DAILY_ROUTINE, "Peaceful rest in Allah's guardianship"),
        HisnulChapter(15, "Before & After Eating & Drinking", "دعاء الطعام والشراب والضيف", HisnulCategory.FOOD_TRAVEL, "Gratitude for sustenance and blessings for the host"),
        HisnulChapter(16, "Fasting & Breaking Fast (Iftar)", "دعاء الصيام والإفطار", HisnulCategory.FOOD_TRAVEL, "The answered supplication of the fasting believer"),
        HisnulChapter(17, "Travel, Vehicle & Returning", "دعاء الركوب والسفر والرجوع", HisnulCategory.FOOD_TRAVEL, "Safe journeying, transport gratitude, and arrival supplications"),
        HisnulChapter(18, "Weather: Wind, Thunder & Rain", "دعاء الريح والرعد ونزول المطر", HisnulCategory.FOOD_TRAVEL, "Reflecting on nature and seeking beneficial rainfall"),
        HisnulChapter(19, "Distress, Grief & Anxiety (Karb)", "دعاء الكرب والهم والحزن", HisnulCategory.TRIALS_PROTECTION, "Relief from heavy hearts, sorrow, anxiety, and depression"),
        HisnulChapter(20, "Protection from Evil Eye & Harm", "التعويذات والوقاية من العين والحسد", HisnulCategory.TRIALS_PROTECTION, "Prophetic shielding against jealousy, harm, and shayateen"),
        HisnulChapter(21, "Sickness, Ruqyah & Visiting Sick", "دعاء عيادة المريض والرقى الشرعية", HisnulCategory.SICKNESS_HEALING, "Healing duas, comfort for the ill, and divine recovery"),
        HisnulChapter(22, "Repaying Debts & Financial Hardship", "دعاء قضاء الدين", HisnulCategory.TRIALS_PROTECTION, "Freedom from debt and seeking halal provision"),
        HisnulChapter(23, "Facing Calamity & Visiting Graves", "دعاء المصيبة وزيارة القبور", HisnulCategory.FAMILY_SOCIAL, "Patience during bereavement and greeting the deceased"),
        HisnulChapter(24, "Repentance & Sayyid al-Istighfar", "سيد الاستغفار والتوبة", HisnulCategory.REPENTANCE_PRAISE, "The master supplication for complete forgiveness"),
        HisnulChapter(25, "Family, Children & Parents", "دعاء للأهل والأولاد والوالدين", HisnulCategory.FAMILY_SOCIAL, "Righteous descendants, marital harmony, and mercy for parents"),
        HisnulChapter(26, "Market, Sneezing & Shield from Shirk", "دعاء السوق والعطاس والحذر من الشرك", HisnulCategory.FAMILY_SOCIAL, "Remembrance in busy places, social courtesies, and pure tawheed")
    )

    val DUAS = listOf(
        // Chapter 1: Waking Up
        HisnulDua(
            id = "hisnul_wake_1",
            chapterId = 1,
            chapterTitle = "When Waking Up",
            occasion = "Waking Up",
            title = "Praise to the One who Gave Us Life",
            arabic = "الْحَمْدُ لِلَّهِ الَّذِي أَحْيَانَا بَعْدَ مَا أَمَاتَنَا وَإِلَيْهِ النُّشُورُ",
            transliteration = "Alhamdu lillahil-lathee ahyana ba'da ma amatana wa-ilayhin-nushoor.",
            translation = "All praise is for Allah who gave us life after having taken it from us and unto Him is the resurrection.",
            reference = "Sahih al-Bukhari 6324, Sahih Muslim 2711",
            repetition = "1x"
        ),
        HisnulDua(
            id = "hisnul_wake_2",
            chapterId = 1,
            chapterTitle = "When Waking Up",
            occasion = "Waking Up in the Night",
            title = "Dhikr when Awakening during Night",
            arabic = "لاَ إِلَهَ إِلاَّ اللَّهُ وَحْدَهُ لاَ شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ، وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ، سُبْحَانَ اللَّهِ، وَالْحَمْدُ لِلَّهِ، وَلاَ إِلَهَ إِلاَّ اللَّهُ، وَاللَّهُ أَكْبَرُ، وَلاَ حَوْلَ وَلاَ قُوَّةَ إِلاَّ بِاللَّهِ الْعَلِيِّ الْعَظِيمِ",
            transliteration = "La ilaha illallahu wahdahu la shareeka lah, lahul-mulku wa lahul-hamd, wa Huwa 'ala kulli shay-in qadeer. Subhanallah, wal-hamdu lillah, wa la ilaha illallah, wallahu akbar, wa la hawla wa la quwwata illa billahil-'Aliyyil-'Atheem.",
            translation = "None has the right to be worshipped except Allah alone, without partner. To Him belongs all sovereignty and praise, and He is over all things omnipotent. Glorified is Allah, praise be to Allah, none is worthy of worship but Allah, Allah is the Greatest, and there is no power or strength except through Allah, the Most High, the Supreme.",
            reference = "Sahih al-Bukhari 1154",
            repetition = "1x"
        ),
        HisnulDua(
            id = "hisnul_wake_3",
            chapterId = 1,
            chapterTitle = "When Waking Up",
            occasion = "Waking Up",
            title = "Gratitude for Sound Health & Soul",
            arabic = "الْحَمْدُ لِلَّهِ الَّذِي عَافَانِي فِي جَسَدِي، وَرَدَّ عَلَيَّ رُوحِي، وَأَذِنَ لِي بِذِكْرِهِ",
            transliteration = "Alhamdu lillahil-lathee 'afanee fee jasadee, wa radda 'alayya roohee, wa athina lee bi-thikrih.",
            translation = "All praise is for Allah who restored health to my body, returned my soul to me, and permitted me to remember Him.",
            reference = "Jami` at-Tirmidhi 3401 (Hasan)",
            repetition = "1x"
        ),

        // Chapter 2: Putting on Clothes
        HisnulDua(
            id = "hisnul_dress_1",
            chapterId = 2,
            chapterTitle = "Putting on Clothes",
            occasion = "Clothing",
            title = "When Putting on a Garment",
            arabic = "الْحَمْدُ لِلَّهِ الَّذِي كَسَانِي هَذَا (الثَّوْبَ) وَرَزَقَنِيهِ مِنْ غَيْرِ حَوْلٍ مِنِّي وَلاَ قُوَّةٍ",
            transliteration = "Alhamdu lillahil-lathee kasanee hatha (ath-thawba) wa razaqaneehi min ghayri hawlin minnee wa la quwwah.",
            translation = "Praise be to Allah who has clothed me with this garment and provided it for me without any power or might on my part.",
            reference = "Sunan Abi Dawud 4023, Jami` at-Tirmidhi 3458 (Hasan)",
            repetition = "1x"
        ),
        HisnulDua(
            id = "hisnul_dress_2",
            chapterId = 2,
            chapterTitle = "Putting on Clothes",
            occasion = "New Garments",
            title = "When Wearing New Clothes",
            arabic = "اللَّهُمَّ لَكَ الْحَمْدُ أَنْتَ كَسَوْتَنِيهِ، أَسْأَلُكَ مِنْ خَيْرِهِ وَخَيْرِ مَا صُنِعَ لَهُ، وَأَعُوذُ بِكَ مِنْ شَرِّهِ وَشَرِّ مَا صُنِعَ لَهُ",
            transliteration = "Allahumma lakal-hamdu Anta kasawtaneeh, as-aluka min khayrihi wa khayri ma suni'a lah, wa a'oothu bika min sharrihi wa sharri ma suni'a lah.",
            translation = "O Allah, to You belongs all praise; You have clothed me with it. I ask You for its good and the good for which it was made, and I seek refuge in You from its evil and the evil for which it was made.",
            reference = "Sunan Abi Dawud 4020, Jami` at-Tirmidhi 1767",
            repetition = "1x"
        ),

        // Chapter 3: Entering & Leaving Restroom
        HisnulDua(
            id = "hisnul_toilet_1",
            chapterId = 3,
            chapterTitle = "Entering & Leaving Restroom",
            occasion = "Entering the Restroom",
            title = "Protection when Entering the Lavatory",
            arabic = "بِسْمِ اللَّهِ، اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْخُبْثِ وَالْخَبَائِثِ",
            transliteration = "[Bismillah] Allahumma innee a'oothu bika minal-khubthi wal-khaba-ith.",
            translation = "[In the name of Allah] O Allah, I seek refuge in You from all foul male and female evil spirits.",
            reference = "Sahih al-Bukhari 142, Sahih Muslim 375",
            repetition = "1x"
        ),
        HisnulDua(
            id = "hisnul_toilet_2",
            chapterId = 3,
            chapterTitle = "Entering & Leaving Restroom",
            occasion = "Leaving the Restroom",
            title = "Seeking Forgiveness when Exiting",
            arabic = "غُفْرَانَكَ",
            transliteration = "Ghufranaka.",
            translation = "I seek Your forgiveness, O Allah.",
            reference = "Sunan Abi Dawud 30, Jami` at-Tirmidhi 7",
            repetition = "1x"
        ),

        // Chapter 4: Wudu (Ablution)
        HisnulDua(
            id = "hisnul_wudu_1",
            chapterId = 4,
            chapterTitle = "Before & After Wudu",
            occasion = "Before Ablution",
            title = "Commencing Wudu",
            arabic = "بِسْمِ اللَّهِ",
            transliteration = "Bismillah.",
            translation = "In the name of Allah.",
            reference = "Sunan Abi Dawud 101, Sunan Ibn Majah 397",
            repetition = "1x"
        ),
        HisnulDua(
            id = "hisnul_wudu_2",
            chapterId = 4,
            chapterTitle = "Before & After Wudu",
            occasion = "After Ablution",
            title = "Shahadah & Purification Supplication",
            arabic = "أَشْهَدُ أَنْ لاَ إِلَهَ إِلاَّ اللَّهُ وَحْدَهُ لاَ شَرِيكَ لَهُ، وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ، اللَّهُمَّ اجْعَلْنِي مِنَ التَّوَّابِينَ وَاجْعَلْنِي مِنَ الْمُتَطَهِّرِينَ",
            transliteration = "Ash-hadu alla ilaha illallahu wahdahu la shareeka lah, wa ash-hadu anna Muhammadan 'abduhu wa rasooluh. Allahummaj'alnee minat-tawwabeena waj'alnee minal-mutatahhireen.",
            translation = "I bear witness that none has the right to be worshipped except Allah alone without partner, and I bear witness that Muhammad is His servant and messenger. O Allah, make me among those who constantly repent and make me among those who purify themselves.",
            reference = "Sahih Muslim 234, Jami` at-Tirmidhi 55",
            repetition = "1x"
        ),

        // Chapter 5: Leaving & Entering House
        HisnulDua(
            id = "hisnul_house_1",
            chapterId = 5,
            chapterTitle = "Leaving & Entering the House",
            occasion = "Leaving Home",
            title = "Trust in Allah when Leaving the House",
            arabic = "بِسْمِ اللَّهِ تَوَكَّلْتُ عَلَى اللَّهِ، وَلاَ حَوْلَ وَلاَ قُوَّةَ إِلاَّ بِاللَّهِ",
            transliteration = "Bismillahi tawakkaltu 'alallah, wa la hawla wa la quwwata illa billah.",
            translation = "In the name of Allah, I place my trust in Allah; there is no power or might except with Allah.",
            reference = "Sunan Abi Dawud 5095, Jami` at-Tirmidhi 3426",
            repetition = "1x"
        ),
        HisnulDua(
            id = "hisnul_house_2",
            chapterId = 5,
            chapterTitle = "Leaving & Entering the House",
            occasion = "Leaving Home",
            title = "Seeking Refuge from Straying or Oppression",
            arabic = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ أَنْ أَضِلَّ أَوْ أُضَلَّ، أَوْ أَزِلَّ أَوْ أُزَلَّ، أَوْ أَظْلِمَ أَوْ أُظْلَمَ، أَوْ أَجْهَلَ أَوْ يُجْهَلَ عَلَيَّ",
            transliteration = "Allahumma innee a'oothu bika an adilla aw udall, aw azilla aw uzall, aw athlima aw uthlam, aw ajhala aw yujhala 'alayya.",
            translation = "O Allah, I seek refuge in You lest I should stray or be led astray, or slip or be tripped, or oppress or be oppressed, or behave ignorantly or be treated with ignorance.",
            reference = "Sunan Abi Dawud 5094, Jami` at-Tirmidhi 3427",
            repetition = "1x"
        ),
        HisnulDua(
            id = "hisnul_house_3",
            chapterId = 5,
            chapterTitle = "Leaving & Entering the House",
            occasion = "Entering Home",
            title = "Supplication upon Entering Home",
            arabic = "بِسْمِ اللَّهِ وَلَجْنَا، وَبِسْمِ اللَّهِ خَرَجْنَا، وَعَلَى رَبِّنَا تَوَكَّلْنَا",
            transliteration = "Bismillahi walajna, wa bismillahi kharajna, wa 'ala Rabbina tawakkalna.",
            translation = "In the name of Allah we enter, and in the name of Allah we leave, and upon our Lord we rely. (Then greet the household).",
            reference = "Sunan Abi Dawud 5096",
            repetition = "1x"
        ),

        // Chapter 6: Mosque Duas
        HisnulDua(
            id = "hisnul_mosque_1",
            chapterId = 6,
            chapterTitle = "Mosque: Going, Entering & Leaving",
            occasion = "Walking to Mosque",
            title = "Supplication for Divine Light on Way to Mosque",
            arabic = "اللَّهُمَّ اجْعَلْ فِي قَلْبِي نُورًا، وَفِي لِسَانِي نُورًا، وَفِي سَمْعِي نُورًا، وَفِي بَصَرِي نُورًا، وَمِنْ فَوْقِي نُورًا، وَمِنْ تَحْتِي نُورًا، وَعَنْ يَمِينِي نُورًا، وَعَنْ شِمَالِي نُورًا، وَمِنْ أَمَامِي نُورًا، وَمِنْ خَلْفِي نُورًا، وَاجْعَلْ لِي فِي نَفْسِي نُورًا، وَأَعْظِمْ لِي نُورًا",
            transliteration = "Allahummaj'al fee qalbee noora, wa fee lisanee noora, wa fee sam'ee noora, wa fee basaree noora, wa min fawqee noora, wa min tahtee noora, wa 'an yameenee noora, wa 'an shimalee noora, wa min amamee noora, wa min khalfee noora, waj'al fee nafsee noora, wa a'thim lee noora.",
            translation = "O Allah, place light in my heart, light on my tongue, light in my hearing, light in my sight, light above me, light below me, light to my right, light to my left, light in front of me, light behind me, light within myself, and magnify for me light.",
            reference = "Sahih al-Bukhari 6316, Sahih Muslim 763",
            repetition = "1x"
        ),
        HisnulDua(
            id = "hisnul_mosque_2",
            chapterId = 6,
            chapterTitle = "Mosque: Going, Entering & Leaving",
            occasion = "Entering Mosque",
            title = "Entering the Mosque",
            arabic = "أَعُوذُ بِاللَّهِ الْعَظِيمِ، وَبِوَجْهِهِ الْكَرِيمِ، وَسُلْطَانِهِ الْقَدِيمِ، مِنَ الشَّيْطَانِ الرَّجِيمِ. بِسْمِ اللَّهِ، وَالصَّلاَةُ وَالسَّلاَمُ عَلَى رَسُولِ اللَّهِ، اللَّهُمَّ افْتَحْ لِي أَبْوَابَ رَحْمَتِكَ",
            transliteration = "A'oothu billahil-'Atheem, wa bi-wajhihil-kareem, wa sultanihil-qadeem, minash-shaytanir-rajeem. Bismillah, was-salatu was-salamu 'ala Rasoolillah. Allahummaftah lee abwaba rahmatik.",
            translation = "I seek refuge in Allah the Supreme, and in His Noble Face, and His Eternal Sovereignty from Satan the accursed. In the name of Allah, and prayers and peace be upon the Messenger of Allah. O Allah, open for me the gates of Your mercy.",
            reference = "Sunan Abi Dawud 465, Sahih Muslim 713",
            repetition = "1x"
        ),
        HisnulDua(
            id = "hisnul_mosque_3",
            chapterId = 6,
            chapterTitle = "Mosque: Going, Entering & Leaving",
            occasion = "Leaving Mosque",
            title = "Leaving the Mosque",
            arabic = "بِسْمِ اللَّهِ وَالصَّلاَةُ وَالسَّلاَمُ عَلَى رَسُولِ اللَّهِ، اللَّهُمَّ إِنِّي أَسْأَلُكَ مِنْ فَضْلِكَ، اللَّهُمَّ اعْصِمْنِي مِنَ الشَّيْطَانِ الرَّجِيمِ",
            transliteration = "Bismillahi was-salatu was-salamu 'ala Rasoolillah, Allahumma innee as-aluka min fadlik, Allahumma'-simnee minash-shaytanir-rajeem.",
            translation = "In the name of Allah, and blessings and peace be upon the Messenger of Allah. O Allah, I ask You from Your bounty; O Allah, protect me from Satan the accursed.",
            reference = "Sahih Muslim 713, Sunan Ibn Majah 773",
            repetition = "1x"
        ),

        // Chapter 7: Istiftah
        HisnulDua(
            id = "hisnul_istiftah_1",
            chapterId = 7,
            chapterTitle = "Opening Duas of Prayer",
            occasion = "Commencing Salah",
            title = "Subhanakallahumma (Glory be to You O Allah)",
            arabic = "سُبْحَانَكَ اللَّهُمَّ وَبِحَمْدِكَ، وَتَبَارَكَ اسْمُكَ، وَتَعَالَى جَدُّكَ، وَلاَ إِلَهَ غَيْرُكَ",
            transliteration = "Subhanakal-lahumma wa bihamdika wa tabarakasmuka wa ta'ala jadduka wa la ilaha ghayruk.",
            translation = "Glory be to You, O Allah, and praise. Blessed is Your Name, exalted is Your Majesty, and there is no deity worthy of worship besides You.",
            reference = "Sunan Abi Dawud 775, Jami` at-Tirmidhi 243",
            repetition = "1x"
        ),
        HisnulDua(
            id = "hisnul_istiftah_2",
            chapterId = 7,
            chapterTitle = "Opening Duas of Prayer",
            occasion = "Commencing Salah",
            title = "Distance from Sins Supplication",
            arabic = "اللَّهُمَّ بَاعِدْ بَيْنِي وَبَيْنَ خَطَايَايَ كَمَا بَاعَدْتَ بَيْنَ الْمَشْرِقِ وَالْمَغْرِبِ، اللَّهُمَّ نَقِّنِي مِنْ خَطَايَايَ كَمَا يُنَقَّى الثَّوْبُ الأَبْيَضُ مِنَ الدَّنَسِ، اللَّهُمَّ اغْسِلْنِي مِنْ خَطَايَايَ بِالثَّلْجِ وَالْمَاءِ وَالْبَرَدِ",
            transliteration = "Allahumma ba'id baynee wa bayna khatayaya kama ba'adta baynal-mashriqi wal-maghrib. Allahumma naqqinee min khatayaya kama yunaqqath-thawbul-abyadu minad-danas. Allahummagh-silnee min khatayaya bith-thalji wal-ma-i wal-barad.",
            translation = "O Allah, distance me from my sins as You have distanced the East from the West. O Allah, cleanse me of my sins as a white garment is cleansed from stains. O Allah, wash away my sins with snow, water, and hail.",
            reference = "Sahih al-Bukhari 744, Sahih Muslim 598",
            repetition = "1x"
        ),

        // Chapter 8: Ruku'
        HisnulDua(
            id = "hisnul_ruku_1",
            chapterId = 8,
            chapterTitle = "During Ruku' (Bowing)",
            occasion = "Bowing in Prayer",
            title = "Glorifying the Most Supreme in Ruku'",
            arabic = "سُبْحَانَ رَبِّيَ الْعَظِيمِ",
            transliteration = "Subhana Rabbiyal-'Atheem.",
            translation = "Glory be to my Lord, the Supreme.",
            reference = "Sunan Abi Dawud 871, Jami` at-Tirmidhi 261",
            repetition = "3x"
        ),
        HisnulDua(
            id = "hisnul_ruku_2",
            chapterId = 8,
            chapterTitle = "During Ruku' (Bowing)",
            occasion = "Rising from Bowing",
            title = "Abundant Praise when Standing Upright",
            arabic = "رَبَّنَا وَلَكَ الْحَمْدُ، حَمْدًا كَثِيرًا طَيِّبًا مُبَارَكًا فِيهِ",
            transliteration = "Rabbana wa lakal-hamd, hamdan katheeran tayyiban mubarakan feeh.",
            translation = "Our Lord, to You belongs all praise, abundant, wholesome, and blessed praise.",
            reference = "Sahih al-Bukhari 799",
            repetition = "1x"
        ),

        // Chapter 9: Sujood
        HisnulDua(
            id = "hisnul_sujood_1",
            chapterId = 9,
            chapterTitle = "During Sujood (Prostration)",
            occasion = "Prostration in Prayer",
            title = "Exalting the Most High in Sujood",
            arabic = "سُبْحَانَ رَبِّيَ الأَعْلَى",
            transliteration = "Subhana Rabbiyal-A'la.",
            translation = "Glory be to my Lord, the Most High.",
            reference = "Sunan Abi Dawud 870, Jami` at-Tirmidhi 262",
            repetition = "3x"
        ),
        HisnulDua(
            id = "hisnul_sujood_2",
            chapterId = 9,
            chapterTitle = "During Sujood (Prostration)",
            occasion = "Prostration in Prayer",
            title = "Complete Forgiveness of All Sins",
            arabic = "اللَّهُمَّ اغْفِرْ لِي ذَنْبِي كُلَّهُ: دِقَّهُ وَجِلَّهُ، وَأَوَّلَهُ وَآخِرَهُ، وَعَلاَنِيَتَهُ وَسِرَّهُ",
            transliteration = "Allahummaghfir lee thanbee kullahu: diqqahu wa jillahu, wa awwalahu wa akhirahu, wa 'alaniyatahu wa sirrah.",
            translation = "O Allah, forgive all of my sins: the small and the great, the first and the last, the public and the private.",
            reference = "Sahih Muslim 483",
            repetition = "1x"
        ),
        HisnulDua(
            id = "hisnul_jalsah_1",
            chapterId = 9,
            chapterTitle = "Between Two Prostrations",
            occasion = "Sitting between Sujoods",
            title = "Supplication of Mercy & Elevation",
            arabic = "اللَّهُمَّ اغْفِرْ لِي، وَارْحَمْنِي، وَاهْدِنِي، وَاجْبُرْنِي، وَعَافِنِي، وَارْزُقْنِي، وَارْفَعْنِي",
            transliteration = "Allahummaghfir lee, warhamnee, wahdinee, wajburnee, wa 'afinee, warzuqnee, warfa'nee.",
            translation = "O Allah, forgive me, have mercy on me, guide me, mend my shortcomings, grant me well-being, provide for me, and elevate me.",
            reference = "Sunan Abi Dawud 850, Jami` at-Tirmidhi 284",
            repetition = "1x"
        ),

        // Chapter 10: Tashahhud & Durood
        HisnulDua(
            id = "hisnul_tashahhud_1",
            chapterId = 10,
            chapterTitle = "Tashahhud & Durood",
            occasion = "Sitting in Prayer",
            title = "At-Tahiyyat (The Tashahhud)",
            arabic = "التَّحِيَّاتُ لِلَّهِ وَالصَّلَوَاتُ وَالطَّيِّبَاتُ، السَّلاَمُ عَلَيْكَ أَيُّهَا النَّبِيُّ وَرَحْمَةُ اللَّهِ وَبَرَكَاتُهُ، السَّلاَمُ عَلَيْنَا وَعَلَى عِبَادِ اللَّهِ الصَّالِحِينَ، أَشْهَدُ أَنْ لاَ إِلَهَ إِلاَّ اللَّهُ، وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ",
            transliteration = "At-tahiyyatu lillahi was-salawatu wat-tayyibat, as-salamu 'alayka ayyuhan-Nabiyyu wa rahmatullahi wa barakatuh, as-salamu 'alayna wa 'ala 'ibadillahis-saliheen. Ash-hadu alla ilaha illallah, wa ash-hadu anna Muhammadan 'abduhu wa rasooluh.",
            translation = "All compliments, prayers, and pure words are due to Allah. Peace be upon you, O Prophet, and the mercy of Allah and His blessings. Peace be upon us and upon the righteous servants of Allah. I bear witness that none is worthy of worship except Allah, and I bear witness that Muhammad is His servant and messenger.",
            reference = "Sahih al-Bukhari 831, Sahih Muslim 402",
            repetition = "1x"
        ),
        HisnulDua(
            id = "hisnul_durood_1",
            chapterId = 10,
            chapterTitle = "Tashahhud & Durood",
            occasion = "Salawat upon the Prophet",
            title = "As-Salat Al-Ibrahimiyyah",
            arabic = "اللَّهُمَّ صَلِّ عَلَى مُحَمَّدٍ وَعَلَى آلِ مُحَمَّدٍ، كَمَا صَلَّيْتَ عَلَى إِبْرَاهِيمَ وَعَلَى آلِ إِبْرَاهِيمَ، إِنَّكَ حَمِيدٌ مَجِيدٌ. اللَّهُمَّ بَارِكْ عَلَى مُحَمَّدٍ وَعَلَى آلِ مُحَمَّدٍ، كَمَا بَارَكْتَ عَلَى إِبْرَاهِيمَ وَعَلَى آلِ إِبْرَاهِيمَ، إِنَّكَ حَمِيدٌ مَجِيدٌ",
            transliteration = "Allahumma salli 'ala Muhammadin wa 'ala ali Muhammad, kama sallayta 'ala Ibraheema wa 'ala ali Ibraheem, innaka Hameedum Majeed. Allahumma barik 'ala Muhammadin wa 'ala ali Muhammad, kama barakta 'ala Ibraheema wa 'ala ali Ibraheem, innaka Hameedum Majeed.",
            translation = "O Allah, bestow Your favor upon Muhammad and upon the family of Muhammad as You bestowed Your favor upon Abraham and the family of Abraham, indeed You are Praiseworthy, Most Glorious. O Allah, bless Muhammad and the family of Muhammad as You blessed Abraham and the family of Abraham, indeed You are Praiseworthy, Most Glorious.",
            reference = "Sahih al-Bukhari 3370, Sahih Muslim 406",
            repetition = "1x"
        ),

        // Chapter 11: Before & After Salam
        HisnulDua(
            id = "hisnul_salam_1",
            chapterId = 11,
            chapterTitle = "Before & After Salam",
            occasion = "Before Final Tasleem",
            title = "Seeking Refuge from the Four Great Trials",
            arabic = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنْ عَذَابِ جَهَنَّمَ، وَمِنْ عَذَابِ الْقَبْرِ، وَمِنْ فِتْنَةِ الْمَحْيَا وَالْمَمَاتِ، وَمِنْ شَرِّ فِتْنَةِ الْمَسِيحِ الدَّجَّالِ",
            transliteration = "Allahumma innee a'oothu bika min 'athabi jahannam, wa min 'athabil-qabr, wa min fitnatil-mahya wal-mamat, wa min sharri fitnatil-maseehid-dajjal.",
            translation = "O Allah, I seek refuge in You from the punishment of Hellfire, from the punishment of the grave, from the trials of life and death, and from the evil trial of the False Messiah (Dajjal).",
            reference = "Sahih al-Bukhari 1377, Sahih Muslim 588",
            repetition = "1x"
        ),
        HisnulDua(
            id = "hisnul_post_prayer_1",
            chapterId = 11,
            chapterTitle = "Before & After Salam",
            occasion = "After Prayer Tasleem",
            title = "Istighfar and Allah is Peace",
            arabic = "أَسْتَغْفِرُ اللَّهَ (ثَلاَثًا)، اللَّهُمَّ أَنْتَ السَّلاَمُ وَمِنْكَ السَّلاَمُ، تَبَارَكْتَ يَا ذَا الْجَلاَلِ وَالإِكْرَامِ",
            transliteration = "Astaghfirullah (3x), Allahumma Antas-Salam wa minkas-salam, tabarakta ya Dhal-Jalali wal-Ikram.",
            translation = "I seek forgiveness of Allah (3 times). O Allah, You are Peace and from You comes peace. Blessed are You, O Possessor of Majesty and Honor.",
            reference = "Sahih Muslim 591",
            repetition = "1x"
        ),

        // Chapter 12: Istikharah
        HisnulDua(
            id = "hisnul_istikhara",
            chapterId = 12,
            chapterTitle = "Seeking Guidance (Istikharah)",
            occasion = "Consulting Allah in Decisions",
            title = "The Complete Supplication of Istikharah",
            arabic = "اللَّهُمَّ إِنِّي أَسْتَخِيرُكَ بِعِلْمِكَ، وَأَسْتَقْدِرُكَ بِقُدْرَتِكَ، وَأَسْأَلُكَ مِنْ فَضْلِكَ الْعَظِيمِ، فَإِنَّكَ تَقْدِرُ وَلاَ أَقْدِرُ، وَتَعْلَمُ وَلاَ أَعْلَمُ، وَأَنْتَ عَلاَّمُ الْغُيُوبِ. اللَّهُمَّ إِنْ كُنْتَ تَعْلَمُ أَنَّ هَذَا الأَمْرَ خَيْرٌ لِي فِي دِينِي وَمَعَاشِي وَعَاقِبَةِ أَمْرِي، فَاقْدُرْهُ لِي وَيَسِّرْهُ لِي ثُمَّ بَارِكْ لِي فِيهِ، وَإِنْ كُنْتَ تَعْلَمُ أَنَّ هَذَا الأَمْرَ شَرٌّ لِي فِي دِينِي وَمَعَاشِي وَعَاقِبَةِ أَمْرِي، فَاصْرِفْهُ عَنِّي وَاصْرِفْنِي عَنْهُ، وَاقْدُرْ لِيَ الْخَيْرَ حَيْثُ كَانَ ثُمَّ أَرْضِنِي بِهِ",
            transliteration = "Allahumma innee astakheeruka bi-'ilmik, wa astaqdiruka bi-qudratik, wa as-aluka min fadlikal-'atheem, fa-innaka taqdiru wa la aqdir, wa ta'lamu wa la a'lam, wa Anta 'Allamul-ghuyoob. Allahumma in kunta ta'lamu anna hathal-amra khayrun lee fee deenee wa ma'ashee wa 'aqibati amree faqdurhu lee wa yassirhu lee thumma barik lee feeh, wa in kunta ta'lamu anna hathal-amra sharrun lee fee deenee wa ma'ashee wa 'aqibati amree fasrifhu 'annee wasrifnee 'anh, waqdur liyal-khayra haythu kana thumma ardinee bih.",
            translation = "O Allah, I seek Your guidance through Your knowledge, and power through Your might, and I ask of Your immense bounty. For You are capable and I am not, You know and I do not, and You are the Knower of the unseen. O Allah, if You know that this matter is good for me in my religion, livelihood, and the end of my affair, then decree it for me, facilitate it for me, and bless me in it. And if You know that this matter is harmful for me in my religion, livelihood, and the end of my affair, then turn it away from me and turn me away from it, and decree for me what is good wherever it may be, and make me pleased with it.",
            reference = "Sahih al-Bukhari 1166",
            repetition = "1x"
        ),

        // Chapter 14: Sleeping & Nightmares
        HisnulDua(
            id = "hisnul_sleep_1",
            chapterId = 14,
            chapterTitle = "Before Sleeping",
            occasion = "Placing Side on Bed",
            title = "Entrusting One's Soul before Sleep",
            arabic = "بِاسْمِكَ رَبِّي وَضَعْتُ جَنْبِي، وَبِكَ أَرْفَعُهُ، فَإِنْ أَمْسَكْتَ نَفْسِي فَارْحَمْهَا، وَإِنْ أَرْسَلْتَهَا فَاحْفَظْهَا بِمَا تَحْفَظُ بِهِ عِبَادَكَ الصَّالِحِينَ",
            transliteration = "Bismika Rabbi wada'tu jambee, wa bika arfa'uh, fa-in amsakta nafsee farhamha, wa in arsaltaha fahfath-ha bima tahfathu bihi 'ibadakas-saliheen.",
            translation = "In Your name my Lord, I lay down my side, and by Your grace I rise. If You keep my soul, have mercy on it; and if You return it, protect it as You protect Your righteous servants.",
            reference = "Sahih al-Bukhari 6320, Sahih Muslim 2714",
            repetition = "1x"
        ),
        HisnulDua(
            id = "hisnul_sleep_2",
            chapterId = 14,
            chapterTitle = "Before Sleeping",
            occasion = "Going to Sleep",
            title = "In Your Name I Die and Live",
            arabic = "بِاسْمِكَ اللَّهُمَّ أَمُوتُ وَأَحْيَا",
            transliteration = "Bismikallahumma amootu wa ahya.",
            translation = "In Your name, O Allah, I die and I live.",
            reference = "Sahih al-Bukhari 6324, Sahih Muslim 2711",
            repetition = "1x"
        ),
        HisnulDua(
            id = "hisnul_sleep_3",
            chapterId = 14,
            chapterTitle = "Nightmare & Anxiety at Night",
            occasion = "Bad Dreams or Night Fears",
            title = "Protection from Fright and Nightmares",
            arabic = "أَعُوذُ بِكَلِمَاتِ اللَّهِ التَّامَّاتِ مِنْ غَضَبِهِ وَعِقَابِهِ، وَشَرِّ عِبَادِهِ، وَمِنْ هَمَزَاتِ الشَّيَاطِينِ وَأَنْ يَحْضُرُونِ",
            transliteration = "A'oothu bi-kalimatil-lahit-tammati min ghadabihi wa 'iqabihi, wa sharri 'ibadihi, wa min hamazatish-shayatiyni wa an yahduroon.",
            translation = "I seek refuge in the perfect words of Allah from His anger and punishment, from the evil of His servants, and from the whisperings of devils and from their presence.",
            reference = "Sunan Abi Dawud 3893, Jami` at-Tirmidhi 3528 (Hasan)",
            repetition = "1x"
        ),

        // Chapter 15: Eating & Host Dua
        HisnulDua(
            id = "hisnul_food_1",
            chapterId = 15,
            chapterTitle = "Food & Drinking",
            occasion = "Before Eating",
            title = "Commencing a Meal",
            arabic = "بِسْمِ اللَّهِ (فَإِنْ نَسِيَ فَلْيَقُلْ: بِسْمِ اللَّهِ فِي أَوَّلِهِ وَآخِرِهِ)",
            transliteration = "Bismillah (If forgotten: Bismillahi fee awwalihi wa akhirih).",
            translation = "In the name of Allah. (If one forgets at the start, say: In the name of Allah at its beginning and end).",
            reference = "Sahih al-Bukhari 5376, Sunan Abi Dawud 3767",
            repetition = "1x"
        ),
        HisnulDua(
            id = "hisnul_food_2",
            chapterId = 15,
            chapterTitle = "Food & Drinking",
            occasion = "After Eating",
            title = "Praise to Allah for Food and Provision",
            arabic = "الْحَمْدُ لِلَّهِ الَّذِي أَطْعَمَنِي هَذَا وَرَزَقَنِيهِ مِنْ غَيْرِ حَوْلٍ مِنِّي وَلاَ قُوَّةٍ",
            transliteration = "Alhamdu lillahil-lathee at'amanee hatha wa razaqaneehi min ghayri hawlin minnee wa la quwwah.",
            translation = "Praise be to Allah who fed me this and provided it for me without any power or might on my part.",
            reference = "Jami` at-Tirmidhi 3458, Sunan Abi Dawud 4023",
            repetition = "1x"
        ),
        HisnulDua(
            id = "hisnul_host_1",
            chapterId = 15,
            chapterTitle = "Food & Drinking",
            occasion = "Guest Supplication for Host",
            title = "Dua for the Person Who Provides Food",
            arabic = "اللَّهُمَّ بَارِكْ لَهُمْ فِيمَا رَزَقْتَهُمْ، وَاغْفِرْ لَهُمْ وَارْحَمْهُمْ",
            transliteration = "Allahumma barik lahum feema razaqtahum, waghfir lahum warhamhum.",
            translation = "O Allah, bless them in what You have provided them, forgive them, and have mercy upon them.",
            reference = "Sahih Muslim 2042",
            repetition = "1x"
        ),

        // Chapter 16: Fasting & Iftar
        HisnulDua(
            id = "hisnul_iftar_1",
            chapterId = 16,
            chapterTitle = "Fasting & Iftar",
            occasion = "Breaking the Fast",
            title = "Thirst is Gone and Reward Confirmed",
            arabic = "ذَهَبَ الظَّمَأُ وَابْتَلَّتِ الْعُرُوقُ، وَثَبَتَ الأَجْرُ إِنْ شَاءَ اللَّهُ",
            transliteration = "Dhahabadh-dhama-u wabtallatil-'urooq, wa thabatal-ajru in sha Allah.",
            translation = "The thirst is gone, the veins are moistened, and the reward is confirmed, if Allah wills.",
            reference = "Sunan Abi Dawud 2357 (Hasan)",
            repetition = "1x"
        ),

        // Chapter 17: Travel & Vehicle
        HisnulDua(
            id = "hisnul_travel_1",
            chapterId = 17,
            chapterTitle = "Travel & Vehicle",
            occasion = "Boarding Vehicle / Starting Journey",
            title = "Supplication for Travel & Transportation",
            arabic = "اللَّهُ أَكْبَرُ، اللَّهُ أَكْبَرُ، اللَّهُ أَكْبَرُ، سُبْحَانَ الَّذِي سَخَّرَ لَنَا هَذَا وَمَا كُنَّا لَهُ مُقْرِنِينَ، وَإِنَّا إِلَى رَبِّنَا لَمُنْقَلِبُونَ. اللَّهُمَّ إِنَّا نَسْأَلُكَ فِي سَفَرِنَا هَذَا الْبِرَّ وَالتَّقْوَى، وَمِنَ الْعَمَلِ مَا تَرْضَى، اللَّهُمَّ هَوِّنْ عَلَيْنَا سَفَرَنَا هَذَا وَاطْوِ عَنَّا بُعْدَهُ، اللَّهُمَّ أَنْتَ الصَّاحِبُ فِي السَّفَرِ، وَالْخَلِيفَةُ فِي الأَهْلِ",
            transliteration = "Allahu Akbar (3x), Subhanal-lathee sakh-khara lana hatha wa ma kunna lahu muqrineen, wa inna ila Rabbina lamunqaliboon. Allahumma inna nas-aluka fee safarina hathal-birra wat-taqwa, wa minal-'amali ma tarda. Allahumma hawwin 'alayna safarana hatha watwi 'anna bu'dah, Allahumma Antas-sahibu fis-safar, wal-khaleefatu fil-ahl.",
            translation = "Allah is the Greatest (3 times). Glory to Him who has brought this under our control though we were unable to subdue it, and to our Lord we surely return. O Allah, we ask You in this journey of ours for righteousness, piety, and actions that please You. O Allah, ease this journey for us and shorten its distance. O Allah, You are our Companion in journey and Guardian of our family.",
            reference = "Sahih Muslim 1342",
            repetition = "1x"
        ),
        HisnulDua(
            id = "hisnul_travel_2",
            chapterId = 17,
            chapterTitle = "Travel & Vehicle",
            occasion = "Returning from Travel",
            title = "Returning Home from Travel",
            arabic = "آيِبُونَ، تَائِبُونَ، عَابِدُونَ، لِرَبِّنَا حَامِدُونَ",
            transliteration = "Ayiboona, ta-iboona, 'abidoona, li-Rabbina hamidoon.",
            translation = "We return, repenting, worshipping, and praising our Lord.",
            reference = "Sahih al-Bukhari 1797, Sahih Muslim 1342",
            repetition = "1x"
        ),

        // Chapter 18: Weather
        HisnulDua(
            id = "hisnul_weather_1",
            chapterId = 18,
            chapterTitle = "Weather: Wind & Rain",
            occasion = "During Thunder",
            title = "Glorifying Allah when Thunder is Heard",
            arabic = "سُبْحَانَ الَّذِي يُسَبِّحُ الرَّعْدُ بِحَمْدِهِ وَالْمَلاَئِكَةُ مِنْ خِيفَتِهِ",
            transliteration = "Subhanal-lathee yusabbihur-ra'du bihamdihi wal-mala-ikatu min kheefatih.",
            translation = "Glory be to Him whom the thunder glorifies with His praise and the angels out of fear of Him.",
            reference = "Al-Muwatta 1808 (Sahih Mawquf)",
            repetition = "1x"
        ),
        HisnulDua(
            id = "hisnul_weather_2",
            chapterId = 18,
            chapterTitle = "Weather: Wind & Rain",
            occasion = "When It Rains",
            title = "Beneficial Rainfall",
            arabic = "اللَّهُمَّ صَيِّبًا نَافِعًا",
            transliteration = "Allahumma sayyiban nafi'an.",
            translation = "O Allah, make it a beneficial downpour.",
            reference = "Sahih al-Bukhari 1032",
            repetition = "1x"
        ),

        // Chapter 19: Distress & Anxiety (Karb)
        HisnulDua(
            id = "hisnul_anxiety_1",
            chapterId = 19,
            chapterTitle = "Distress, Grief & Anxiety",
            occasion = "Severe Grief & Distress",
            title = "Dua of the Prophet during Severe Distress",
            arabic = "لاَ إِلَهَ إِلاَّ اللَّهُ الْعَظِيمُ الْحَلِيمُ، لاَ إِلَهَ إِلاَّ اللَّهُ رَبُّ الْعَرْشِ الْعَظِيمِ، لاَ إِلَهَ إِلاَّ اللَّهُ رَبُّ السَّمَاوَاتِ وَرَبُّ الأَرْضِ وَرَبُّ الْعَرْشِ الْكَرِيمِ",
            transliteration = "La ilaha illallahul-'Atheemul-Haleem, la ilaha illallahu Rabbul-'Arshil-'Atheem, la ilaha illallahu Rabbus-samawati wa Rabbul-ardi wa Rabbul-'Arshil-Kareem.",
            translation = "There is no deity worthy of worship except Allah, the Supreme, the Forbearing. There is no deity worthy of worship except Allah, Lord of the Mighty Throne. There is no deity worthy of worship except Allah, Lord of the heavens and Lord of the earth and Lord of the Noble Throne.",
            reference = "Sahih al-Bukhari 6346, Sahih Muslim 2730",
            repetition = "1x"
        ),
        HisnulDua(
            id = "hisnul_anxiety_2",
            chapterId = 19,
            chapterTitle = "Distress, Grief & Anxiety",
            occasion = "Worry, Sadness & Helplessness",
            title = "Refuge from Worry, Sorrow & Weakness",
            arabic = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْهَمِّ وَالْحَزَنِ، وَالْعَجْزِ وَالْكَسَلِ، وَالْبُخْلِ وَالْجُبْنِ، وَضَلَعِ الدَّيْنِ وَغَلَبَةِ الرِّجَالِ",
            transliteration = "Allahumma innee a'oothu bika minal-hammi wal-hazan, wal-'ajzi wal-kasal, wal-bukhli wal-jubn, wa dala'id-dayni wa ghalabatir-rijal.",
            translation = "O Allah, I seek refuge in You from anxiety and sorrow, from incapacity and laziness, from stinginess and cowardice, from the burden of debt and from being overpowered by men.",
            reference = "Sahih al-Bukhari 2893",
            repetition = "1x"
        ),
        HisnulDua(
            id = "hisnul_anxiety_3",
            chapterId = 19,
            chapterTitle = "Distress, Grief & Anxiety",
            occasion = "When Facing Overwhelming Hardship",
            title = "Supplication of Yunus (Dhul-Nun)",
            arabic = "لاَ إِلَهَ إِلاَّ أَنْتَ سُبْحَانَكَ إِنِّي كُنْتُ مِنَ الظَّالِمِينَ",
            transliteration = "La ilaha illa Anta subhanaka innee kuntu minadh-dhalimeen.",
            translation = "There is no deity worthy of worship except You; glory be to You, indeed I was among the wrongdoers.",
            reference = "Surah Al-Anbiya 21:87, Jami` at-Tirmidhi 3505",
            repetition = "1x"
        ),

        // Chapter 20: Protection & Evil Eye
        HisnulDua(
            id = "hisnul_protection_1",
            chapterId = 20,
            chapterTitle = "Protection from Evil Eye & Harm",
            occasion = "Shielding Against All Harm",
            title = "In the Name of Allah with Whose Name Nothing Harms",
            arabic = "بِسْمِ اللَّهِ الَّذِي لاَ يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الأَرْضِ وَلاَ فِي السَّمَاءِ وَهُوَ السَّمِيعُ الْعَلِيمُ",
            transliteration = "Bismillahi-llathee la yadurru ma'asmihi shay-un fil-ardi wa la fis-sama-i wa Huwas-Samee'ul-'Aleem.",
            translation = "In the name of Allah, with whose name nothing on earth or in heaven can cause harm, and He is the All-Hearing, All-Knowing.",
            reference = "Sunan Abi Dawud 5088, Jami` at-Tirmidhi 3388",
            repetition = "3x"
        ),
        HisnulDua(
            id = "hisnul_protection_2",
            chapterId = 20,
            chapterTitle = "Protection from Evil Eye & Harm",
            occasion = "Shielding Children & Loved Ones",
            title = "Ibrahim's Supplication for Protection",
            arabic = "أُعِيذُكَ بِكَلِمَاتِ اللَّهِ التَّامَّةِ، مِنْ كُلِّ شَيْطَانٍ وَهَامَّةٍ، وَمِنْ كُلِّ عَيْنٍ لاَمَّةٍ",
            transliteration = "U'eethuka bi-kalimatil-lahit-tammah, min kulli shaytanin wa hammah, wa min kulli 'aynin lammah.",
            translation = "I seek protection for you in the perfect words of Allah from every devil and poisonous reptile, and from every evil, envious eye.",
            reference = "Sahih al-Bukhari 3371",
            repetition = "1x"
        ),

        // Chapter 21: Sickness & Ruqyah
        HisnulDua(
            id = "hisnul_sick_1",
            chapterId = 21,
            chapterTitle = "Sickness, Ruqyah & Healing",
            occasion = "Visiting a Sick Person",
            title = "Consolation: No Harm, It is Purification",
            arabic = "لاَ بَأْسَ، طَهُورٌ إِنْ شَاءَ اللَّهُ",
            transliteration = "La ba'sa, tahoorun in sha Allah.",
            translation = "No harm, may it be a purification for you, if Allah wills.",
            reference = "Sahih al-Bukhari 3616",
            repetition = "1x"
        ),
        HisnulDua(
            id = "hisnul_sick_2",
            chapterId = 21,
            chapterTitle = "Sickness, Ruqyah & Healing",
            occasion = "Supplication for Healing (7 Times)",
            title = "Asking the Lord of the Throne for Cure",
            arabic = "أَسْأَلُ اللَّهَ الْعَظِيمَ رَبَّ الْعَرْشِ الْعَظِيمِ أَنْ يَشْفِيَكَ",
            transliteration = "As-alullahal-'Atheema Rabbal-'Arshil-'Atheemi an yashfiyak.",
            translation = "I ask Allah the Supreme, Lord of the Mighty Throne, to cure you.",
            reference = "Sunan Abi Dawud 3106, Jami` at-Tirmidhi 2083",
            repetition = "7x"
        ),
        HisnulDua(
            id = "hisnul_sick_3",
            chapterId = 21,
            chapterTitle = "Sickness, Ruqyah & Healing",
            occasion = "Personal Pain / Ruqyah on Body",
            title = "Placing Hand on Area of Pain",
            arabic = "بِسْمِ اللَّهِ (ثَلاَثًا)، أَعُوذُ بِاللَّهِ وَقُدْرَتِهِ مِنْ شَرِّ مَا أَجِدُ وَأُحَاذِرُ (سَبْعًا)",
            transliteration = "Bismillah (3x), A'oothu billahi wa qudratihi min sharri ma ajidu wa uhathir (7x).",
            translation = "Say 'Bismillah' 3 times, then say 7 times: I seek refuge in Allah and His power from the evil of what I feel and fear.",
            reference = "Sahih Muslim 2202",
            repetition = "7x"
        ),

        // Chapter 22: Debt Relief
        HisnulDua(
            id = "hisnul_debt_1",
            chapterId = 22,
            chapterTitle = "Repaying Debts",
            occasion = "Financial Hardship & Debt",
            title = "Sufficiency with Halal & Freedom from Debt",
            arabic = "اللَّهُمَّ اكْفِنِي بِحَلاَلِكَ عَنْ حَرَامِكَ، وَأَغْنِنِي بِفَضْلِكَ عَمَّنْ سِوَاكَ",
            transliteration = "Allahummak-finee bi-halalika 'an haramik, wa aghninee bi-fadlika 'amman siwak.",
            translation = "O Allah, suffice me with Your lawful provision against Your unlawful, and enrich me with Your bounty over all others besides You.",
            reference = "Jami` at-Tirmidhi 3563 (Hasan)",
            repetition = "1x"
        ),

        // Chapter 23: Calamity & Visiting Graves
        HisnulDua(
            id = "hisnul_calamity_1",
            chapterId = 23,
            chapterTitle = "Facing Calamity & Grief",
            occasion = "Upon Hearing Bad News or Loss",
            title = "To Allah We Belong and Reward in Calamity",
            arabic = "إِنَّا لِلَّهِ وَإِنَّا إِلَيْهِ رَاجِعُونَ، اللَّهُمَّ أْجُرْنِي فِي مُصِيبَتِي، وَأَخْلِفْ لِي خَيْرًا مِنْهَا",
            transliteration = "Inna lillahi wa inna ilayhi raji'oon, Allahumma'-jurnee fee museebatee wa akhlif lee khayran minha.",
            translation = "Indeed we belong to Allah, and indeed to Him we return. O Allah, reward me in my affliction and replace it with something better for me.",
            reference = "Sahih Muslim 918",
            repetition = "1x"
        ),
        HisnulDua(
            id = "hisnul_graves_1",
            chapterId = 23,
            chapterTitle = "Visiting Graves",
            occasion = "Entering the Graveyard",
            title = "Greeting the Inhabitants of the Graves",
            arabic = "السَّلاَمُ عَلَيْكُمْ أَهْلَ الدِّيَارِ مِنَ الْمُؤْمِنِينَ وَالْمُسْلِمِينَ، وَإِنَّا إِنْ شَاءَ اللَّهُ بِكُمْ لاَحِقُونَ، أَسْأَلُ اللَّهَ لَنَا وَلَكُمُ الْعَافِيَةَ",
            transliteration = "As-salamu 'alaykum ahlad-diyari minal-mu'mineena wal-muslimeen, wa inna in sha Allahu bikum lahiqoon, as-alullaha lana wa lakumul-'afiyah.",
            translation = "Peace be upon you, O people of the dwellings, of believers and Muslims. And indeed, if Allah wills, we shall join you. I ask Allah for well-being for us and for you.",
            reference = "Sahih Muslim 975",
            repetition = "1x"
        ),

        // Chapter 24: Sayyid al-Istighfar
        HisnulDua(
            id = "hisnul_istighfar_1",
            chapterId = 24,
            chapterTitle = "Repentance & Sayyid al-Istighfar",
            occasion = "Morning and Evening Forgiveness",
            title = "The Master Supplication for Forgiveness",
            arabic = "اللَّهُمَّ أَنْتَ رَبِّي لاَ إِلَهَ إِلاَّ أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ بِذَنْبِي فَاغْفِرْ لِي فَإِنَّهُ لاَ يَغْفِرُ الذُّنُوبَ إِلاَّ أَنْتَ",
            transliteration = "Allahumma Anta Rabbee la ilaha illa Anta, khalaqtanee wa ana 'abduk, wa ana 'ala 'ahdika wa wa'dika mastata't, a'oothu bika min sharri ma sana't, aboo-u laka bi-ni'matika 'alayya, wa aboo-u bi-thanbee faghfir lee fa-innahu la yaghfiruth-thunooba illa Anta.",
            translation = "O Allah, You are my Lord, there is no deity worthy of worship except You. You created me and I am Your servant, and I abide by Your covenant and promise as best I can. I seek refuge in You from the evil of what I have done. I acknowledge to You Your favors upon me, and I confess to You my sin. So forgive me, for none forgives sins except You.",
            reference = "Sahih al-Bukhari 6306",
            repetition = "1x"
        ),

        // Chapter 25: Family & Children
        HisnulDua(
            id = "hisnul_family_1",
            chapterId = 25,
            chapterTitle = "Family & Children",
            occasion = "Spouse and Offspring",
            title = "Comfort of the Eyes in Spouse and Descendants",
            arabic = "رَبَّنَا هَبْ لَنَا مِنْ أَزْوَاجِنَا وَذُرِّيَّاتِنَا قُرَّةَ أَعْيُنٍ وَاجْعَلْنَا لِلْمُتَّقِينَ إِمَامًا",
            transliteration = "Rabbana hab lana min azwajina wa thurriyyatina qurrata a'yunin waj'alna lil-muttaqeena imama.",
            translation = "Our Lord, grant us from among our spouses and offspring comfort to our eyes and make us an example for the righteous.",
            reference = "Surah Al-Furqan 25:74",
            repetition = "1x"
        ),
        HisnulDua(
            id = "hisnul_parents_1",
            chapterId = 25,
            chapterTitle = "Family & Children",
            occasion = "Praying for Parents",
            title = "Mercy upon Parents",
            arabic = "رَبِّ ارْحَمْهُمَا كَمَا رَبَّيَانِي صَغِيرًا",
            transliteration = "Rabbir-hamhuma kama rabbayanee sagheera.",
            translation = "My Lord, have mercy upon them both as they raised me when I was small.",
            reference = "Surah Al-Isra 17:24",
            repetition = "1x"
        ),

        // Chapter 26: Market & Sneezing
        HisnulDua(
            id = "hisnul_market_1",
            chapterId = 26,
            chapterTitle = "Market & Social Etiquette",
            occasion = "Entering the Market / Mall",
            title = "Enormous Reward when Entering the Market",
            arabic = "لاَ إِلَهَ إِلاَّ اللَّهُ وَحْدَهُ لاَ شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ، يُحْيِي وَيُمِيتُ وَهُوَ حَيٌّ لاَ يَمُوتُ، بِيَدِهِ الْخَيْرُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ",
            transliteration = "La ilaha illallahu wahdahu la shareeka lah, lahul-mulku wa lahul-hamd, yuhyee wa yumeetu wa Huwa hayyun la yamoot, biyadihil-khayr wa Huwa 'ala kulli shay-in qadeer.",
            translation = "None has the right to be worshipped except Allah alone without partner. To Him belongs all sovereignty and praise; He gives life and causes death, and He is living and never dies. In His hand is all good, and He is over all things omnipotent.",
            reference = "Jami` at-Tirmidhi 3428 (Hasan)",
            repetition = "1x"
        ),
        HisnulDua(
            id = "hisnul_shirk_1",
            chapterId = 26,
            chapterTitle = "Protection against Shirk",
            occasion = "Preserving Pure Tawheed",
            title = "Protection from Subtle Shirk",
            arabic = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ أَنْ أُشْرِكَ بِكَ وَأَنَا أَعْلَمُ، وَأَسْتَغْفِرُكَ لِمَا لاَ أَعْلَمُ",
            transliteration = "Allahumma innee a'oothu bika an ushrika bika wa ana a'lam, wa astaghfiruka lima la a'lam.",
            translation = "O Allah, I seek refuge in You lest I associate anything with You knowingly, and I seek Your forgiveness for what I do not know.",
            reference = "Musnad Ahmad 4/403, Al-Adab al-Mufrad 716 (Sahih)",
            repetition = "3x"
        )
    )

    fun getDuasByChapter(chapterId: Int): List<HisnulDua> {
        return DUAS.filter { it.chapterId == chapterId }
    }

    fun getDuasByCategory(category: HisnulCategory): List<HisnulDua> {
        if (category == HisnulCategory.ALL) return DUAS
        val chapterIds = CHAPTERS.filter { it.category == category }.map { it.id }.toSet()
        return DUAS.filter { chapterIds.contains(it.chapterId) }
    }
}

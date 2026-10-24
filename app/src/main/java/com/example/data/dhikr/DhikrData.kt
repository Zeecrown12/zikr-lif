package com.example.data.dhikr

enum class DhikrReadingMode(val displayName: String) {
    ARABIC_ONLY("Arabic Only"),
    ARABIC_TRANSLATION("Arabic + Translation"),
    ARABIC_TRANSLITERATION("Arabic + Transliteration"),
    FULL("Arabic + Both")
}

data class DhikrItem(
    val id: String,
    val title: String,
    val arabic: String,
    val transliteration: String,
    val translation: String,
    val source: String,
    val targetCount: Int = 1,
    val virtue: String = "",
    val category: String = "Adhkar",
    val audioUrl: String = ""
)

object DhikrData {

    val MORNING_ADHKAR = listOf(
        DhikrItem(
            id = "m_ayat_kursi",
            title = "Ayat al-Kursi (The Verse of the Throne)",
            arabic = "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَّهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ ۗ مَن ذَا الَّذِي يَشْفَعُ عِندَهُ إِلَّا بِإِذْنِهِ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَيْءٍ مِّنْ عِلْمِهِ إِلَّا بِمَا شَاءَ ۚ وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالْأَرْضَ ۖ وَلَا يَئُودُهُ حِفْظُهُمَا ۚ وَهُوَ الْعَلِيُّ الْعَظِيمُ",
            transliteration = "Allāhu lā ilāha illā huwa al-ḥayyu al-qayyūm, lā ta'khudhuhu sinatun walā nawm, lahu mā fī as-samāwāti wamā fī al-arḍ...",
            translation = "Allah - there is no deity except Him, the Ever-Living, the Sustainer of all existence. Neither drowsiness overtakes Him nor sleep...",
            source = "Surah Al-Baqarah 2:255 / An-Nasa'i",
            targetCount = 1,
            virtue = "Whoever recites it in the morning will be protected from jinn until evening.",
            audioUrl = "https://cdn.islamic.network/quran/audio/128/ar.alafasy/262.mp3"
        ),
        DhikrItem(
            id = "m_sayyid_istighfar",
            title = "Sayyid al-Istighfar (The Master of Forgiveness)",
            arabic = "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلَهَ إِلَّا أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ بِذَنْبِي فَاغْفِرْ لِي فَإِنَّهُ لَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ",
            transliteration = "Allāhumma anta Rabbī lā ilāha illā anta, khalaqtanī wa anā 'abduka, wa anā 'alā 'ahdika wa wa'dika ma-staṭa'tu, a'ūdhu bika min sharri mā ṣana'tu, abū'u laka bini'matika 'alayya, wa abū'u bidhanbī faghfir lī fa'innahu lā yaghfiru adh-dhunūba illā anta",
            translation = "O Allah, You are my Lord, there is no deity except You. You created me and I am Your servant, and I abide by Your covenant and promise as best I can. I seek refuge in You from the evil of what I have done. I acknowledge Your favor upon me, and I acknowledge my sin, so forgive me, for none forgives sins except You.",
            source = "Sahih al-Bukhari (6306)",
            targetCount = 1,
            virtue = "Whoever says this in the morning with firm conviction and dies that day before evening will be among the people of Paradise."
        ),
        DhikrItem(
            id = "m_asbahna_mulk",
            title = "Morning Affirmation of Sovereignty",
            arabic = "أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ",
            transliteration = "Aṣbaḥnā wa aṣbaḥal-mulku lillāh, wal-ḥamdu lillāh, lā ilāha illā Allāhu waḥdahu lā sharīka lahu, lahul-mulku wa lahul-ḥamdu wa Huwa 'alā kulli shay'in qadīr",
            translation = "We have entered morning and all dominion belongs to Allah, and all praise is for Allah. There is no deity except Allah alone, without partner; to Him belongs all dominion and to Him is praise, and He has power over all things.",
            source = "Sahih Muslim (2723)",
            targetCount = 1,
            virtue = "Affirmation of Tawhid and submission at dawn."
        ),
        DhikrItem(
            id = "m_bismillah_ladhi",
            title = "Protection in Allah's Name",
            arabic = "بِسْمِ اللَّهِ الَّذِي لَا يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الْأَرْضِ وَلَا فِي السَّمَاءِ وَهُوَ السَّمِيعُ الْعَلِيمُ",
            transliteration = "Bismillāhilladhī lā yaḍurru ma'asmihi shay'un fīl-arḍi walā fīs-samā'i wa Huwas-Samī'ul-'Alīm",
            translation = "In the name of Allah, with whose name nothing on earth or in heaven can cause harm, and He is the All-Hearing, the All-Knowing.",
            source = "Abu Dawood (5088), Tirmidhi (3388)",
            targetCount = 3,
            virtue = "Whoever recites it three times in the morning and three times in the evening, nothing will harm him."
        ),
        DhikrItem(
            id = "m_raditu_billah",
            title = "Contentment with Allah, Islam, and the Prophet",
            arabic = "رَضِيتُ بِاللَّهِ رَبًّا، وَبِالْإِسْلَامِ دِينًا، وَبِمُحَمَّدٍ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ نَبِيًّا",
            transliteration = "Raḍītu billāhi Rabban, wa bil-Islāmi dīnan, wa bi-Muḥammadin (ṣallallāhu 'alayhi wa sallam) Nabiyyā",
            translation = "I am pleased with Allah as my Lord, with Islam as my religion, and with Muhammad (peace and blessings be upon him) as my Prophet.",
            source = "Abu Dawood (5072), Ahmad",
            targetCount = 3,
            virtue = "Allah has promised that He will surely please whoever recites this three times morning and evening."
        ),
        DhikrItem(
            id = "m_subhanallah_bihamdihi",
            title = "Glorification 100 Times",
            arabic = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ",
            transliteration = "Subḥānallāhi wa biḥamdih",
            translation = "Glory is to Allah and all praise is to Him.",
            source = "Sahih Muslim (2691)",
            targetCount = 100,
            virtue = "Whoever recites it 100 times in the morning and evening, no one will come on the Day of Resurrection with anything better except someone who said the same or more."
        ),
        DhikrItem(
            id = "m_3_quls",
            title = "The Three Protectors (Al-Ikhlas, Al-Falaq, An-Nas)",
            arabic = "سُورَةُ الإِخْلَاصِ ، سُورَةُ الفَلَقِ ، سُورَةُ النَّاسِ (ثَلَاثَ مَرَّاتٍ)",
            transliteration = "Sūrat Al-Ikhlās, Al-Falaq, An-Nās (3x)",
            translation = "Recitation of Surah Al-Ikhlas, Surah Al-Falaq, and Surah An-Nas.",
            source = "Abu Dawood (5082), Tirmidhi (3575)",
            targetCount = 3,
            virtue = "Reciting them three times morning and evening suffices you against all things.",
            audioUrl = "https://server8.mp3quran.net/afs/112.mp3"
        )
    )

    val EVENING_ADHKAR = listOf(
        DhikrItem(
            id = "e_ayat_kursi",
            title = "Ayat al-Kursi",
            arabic = "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ...",
            transliteration = "Allāhu lā ilāha illā huwa al-ḥayyu al-qayyūm...",
            translation = "Allah - there is no deity except Him, the Ever-Living, the Sustainer of all existence...",
            source = "Surah Al-Baqarah 2:255 / An-Nasa'i",
            targetCount = 1,
            virtue = "Protection until the morning.",
            audioUrl = "https://cdn.islamic.network/quran/audio/128/ar.alafasy/262.mp3"
        ),
        DhikrItem(
            id = "e_sayyid_istighfar",
            title = "Sayyid al-Istighfar",
            arabic = "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلَهَ إِلَّا أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ...",
            transliteration = "Allāhumma anta Rabbī lā ilāha illā anta, khalaqtanī wa anā 'abduka...",
            translation = "O Allah, You are my Lord, there is no deity except You...",
            source = "Sahih al-Bukhari (6306)",
            targetCount = 1,
            virtue = "If one dies during the night after saying it, he enters Paradise."
        ),
        DhikrItem(
            id = "e_amsayna_mulk",
            title = "Evening Affirmation of Sovereignty",
            arabic = "أَمْسَيْنَا وَأَمْسَى الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ",
            transliteration = "Amsaynā wa amsal-mulku lillāh, wal-ḥamdu lillāh, lā ilāha illā Allāhu waḥdahu lā sharīka lahu, lahul-mulku wa lahul-ḥamdu wa Huwa 'alā kulli shay'in qadīr",
            translation = "We have reached evening and dominion belongs to Allah, and all praise is for Allah...",
            source = "Sahih Muslim (2723)",
            targetCount = 1,
            virtue = "Affirmation of divine sovereignty as night begins."
        ),
        DhikrItem(
            id = "e_audhu_bikalimat",
            title = "Refuge from Evil of Creation",
            arabic = "أَعُوذُ بِكَلِمَاتِ اللَّهِ التَّامَّاتِ مِنْ شَرِّ مَا خَلَقَ",
            transliteration = "A'ūdhu bi-kalimātillāhit-tāmmāti min sharri mā khalaq",
            translation = "I seek refuge in the perfect words of Allah from the evil of what He has created.",
            source = "Sahih Muslim (2709)",
            targetCount = 3,
            virtue = "Whoever says this three times in the evening will not be harmed by venomous creatures or evil during that night."
        ),
        DhikrItem(
            id = "e_bismillah_ladhi",
            title = "Protection in Allah's Name",
            arabic = "بِسْمِ اللَّهِ الَّذِي لَا يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الْأَرْضِ وَلَا فِي السَّمَاءِ وَهُوَ السَّمِيعُ الْعَلِيمُ",
            transliteration = "Bismillāhilladhī lā yaḍurru ma'asmihi shay'un fīl-arḍi walā fīs-samā'i wa Huwas-Samī'ul-'Alīm",
            translation = "In the name of Allah, with whose name nothing on earth or in heaven can cause harm, and He is the All-Hearing, the All-Knowing.",
            source = "Tirmidhi (3388)",
            targetCount = 3,
            virtue = "Nothing will harm him."
        ),
        DhikrItem(
            id = "e_raditu_billah",
            title = "Pleasure with Allah & Islam",
            arabic = "رَضِيتُ بِاللَّهِ رَبًّا، وَبِالْإِسْلَامِ دِينًا، وَبِمُحَمَّدٍ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ نَبِيًّا",
            transliteration = "Raḍītu billāhi Rabban, wa bil-Islāmi dīnan, wa bi-Muḥammadin Nabiyyā",
            translation = "I am pleased with Allah as my Lord, with Islam as my religion, and with Muhammad as my Prophet.",
            source = "Abu Dawood (5072)",
            targetCount = 3,
            virtue = "Allah's guarantee of satisfaction on the Last Day."
        )
    )

    val AL_MATHURAT = listOf(
        DhikrItem(
            id = "mathurat_1",
            title = "Al-Ma'thurat: Opening Isti'adha & Fatihah",
            arabic = "أَعُوذُ بِاللَّهِ مِنَ الشَّيْطَانِ الرَّجِيمِ • بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ • الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ...",
            transliteration = "A'ūdhu billāhi minash-shayṭānir-rajīm • Bismillāhir-Raḥmānir-Raḥīm...",
            translation = "I seek refuge in Allah from Satan the outcast. In the name of Allah, the Most Merciful, the Especially Merciful. All praise is due to Allah...",
            source = "Al-Ma'thurat (Hasan al-Banna compilation)",
            targetCount = 1,
            virtue = "Foundation of daily spiritual vigilance and fortress.",
            audioUrl = "https://server8.mp3quran.net/afs/001.mp3"
        ),
        DhikrItem(
            id = "mathurat_2",
            title = "First Five Verses of Al-Baqarah",
            arabic = "الم • ذَٰلِكَ الْكِتَابُ لَا رَيْبَ ۛ فِيهِ ۛ هُدًى لِّلْمُتَّقِينَ • الَّذِينَ يُؤْمِنُونَ بِالْغَيْبِ وَيُقِيمُونَ الصَّلَاةَ وَمِمَّا رَزَقْنَاهُمْ يُنفِقُونَ • وَالَّذِينَ يُؤْمِنُونَ بِمَا أُنزِلَ إِلَيْكَ وَمَا أُنزِلَ مِن قَبْلِكَ وَبِالْآخِرَةِ هُمْ يُوقِنُونَ • أُولَٰئِكَ عَلَىٰ هُدًى مِّن رَّبِّهِمْ ۖ وَأُولَٰئِكَ هُمُ الْمُفْلِحُونَ",
            transliteration = "Alif-Lām-Mīm. Dhālikal-kitābu lā rayba fīhi hudal-lil-muttaqīn...",
            translation = "Alif, Lam, Meem. This is the Book about which there is no doubt, a guidance for those conscious of Allah...",
            source = "Al-Baqarah 2:1-5",
            targetCount = 1,
            virtue = "Guards the home and the soul against spiritual harm.",
            audioUrl = "https://cdn.islamic.network/quran/audio/128/ar.alafasy/8.mp3"
        ),
        DhikrItem(
            id = "mathurat_3",
            title = "Dua for Relief from Debt and Worry",
            arabic = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْهَمِّ وَالْحَزَنِ، وَأَعُوذُ بِكَ مِنَ الْعَجْزِ وَالْكَسَلِ، وَأَعُوذُ بِكَ مِنَ الْجُبْنِ وَالْبُخْلِ، وَأَعُوذُ بِكَ مِنْ غَلَبَةِ الدَّيْنِ وَقَهْرِ الرِّجَالِ",
            transliteration = "Allāhumma innī a'ūdhu bika minal-hammi wal-ḥazan, wa a'ūdhu bika minal-'ajzi wal-kasal, wa a'ūdhu bika minal-jubni wal-bukhl, wa a'ūdhu bika min ghalabatid-dayni wa qahrir-rijāl",
            translation = "O Allah, I seek refuge in You from grief and sorrow, and I seek refuge in You from incapacity and laziness, and I seek refuge in You from cowardice and miserliness, and I seek refuge in You from being overcome by debt and oppressed by men.",
            source = "Sahih al-Bukhari (2893)",
            targetCount = 3,
            virtue = "Prophetic remedy for anxiety, sorrow, financial overwhelm, and helplessness."
        ),
        DhikrItem(
            id = "mathurat_4",
            title = "Dua for Well-being (Afiyah)",
            arabic = "اللَّهُمَّ عَافِنِي فِي بَدَنِي، اللَّهُمَّ عَافِنِي فِي سَمْعِي، اللَّهُمَّ عَافِنِي فِي بَصَرِي، لَا إِلَهَ إِلَّا أَنْتَ",
            transliteration = "Allāhumma 'āfinī fī badanī, Allāhumma 'āfinī fī sam'ī, Allāhumma 'āfinī fī baṣarī, lā ilāha illā Ant",
            translation = "O Allah, grant me health in my body. O Allah, grant me health in my hearing. O Allah, grant me health in my sight. There is no deity except You.",
            source = "Abu Dawood (5090)",
            targetCount = 3,
            virtue = "Protection of physical faculties and vitality."
        ),
        DhikrItem(
            id = "mathurat_5",
            title = "The Bond of Brotherhood (Dua al-Rabita)",
            arabic = "اللَّهُمَّ إِنَّكَ تَعْلَمُ أَنَّ هَذِهِ الْقُلُوبَ قَدِ اجْتَمَعَتْ عَلَى مَحَبَّتِكَ، وَالْتَقَتْ عَلَى طَاعَتِكَ، وَتَوَحَّدَتْ عَلَى دَعْوَتِكَ، وَتَعَاهَدَتْ عَلَى نُصْرَةِ شَرِيعَتِكَ، فَوَثِّقِ اللَّهُمَّ رَابِطَتَهَا، وَأَدِمْ وُدَّهَا، وَاهْدِهَا سُبُلَهَا",
            transliteration = "Allāhumma innaka ta'lamu anna hādhihil-qulūba qadijtama'at 'alā maḥabbatik, wal-taqat 'alā ṭā'atik, wa tawaḥḥadat 'alā da'watik, wa ta'āhadat 'alā nuṣrati sharī'atik, fawath-thiqillāhumma rābiṭatahā, wa adim wuddahā...",
            translation = "O Allah, You know that these hearts have united in love for You, met upon obedience to You, unified upon Your call, and pledged to support Your guidance. So strengthen, O Allah, their bond, perpetuate their love, and guide them to their paths...",
            source = "Al-Ma'thurat tradition",
            targetCount = 1,
            virtue = "Spiritual solidarity and mutual love for the Muslim Ummah."
        )
    )

    val AFTER_PRAYER_ADHKAR = listOf(
        DhikrItem(
            id = "post_salah_istighfar",
            title = "Istighfar after Salam",
            arabic = "أَسْتَغْفِرُ اللَّهَ ، أَسْتَغْفِرُ اللَّهَ ، أَسْتَغْفِرُ اللَّهَ",
            transliteration = "Astaghfirullāh, Astaghfirullāh, Astaghfirullāh",
            translation = "I seek the forgiveness of Allah (three times).",
            source = "Sahih Muslim (591)",
            targetCount = 3,
            virtue = "Said immediately upon concluding prayer."
        ),
        DhikrItem(
            id = "post_salah_salam",
            title = "Allahumma Antas-Salam",
            arabic = "اللَّهُمَّ أَنْتَ السَّلَامُ وَمِنْكَ السَّلَامُ ، تَبَارَكْتَ يَا ذَا الْجَلَالِ وَالْإِكْرَامِ",
            transliteration = "Allāhumma Antas-Salāmu wa minkas-salām, tabārakta yā Dhal-Jalāli wal-Ikrām",
            translation = "O Allah, You are Peace and from You comes peace. Blessed are You, O Owner of majesty and honor.",
            source = "Sahih Muslim (591)",
            targetCount = 1,
            virtue = "Acknowledging Allah as the source of all security and peace."
        ),
        DhikrItem(
            id = "post_salah_ayat_kursi",
            title = "Ayat al-Kursi after Every Fard Prayer",
            arabic = "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ...",
            transliteration = "Allāhu lā ilāha illā huwa al-ḥayyu al-qayyūm...",
            translation = "Allah - there is no deity except Him, the Ever-Living, the Sustainer of all existence...",
            source = "Sunan an-Nasa'i (al-Kubra 9928)",
            targetCount = 1,
            virtue = "The Prophet (ﷺ) said: 'Whoever recites Ayat al-Kursi after every obligatory prayer, nothing stands between him and entering Paradise except death.'",
            audioUrl = "https://cdn.islamic.network/quran/audio/128/ar.alafasy/262.mp3"
        ),
        DhikrItem(
            id = "post_salah_tasbih",
            title = "Tasbih Fatimi (33-33-34)",
            arabic = "سُبْحَانَ اللَّهِ (٣٣) ، الْحَمْدُ لِلَّهِ (٣٣) ، اللَّهُ أَكْبَرُ (٣٣) • لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ",
            transliteration = "Subḥānallāh (33x), Al-ḥamdulillāh (33x), Allāhu Akbar (33x)... Lā ilāha illā Allāhu waḥdahu lā sharīka lahu...",
            translation = "Glory be to Allah (33 times), Praise be to Allah (33 times), Allah is the Greatest (33 times), then seal with: There is no deity except Allah alone without partner...",
            source = "Sahih Muslim (597)",
            targetCount = 1,
            virtue = "Whoever says this after every prayer, his sins will be forgiven even if they are like the foam of the sea."
        )
    )

    val BEFORE_SLEEP_ADHKAR = listOf(
        DhikrItem(
            id = "sleep_bismika",
            title = "Bismika Rabbi Wada'tu Janbi",
            arabic = "بِاسْمِكَ رَبِّي وَضَعْتُ جَنْبِي ، وَبِكَ أَرْفَعُهُ ، فَإِنْ أَمْسَكْتَ نَفْسِي فَارْحَمْهَا ، وَإِنْ أَرْسَلْتَهَا فَاحْفَظْهَا بِمَا تَحْفَظُ بِهِ عِبَادَكَ الصَّالِحِينَ",
            transliteration = "Bismika Rabbī waḍa'tu janbī, wa bika arfa'uh, fa-in amsakta nafsī far-ḥamhā, wa in arsaltahā faḥ-faẓhā bimā taḥfaẓu bihi 'ibādakaṣ-ṣāliḥīn",
            translation = "In Your name, my Lord, I lay down my side and in Your name I raise it. If You hold my soul, have mercy upon it, and if You release it, protect it as You protect Your righteous servants.",
            source = "Sahih al-Bukhari (6320), Sahih Muslim (2714)",
            targetCount = 1,
            virtue = "Protection of the soul during sleep."
        ),
        DhikrItem(
            id = "sleep_cupped_hands",
            title = "Three Quls into Cupped Hands",
            arabic = "قُلْ هُوَ اللَّهُ أَحَدٌ • قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ • قُلْ أَعُوذُ بِرَبِّ النَّاسِ",
            transliteration = "Surah Al-Ikhlas, Surah Al-Falaq, Surah An-Nas",
            translation = "Recite the three Quls into cupped hands, blow gently, and wipe over the entire body starting from the head and face (3 times).",
            source = "Sahih al-Bukhari (5017)",
            targetCount = 3,
            virtue = "Sunnah practice of the Prophet (ﷺ) every night before sleeping for physical and spiritual protection.",
            audioUrl = "https://server8.mp3quran.net/afs/112.mp3"
        ),
        DhikrItem(
            id = "sleep_last_baqarah",
            title = "Last Two Verses of Surah Al-Baqarah",
            arabic = "آمَنَ الرَّسُولُ بِمَا أُنزِلَ إِلَيْهِ مِن رَّبِّهِ وَالْمُؤْمِنُونَ...",
            transliteration = "Āmana ar-rasūlu bimā unzila ilayhi min rabbihi wal-mu'minūn...",
            translation = "The Messenger has believed in what was revealed to him from his Lord, and so have the believers...",
            source = "Surah Al-Baqarah 2:285-286 / Sahih al-Bukhari (5009)",
            targetCount = 1,
            virtue = "The Prophet (ﷺ) said: 'Whoever recites the last two verses of Surah Al-Baqarah at night, they will suffice him.'",
            audioUrl = "https://cdn.islamic.network/quran/audio/128/ar.alafasy/292.mp3"
        )
    )

    val AFTER_WAKING_ADHKAR = listOf(
        DhikrItem(
            id = "wake_alhamdulillah",
            title = "Praise upon Waking",
            arabic = "الْحَمْدُ لِلَّهِ الَّذِي أَحْيَانَا بَعْدَ مَا أَمَاتَنَا وَإِلَيْهِ النُّشُورُ",
            transliteration = "Al-ḥamdu lillāhilladhī aḥyānā ba'da mā amātanā wa ilayhin-nushūr",
            translation = "All praise is for Allah who gave us life after having caused us to die, and unto Him is the resurrection.",
            source = "Sahih al-Bukhari (6312)",
            targetCount = 1,
            virtue = "Gratitude for the blessing of a new day of life."
        ),
        DhikrItem(
            id = "wake_afiyah",
            title = "Dua for Vitality & Remembrance",
            arabic = "الْحَمْدُ لِلَّهِ الَّذِي عَافَانِي فِي جَسَدِي ، وَرَدَّ عَلَيَّ رُوحِي ، وَأَذِنَ لِي بِذِكْرِهِ",
            transliteration = "Al-ḥamdu lillāhilladhī 'āfānī fī jasadī, wa radda 'alayya rūḥī, wa adhina lī bidhikrih",
            translation = "All praise is for Allah who restored health to my body, returned my soul to me, and granted me permission to remember Him.",
            source = "Sunan al-Tirmidhi (3401)",
            targetCount = 1,
            virtue = "Joyful submission as morning begins."
        )
    )

    val DAILY_DUAS = listOf(
        DhikrItem(
            id = "dua_waking",
            title = "Upon Waking Up",
            arabic = "الْحَمْدُ لِلَّهِ الَّذِي أَحْيَانَا بَعْدَ مَا أَمَاتَنَا وَإِلَيْهِ النُّشُورُ",
            transliteration = "Al-ḥamdu lillāhilladhī aḥyānā ba'da mā amātanā wa ilayhin-nushūr",
            translation = "All praise is for Allah who gave us life after having caused us to die, and unto Him is the resurrection.",
            source = "Sahih al-Bukhari (6312)",
            category = "Daily Routines"
        ),
        DhikrItem(
            id = "dua_sleeping",
            title = "Before Sleeping",
            arabic = "بِاسْمِكَ اللَّهُمَّ أَمُوتُ وَأَحْيَا",
            transliteration = "Bismika Allāhumma amūtu wa aḥyā",
            translation = "In Your name, O Allah, I die and I live.",
            source = "Sahih al-Bukhari (6324)",
            category = "Daily Routines"
        ),
        DhikrItem(
            id = "dua_eating_before",
            title = "Before Eating",
            arabic = "بِسْمِ اللَّهِ",
            transliteration = "Bismillāh",
            translation = "In the name of Allah.",
            source = "Abu Dawood (3767)",
            category = "Food & Drink"
        ),
        DhikrItem(
            id = "dua_eating_after",
            title = "After Finishing Meal",
            arabic = "الْحَمْدُ لِلَّهِ الَّذِي أَطْعَمَنِي هَذَا وَرَزَقَنِيهِ مِنْ غَيْرِ حَوْلٍ مِنِّي وَلَا قُوَّةٍ",
            transliteration = "Al-ḥamdu lillāhilladhī aṭ'amanī hādhā wa razaqanīhi min ghayri ḥawlin minnī walā quwwah",
            translation = "All praise is for Allah who fed me this and provided it for me without any might or power from myself.",
            source = "Tirmidhi (3458)",
            category = "Food & Drink"
        ),
        DhikrItem(
            id = "dua_leaving_home",
            title = "Leaving the House",
            arabic = "بِسْمِ اللَّهِ ، تَوَكَّلْتُ عَلَى اللَّهِ ، وَلَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ",
            transliteration = "Bismillāhi, tawakkaltu 'alallāh, wa lā ḥawla wa lā quwwata illā billāh",
            translation = "In the name of Allah, I trust in Allah, and there is no power or might except with Allah.",
            source = "Abu Dawood (5095)",
            category = "Travel & Home"
        ),
        DhikrItem(
            id = "dua_entering_home",
            title = "Entering the House",
            arabic = "بِسْمِ اللَّهِ وَلَجْنَا، وَبِسْمِ اللَّهِ خَرَجْنَا، وَعَلَى رَبِّنَا تَوَكَّلْنَا",
            transliteration = "Bismillāhi walajnā, wa bismillāhi kharajnā, wa 'alā Rabbinā tawakkalnā",
            translation = "In the name of Allah we enter, and in the name of Allah we leave, and upon our Lord we place our trust.",
            source = "Abu Dawood (5096)",
            category = "Travel & Home"
        ),
        DhikrItem(
            id = "dua_travelling",
            title = "Dua for Travelling (Safar)",
            arabic = "سُبْحَانَ الَّذِي سَخَّرَ لَنَا هَذَا وَمَا كُنَّا لَهُ مُقْرِنِينَ وَإِنَّا إِلَىٰ رَبِّنَا لَمُنقَلِبُونَ",
            transliteration = "Subḥānalladhī sakh-khara lanā hādhā wa mā kunnā lahu muqrinīn, wa innā ilā Rabbinā lamunqalibūn",
            translation = "Glory to Him who has subjected this to us, and we could never have achieved it by ourselves, and indeed to our Lord we will return.",
            source = "Surah Az-Zukhruf 43:13-14 / Sahih Muslim (1342)",
            category = "Travel & Home"
        ),
        DhikrItem(
            id = "dua_parents",
            title = "Dua for Parents",
            arabic = "رَبِّ ارْحَمْهُمَا كَمَا رَبَّيَانِي صَغِيرًا",
            transliteration = "Rabbir-ḥamhumā kamā rabbayānī ṣaghīrā",
            translation = "My Lord, have mercy upon them both as they brought me up [when I was] small.",
            source = "Surah Al-Isra 17:24",
            category = "Family & Parents"
        ),
        DhikrItem(
            id = "dua_anxiety",
            title = "In Distress and Anxiety (Dua of Yunus)",
            arabic = "لَّا إِلَٰهَ إِلَّا أَنتَ سُبْحَانَكَ إِنِّي كُنتُ مِنَ الظَّالِمِينَ",
            transliteration = "Lā ilāha illā Anta subḥānaka innī kuntu minaẓ-ẓālimīn",
            translation = "There is no deity except You; exalted are You. Indeed, I have been of the wrongdoers.",
            source = "Surah Al-Anbiya 21:87 / Tirmidhi (3505)",
            virtue = "No Muslim supplicates with it for anything except that Allah answers him.",
            category = "Hardship & Relief"
        ),
        DhikrItem(
            id = "dua_mosque_enter",
            title = "Entering the Mosque",
            arabic = "اللَّهُمَّ افْتَحْ لِي أَبْوَابَ رَحْمَتِكَ",
            transliteration = "Allāhummaf-taḥ lī abwāba raḥmatik",
            translation = "O Allah, open for me the doors of Your mercy.",
            source = "Sahih Muslim (713)",
            category = "Worship & Masjid"
        ),
        DhikrItem(
            id = "dua_iftar",
            title = "Breaking the Fast (Iftar)",
            arabic = "ذَهَبَ الظَّمَأُ وَابْتَلَّتِ الْعُرُوقُ وَثَبَتَ الْأَجْرُ إِنْ شَاءَ اللَّهُ",
            transliteration = "Dhahabadh-dhama'u wabtallatil-'urūqu wa thabatal-ajru in shā' Allāh",
            translation = "The thirst has gone, the veins are moistened, and the reward is confirmed, if Allah wills.",
            source = "Abu Dawood (2357)",
            category = "Fasting & Ramadan"
        ),
        DhikrItem(
            id = "dua_laylatul_qadr",
            title = "Dua for Laylatul Qadr (The Night of Decree)",
            arabic = "اللَّهُمَّ إِنَّكَ عَفُوٌّ تُحِبُّ الْعَفْوَ فَاعْفُ عَنِّي",
            transliteration = "Allāhumma innaka 'Afuwwun tuḥibbul-'afwa fa'fu 'annī",
            translation = "O Allah, You are Most Forgiving, and You love forgiveness, so forgive me.",
            source = "Tirmidhi (3513), Ibn Majah (3850)",
            category = "Fasting & Ramadan"
        )
    )

    data class TasbihPhrase(
        val key: String,
        val arabic: String,
        val transliteration: String,
        val meaning: String,
        val defaultTarget: Int,
        val audioUrl: String = ""
    )

    val TASBIH_PHRASES = listOf(
        TasbihPhrase("SubhanAllah", "سُبْحَانَ اللَّهِ", "Subḥānallāh", "Glory be to Allah", 33),
        TasbihPhrase("Alhamdulillah", "الْحَمْدُ لِلَّهِ", "Al-ḥamdulillāh", "All praise is for Allah", 33),
        TasbihPhrase("Allahu Akbar", "اللَّهُ أَكْبَرُ", "Allāhu Akbar", "Allah is the Greatest", 34),
        TasbihPhrase("Astaghfirullah", "أَسْتَغْفِرُ اللَّهَ", "Astaghfirullāh", "I seek Allah's forgiveness", 100),
        TasbihPhrase("La ilaha illallah", "لَا إِلَٰهَ إِلَّا اللَّهُ", "Lā ilāha illā Allāh", "There is no god but Allah", 100),
        TasbihPhrase("Salawat", "اللَّهُمَّ صَلِّ عَلَىٰ مُحَمَّدٍ", "Allāhumma ṣalli 'alā Muḥammad", "Blessings upon the Prophet Muhammad (ﷺ)", 100),
        TasbihPhrase("Hawqala", "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ", "Lā ḥawla walā quwwata illā billāh", "There is no power nor might except with Allah", 100)
    )
}

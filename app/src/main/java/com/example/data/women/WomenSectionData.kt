package com.example.data.women

data class GuidanceTopic(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: String,
    val summary: String,
    val keyPoints: List<String>,
    val scholarlyReference: String
)

object WomenSectionData {

    val TOPICS = listOf(
        GuidanceTopic(
            id = "w_menstruation",
            title = "Menstruation (Hayd) & Worship Rulings",
            subtitle = "Divine mercy, exemptions, and alternative worship",
            category = "Fiqh & Worship",
            summary = "In Islamic law, menstruation is a natural biological state created by Allah. During this time, women are divinely exempted from ritual prayers (Salah) and fasting. These exemptions are a mercy and ease from Allah.",
            keyPoints = listOf(
                "Exemption from Salah: Women do not pray Salah during their period, and these missed prayers do NOT need to be made up later.",
                "Fasting in Ramadan: Fasting is paused during menses, but missed Ramadan fasts MUST be made up before the next Ramadan arrives.",
                "Forms of Worship Allowed: While ritual Salah and fasting pause, remembrance of Allah (Dhikr), Istighfar, Salawat upon the Prophet (ﷺ), Dua, listening to Quran recitation, and reading translation/tafsir remain strongly rewarded.",
                "Supplication at Prayer Times: It is commendable to sit on your prayer mat at prayer times and engage in Dhikr and personal Dua to maintain spiritual rhythm.",
                "End of Period (Ghusl): When the menstrual flow completely stops (indicated by clear discharge or complete dryness), a ritual bath (Ghusl) is performed, restoring eligibility for Salah and fasting."
            ),
            scholarlyReference = "Surah Al-Baqarah 2:222; Sahih al-Bukhari (304, 321); Sahih Muslim (335)."
        ),
        GuidanceTopic(
            id = "w_making_up_fasts",
            title = "Making Up Missed Fasts (Qadha)",
            subtitle = "Practical plan to repay Ramadan fasts with ease",
            category = "Fasting & Qadha",
            summary = "Aisha (may Allah be pleased with her) narrated: 'We were commanded to make up the fasts, but we were not commanded to make up the prayers.' (Sahih Muslim 335).",
            keyPoints = listOf(
                "Timeframe: Missed fasts can be made up any time between Shawwal and the following Sha'ban.",
                "Consecutive or Separate: It is permissible to fast missed days consecutively (back-to-back) or spread them out throughout the year (such as on Mondays and Thursdays or during winter months when days are short).",
                "Fasting in Winter: Prophet Muhammad (ﷺ) said: 'Winter is the spring of the believer: its days are short so he fasts, and its nights are long so he stands in prayer.'",
                "If delayed until next Ramadan without a valid excuse, scholars state one should make them up alongside giving fidya (feeding a poor person for each missed day)."
            ),
            scholarlyReference = "Sahih Muslim (335); Fiqh al-Sunnah; Al-Nawawi in Majmu'."
        ),
        GuidanceTopic(
            id = "w_nifas",
            title = "Postpartum Bleeding (Nifas)",
            subtitle = "Rulings and self-care after childbirth",
            category = "Fiqh & Worship",
            summary = "Postpartum bleeding (Nifas) refers to bleeding following delivery. The mother is completely relieved of prayers and fasting during this critical healing window.",
            keyPoints = listOf(
                "Maximum Duration: According to the majority of scholars (including the Hanafi and Hanbali schools), the maximum duration of Nifas is 40 days, based on the Hadith of Umm Salamah.",
                "Early Cessation: If bleeding stops completely before 40 days, the woman performs Ghusl and resumes Salah and fasting.",
                "Gentle Recovery: Islam emphasizes rest, family support, and gentle nutrition for the postpartum mother."
            ),
            scholarlyReference = "Sunan Abi Dawood (311); Sunan al-Tirmidhi (139)."
        ),
        GuidanceTopic(
            id = "w_motherhood",
            title = "The High Rank of Mothers in Islam",
            subtitle = "Honor, devotion, and celestial status",
            category = "Spiritual Status",
            summary = "Islam elevated mothers to the highest degrees of veneration and kindness, dedicating three-quarters of filial devotion specifically to the mother.",
            keyPoints = listOf(
                "A man asked the Prophet (ﷺ): 'Who is most worthy of my good companionship?' The Prophet replied: 'Your mother.' The man asked, 'Then who?' He replied, 'Your mother.' The man asked, 'Then who?' He replied, 'Your mother.' The man asked, 'Then who?' He replied, 'Then your father.' (Bukhari & Muslim)",
                "Prophet Muhammad (ﷺ) said to a companion seeking Jihad: 'Stay with your mother, for Paradise lies beneath her feet.' (Sunan an-Nasa'i 3104)",
                "Every nursing session, night of care, and quiet patience is inscribed as ongoing righteousness."
            ),
            scholarlyReference = "Sahih al-Bukhari (5971); Sahih Muslim (2548); Sunan an-Nasa'i (3104)."
        ),
        GuidanceTopic(
            id = "w_modesty",
            title = "Modesty (Haya) & Inner Beauty",
            subtitle = "The radiant jewel of character",
            category = "Character & Lifestyle",
            summary = "Haya is a comprehensive virtue encompassing humility before Allah, dignity of speech, modest attire, and pure demeanor.",
            keyPoints = listOf(
                "The Prophet (ﷺ) said: 'Modesty produces nothing but good.' (Sahih al-Bukhari 6117)",
                "The outer dress reflects inner devotion and divine obedience, liberating the soul from superficial societal standards.",
                "Modesty is not weakness or silence; rather, it is elevated dignity, self-respect, and moral strength."
            ),
            scholarlyReference = "Surah An-Nur 24:31; Surah Al-Ahzab 33:59; Sahih al-Bukhari (6117)."
        ),
        GuidanceTopic(
            id = "w_duas_family",
            title = "Duas for Family, Children & Pregnancy",
            subtitle = "Prophetic and Quranic supplications",
            category = "Duas & Supplications",
            summary = "Powerful Quranic prayers recited by the Prophets for righteous offspring and peaceful family life.",
            keyPoints = listOf(
                "Dua for Righteous Family: 'Rabbana hab lana min azwajina wa dhurriyyatina qurrata a'yunin waj'alna lil-muttaqina imama' (Our Lord, grant us from among our spouses and offspring comfort to our eyes and make us an example for the righteous. - Quran 25:74)",
                "Dua of Ibrahim (AS) for Children: 'Rabbi hab li minas-salihin' (My Lord, grant me [a child] from among the righteous. - Quran 37:100)",
                "Dua for Safety of Newborn: Prophet (ﷺ) would seek protection for Hasan and Husayn with: 'U'idhukuma bi-kalimatillahi at-tammah, min kulli shaytanin wa hammah, wa min kulli 'aynin lammah'."
            ),
            scholarlyReference = "Quran 25:74; Quran 37:100; Sahih al-Bukhari (3371)."
        )
    )
}

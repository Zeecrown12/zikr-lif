package com.example.data.community

data class CommunityChallenge(
    val id: String,
    val title: String,
    val subtitle: String,
    val targetCount: Long,
    var currentCount: Long,
    val unit: String,
    val iconName: String,
    val description: String
)

data class CommunityReflection(
    val id: String,
    val author: String,
    val location: String,
    val content: String,
    val timestamp: String,
    val likesCount: Int
)

object CommunityData {

    val INITIAL_CHALLENGES = listOf(
        CommunityChallenge(
            id = "global_salawat",
            title = "Global Ummah Salawat Goal",
            subtitle = "Sending millions of blessings upon the Prophet (ﷺ)",
            targetCount = 100000L,
            currentCount = 74380L,
            unit = "Salawat Recited",
            iconName = "Spa",
            description = "Join hundreds of thousands of believers worldwide. Every time you recite Salawat, tap below to add to our collective total!"
        ),
        CommunityChallenge(
            id = "friday_kahf",
            title = "Friday Surah Al-Kahf Circle",
            subtitle = "Illuminating our week with divine light",
            targetCount = 20000L,
            currentCount = 15420L,
            unit = "Readers Completed",
            iconName = "MenuBook",
            description = "Prophet Muhammad (ﷺ) said: 'Whoever reads Surah Al-Kahf on Friday will have a light shining between him and the Ancient House.' (Bayhaqi)"
        ),
        CommunityChallenge(
            id = "daily_istighfar",
            title = "Worldwide Istighfar Wave",
            subtitle = "Seeking Allah's forgiveness for ourselves and the Ummah",
            targetCount = 50000L,
            currentCount = 38910L,
            unit = "Istighfar Pledged",
            iconName = "Loop",
            description = "A collective initiative to purify hearts and seek ease, rain, and relief from difficulties across our world."
        )
    )

    val REFLECTIONS = listOf(
        CommunityReflection(
            id = "ref_1",
            author = "Brother Zayd",
            location = "London, UK",
            content = "SubhanAllah, whenever life feels heavy, performing wudu and offering two rak'ahs with sincere sujud washes away every tension. Never delay your prayer.",
            timestamp = "Today at 7:15 AM",
            likesCount = 342
        ),
        CommunityReflection(
            id = "ref_2",
            author = "Sister Maryam",
            location = "Kuala Lumpur, MY",
            content = "Started doing the Morning Adhkar consistently for 2 weeks. The sense of barakah and calm that fills the morning hours is truly miraculous. Keep going even if you feel tired!",
            timestamp = "Today at 8:40 AM",
            likesCount = 519
        ),
        CommunityReflection(
            id = "ref_3",
            author = "Imam Abdullah",
            location = "Cairo, EG",
            content = "The Prophet (ﷺ) reminded us that Allah looks not at your bodies or your appearances, but looks at your hearts and your deeds. Keep your worship sincere.",
            timestamp = "Yesterday",
            likesCount = 890
        ),
        CommunityReflection(
            id = "ref_4",
            author = "Sister Fatima",
            location = "Toronto, CA",
            content = "Remember that a small deed done regularly is more beloved to Allah than a massive deed done once. Don't be discouraged by setbacks; Allah loves the repentant.",
            timestamp = "Yesterday",
            likesCount = 421
        )
    )
}

package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.calendar.IslamicCalendarData
import com.example.data.community.CommunityData
import com.example.data.habits.HabitData
import com.example.data.prayer.PrayerCalculator
import com.example.data.prayer.PrayerTimes
import com.example.data.repository.ZikrPreferencesRepository
import com.example.ui.components.SectionHeader
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(
    repository: ZikrPreferencesRepository,
    onNavigateToQuran: () -> Unit,
    onNavigateToDhikrTab: (Int) -> Unit, // 0: Tasbih, 1: Morning, 2: Evening, 3: Mathurat, 4: Duas
    onNavigateToHabits: () -> Unit,
    onNavigateToCommunity: () -> Unit,
    onNavigateToRamadan: () -> Unit,
    onNavigateToAllahNames: () -> Unit,
    onNavigateToQibla: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToHisnulMuslim: (Int?) -> Unit
) {
    val currentCity by repository.currentCity.collectAsState()
    val calcMethod by repository.calculationMethod.collectAsState()
    val juristicMethod by repository.juristicMethod.collectAsState()
    val todayHabits by repository.todayHabits.collectAsState()

    var now by remember { mutableStateOf(LocalTime.now()) }
    val todayDate = remember { LocalDate.now() }
    val hijriDate = remember(todayDate) { IslamicCalendarData.getEstimatedHijriDate(todayDate) }

    // Live 1-second ticker for countdown
    LaunchedEffect(Unit) {
        while (true) {
            now = LocalTime.now()
            delay(1000)
        }
    }

    val prayerTimes = remember(currentCity, calcMethod, juristicMethod, todayDate) {
        PrayerCalculator.calculateTimes(
            latitude = currentCity.latitude,
            longitude = currentCity.longitude,
            timeZoneOffset = currentCity.timeZoneOffsetHours,
            date = todayDate,
            method = calcMethod,
            juristicMethod = juristicMethod
        )
    }

    val (nextPrayerName, nextPrayerTime) = prayerTimes.getNextPrayer(now)

    // Calculate countdown string
    val countdownText = remember(now, nextPrayerTime) {
        val nowSec = now.toSecondOfDay()
        val targetSec = nextPrayerTime.toSecondOfDay()
        val diffSec = if (targetSec >= nowSec) {
            targetSec - nowSec
        } else {
            (24 * 3600 - nowSec) + targetSec
        }
        val hours = diffSec / 3600
        val mins = (diffSec % 3600) / 60
        val secs = diffSec % 60
        String.format("%02d:%02d:%02d", hours, mins, secs)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBg),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // 1. Top Header & Greeting
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(EmeraldDark, EmeraldPrimary)
                        )
                    )
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Assalamu Alaikum",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = "“Remember Allah. Grow Together.”",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = GoldLight,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }

                        Row {
                            IconButton(
                                onClick = onNavigateToQibla,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.15f))
                                    .testTag("home_qibla_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Explore,
                                    contentDescription = "Qibla",
                                    tint = GoldLight
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(
                                onClick = onNavigateToSettings,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.15f))
                                    .testTag("home_settings_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Settings",
                                    tint = Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Islamic Date & Location Badge
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.12f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CalendarMonth,
                                    contentDescription = null,
                                    tint = GoldLight,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = hijriDate.formatted(),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { onNavigateToSettings() }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = EmeraldSoft,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = currentCity.name,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = EmeraldPale,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2. Next Prayer Countdown Hero Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    // Optional Mosque illustration banner
                    Image(
                        painter = painterResource(id = R.drawable.hero_mosque),
                        contentDescription = "Mosque at Dawn",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    )

                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        EmeraldDark.copy(alpha = 0.85f),
                                        EmeraldDark
                                    )
                                )
                            )
                    )

                    // Countdown details
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(28.dp))
                        Surface(
                            color = GoldAccent,
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.padding(bottom = 6.dp)
                        ) {
                            Text(
                                text = "NEXT PRAYER: $nextPrayerName",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                ),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }

                        Text(
                            text = countdownText,
                            style = MaterialTheme.typography.headlineLarge.copy(
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 2.sp
                            )
                        )

                        Text(
                            text = "at ${prayerTimes.formatted(nextPrayerTime)}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = EmeraldPale
                            )
                        )
                    }
                }
            }
        }

        // 3. Five Daily Prayer Times Carousel/Row
        item {
            SectionHeader(
                title = "Today's Prayer Times",
                subtitle = "Calculated via ${calcMethod.displayName}",
                actionLabel = "Adjust",
                onActionClick = onNavigateToSettings
            )

            val prayersList = listOf(
                "Fajr" to prayerTimes.fajr,
                "Sunrise" to prayerTimes.sunrise,
                "Dhuhr" to prayerTimes.dhuhr,
                "Asr" to prayerTimes.asr,
                "Maghrib" to prayerTimes.maghrib,
                "Isha" to prayerTimes.isha
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(prayersList) { (name, time) ->
                    val isNext = name == nextPrayerName
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isNext) EmeraldPrimary else Color.White
                        ),
                        border = if (isNext) null else CardDefaults.outlinedCardBorder(),
                        elevation = CardDefaults.cardElevation(if (isNext) 4.dp else 1.dp),
                        modifier = Modifier
                            .width(105.dp)
                            .testTag("prayer_card_$name")
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp, horizontal = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = name,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isNext) GoldLight else EmeraldDark
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = prayerTimes.formatted(time),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isNext) Color.White else TextDark
                                )
                            )
                        }
                    }
                }
            }
        }

        // 4. Daily Islamic Reminder Card
        item {
            Spacer(modifier = Modifier.height(14.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = EmeraldPale.copy(alpha = 0.6f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = GoldAccent,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "DAILY INSPIRATION",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldDark,
                                    letterSpacing = 1.sp
                                )
                            )
                        }
                        Text(
                            text = "Surah Ar-Ra'd (13:28)",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "الَّذِينَ آمَنُوا وَتَطْمَئِنُّ قُلُوبُهُم بِذِكْرِ اللَّهِ ۗ أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = EmeraldDark,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "“Those who have believed and whose hearts are assured by the remembrance of Allah. Unquestionably, by the remembrance of Allah hearts are assured.”",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextDark,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    )
                }
            }
        }

        // 5. Quick Access Grid
        item {
            Spacer(modifier = Modifier.height(18.dp))
            SectionHeader(
                title = "Quick Access",
                subtitle = "Dhikr, Quran, Duas & Daily Worship"
            )

            val quickActions = listOf(
                QuickActionItem("Holy Quran", Icons.Default.MenuBook, EmeraldPrimary) { onNavigateToQuran() },
                QuickActionItem("Hisnul Muslim", Icons.Default.Shield, EmeraldDark) { onNavigateToHisnulMuslim(null) },
                QuickActionItem("Digital Tasbih", Icons.Default.TouchApp, GoldAccent) { onNavigateToDhikrTab(0) },
                QuickActionItem("Morning Adhkar", Icons.Default.WbTwilight, Color(0xFF0284C7)) { onNavigateToDhikrTab(1) },
                QuickActionItem("Evening Adhkar", Icons.Default.NightsStay, Color(0xFF6366F1)) { onNavigateToDhikrTab(2) },
                QuickActionItem("Al-Ma'thurat", Icons.Default.Bookmark, EmeraldMedium) { onNavigateToDhikrTab(3) },
                QuickActionItem("Daily Duas", Icons.Default.VolunteerActivism, Color(0xFFE11D48)) { onNavigateToDhikrTab(4) },
                QuickActionItem("Ramadan", Icons.Default.Star, GoldAccent) { onNavigateToRamadan() }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                for (row in quickActions.chunked(4)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        for (item in row) {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { item.onClick() }
                                    .testTag("quick_action_${item.title.replace(" ", "_").lowercase()}")
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp, horizontal = 4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(CircleShape)
                                            .background(item.color.copy(alpha = 0.12f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = item.icon,
                                            contentDescription = item.title,
                                            tint = item.color,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = item.title,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            color = TextDark,
                                            textAlign = TextAlign.Center
                                        ),
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Dedicated Home Section: Hisnul Muslim — Fortress of the Muslim (CRITICAL REQUIREMENT 1)
        item {
            Spacer(modifier = Modifier.height(18.dp))
            SectionHeader(
                title = "Hisnul Muslim — Fortress of the Muslim",
                subtitle = "Authentic Supplications • Sa'id ibn Wahf al-Qahtani",
                actionLabel = "View All",
                onActionClick = { onNavigateToHisnulMuslim(null) }
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable { onNavigateToHisnulMuslim(null) }
                    .testTag("hisnul_muslim_hero_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = EmeraldDark),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = GoldAccent,
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = "حصن المسلم • 26 CHAPTERS",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                ),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = GoldLight,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Fortress of the Muslim",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Complete authentic collection of daily remembrances and supplications from the Quran and Sunnah, categorized for every occasion with Arabic, English, transliteration, and audio.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = EmeraldPale,
                            lineHeight = 18.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Featured quick chapter chips
                    val featuredChapters = listOf(
                        1 to "Waking Up",
                        14 to "Before Sleep",
                        7 to "In Prayer",
                        19 to "Distress & Anxiety",
                        21 to "Ruqyah & Healing",
                        17 to "Travel & Vehicle",
                        24 to "Sayyid al-Istighfar"
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(featuredChapters) { (chapterId, name) ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White.copy(alpha = 0.15f),
                                modifier = Modifier.clickable { onNavigateToHisnulMuslim(chapterId) }
                            ) {
                                Text(
                                    text = name,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { onNavigateToHisnulMuslim(null) },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("open_hisnul_muslim_btn")
                    ) {
                        Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Open Fortress of the Muslim", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 6. Daily Habits Checklist Preview
        item {
            Spacer(modifier = Modifier.height(18.dp))
            SectionHeader(
                title = "Daily Islamic Habits",
                subtitle = "${todayHabits.size} of ${HabitData.HABITS.size} completed today",
                actionLabel = "View All",
                onActionClick = onNavigateToHabits
            )

            val progressRatio = todayHabits.size.toFloat() / HabitData.HABITS.size.toFloat()

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Today's Worship Progress",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "${(progressRatio * 100).toInt()}%",
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = EmeraldPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { progressRatio },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = EmeraldPrimary,
                        trackColor = EmeraldContainer
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // 4 Quick toggles
                    val primaryHabits = HabitData.HABITS.take(4)
                    for (habit in primaryHabits) {
                        val isDone = todayHabits.contains(habit.id)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { repository.toggleHabit(habit.id) }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = isDone,
                                    onCheckedChange = { repository.toggleHabit(habit.id) },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = EmeraldPrimary,
                                        checkmarkColor = Color.White
                                    )
                                )
                                Text(
                                    text = habit.title,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = if (isDone) FontWeight.Normal else FontWeight.Medium,
                                        color = if (isDone) TextMuted else TextDark
                                    )
                                )
                            }
                            if (isDone) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = EmeraldMedium,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // 7. Community Challenge Highlight
        item {
            Spacer(modifier = Modifier.height(18.dp))
            SectionHeader(
                title = "Our Ummah Challenge",
                subtitle = "Join collective worship goals",
                actionLabel = "Community",
                onActionClick = onNavigateToCommunity
            )

            var localSalawatAdd by remember { mutableStateOf(0L) }
            val challenge = CommunityData.INITIAL_CHALLENGES.first()
            val totalSalawat = challenge.currentCount + localSalawatAdd

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(GoldContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Spa,
                                contentDescription = null,
                                tint = GoldAccent,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = challenge.title,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = challenge.subtitle,
                                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "%,d / %,d".format(totalSalawat, challenge.targetCount),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = EmeraldDark
                            )
                        )
                        Button(
                            onClick = {
                                localSalawatAdd += 10
                                repository.addTasbihCount(10)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.testTag("home_add_salawat_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("+10 Salawat")
                        }
                    }
                }
            }
        }
    }
}

private data class QuickActionItem(
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val onClick: () -> Unit
)

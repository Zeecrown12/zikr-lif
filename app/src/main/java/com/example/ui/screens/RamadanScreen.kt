package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.prayer.PrayerCalculator
import com.example.data.ramadan.RamadanData
import com.example.data.repository.ZikrPreferencesRepository
import com.example.ui.components.ArabicTextView
import com.example.ui.components.SectionHeader
import com.example.ui.components.ZikrTopBar
import com.example.ui.theme.*

@Composable
fun RamadanScreen(
    repository: ZikrPreferencesRepository,
    onBack: () -> Unit
) {
    val currentCity by repository.currentCity.collectAsState()
    val calcMethod by repository.calculationMethod.collectAsState()
    val juristicMethod by repository.juristicMethod.collectAsState()
    val missedFasts by repository.missedFastsCount.collectAsState()

    val prayerTimes = remember(currentCity, calcMethod, juristicMethod) {
        PrayerCalculator.calculateTimes(
            latitude = currentCity.latitude,
            longitude = currentCity.longitude,
            timeZoneOffset = currentCity.timeZoneOffsetHours,
            method = calcMethod,
            juristicMethod = juristicMethod
        )
    }

    val fastedDays = remember { mutableStateMapOf<Int, Boolean>() }
    val quranJuzCompleted = remember { mutableStateMapOf<Int, Boolean>() }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBg),
        contentPadding = PaddingValues(bottom = 60.dp)
    ) {
        item {
            ZikrTopBar(
                title = "Ramadan Companion",
                subtitle = "Fasting, Khatam & Blessed Deeds",
                showBack = true,
                onBackClick = onBack
            )
        }

        // Suhoor & Iftar Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = EmeraldDark),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Today's Fasting Times",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Text(
                            text = currentCity.name,
                            style = MaterialTheme.typography.bodySmall.copy(color = GoldLight)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Suhoor End (Fajr)
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "SUHOOR ENDS",
                                style = MaterialTheme.typography.labelSmall.copy(color = EmeraldPale)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = prayerTimes.formatted(prayerTimes.fajr),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }

                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(40.dp)
                                .background(Color.White.copy(alpha = 0.2f))
                        )

                        // Iftar (Maghrib)
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "IFTAR (MAGHRIB)",
                                style = MaterialTheme.typography.labelSmall.copy(color = GoldLight)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = prayerTimes.formatted(prayerTimes.maghrib),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = GoldLight
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Surface(
                        color = Color.White.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Dua for Iftar: ذَهَبَ الظَّمَأُ وَابْتَلَّتِ الْعُرُوقُ وَثَبَتَ الْأَجْرُ إِنْ شَاءَ اللَّهُ\n“The thirst has gone, the veins are moistened, and the reward is confirmed, if Allah wills.”",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.White),
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
        }

        // Fasting Tracker & Missed Fasts (Qadha) Stepper
        item {
            SectionHeader(
                title = "Fasting & Qadha Tracker",
                subtitle = "Keep track of completed and missed fasts"
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Missed Fasts to Make Up (Qadha)",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Private on-device counter",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { repository.setMissedFasts(missedFasts - 1) },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldPale)
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = EmeraldDark)
                            }
                            Text(
                                text = "$missedFasts",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )
                            IconButton(
                                onClick = { repository.setMissedFasts(missedFasts + 1) },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldPale)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Increase", tint = EmeraldDark)
                            }
                        }
                    }
                }
            }
        }

        // 30-Day Quran Khatam Reading Goal
        item {
            Spacer(modifier = Modifier.height(18.dp))
            SectionHeader(
                title = "30-Day Quran Khatam Plan",
                subtitle = "${quranJuzCompleted.count { it.value }} of 30 Juz Completed"
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Read 1 Juz each day to complete the Holy Quran during the blessed month.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextDark)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    val completedJuzCount = quranJuzCompleted.count { it.value }
                    LinearProgressIndicator(
                        progress = { completedJuzCount.toFloat() / 30f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = EmeraldPrimary,
                        trackColor = EmeraldContainer
                    )
                }
            }
        }

        // Last 10 Nights & Laylat al-Qadr
        item {
            Spacer(modifier = Modifier.height(18.dp))
            SectionHeader(
                title = "The Blessed Last Ten Nights",
                subtitle = "Seeking Laylat al-Qadr (The Night of Decree)"
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = GoldContainer),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldLight)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = GoldMuted)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Special Supplication for Laylat al-Qadr",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = GoldMuted
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    ArabicTextView(
                        text = "اللَّهُمَّ إِنَّكَ عَفُوٌّ تُحِبُّ الْعَفْوَ فَاعْفُ عَنِّي",
                        fontSize = 20.sp,
                        color = GoldMuted
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Allāhumma innaka 'Afuwwun tuḥibbul-'afwa fa'fu 'annī",
                        style = MaterialTheme.typography.bodySmall.copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                    )
                    Text(
                        text = "“O Allah, You are Most Forgiving and love to forgive, so forgive me.” (Tirmidhi)",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextDark, fontWeight = FontWeight.Medium)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    for (tip in RamadanData.LAST_TEN_NIGHTS_TIPS) {
                        Row(
                            modifier = Modifier.padding(vertical = 2.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text("• ", color = GoldMuted, fontWeight = FontWeight.Bold)
                            Text(tip, style = MaterialTheme.typography.bodySmall.copy(color = TextDark))
                        }
                    }
                }
            }
        }

        // Daily Ramadan Plan (Days 1 to 30)
        item {
            Spacer(modifier = Modifier.height(18.dp))
            SectionHeader(
                title = "30-Day Ramadan Journey",
                subtitle = "Daily Juz, dua, and charity suggestions"
            )
        }

        items(RamadanData.RAMADAN_30_DAYS) { plan ->
            val isFasted = fastedDays[plan.dayNumber] ?: false
            val isJuzDone = quranJuzCompleted[plan.dayNumber] ?: false

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldPale),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${plan.dayNumber}",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldDark
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Day ${plan.dayNumber} • ${plan.keySurah}",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = plan.theme,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = if (plan.dayNumber >= 21) GoldAccent else TextMuted
                                    )
                                )
                            }
                        }

                        // Fasted Toggle Checkbox
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Fasted", style = MaterialTheme.typography.labelSmall)
                            Checkbox(
                                checked = isFasted,
                                onCheckedChange = { fastedDays[plan.dayNumber] = it },
                                colors = CheckboxDefaults.colors(checkedColor = EmeraldPrimary)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Charity idea: ${plan.charityGoal}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = EmeraldPrimary,
                            fontWeight = FontWeight.Medium
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Daily Dua: ${plan.duaMeaning}",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextDark),
                            modifier = Modifier.weight(1f)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Juz Read", style = MaterialTheme.typography.labelSmall)
                            Checkbox(
                                checked = isJuzDone,
                                onCheckedChange = { quranJuzCompleted[plan.dayNumber] = it },
                                colors = CheckboxDefaults.colors(checkedColor = GoldAccent)
                            )
                        }
                    }
                }
            }
        }
    }
}

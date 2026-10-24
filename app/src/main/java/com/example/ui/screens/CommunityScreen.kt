package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.community.CommunityData
import com.example.data.repository.ZikrPreferencesRepository
import com.example.ui.components.SectionHeader
import com.example.ui.components.ZikrTopBar
import com.example.ui.theme.*

@Composable
fun CommunityScreen(
    repository: ZikrPreferencesRepository,
    onNavigateToRamadan: () -> Unit,
    onNavigateToAllahNames: () -> Unit,
    onNavigateToWomen: () -> Unit,
    onNavigateToCalendar: () -> Unit,
    onNavigateToQibla: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    var salawatContribution by remember { mutableStateOf(0L) }
    var likedIds by remember { mutableStateOf(setOf<String>()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBg),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        item {
            ZikrTopBar(
                title = "Our Ummah & More",
                subtitle = "Shared Challenges, Ramadan & Knowledge",
                onSettingsClick = onNavigateToSettings,
                onQiblaClick = onNavigateToQibla
            )
        }

        // More Islamic Modules Hub (Ramadan, 99 Names, Calendar, Women's Section, Qibla)
        item {
            Spacer(modifier = Modifier.height(8.dp))
            SectionHeader(
                title = "Islamic Lifestyle Hub",
                subtitle = "Explore specialized Islamic features"
            )

            val modules = listOf(
                Triple("Ramadan Companion", Icons.Default.Star, GoldAccent) to onNavigateToRamadan,
                Triple("99 Names of Allah", Icons.Default.AutoAwesome, EmeraldDark) to onNavigateToAllahNames,
                Triple("Islamic Calendar", Icons.Default.CalendarMonth, Color(0xFF0284C7)) to onNavigateToCalendar,
                Triple("Women & Girls Guide", Icons.Default.Favorite, Color(0xFFE11D48)) to onNavigateToWomen,
                Triple("Qibla Compass", Icons.Default.Explore, EmeraldMedium) to onNavigateToQibla,
                Triple("App Settings", Icons.Default.Settings, TextDark) to onNavigateToSettings
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (chunk in modules.chunked(2)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        for ((item, onClick) in chunk) {
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onClick() }
                                    .testTag("hub_card_${item.first.replace(" ", "_").lowercase()}"),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(item.third.copy(alpha = 0.12f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = item.second,
                                            contentDescription = item.first,
                                            tint = item.third,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = item.first,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            color = TextDark
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

        // Shared Worship Challenges
        item {
            Spacer(modifier = Modifier.height(18.dp))
            SectionHeader(
                title = "Ummah Worship Challenges",
                subtitle = "Multiply your rewards with the worldwide community"
            )
        }

        items(CommunityData.INITIAL_CHALLENGES) { challenge ->
            val count = if (challenge.id == "global_salawat") {
                challenge.currentCount + salawatContribution
            } else {
                challenge.currentCount
            }
            val progress = count.toFloat() / challenge.targetCount.toFloat()

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
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
                                .background(EmeraldPale),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (challenge.iconName) {
                                    "Spa" -> Icons.Default.Spa
                                    "MenuBook" -> Icons.Default.MenuBook
                                    else -> Icons.Default.Loop
                                },
                                contentDescription = null,
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = challenge.title,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = challenge.subtitle,
                                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = challenge.description,
                        style = MaterialTheme.typography.bodySmall.copy(color = TextDark)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "%,d / %,d %s".format(count, challenge.targetCount, challenge.unit),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = EmeraldDark
                            )
                        )
                        Text(
                            text = "${(progress * 100).toInt()}%",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = GoldAccent
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    LinearProgressIndicator(
                        progress = { progress.coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = EmeraldPrimary,
                        trackColor = EmeraldContainer
                    )

                    if (challenge.id == "global_salawat") {
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                salawatContribution += 10
                                repository.addTasbihCount(10)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("challenge_add_salawat_btn"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Pledge +10 Salawat to Ummah Total")
                        }
                    }
                }
            }
        }

        // Inspiring Ummah Reflections
        item {
            Spacer(modifier = Modifier.height(18.dp))
            SectionHeader(
                title = "Inspiring Reminders & Reflections",
                subtitle = "Uplifting messages from fellow Muslims"
            )
        }

        items(CommunityData.REFLECTIONS) { reflection ->
            val hasLiked = likedIds.contains(reflection.id)
            val likes = reflection.likesCount + if (hasLiked) 1 else 0

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = reflection.author.take(1),
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldDark
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = reflection.author,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextDark
                                    )
                                )
                                Text(
                                    text = "${reflection.location} • ${reflection.timestamp}",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = reflection.content,
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextDark, lineHeight = 22.sp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                likedIds = if (hasLiked) likedIds - reflection.id else likedIds + reflection.id
                            }
                        ) {
                            Icon(
                                imageVector = if (hasLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Like",
                                tint = if (hasLiked) Color(0xFFE11D48) else TextMuted
                            )
                        }
                        Text(
                            text = "$likes",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                        )
                    }
                }
            }
        }

        // Privacy Guarantee Box
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = CreamSurface)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = EmeraldDark)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Privacy Guarantee: Your worship logs, prayers, and habits are kept strictly private on your phone. Community challenges only aggregate count totals.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextDark)
                    )
                }
            }
        }
    }
}

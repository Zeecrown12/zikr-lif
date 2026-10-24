package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import com.example.data.repository.ZikrPreferencesRepository
import com.example.data.women.GuidanceTopic
import com.example.data.women.WomenSectionData
import com.example.ui.components.SectionHeader
import com.example.ui.components.ZikrTopBar
import com.example.ui.theme.*

@Composable
fun WomenScreen(
    repository: ZikrPreferencesRepository,
    onBack: () -> Unit
) {
    val missedFasts by repository.missedFastsCount.collectAsState()
    var isPrivateTrackerEnabled by remember { mutableStateOf(false) }
    val expandedTopics = remember { mutableStateMapOf<String, Boolean>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBg)
    ) {
        ZikrTopBar(
            title = "Women & Girls in Islam",
            subtitle = "Respectful Islamic Guidance & Fiqh",
            showBack = true,
            onBackClick = onBack
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Introductory respectful banner
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = EmeraldPale.copy(alpha = 0.7f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Spa, contentDescription = null, tint = EmeraldDark)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Sacred Guidance with Dignity & Mercy",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldDark
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Islamic rulings regarding menstruation, postpartum recovery, and motherhood are rooted in divine compassion, spiritual wisdom, and practical ease.",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextDark)
                        )
                    }
                }
            }

            // Optional Private Missed Fast (Qadha) Tracker (Disabled by default)
            item {
                Card(
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
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Private Missed Fasts (Qadha) Tracker",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "Disabled by default • Strictly stored on-device",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                                )
                            }
                            Switch(
                                checked = isPrivateTrackerEnabled,
                                onCheckedChange = { isPrivateTrackerEnabled = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = EmeraldPrimary
                                ),
                                modifier = Modifier.testTag("women_tracker_switch")
                            )
                        }

                        AnimatedVisibility(visible = isPrivateTrackerEnabled) {
                            Column(modifier = Modifier.padding(top = 14.dp)) {
                                Divider(color = CreamBorder)
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Days to Make Up:",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        IconButton(
                                            onClick = { repository.setMissedFasts(missedFasts - 1) },
                                            modifier = Modifier
                                                .size(34.dp)
                                                .clip(CircleShape)
                                                .background(EmeraldPale)
                                        ) {
                                            Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = EmeraldDark)
                                        }
                                        Text(
                                            text = "$missedFasts days",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = EmeraldDark
                                            ),
                                            modifier = Modifier.padding(horizontal = 12.dp)
                                        )
                                        IconButton(
                                            onClick = { repository.setMissedFasts(missedFasts + 1) },
                                            modifier = Modifier
                                                .size(34.dp)
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
                }
            }

            // Topics list
            items(WomenSectionData.TOPICS, key = { it.id }) { topic ->
                val isExpanded = expandedTopics[topic.id] ?: false

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expandedTopics[topic.id] = !isExpanded }
                        .testTag("women_topic_${topic.id}")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Surface(
                                    color = GoldContainer,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = topic.category,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = GoldMuted,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = topic.title,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldDark
                                    )
                                )
                                Text(
                                    text = topic.subtitle,
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                                )
                            }

                            Icon(
                                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = if (isExpanded) "Collapse" else "Expand",
                                tint = EmeraldPrimary
                            )
                        }

                        AnimatedVisibility(visible = isExpanded) {
                            Column(modifier = Modifier.padding(top = 12.dp)) {
                                Divider(color = CreamBorder)
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = topic.summary,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = TextDark,
                                        lineHeight = 22.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Key Guidelines & Takeaways:",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldDark
                                    )
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                for (point in topic.keyPoints) {
                                    Row(
                                        modifier = Modifier.padding(vertical = 3.dp),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Text(
                                            text = "• ",
                                            color = EmeraldPrimary,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = point,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = TextDark,
                                                lineHeight = 18.sp
                                            )
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Surface(
                                    color = CreamSurface,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Sources: ${topic.scholarlyReference}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = TextMuted,
                                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                        ),
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

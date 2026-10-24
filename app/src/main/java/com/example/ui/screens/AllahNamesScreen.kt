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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.allahnames.AllahName
import com.example.data.allahnames.AllahNamesData
import com.example.ui.components.ArabicTextView
import com.example.ui.components.ZikrTopBar
import com.example.ui.theme.*

@Composable
fun AllahNamesScreen(
    onBack: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val memorizedMap = remember { mutableStateMapOf<Int, Boolean>() }

    val filtered = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            AllahNamesData.NAMES
        } else {
            AllahNamesData.NAMES.filter {
                it.english.contains(searchQuery, ignoreCase = true) ||
                        it.transliteration.contains(searchQuery, ignoreCase = true) ||
                        it.arabic.contains(searchQuery) ||
                        it.number.toString() == searchQuery.trim()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBg)
    ) {
        ZikrTopBar(
            title = "99 Names of Allah",
            subtitle = "Asma ul-Husna • The Most Beautiful Names",
            showBack = true,
            onBackClick = onBack
        )

        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("allah_names_search_field"),
                placeholder = { Text("Search by name, meaning or number...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = EmeraldDark) },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EmeraldPrimary,
                    unfocusedBorderColor = CreamBorder,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                color = EmeraldPale.copy(alpha = 0.6f),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = EmeraldDark, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Prophet Muhammad (ﷺ) said: 'Allah has ninety-nine names; whoever memorizes and lives by them will enter Paradise.' (Sahih al-Bukhari)",
                        style = MaterialTheme.typography.bodySmall.copy(color = EmeraldDark)
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filtered, key = { it.number }) { item ->
                val isMemorized = memorizedMap[item.number] ?: false

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = if (isMemorized) androidx.compose.foundation.BorderStroke(1.dp, EmeraldLight) else null,
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(GoldContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${item.number}",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = GoldMuted
                                    )
                                )
                            }

                            // Memorized Toggle
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable {
                                    memorizedMap[item.number] = !isMemorized
                                }
                            ) {
                                Text(
                                    text = if (isMemorized) "Memorized" else "Mark Learned",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (isMemorized) EmeraldMedium else TextMuted
                                    )
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = if (isMemorized) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                    contentDescription = null,
                                    tint = if (isMemorized) EmeraldMedium else TextMuted,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Large Arabic Display
                        ArabicTextView(
                            text = item.arabic,
                            fontSize = 28.sp,
                            color = EmeraldDark
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "${item.transliteration} — ${item.english}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = item.meaning,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextMedium,
                                lineHeight = 20.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Mentioned in Quran: Surah ${item.quranRef}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = GoldAccent,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }
        }
    }
}

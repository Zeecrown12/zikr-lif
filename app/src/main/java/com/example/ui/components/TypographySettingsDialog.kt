package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.quran.QuranReadingMode
import com.example.data.repository.ZikrPreferencesRepository
import com.example.ui.theme.*

@Composable
fun TypographySettingsDialog(
    repository: ZikrPreferencesRepository,
    onDismiss: () -> Unit
) {
    val selectedArabicFont by repository.arabicFont.collectAsState()
    val selectedEnglishFont by repository.englishFont.collectAsState()
    val quranFontSize by repository.quranFontSize.collectAsState()
    val translationFontSize by repository.translationFontSize.collectAsState()
    val selectedLineSpacing by repository.lineSpacing.collectAsState()
    val readingMode by repository.quranReadingMode.collectAsState()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = CreamBg,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.92f)
                .clip(RoundedCornerShape(24.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // Header
                Surface(
                    color = EmeraldDark,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(GoldAccent),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FormatSize,
                                    contentDescription = null,
                                    tint = EmeraldDark,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Reading Typography",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = "Customize Qur'anic Arabic & English Fonts",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = EmeraldPale
                                    )
                                )
                            }
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.testTag("typography_close_btn")
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                        }
                    }
                }

                // Scrollable Content
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Interactive Live Preview
                    item {
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "LIVE READING PREVIEW",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = GoldAccent,
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 1.sp
                                        )
                                    )
                                    Text(
                                        text = "Surah Al-Ikhlas (112:1-2)",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = TextMuted
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Arabic text with selected font & line spacing
                                Text(
                                    text = "قُلْ هُوَ اللَّهُ أَحَدٌ ۝ اللَّهُ الصَّمَدُ ۝",
                                    fontFamily = selectedArabicFont.fontFamily,
                                    fontSize = quranFontSize.sp,
                                    lineHeight = (quranFontSize * selectedLineSpacing.multiplier).sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = EmeraldDark,
                                    textAlign = TextAlign.End,
                                    style = LocalTextStyle.current.copy(textDirection = TextDirection.Rtl),
                                    modifier = Modifier.fillMaxWidth()
                                )

                                if (readingMode != QuranReadingMode.ARABIC_ONLY) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    HorizontalDivider(color = CreamBorder, thickness = 1.dp)
                                    Spacer(modifier = Modifier.height(10.dp))

                                    if (readingMode == QuranReadingMode.ARABIC_TRANSLITERATION || readingMode == QuranReadingMode.FULL) {
                                        Text(
                                            text = "Qul Huwa Allāhu Aḥad. Allāhu aṣ-Ṣamad.",
                                            fontFamily = selectedEnglishFont.fontFamily,
                                            fontSize = (translationFontSize - 1).sp,
                                            lineHeight = (translationFontSize * 1.5).sp,
                                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                            color = GoldMuted,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                    }

                                    if (readingMode == QuranReadingMode.ARABIC_TRANSLATION || readingMode == QuranReadingMode.FULL) {
                                        Text(
                                            text = "Say, \"He is Allah, [who is] One. Allah, the Eternal Refuge.\"",
                                            fontFamily = selectedEnglishFont.fontFamily,
                                            fontSize = translationFontSize.sp,
                                            lineHeight = (translationFontSize * 1.6).sp,
                                            color = TextDark,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Section 1: Arabic Font Selection
                    item {
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Translate, contentDescription = null, tint = EmeraldPrimary)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "Arabic Qur'anic Typeface",
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = EmeraldDark)
                                        )
                                        Text(
                                            text = "Select high-fidelity Arabic calligraphy for Qur'an reading",
                                            style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                ArabicFontOption.entries.forEach { option ->
                                    val isSelected = selectedArabicFont == option
                                    Surface(
                                        color = if (isSelected) EmeraldContainer else CreamSurface,
                                        shape = RoundedCornerShape(12.dp),
                                        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, EmeraldPrimary) else null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .clickable { repository.setArabicFont(option) }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            RadioButton(
                                                selected = isSelected,
                                                onClick = { repository.setArabicFont(option) },
                                                colors = RadioButtonDefaults.colors(selectedColor = EmeraldPrimary)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = option.displayName,
                                                    style = MaterialTheme.typography.bodyLarge.copy(
                                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                                        color = if (isSelected) EmeraldDark else TextDark
                                                    )
                                                )
                                                Text(
                                                    text = option.subtitle,
                                                    style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                                                    fontFamily = option.fontFamily,
                                                    fontSize = 20.sp,
                                                    color = EmeraldPrimary,
                                                    textAlign = TextAlign.End,
                                                    style = LocalTextStyle.current.copy(textDirection = TextDirection.Rtl),
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Section 2: English Translation Font Selection
                    item {
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Abc, contentDescription = null, tint = EmeraldPrimary)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "English Translation Typeface",
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = EmeraldDark)
                                        )
                                        Text(
                                            text = "Choose typography for meanings and transliteration",
                                            style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                EnglishFontOption.entries.forEach { option ->
                                    val isSelected = selectedEnglishFont == option
                                    Surface(
                                        color = if (isSelected) EmeraldContainer else CreamSurface,
                                        shape = RoundedCornerShape(12.dp),
                                        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, EmeraldPrimary) else null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .clickable { repository.setEnglishFont(option) }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            RadioButton(
                                                selected = isSelected,
                                                onClick = { repository.setEnglishFont(option) },
                                                colors = RadioButtonDefaults.colors(selectedColor = EmeraldPrimary)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = option.displayName,
                                                    fontFamily = option.fontFamily,
                                                    style = MaterialTheme.typography.bodyLarge.copy(
                                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                                        color = if (isSelected) EmeraldDark else TextDark
                                                    )
                                                )
                                                Text(
                                                    text = option.subtitle,
                                                    style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                                                )
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = "\"In the name of Allah, the Entirely Merciful, the Especially Merciful.\"",
                                                    fontFamily = option.fontFamily,
                                                    fontSize = 13.sp,
                                                    color = TextDark
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Section 3: Text Sizes & Line Spacing
                    item {
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Text Sizing & Line Spacing",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = EmeraldDark)
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                // Arabic Size Slider
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Arabic Script Size", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                                    Text("${quranFontSize}sp", style = MaterialTheme.typography.titleSmall.copy(color = EmeraldPrimary, fontWeight = FontWeight.Bold))
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    IconButton(
                                        onClick = { repository.setQuranFontSize(quranFontSize - 2) },
                                        enabled = quranFontSize > 18
                                    ) {
                                        Icon(Icons.Default.Remove, contentDescription = "Decrease Arabic Size")
                                    }
                                    Slider(
                                        value = quranFontSize.toFloat(),
                                        onValueChange = { repository.setQuranFontSize(it.toInt()) },
                                        valueRange = 18f..38f,
                                        steps = 9,
                                        modifier = Modifier.weight(1f),
                                        colors = SliderDefaults.colors(thumbColor = EmeraldPrimary, activeTrackColor = EmeraldPrimary)
                                    )
                                    IconButton(
                                        onClick = { repository.setQuranFontSize(quranFontSize + 2) },
                                        enabled = quranFontSize < 38
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = "Increase Arabic Size")
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Translation Size Slider
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Translation Text Size", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                                    Text("${translationFontSize}sp", style = MaterialTheme.typography.titleSmall.copy(color = EmeraldPrimary, fontWeight = FontWeight.Bold))
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    IconButton(
                                        onClick = { repository.setTranslationFontSize(translationFontSize - 1) },
                                        enabled = translationFontSize > 12
                                    ) {
                                        Icon(Icons.Default.Remove, contentDescription = "Decrease Translation Size")
                                    }
                                    Slider(
                                        value = translationFontSize.toFloat(),
                                        onValueChange = { repository.setTranslationFontSize(it.toInt()) },
                                        valueRange = 12f..26f,
                                        steps = 13,
                                        modifier = Modifier.weight(1f),
                                        colors = SliderDefaults.colors(thumbColor = EmeraldPrimary, activeTrackColor = EmeraldPrimary)
                                    )
                                    IconButton(
                                        onClick = { repository.setTranslationFontSize(translationFontSize + 1) },
                                        enabled = translationFontSize < 26
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = "Increase Translation Size")
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Line Spacing Options
                                Text("Arabic Line Spacing", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    LineSpacingOption.entries.forEach { option ->
                                        val isSelected = selectedLineSpacing == option
                                        FilterChip(
                                            selected = isSelected,
                                            onClick = { repository.setLineSpacing(option) },
                                            label = { Text(option.displayName) },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = EmeraldPrimary,
                                                selectedLabelColor = Color.White
                                            ),
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Section 4: Reading Mode Switcher
                    item {
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Qur'an Display Mode",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = EmeraldDark)
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                QuranReadingMode.entries.forEach { mode ->
                                    val isSelected = readingMode == mode
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { repository.setQuranReadingMode(mode) }
                                            .padding(vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = isSelected,
                                            onClick = { repository.setQuranReadingMode(mode) },
                                            colors = RadioButtonDefaults.colors(selectedColor = EmeraldPrimary)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                text = mode.displayName,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                    color = if (isSelected) EmeraldDark else TextDark
                                                )
                                            )
                                            Text(
                                                text = when (mode) {
                                                    QuranReadingMode.ARABIC_ONLY -> "Display pristine Arabic calligraphy only"
                                                    QuranReadingMode.ARABIC_TRANSLATION -> "Arabic text with English translation"
                                                    QuranReadingMode.ARABIC_TRANSLITERATION -> "Arabic text with English phonetic transliteration"
                                                    QuranReadingMode.FULL -> "Complete Arabic, English meaning, and transliteration"
                                                    QuranReadingMode.CONTINUOUS -> "Paragraph-style Mushaf flow without card division"
                                                },
                                                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Done Button
                Surface(
                    color = Color.White,
                    tonalElevation = 4.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Button(
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldDark),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("typography_apply_done_btn")
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = GoldLight)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Apply & Save Settings",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

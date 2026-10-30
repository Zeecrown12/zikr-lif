package com.example.ui.screens

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.dhikr.DhikrData
import com.example.data.dhikr.DhikrItem
import com.example.data.dhikr.DhikrReadingMode
import com.example.data.repository.AudioPlayerManager
import com.example.data.repository.AudioPlayerState
import com.example.data.repository.ZikrPreferencesRepository
import com.example.ui.components.ArabicTextView
import com.example.ui.components.ZikrTopBar
import com.example.ui.theme.*

@Composable
fun DhikrScreen(
    repository: ZikrPreferencesRepository,
    audioManager: AudioPlayerManager,
    initialTab: Int = 0
) {
    var selectedTabIndex by remember { mutableStateOf(initialTab) }
    val dhikrReadingMode by repository.dhikrReadingMode.collectAsState()

    val tabs = listOf(
        "Digital Tasbih",
        "Morning Adhkar",
        "Evening Adhkar",
        "Al-Ma'thurat",
        "After Prayer",
        "Sleep & Wake",
        "Daily Duas"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBg)
    ) {
        ZikrTopBar(
            title = "Remembrance & Duas",
            subtitle = "Tasbih, Adhkar & Authentic Audio Recitations"
        )

        // Scrollable Tab Row
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = EmeraldDark,
            contentColor = Color.White,
            edgePadding = 16.dp,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = GoldLight,
                    height = 3.dp
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTabIndex == index) GoldLight else Color.White.copy(alpha = 0.8f)
                            )
                        )
                    },
                    modifier = Modifier.testTag("dhikr_tab_$index")
                )
            }
        }

        when (selectedTabIndex) {
            0 -> DigitalTasbihView(repository = repository, audioManager = audioManager)
            1 -> AdhkarAudioListView(
                title = "Morning Adhkar (Azkar al-Sabah)",
                items = DhikrData.MORNING_ADHKAR,
                readingMode = dhikrReadingMode,
                onSelectReadingMode = { repository.setDhikrReadingMode(it) },
                repository = repository,
                audioManager = audioManager
            )
            2 -> AdhkarAudioListView(
                title = "Evening Adhkar (Azkar al-Masa')",
                items = DhikrData.EVENING_ADHKAR,
                readingMode = dhikrReadingMode,
                onSelectReadingMode = { repository.setDhikrReadingMode(it) },
                repository = repository,
                audioManager = audioManager
            )
            3 -> AdhkarAudioListView(
                title = "Al-Ma'thurat (Daily Fortress)",
                items = DhikrData.AL_MATHURAT,
                readingMode = dhikrReadingMode,
                onSelectReadingMode = { repository.setDhikrReadingMode(it) },
                repository = repository,
                audioManager = audioManager
            )
            4 -> AdhkarAudioListView(
                title = "Adhkar After Obligatory Prayer",
                items = DhikrData.AFTER_PRAYER_ADHKAR,
                readingMode = dhikrReadingMode,
                onSelectReadingMode = { repository.setDhikrReadingMode(it) },
                repository = repository,
                audioManager = audioManager
            )
            5 -> AdhkarAudioListView(
                title = "Before Sleep & After Waking",
                items = DhikrData.BEFORE_SLEEP_ADHKAR + DhikrData.AFTER_WAKING_ADHKAR,
                readingMode = dhikrReadingMode,
                onSelectReadingMode = { repository.setDhikrReadingMode(it) },
                repository = repository,
                audioManager = audioManager
            )
            6 -> DailyDuasListView(
                items = DhikrData.DAILY_DUAS,
                readingMode = dhikrReadingMode,
                onSelectReadingMode = { repository.setDhikrReadingMode(it) },
                repository = repository,
                audioManager = audioManager
            )
        }
    }
}

@Composable
fun DigitalTasbihView(
    repository: ZikrPreferencesRepository,
    audioManager: AudioPlayerManager
) {
    val context = LocalContext.current
    val hapticFeedback = LocalHapticFeedback.current
    val lifetimeTotal by repository.lifetimeTasbih.collectAsState()
    val isHapticEnabled by repository.hapticEnabled.collectAsState()
    val audioState by audioManager.playerState.collectAsState()
    val currentAudioId by audioManager.currentPlayingId.collectAsState()
    val isRepeat by audioManager.isRepeatEnabled.collectAsState()
    val arabicFont by repository.arabicFont.collectAsState()
    val englishFont by repository.englishFont.collectAsState()

    val phrases = DhikrData.TASBIH_PHRASES
    var selectedPhraseIndex by remember { mutableStateOf(0) }
    val currentPhraseItem = phrases[selectedPhraseIndex.coerceIn(0, phrases.size - 1)]

    var currentTarget by remember { mutableStateOf(currentPhraseItem.defaultTarget) }
    var currentCount by remember { mutableStateOf(0) }
    var completedRounds by remember { mutableStateOf(0) }
    var isAudioOnTapEnabled by remember { mutableStateOf(false) } // Optional audio on each tap
    var showCustomDialog by remember { mutableStateOf(false) }

    val playId = "tasbih_${currentPhraseItem.key}"
    val isPlayingThisDhikr = currentAudioId == "dhikr_$playId" && audioState is AudioPlayerState.Playing
    val isBufferingThisDhikr = currentAudioId == "dhikr_$playId" && audioState is AudioPlayerState.Buffering

    fun vibrateTap() {
        if (!isHapticEnabled) return
        try {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(30)
            }
        } catch (_: Exception) {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
    }

    fun vibrateGoal() {
        if (!isHapticEnabled) return
        try {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(120, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(120)
            }
        } catch (_: Exception) {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Preset Chips
        Text(
            text = "Select Dhikr Phrase",
            style = MaterialTheme.typography.labelMedium.copy(color = TextMuted),
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(6.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(phrases.indices.toList()) { index ->
                val p = phrases[index]
                val isSelected = selectedPhraseIndex == index
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        selectedPhraseIndex = index
                        currentTarget = p.defaultTarget
                        currentCount = 0
                    },
                    label = { Text("${p.key} (${p.defaultTarget})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = EmeraldPrimary,
                        selectedLabelColor = Color.White,
                        containerColor = Color.White,
                        labelColor = TextDark
                    ),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.testTag("tasbih_preset_${p.key}")
                )
            }
            item {
                ElevatedFilterChip(
                    selected = false,
                    onClick = { showCustomDialog = true },
                    label = { Text("+ Custom Target") },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = GoldContainer,
                        labelColor = GoldMuted
                    ),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.testTag("tasbih_custom_btn")
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Selected Dhikr Display Card with Audio Playback Controls
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top row with Audio controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Play Audio Pronunciation
                    Button(
                        onClick = {
                            audioManager.playDhikr(
                                id = playId,
                                audioUrl = currentPhraseItem.audioUrl,
                                arabicText = currentPhraseItem.arabic
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isPlayingThisDhikr) EmeraldDark else EmeraldPale,
                            contentColor = if (isPlayingThisDhikr) Color.White else EmeraldDark
                        ),
                        shape = RoundedCornerShape(50),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("tasbih_audio_play_btn")
                    ) {
                        if (isBufferingThisDhikr) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), color = EmeraldDark, strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Loading...", style = MaterialTheme.typography.labelSmall)
                        } else {
                            Icon(
                                imageVector = if (isPlayingThisDhikr) Icons.Default.Pause else Icons.Default.VolumeUp,
                                contentDescription = if (isPlayingThisDhikr) "Pause Pronunciation" else "Listen to Pronunciation",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isPlayingThisDhikr) "Pause Audio" else "Listen Audio",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold)
                            )
                        }
                    }

                    // Optional Audio on Tap toggle
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { isAudioOnTapEnabled = !isAudioOnTapEnabled }
                    ) {
                        Text(
                            text = "Audio on tap",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (isAudioOnTapEnabled) EmeraldDark else TextMuted,
                                fontWeight = if (isAudioOnTapEnabled) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Switch(
                            checked = isAudioOnTapEnabled,
                            onCheckedChange = { isAudioOnTapEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = EmeraldPrimary
                            ),
                            modifier = Modifier.testTag("tasbih_audio_on_tap_switch")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Arabic Text
                Text(
                    text = currentPhraseItem.arabic,
                    fontFamily = arabicFont.fontFamily,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = EmeraldDark
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Transliteration & English Meaning
                Text(
                    text = "${currentPhraseItem.transliteration} — “${currentPhraseItem.meaning}”",
                    fontFamily = englishFont.fontFamily,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextMedium,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Circular Interactive Tap Bead
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(EmeraldPrimary, EmeraldDark)
                    )
                )
                .border(4.dp, GoldAccent, CircleShape)
                .clickable {
                    vibrateTap()
                    currentCount++
                    repository.addTasbihCount(1)

                    if (isAudioOnTapEnabled) {
                        audioManager.playDhikr(
                            id = "tap_${currentPhraseItem.key}",
                            audioUrl = "",
                            arabicText = currentPhraseItem.arabic
                        )
                    }

                    if (currentCount >= currentTarget) {
                        vibrateGoal()
                        completedRounds++
                        currentCount = 0
                    }
                }
                .testTag("tasbih_counter_button"),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$currentCount",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 50.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                )
                Text(
                    text = "Target: $currentTarget",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = GoldLight,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isAudioOnTapEnabled) "TAP TO COUNT & CHANT" else "TAP TO COUNT (SILENT)",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = EmeraldPale,
                        letterSpacing = 1.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Stats & Controls Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("ROUNDS", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                Text(
                    text = "$completedRounds",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = EmeraldDark)
                )
            }

            OutlinedButton(
                onClick = { currentCount = 0 },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextDark),
                shape = RoundedCornerShape(50),
                modifier = Modifier.testTag("tasbih_reset_btn")
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Reset", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Reset")
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("LIFETIME TOTAL", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                Text(
                    text = "%,d".format(lifetimeTotal),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = EmeraldDark)
                )
            }
        }
    }

    if (showCustomDialog) {
        var customTargetInput by remember { mutableStateOf("$currentTarget") }

        AlertDialog(
            onDismissRequest = { showCustomDialog = false },
            title = { Text("Set Repetition Target") },
            text = {
                Column {
                    Text("Target count for ${currentPhraseItem.key}:", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = customTargetInput,
                        onValueChange = { customTargetInput = it.filter { char -> char.isDigit() } },
                        label = { Text("Target Count (e.g. 33, 99, 100, 1000)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val parsed = customTargetInput.toIntOrNull()
                        if (parsed != null && parsed > 0) {
                            currentTarget = parsed
                            currentCount = 0
                            showCustomDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                ) {
                    Text("Save Target")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCustomDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun AdhkarAudioListView(
    title: String,
    items: List<DhikrItem>,
    readingMode: DhikrReadingMode,
    onSelectReadingMode: (DhikrReadingMode) -> Unit,
    repository: ZikrPreferencesRepository,
    audioManager: AudioPlayerManager
) {
    val hapticFeedback = LocalHapticFeedback.current
    val audioState by audioManager.playerState.collectAsState()
    val currentAudioId by audioManager.currentPlayingId.collectAsState()
    val arabicFont by repository.arabicFont.collectAsState()
    val englishFont by repository.englishFont.collectAsState()
    val lineSpacing by repository.lineSpacing.collectAsState()
    val arabicFontSize by repository.arabicFontSize.collectAsState()
    val translationFontSize by repository.translationFontSize.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    val countsMap = remember { mutableStateMapOf<String, Int>() }

    val filtered = remember(searchQuery, items) {
        if (searchQuery.isBlank()) items
        else items.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
                    it.translation.contains(searchQuery, ignoreCase = true) ||
                    it.arabic.contains(searchQuery)
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Display Mode Switcher
        item {
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Display Mode:",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = TextMuted)
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(DhikrReadingMode.values()) { mode ->
                            val isSelected = readingMode == mode
                            FilterChip(
                                selected = isSelected,
                                onClick = { onSelectReadingMode(mode) },
                                label = {
                                    Text(
                                        text = mode.displayName,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = EmeraldPrimary,
                                    selectedLabelColor = Color.White,
                                    containerColor = CreamSurface,
                                    labelColor = TextDark
                                ),
                                shape = RoundedCornerShape(50)
                            )
                        }
                    }
                }
            }
        }

        // Search Field
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search supplications & remembrances...") },
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
        }

        items(filtered, key = { it.id }) { item ->
            val count = countsMap[item.id] ?: 0
            val isComplete = count >= item.targetCount
            val playId = "dhikr_${item.id}"
            val isPlayingThis = currentAudioId == playId && audioState is AudioPlayerState.Playing
            val isBufferingThis = currentAudioId == playId && audioState is AudioPlayerState.Buffering

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = if (isComplete) androidx.compose.foundation.BorderStroke(1.5.dp, EmeraldMedium) else null,
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = EmeraldDark
                            ),
                            modifier = Modifier.weight(1f)
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Audio Play/Pause Button
                            IconButton(
                                onClick = {
                                    audioManager.playDhikr(
                                        id = item.id,
                                        audioUrl = item.audioUrl,
                                        arabicText = item.arabic
                                    )
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (isPlayingThis) EmeraldPrimary else EmeraldPale)
                                    .testTag("dhikr_audio_btn_${item.id}")
                            ) {
                                if (isBufferingThis) {
                                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = EmeraldDark, strokeWidth = 2.dp)
                                } else {
                                    Icon(
                                        imageVector = if (isPlayingThis) Icons.Default.Pause else Icons.Default.VolumeUp,
                                        contentDescription = "Play Audio",
                                        tint = if (isPlayingThis) Color.White else EmeraldDark,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Mini Interactive Counter
                            Button(
                                onClick = {
                                    val current = countsMap[item.id] ?: 0
                                    if (current < item.targetCount) {
                                        countsMap[item.id] = current + 1
                                        repository.addTasbihCount(1)
                                        hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isComplete) EmeraldMedium else EmeraldPale,
                                    contentColor = if (isComplete) Color.White else EmeraldDark
                                ),
                                shape = RoundedCornerShape(50),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier.testTag("adhkar_counter_btn_${item.id}")
                            ) {
                                if (isComplete) {
                                    Icon(Icons.Default.Check, contentDescription = "Completed", modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Done (${item.targetCount}x)")
                                } else {
                                    Text("$count / ${item.targetCount}x")
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Arabic Text (Shown in all modes)
                    ArabicTextView(
                        text = item.arabic,
                        fontSize = arabicFontSize.sp,
                        fontFamily = arabicFont.fontFamily,
                        lineHeightMultiplier = lineSpacing.multiplier
                    )

                    // Transliteration
                    if (readingMode == DhikrReadingMode.FULL || readingMode == DhikrReadingMode.ARABIC_TRANSLITERATION) {
                        if (item.transliteration.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = item.transliteration,
                                fontFamily = englishFont.fontFamily,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontStyle = FontStyle.Italic,
                                    color = TextMedium,
                                    fontSize = (translationFontSize - 1).sp,
                                    lineHeight = ((translationFontSize - 1) * lineSpacing.multiplier).sp
                                )
                            )
                        }
                    }

                    // Translation
                    if (readingMode == DhikrReadingMode.FULL || readingMode == DhikrReadingMode.ARABIC_TRANSLATION) {
                        if (item.translation.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = item.translation,
                                fontFamily = englishFont.fontFamily,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = TextDark,
                                    fontSize = translationFontSize.sp,
                                    lineHeight = (translationFontSize * lineSpacing.multiplier).sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = item.source,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = GoldAccent,
                            fontWeight = FontWeight.SemiBold
                        )
                    )

                    if (item.virtue.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            color = EmeraldPale.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Virtue: ${item.virtue}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = EmeraldDark,
                                    fontStyle = FontStyle.Italic
                                ),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DailyDuasListView(
    items: List<DhikrItem>,
    readingMode: DhikrReadingMode,
    onSelectReadingMode: (DhikrReadingMode) -> Unit,
    repository: ZikrPreferencesRepository,
    audioManager: AudioPlayerManager
) {
    val audioState by audioManager.playerState.collectAsState()
    val currentAudioId by audioManager.currentPlayingId.collectAsState()
    val arabicFont by repository.arabicFont.collectAsState()
    val englishFont by repository.englishFont.collectAsState()
    val lineSpacing by repository.lineSpacing.collectAsState()
    val arabicFontSize by repository.arabicFontSize.collectAsState()
    val translationFontSize by repository.translationFontSize.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    val categories = remember(items) {
        listOf("All") + items.map { it.category }.distinct()
    }

    val filtered = remember(searchQuery, selectedCategory, items) {
        items.filter { item ->
            val matchesSearch = searchQuery.isBlank() ||
                    item.title.contains(searchQuery, ignoreCase = true) ||
                    item.translation.contains(searchQuery, ignoreCase = true) ||
                    item.arabic.contains(searchQuery)
            val matchesCategory = selectedCategory == "All" || item.category == selectedCategory
            matchesSearch && matchesCategory
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Display Mode Switcher
        item {
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Display Mode:",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = TextMuted)
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(DhikrReadingMode.values()) { mode ->
                            val isSelected = readingMode == mode
                            FilterChip(
                                selected = isSelected,
                                onClick = { onSelectReadingMode(mode) },
                                label = {
                                    Text(
                                        text = mode.displayName,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = EmeraldPrimary,
                                    selectedLabelColor = Color.White,
                                    containerColor = CreamSurface,
                                    labelColor = TextDark
                                ),
                                shape = RoundedCornerShape(50)
                            )
                        }
                    }
                }
            }
        }

        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search authentic everyday duas...") },
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

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = EmeraldPrimary,
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = TextDark
                        ),
                        shape = RoundedCornerShape(50)
                    )
                }
            }
        }

        items(filtered, key = { it.id }) { item ->
            val playId = "dhikr_${item.id}"
            val isPlayingThis = currentAudioId == playId && audioState is AudioPlayerState.Playing
            val isBufferingThis = currentAudioId == playId && audioState is AudioPlayerState.Buffering

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = EmeraldDark
                            ),
                            modifier = Modifier.weight(1f)
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = {
                                    audioManager.playDhikr(
                                        id = item.id,
                                        audioUrl = item.audioUrl,
                                        arabicText = item.arabic
                                    )
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (isPlayingThis) EmeraldPrimary else EmeraldPale)
                                    .testTag("dua_audio_btn_${item.id}")
                            ) {
                                if (isBufferingThis) {
                                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = EmeraldDark, strokeWidth = 2.dp)
                                } else {
                                    Icon(
                                        imageVector = if (isPlayingThis) Icons.Default.Pause else Icons.Default.VolumeUp,
                                        contentDescription = "Play Dua Audio",
                                        tint = if (isPlayingThis) Color.White else EmeraldDark,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = GoldContainer,
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    text = item.category,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = GoldMuted,
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    ArabicTextView(
                        text = item.arabic,
                        fontSize = arabicFontSize.sp,
                        fontFamily = arabicFont.fontFamily,
                        lineHeightMultiplier = lineSpacing.multiplier
                    )

                    if (readingMode == DhikrReadingMode.FULL || readingMode == DhikrReadingMode.ARABIC_TRANSLITERATION) {
                        if (item.transliteration.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = item.transliteration,
                                fontFamily = englishFont.fontFamily,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontStyle = FontStyle.Italic,
                                    color = TextMedium,
                                    fontSize = (translationFontSize - 1).sp,
                                    lineHeight = ((translationFontSize - 1) * lineSpacing.multiplier).sp
                                )
                            )
                        }
                    }

                    if (readingMode == DhikrReadingMode.FULL || readingMode == DhikrReadingMode.ARABIC_TRANSLATION) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = item.translation,
                            fontFamily = englishFont.fontFamily,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextDark,
                                fontSize = translationFontSize.sp,
                                lineHeight = (translationFontSize * lineSpacing.multiplier).sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = item.source,
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

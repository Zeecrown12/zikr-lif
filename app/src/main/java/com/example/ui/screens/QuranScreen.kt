package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.quran.Ayah
import com.example.data.quran.QuranData
import com.example.data.quran.QuranReadingMode
import com.example.data.quran.Surah
import com.example.data.repository.AudioPlayerManager
import com.example.data.repository.AudioPlayerState
import com.example.data.repository.ZikrPreferencesRepository
import com.example.ui.components.ArabicTextView
import com.example.ui.components.ZikrTopBar
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun QuranScreen(
    repository: ZikrPreferencesRepository,
    audioManager: AudioPlayerManager
) {
    val context = LocalContext.current
    val bookmarkedSurahs by repository.bookmarkedSurahs.collectAsState()
    val bookmarkedVerses by repository.bookmarkedVerses.collectAsState()
    val lastReadSurahNum by repository.lastReadSurah.collectAsState()
    val lastReadAyahNum by repository.lastReadAyah.collectAsState()
    val quranFontSize by repository.quranFontSize.collectAsState()
    val readingMode by repository.quranReadingMode.collectAsState()
    val audioState by audioManager.playerState.collectAsState()
    val currentAudioId by audioManager.currentPlayingId.collectAsState()
    val currentAudioSurah by audioManager.currentPlayingSurahNumber.collectAsState()
    val currentAudioAyah by audioManager.currentPlayingAyahNumber.collectAsState()
    val isRepeat by audioManager.isRepeatEnabled.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") } // "All", "Bookmarked", "Meccan", "Medinan"
    var activeSurahNumber by remember { mutableStateOf<Int?>(null) }

    // Load full Surah with all verses from local assets when activeSurahNumber changes
    val activeSurah = remember(activeSurahNumber) {
        activeSurahNumber?.let { num ->
            QuranData.loadSurah(context, num)
        }
    }

    // Handle system back button when reading a Surah
    BackHandler(enabled = activeSurah != null) {
        activeSurahNumber = null
    }

    if (activeSurah != null) {
        SurahReaderView(
            surah = activeSurah,
            fontSize = quranFontSize,
            readingMode = readingMode,
            isBookmarked = bookmarkedSurahs.contains(activeSurah.number),
            bookmarkedVerses = bookmarkedVerses,
            lastReadAyah = if (lastReadSurahNum == activeSurah.number) lastReadAyahNum else 1,
            audioState = audioState,
            currentAudioSurah = currentAudioSurah,
            currentAudioAyah = currentAudioAyah,
            isRepeatEnabled = isRepeat,
            onBack = { activeSurahNumber = null },
            onToggleBookmark = { repository.toggleBookmark(activeSurah.number) },
            onToggleAyahBookmark = { ayahNum -> repository.toggleAyahBookmark(activeSurah.number, ayahNum) },
            onSaveProgress = { ayahNum ->
                repository.setLastReadPosition(activeSurah.number, ayahNum)
                Toast.makeText(context, "Saved reading progress: Surah ${activeSurah.nameEnglish}, Ayah $ayahNum", Toast.LENGTH_SHORT).show()
            },
            onChangeFontSize = { delta -> repository.setQuranFontSize(quranFontSize + delta) },
            onSelectReadingMode = { repository.setQuranReadingMode(it) },
            onNavigateSurah = { nextNum ->
                activeSurahNumber = nextNum.coerceIn(1, 114)
                repository.setLastReadPosition(activeSurahNumber!!, 1)
            },
            onPlaySurah = { audioManager.playSurah(activeSurah.number, activeSurah.audioUrl) },
            onPlayAyah = { ayah ->
                audioManager.playAyah(
                    surahNumber = activeSurah.number,
                    ayahNumberInSurah = ayah.numberInSurah,
                    audioUrl = ayah.audioUrl,
                    arabicText = ayah.arabic
                )
            },
            onSeekAudio = { posMs -> audioManager.seekTo(posMs) },
            onToggleRepeat = { audioManager.toggleRepeat() }
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CreamBg)
        ) {
            ZikrTopBar(
                title = "The Holy Quran",
                subtitle = "Complete 114 Surahs • Offline Arabic, Reading Modes & Audio"
            )

            // Search Bar & Filter Chips
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("quran_search_field"),
                    placeholder = { Text("Search Surah by name, translation, or number...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = EmeraldDark
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear",
                                    tint = TextMuted
                                )
                            }
                        }
                    },
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

                // Filter chips: All, Bookmarked, Meccan, Medinan
                val filterOptions = listOf("All", "Bookmarked", "Meccan", "Medinan")
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(filterOptions) { filter ->
                        val isSelected = selectedFilter == filter
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedFilter = filter },
                            label = {
                                Text(
                                    text = if (filter == "Bookmarked") "Bookmarked (${bookmarkedSurahs.size})" else filter,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = EmeraldPrimary,
                                selectedLabelColor = Color.White,
                                containerColor = Color.White,
                                labelColor = TextDark
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = CreamBorder,
                                selectedBorderColor = EmeraldPrimary
                            )
                        )
                    }
                }
            }

            // Resume Reading Banner
            val lastSurah = remember(lastReadSurahNum) {
                QuranData.SURAH_METADATA_LIST.find { it.number == lastReadSurahNum }
                    ?: QuranData.SURAH_METADATA_LIST.first()
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clickable {
                        activeSurahNumber = lastReadSurahNum
                    }
                    .testTag("resume_reading_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = EmeraldDark)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = null,
                                tint = GoldLight,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "CONTINUE READING",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = GoldLight,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${lastSurah.number}. ${lastSurah.nameEnglish} • Ayah $lastReadAyahNum",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "${lastSurah.nameArabic} • ${lastSurah.englishMeaning}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = EmeraldPale
                            )
                        )
                    }

                    FilledTonalButton(
                        onClick = { activeSurahNumber = lastReadSurahNum },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = GoldAccent,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Resume", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Filtered Surahs List
            val filteredSurahs = remember(searchQuery, selectedFilter, bookmarkedSurahs) {
                QuranData.SURAH_METADATA_LIST.filter { surah ->
                    val matchesSearch = if (searchQuery.isBlank()) true else {
                        val q = searchQuery.trim().lowercase()
                        surah.nameEnglish.lowercase().contains(q) ||
                                surah.englishMeaning.lowercase().contains(q) ||
                                surah.nameArabic.contains(q) ||
                                surah.number.toString() == q
                    }
                    val matchesFilter = when (selectedFilter) {
                        "Bookmarked" -> bookmarkedSurahs.contains(surah.number)
                        "Meccan" -> surah.revelationType.equals("Meccan", ignoreCase = true)
                        "Medinan" -> surah.revelationType.equals("Medinan", ignoreCase = true)
                        else -> true
                    }
                    matchesSearch && matchesFilter
                }
            }

            if (filteredSurahs.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No Surahs found matching \"$searchQuery\"",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 90.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredSurahs, key = { it.number }) { surah ->
                        val isBookmarked = bookmarkedSurahs.contains(surah.number)
                        SurahListItem(
                            surah = surah,
                            isBookmarked = isBookmarked,
                            onItemClick = {
                                activeSurahNumber = surah.number
                                repository.setLastReadPosition(surah.number, 1)
                            },
                            onBookmarkToggle = { repository.toggleBookmark(surah.number) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SurahListItem(
    surah: Surah,
    isBookmarked: Boolean,
    onItemClick: () -> Unit,
    onBookmarkToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
            .testTag("surah_item_${surah.number}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Surah Number Badge
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(EmeraldPale),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${surah.number}",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = EmeraldDark
                        )
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = surah.nameEnglish,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                    )
                    Text(
                        text = "${surah.englishMeaning} • ${surah.totalVerses} Ayahs • ${surah.revelationType}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextMuted
                        )
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = surah.nameArabic,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = EmeraldDark
                    ),
                    modifier = Modifier.padding(end = 8.dp)
                )

                IconButton(
                    onClick = onBookmarkToggle,
                    modifier = Modifier.testTag("surah_bookmark_btn_${surah.number}")
                ) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (isBookmarked) GoldAccent else TextLight
                    )
                }
            }
        }
    }
}

@Composable
fun SurahReaderView(
    surah: Surah,
    fontSize: Int,
    readingMode: QuranReadingMode,
    isBookmarked: Boolean,
    bookmarkedVerses: Set<String>,
    lastReadAyah: Int,
    audioState: AudioPlayerState,
    currentAudioSurah: Int?,
    currentAudioAyah: Int?,
    isRepeatEnabled: Boolean,
    onBack: () -> Unit,
    onToggleBookmark: () -> Unit,
    onToggleAyahBookmark: (Int) -> Unit,
    onSaveProgress: (Int) -> Unit,
    onChangeFontSize: (Int) -> Unit,
    onSelectReadingMode: (QuranReadingMode) -> Unit,
    onNavigateSurah: (Int) -> Unit,
    onPlaySurah: () -> Unit,
    onPlayAyah: (Ayah) -> Unit,
    onSeekAudio: (Int) -> Unit,
    onToggleRepeat: () -> Unit
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var verseSearchQuery by remember { mutableStateOf("") }
    var selectedAyahForDialog by remember { mutableStateOf<Ayah?>(null) }

    val isPlayingFullSurah = currentAudioSurah == surah.number && currentAudioAyah == null && audioState is AudioPlayerState.Playing
    val isBufferingFullSurah = currentAudioSurah == surah.number && currentAudioAyah == null && audioState is AudioPlayerState.Buffering

    // Auto-scroll to currently playing Ayah
    LaunchedEffect(currentAudioAyah) {
        if (currentAudioSurah == surah.number && currentAudioAyah != null && currentAudioAyah > 0) {
            val idx = (currentAudioAyah - 1).coerceAtLeast(0)
            if (idx < surah.verses.size) {
                listState.animateScrollToItem(idx)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBg)
    ) {
        // Reader Top Bar
        Surface(
            color = EmeraldDark,
            tonalElevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier.testTag("reader_back_btn")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                        Column {
                            Text(
                                text = "${surah.number}. ${surah.nameEnglish} (${surah.nameArabic})",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = "${surah.englishMeaning} • ${surah.totalVerses} Ayahs • ${surah.revelationType}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = EmeraldContainer
                                )
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Font Size controls
                        IconButton(onClick = { onChangeFontSize(-2) }) {
                            Text("A-", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        IconButton(onClick = { onChangeFontSize(2) }) {
                            Text("A+", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }

                        // Bookmark Surah
                        IconButton(onClick = onToggleBookmark) {
                            Icon(
                                imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                contentDescription = "Bookmark",
                                tint = if (isBookmarked) GoldLight else Color.White
                            )
                        }
                    }
                }
            }
        }

        // Reading Mode Switcher Row (CRITICAL REQUIREMENT 2)
        Surface(
            color = Color.White,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                item {
                    Text(
                        text = "Mode:",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextMuted
                        ),
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }
                items(QuranReadingMode.values()) { mode ->
                    val isSelected = readingMode == mode
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectReadingMode(mode) },
                        label = {
                            Text(
                                text = mode.displayName,
                                style = MaterialTheme.typography.labelSmall.copy(
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
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.testTag("reading_mode_${mode.name.lowercase()}")
                    )
                }
            }
        }

        // Complete Surah Audio Player Bar (with seek bar & time indicators)
        Surface(
            color = EmeraldPrimary,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        IconButton(
                            onClick = onPlaySurah,
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f))
                                .testTag("quran_audio_play_pause_btn")
                        ) {
                            if (isBufferingFullSurah) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = GoldLight,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = if (isPlayingFullSurah) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = if (isPlayingFullSurah) "Pause" else "Play",
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Surah Recitation (Sheikh Mishary Alafasy)",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                            Text(
                                text = when {
                                    isPlayingFullSurah -> "Playing Surah ${surah.nameEnglish}"
                                    isBufferingFullSurah -> "Connecting to audio stream..."
                                    else -> "Tap to listen to verified recitation"
                                },
                                style = MaterialTheme.typography.bodySmall.copy(color = EmeraldPale)
                            )
                        }
                    }

                    IconButton(
                        onClick = onToggleRepeat,
                        modifier = Modifier.testTag("quran_audio_repeat_btn")
                    ) {
                        Icon(
                            imageVector = if (isRepeatEnabled) Icons.Default.RepeatOne else Icons.Default.Repeat,
                            contentDescription = "Repeat",
                            tint = if (isRepeatEnabled) GoldLight else Color.White.copy(alpha = 0.7f)
                        )
                    }
                }

                // Audio Seeker & Duration when playing
                if (isPlayingFullSurah && audioState is AudioPlayerState.Playing) {
                    val pos = (audioState as AudioPlayerState.Playing).currentPositionMs
                    val total = (audioState as AudioPlayerState.Playing).durationMs
                    if (total > 0) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                        ) {
                            Text(
                                text = formatDuration(pos),
                                style = MaterialTheme.typography.labelSmall.copy(color = EmeraldPale),
                                fontSize = 11.sp
                            )
                            Slider(
                                value = pos.toFloat().coerceIn(0f, total.toFloat()),
                                onValueChange = { onSeekAudio(it.toInt()) },
                                valueRange = 0f..total.toFloat(),
                                colors = SliderDefaults.colors(
                                    thumbColor = GoldLight,
                                    activeTrackColor = GoldLight,
                                    inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 8.dp)
                                    .height(20.dp)
                            )
                            Text(
                                text = formatDuration(total),
                                style = MaterialTheme.typography.labelSmall.copy(color = EmeraldPale),
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // Quick Jump / Search to Verse Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = verseSearchQuery,
                onValueChange = { query ->
                    verseSearchQuery = query
                    val verseNum = query.trim().toIntOrNull()
                    if (verseNum != null && verseNum in 1..surah.totalVerses) {
                        scope.launch {
                            listState.animateScrollToItem((verseNum - 1).coerceAtLeast(0))
                        }
                    }
                },
                placeholder = { Text("Jump to verse (1–${surah.totalVerses}) or search text...") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EmeraldPrimary,
                    unfocusedBorderColor = CreamBorder,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                leadingIcon = {
                    Icon(Icons.Default.FilterList, contentDescription = null, tint = EmeraldDark)
                },
                trailingIcon = {
                    if (verseSearchQuery.isNotEmpty()) {
                        IconButton(onClick = { verseSearchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear", tint = TextMuted)
                        }
                    }
                }
            )
        }

        // Bismillah Header (for all Surahs except Surah 9 At-Tawbah)
        if (surah.number != 9) {
            Surface(
                color = EmeraldPale.copy(alpha = 0.5f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = EmeraldDark,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                )
            }
        }

        // Filtered verses
        val displayedVerses = remember(surah.verses, verseSearchQuery) {
            if (verseSearchQuery.isBlank() || verseSearchQuery.trim().toIntOrNull() != null) {
                surah.verses
            } else {
                surah.verses.filter {
                    it.arabic.contains(verseSearchQuery) ||
                            it.translation.contains(verseSearchQuery, ignoreCase = true) ||
                            it.transliteration.contains(verseSearchQuery, ignoreCase = true)
                }
            }
        }

        // ==========================================
        // CONTINUOUS READING MODE vs VERSE-BY-VERSE
        // ==========================================
        if (readingMode == QuranReadingMode.CONTINUOUS) {
            ContinuousQuranReadingView(
                surah = surah,
                fontSize = fontSize,
                currentAudioAyah = currentAudioAyah,
                onAyahSelected = { ayah -> selectedAyahForDialog = ayah },
                onPlayAyah = onPlayAyah,
                onNavigateSurah = onNavigateSurah
            )
        } else {
            // VERSE-BY-VERSE MODE
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(displayedVerses, key = { it.numberInSurah }) { ayah ->
                    val isPlayingThisAyah = currentAudioSurah == surah.number && currentAudioAyah == ayah.numberInSurah && audioState is AudioPlayerState.Playing
                    val isBufferingThisAyah = currentAudioSurah == surah.number && currentAudioAyah == ayah.numberInSurah && audioState is AudioPlayerState.Buffering
                    val isAyahBookmarked = bookmarkedVerses.contains("${surah.number}:${ayah.numberInSurah}")

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("ayah_${surah.number}_${ayah.numberInSurah}"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isPlayingThisAyah) Color(0xFFF0FDF4) else Color.White
                        ),
                        border = if (isPlayingThisAyah) {
                            androidx.compose.foundation.BorderStroke(1.5.dp, EmeraldPrimary)
                        } else {
                            androidx.compose.foundation.BorderStroke(0.5.dp, CreamBorder)
                        },
                        elevation = CardDefaults.cardElevation(if (isPlayingThisAyah) 3.dp else 1.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            // Header with Ayah Number & Actions (Bookmark, Save Progress, Audio Play)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Ayah Number Badge
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(34.dp)
                                            .clip(CircleShape)
                                            .background(if (isPlayingThisAyah) EmeraldPrimary else EmeraldPale),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${ayah.numberInSurah}",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = if (isPlayingThisAyah) Color.White else EmeraldDark
                                            )
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "${surah.nameEnglish} • Verse ${ayah.numberInSurah}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = TextMuted,
                                            fontWeight = FontWeight.Medium
                                        )
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    // Save Reading Progress button
                                    IconButton(
                                        onClick = { onSaveProgress(ayah.numberInSurah) },
                                        modifier = Modifier.size(32.dp).testTag("save_progress_${ayah.numberInSurah}")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Done,
                                            contentDescription = "Save progress to this verse",
                                            tint = EmeraldMedium,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }

                                    // Bookmark verse button
                                    IconButton(
                                        onClick = { onToggleAyahBookmark(ayah.numberInSurah) },
                                        modifier = Modifier.size(32.dp).testTag("bookmark_ayah_${ayah.numberInSurah}")
                                    ) {
                                        Icon(
                                            imageVector = if (isAyahBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                            contentDescription = "Bookmark Verse",
                                            tint = if (isAyahBookmarked) GoldAccent else TextLight,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(4.dp))

                                    // Individual Verse Audio Play Button
                                    IconButton(
                                        onClick = { onPlayAyah(ayah) },
                                        modifier = Modifier
                                            .size(34.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (isPlayingThisAyah) EmeraldPrimary else EmeraldPale
                                            )
                                            .testTag("play_ayah_${ayah.numberInSurah}")
                                    ) {
                                        if (isBufferingThisAyah) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(16.dp),
                                                color = EmeraldDark,
                                                strokeWidth = 2.dp
                                            )
                                        } else {
                                            Icon(
                                                imageVector = if (isPlayingThisAyah) Icons.Default.Pause else Icons.Default.VolumeUp,
                                                contentDescription = "Listen to verse",
                                                tint = if (isPlayingThisAyah) Color.White else EmeraldDark,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // 1. Arabic Text
                            ArabicTextView(
                                text = ayah.arabic,
                                fontSize = fontSize.sp,
                                color = EmeraldDark
                            )

                            // 2. Transliteration (Displayed if FULL or ARABIC_TRANSLITERATION)
                            if (readingMode == QuranReadingMode.FULL || readingMode == QuranReadingMode.ARABIC_TRANSLITERATION) {
                                if (ayah.transliteration.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = ayah.transliteration,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontStyle = FontStyle.Italic,
                                            color = EmeraldMedium,
                                            lineHeight = 22.sp
                                        )
                                    )
                                }
                            }

                            // 3. Translation (Displayed if FULL or ARABIC_TRANSLATION)
                            if (readingMode == QuranReadingMode.FULL || readingMode == QuranReadingMode.ARABIC_TRANSLATION) {
                                if (ayah.translation.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = ayah.translation,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = TextDark,
                                            lineHeight = 22.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                // Previous & Next Surah Navigation Buttons at bottom
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    SurahNavigationRow(
                        currentSurahNumber = surah.number,
                        onNavigateSurah = onNavigateSurah
                    )
                }
            }
        }
    }

    // Modal/Dialog for Ayah details in Continuous Mode
    if (selectedAyahForDialog != null) {
        val ayah = selectedAyahForDialog!!
        AlertDialog(
            onDismissRequest = { selectedAyahForDialog = null },
            title = {
                Text(
                    text = "Surah ${surah.nameEnglish} • Verse ${ayah.numberInSurah}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = EmeraldDark
                    )
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ArabicTextView(
                        text = ayah.arabic,
                        fontSize = (fontSize - 2).sp,
                        color = EmeraldDark
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    if (ayah.transliteration.isNotBlank()) {
                        Text(
                            text = ayah.transliteration,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontStyle = FontStyle.Italic,
                                color = EmeraldMedium
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                    Text(
                        text = ayah.translation,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextDark
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onPlayAyah(ayah)
                        selectedAyahForDialog = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                ) {
                    Icon(Icons.Default.VolumeUp, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Listen")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedAyahForDialog = null }) {
                    Text("Close")
                }
            }
        )
    }
}

/**
 * CONTINUOUS READING MODE:
 * Renders the full authentic Arabic text flowing continuously like a physical Mushaf,
 * preserving right-to-left direction, letter connections, diacritics, and verse order,
 * with ornamental verse end markers containing the ayah number (۝N).
 */
@Composable
fun ContinuousQuranReadingView(
    surah: Surah,
    fontSize: Int,
    currentAudioAyah: Int?,
    onAyahSelected: (Ayah) -> Unit,
    onPlayAyah: (Ayah) -> Unit,
    onNavigateSurah: (Int) -> Unit
) {
    val scrollState = rememberLazyListState()

    LazyColumn(
        state = scrollState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Continuous Mushaf Page Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("continuous_mushaf_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, CreamBorder),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    // Frame Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "سُورَةُ ${surah.nameArabic}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = EmeraldDark
                            )
                        )
                        Surface(
                            color = GoldAccent.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = "Mushaf Continuous Flow",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = GoldAccent,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = CreamBorder, thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(14.dp))

                    // Flowing Arabic Text with subtle verse boundary markers
                    val continuousAnnotatedText = remember(surah.verses, fontSize, currentAudioAyah) {
                        buildAnnotatedString {
                            surah.verses.forEachIndexed { index, ayah ->
                                val isHighlight = currentAudioAyah == ayah.numberInSurah
                                if (isHighlight) {
                                    pushStyle(
                                        SpanStyle(
                                            background = EmeraldPale,
                                            color = EmeraldDark,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }

                                append(ayah.arabic.trim())

                                if (isHighlight) {
                                    pop()
                                }

                                // Ornamental verse boundary end marker with ayah number
                                append(" ")
                                pushStyle(
                                    SpanStyle(
                                        color = GoldAccent,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                append(" ۝${ayah.numberInSurah} ")
                                pop()
                                append(" ")
                            }
                        }
                    }

                    Text(
                        text = continuousAnnotatedText,
                        fontSize = fontSize.sp,
                        lineHeight = (fontSize * 1.85).sp,
                        fontWeight = FontWeight.Normal,
                        color = EmeraldDark,
                        textAlign = TextAlign.Justify,
                        style = TextStyle(textDirection = TextDirection.Rtl),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Ayah Quick Selector Bar for Continuous Mode
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CreamSurface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Tap a Verse to Read Meaning & Listen:",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(surah.verses) { ayah ->
                            val isPlaying = currentAudioAyah == ayah.numberInSurah
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isPlaying) EmeraldPrimary else Color.White,
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (isPlaying) EmeraldPrimary else CreamBorder),
                                modifier = Modifier
                                    .clickable { onAyahSelected(ayah) }
                            ) {
                                Text(
                                    text = "${ayah.numberInSurah}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isPlaying) Color.White else TextDark
                                    ),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Surah Navigation row at bottom
        item {
            SurahNavigationRow(
                currentSurahNumber = surah.number,
                onNavigateSurah = onNavigateSurah
            )
        }
    }
}

@Composable
fun SurahNavigationRow(
    currentSurahNumber: Int,
    onNavigateSurah: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (currentSurahNumber > 1) {
            val prevSurah = QuranData.SURAH_METADATA_LIST.find { it.number == currentSurahNumber - 1 }
            OutlinedButton(
                onClick = { onNavigateSurah(currentSurahNumber - 1) },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = EmeraldDark),
                border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Surah ${currentSurahNumber - 1}: ${prevSurah?.nameEnglish ?: ""}")
            }
        } else {
            Spacer(modifier = Modifier.width(1.dp))
        }

        if (currentSurahNumber < 114) {
            val nextSurah = QuranData.SURAH_METADATA_LIST.find { it.number == currentSurahNumber + 1 }
            Button(
                onClick = { onNavigateSurah(currentSurahNumber + 1) },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
            ) {
                Text("Surah ${currentSurahNumber + 1}: ${nextSurah?.nameEnglish ?: ""}")
                Spacer(modifier = Modifier.width(6.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
            }
        }
    }
}

private fun formatDuration(millis: Int): String {
    val totalSec = millis / 1000
    val minutes = totalSec / 60
    val seconds = totalSec % 60
    return String.format("%02d:%02d", minutes, seconds)
}

package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.dhikr.DhikrReadingMode
import com.example.data.hisnulmuslim.HisnulCategory
import com.example.data.hisnulmuslim.HisnulChapter
import com.example.data.hisnulmuslim.HisnulDua
import com.example.data.hisnulmuslim.HisnulMuslimData
import com.example.data.repository.AudioPlayerManager
import com.example.data.repository.AudioPlayerState
import com.example.data.repository.ZikrPreferencesRepository
import com.example.ui.components.ArabicTextView
import com.example.ui.components.ZikrTopBar
import com.example.ui.theme.*

@Composable
fun HisnulMuslimScreen(
    repository: ZikrPreferencesRepository,
    audioManager: AudioPlayerManager,
    initialChapterId: Int? = null,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val bookmarkedIds by repository.hisnulBookmarks.collectAsState()
    val favoriteIds by repository.hisnulFavorites.collectAsState()
    val dhikrReadingMode by repository.dhikrReadingMode.collectAsState()
    val audioState by audioManager.playerState.collectAsState()
    val currentAudioId by audioManager.currentPlayingId.collectAsState()
    val isRepeat by audioManager.isRepeatEnabled.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(HisnulCategory.ALL) }
    var selectedChapterId by remember { mutableStateOf(initialChapterId) }
    var showOnlySaved by remember { mutableStateOf(false) }

    BackHandler {
        if (selectedChapterId != null && initialChapterId == null) {
            selectedChapterId = null
        } else {
            onBack()
        }
    }

    val filteredDuas = remember(searchQuery, selectedCategory, selectedChapterId, showOnlySaved, bookmarkedIds, favoriteIds) {
        var list = HisnulMuslimData.DUAS

        if (showOnlySaved) {
            list = list.filter { bookmarkedIds.contains(it.id) || favoriteIds.contains(it.id) }
        } else {
            if (selectedChapterId != null) {
                list = list.filter { it.chapterId == selectedChapterId }
            } else if (selectedCategory != HisnulCategory.ALL) {
                list = HisnulMuslimData.getDuasByCategory(selectedCategory)
            }
        }

        if (searchQuery.isNotBlank()) {
            val q = searchQuery.trim().lowercase()
            list = list.filter {
                it.title.lowercase().contains(q) ||
                it.chapterTitle.lowercase().contains(q) ||
                it.occasion.lowercase().contains(q) ||
                it.translation.lowercase().contains(q) ||
                it.transliteration.lowercase().contains(q) ||
                it.arabic.contains(q) ||
                it.reference.lowercase().contains(q)
            }
        }
        list
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBg)
    ) {
        // Top App Bar
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
                            modifier = Modifier.testTag("hisnul_back_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                        Column {
                            Text(
                                text = "Hisnul Muslim",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = "Fortress of the Muslim • حصن المسلم",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = EmeraldContainer
                                )
                            )
                        }
                    }

                    // Saved filter toggle
                    IconButton(
                        onClick = { showOnlySaved = !showOnlySaved },
                        modifier = Modifier.testTag("hisnul_saved_filter_btn")
                    ) {
                        Icon(
                            imageVector = if (showOnlySaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Saved Duas",
                            tint = if (showOnlySaved) GoldLight else Color.White
                        )
                    }
                }
            }
        }

        // Search Bar
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
                    .testTag("hisnul_search_field"),
                placeholder = { Text("Search by occasion, dua title, or keyword...") },
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
        }

        // Category Filter Chips
        if (!showOnlySaved) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(HisnulCategory.values()) { category ->
                    val isSelected = selectedCategory == category && selectedChapterId == null
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedCategory = category
                            selectedChapterId = null
                        },
                        label = {
                            Text(
                                text = category.displayName,
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

        // Active Chapter Header Filter Pill (if a specific chapter is isolated)
        if (selectedChapterId != null) {
            val activeChapter = HisnulMuslimData.CHAPTERS.find { it.id == selectedChapterId }
            Surface(
                color = EmeraldPale,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Chapter ${activeChapter?.id}: ${activeChapter?.titleEnglish}",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = EmeraldDark
                            )
                        )
                        Text(
                            text = activeChapter?.titleArabic ?: "",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextMuted
                            )
                        )
                    }
                    IconButton(
                        onClick = { selectedChapterId = null },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Show All Chapters",
                            tint = EmeraldDark,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // Display Mode Switcher Row
        Surface(
            color = Color.White,
            tonalElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${filteredDuas.size} supplications found",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = TextMuted
                    )
                )

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    DhikrReadingMode.values().forEach { mode ->
                        val isModeSelected = dhikrReadingMode == mode
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isModeSelected) EmeraldPrimary else Color.Transparent,
                            modifier = Modifier
                                .clickable { repository.setDhikrReadingMode(mode) }
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = when (mode) {
                                    DhikrReadingMode.FULL -> "All"
                                    DhikrReadingMode.ARABIC_ONLY -> "Arabic"
                                    DhikrReadingMode.ARABIC_TRANSLATION -> "Meaning"
                                    DhikrReadingMode.ARABIC_TRANSLITERATION -> "Translit"
                                },
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isModeSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isModeSelected) Color.White else TextMuted
                                ),
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }

        // Duas List
        if (filteredDuas.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.SearchOff,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (showOnlySaved) "No saved supplications yet." else "No supplications match your search.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted),
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(filteredDuas, key = { it.id }) { dua ->
                    val isPlaying = currentAudioId == dua.id && audioState is AudioPlayerState.Playing
                    val isBuffering = currentAudioId == dua.id && audioState is AudioPlayerState.Buffering
                    val isBookmarked = bookmarkedIds.contains(dua.id)
                    val isFavorite = favoriteIds.contains(dua.id)

                    HisnulDuaCard(
                        dua = dua,
                        readingMode = dhikrReadingMode,
                        isPlaying = isPlaying,
                        isBuffering = isBuffering,
                        isRepeat = isRepeat,
                        isBookmarked = isBookmarked,
                        isFavorite = isFavorite,
                        onPlayAudio = {
                            if (isPlaying) {
                                audioManager.pause()
                            } else {
                                audioManager.playDhikr(dua.id, dua.arabic, dua.audioUrl)
                            }
                        },
                        onToggleRepeat = { audioManager.toggleRepeat() },
                        onToggleBookmark = { repository.toggleHisnulBookmark(dua.id) },
                        onToggleFavorite = { repository.toggleHisnulFavorite(dua.id) },
                        onCopyText = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText(
                                dua.title,
                                "${dua.title}\n\n${dua.arabic}\n\n${dua.transliteration}\n\nMeaning:\n${dua.translation}\n\nReference: ${dua.reference}"
                            )
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Supplication copied to clipboard", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun HisnulDuaCard(
    dua: HisnulDua,
    readingMode: DhikrReadingMode,
    isPlaying: Boolean,
    isBuffering: Boolean,
    isRepeat: Boolean,
    isBookmarked: Boolean,
    isFavorite: Boolean,
    onPlayAudio: () -> Unit,
    onToggleRepeat: () -> Unit,
    onToggleBookmark: () -> Unit,
    onToggleFavorite: () -> Unit,
    onCopyText: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("hisnul_card_${dua.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPlaying) Color(0xFFF0FDF4) else Color.White
        ),
        border = if (isPlaying) {
            androidx.compose.foundation.BorderStroke(1.5.dp, EmeraldPrimary)
        } else {
            androidx.compose.foundation.BorderStroke(1.dp, CreamBorder)
        },
        elevation = CardDefaults.cardElevation(if (isPlaying) 4.dp else 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Chapter, Occasion & Repetition Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        color = EmeraldPale,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = dua.occasion,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = EmeraldDark,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    Surface(
                        color = GoldAccent.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = dua.repetition,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = GoldAccent,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }

                // Bookmark & Favorite Action Buttons
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier.size(32.dp).testTag("fav_btn_${dua.id}")
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color(0xFFE11D48) else TextLight,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = onToggleBookmark,
                        modifier = Modifier.size(32.dp).testTag("bookmark_btn_${dua.id}")
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (isBookmarked) GoldAccent else TextLight,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Supplication Title
            Text(
                text = dua.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Arabic Calligraphy Card
            Surface(
                color = CreamSurface,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(0.5.dp, CreamBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                ArabicTextView(
                    arabicText = dua.arabic,
                    fontSize = 22,
                    textColor = EmeraldDark,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                )
            }

            // Transliteration (if not Arabic Only)
            if (readingMode == DhikrReadingMode.FULL || readingMode == DhikrReadingMode.ARABIC_TRANSLITERATION) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = dua.transliteration,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = EmeraldMedium,
                        fontStyle = FontStyle.Italic,
                        lineHeight = 20.sp
                    )
                )
            }

            // English Translation (if not Arabic Only & not Arabic+Transliteration)
            if (readingMode == DhikrReadingMode.FULL || readingMode == DhikrReadingMode.ARABIC_TRANSLATION) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = dua.translation,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextDark,
                        lineHeight = 21.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = CreamBorder, thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(8.dp))

            // Footer: Authentic Reference & Audio / Copy Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Reference Tag
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = GoldAccent,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = dua.reference,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextMuted,
                            fontWeight = FontWeight.Medium
                        ),
                        maxLines = 1
                    )
                }

                // Controls: Copy, Audio Repeat, Audio Play/Pause
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = onCopyText,
                        modifier = Modifier.size(34.dp).testTag("copy_btn_${dua.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy text",
                            tint = TextMuted,
                            modifier = Modifier.size(17.dp)
                        )
                    }

                    if (isPlaying) {
                        IconButton(
                            onClick = onToggleRepeat,
                            modifier = Modifier.size(34.dp)
                        ) {
                            Icon(
                                imageVector = if (isRepeat) Icons.Default.RepeatOne else Icons.Default.Repeat,
                                contentDescription = "Repeat",
                                tint = if (isRepeat) GoldAccent else TextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    FilledTonalButton(
                        onClick = onPlayAudio,
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = if (isPlaying) EmeraldPrimary else EmeraldPale,
                            contentColor = if (isPlaying) Color.White else EmeraldDark
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("play_btn_${dua.id}")
                    ) {
                        if (isBuffering) {
                            CircularProgressIndicator(
                                color = if (isPlaying) Color.White else EmeraldPrimary,
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "Pause" else "Play",
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isPlaying) "Pause" else "Audio",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}

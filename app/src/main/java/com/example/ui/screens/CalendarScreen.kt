package com.example.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.data.calendar.CalendarEntryCategory
import com.example.data.calendar.HijriConvention
import com.example.data.calendar.IslamicCalendarData
import com.example.data.calendar.IslamicEvent
import com.example.data.calendar.PersonalCalendarEntry
import com.example.data.reminder.ReminderScheduler
import com.example.data.repository.ZikrPreferencesRepository
import com.example.ui.components.SectionHeader
import com.example.ui.components.ZikrTopBar
import com.example.ui.theme.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    repository: ZikrPreferencesRepository,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val today = remember { LocalDate.now() }
    var currentYearMonth by remember { mutableStateOf(YearMonth.from(today)) }
    var selectedDate by remember { mutableStateOf(today) }
    var selectedTab by remember { mutableStateOf(0) } // 0: Month Grid, 1: Islamic Holidays, 2: My Tracking

    val hijriOffset by repository.hijriOffsetDays.collectAsState()
    val hijriConvention by repository.hijriConvention.collectAsState()
    val personalEntries by repository.calendarEntries.collectAsState()

    var showConventionDialog by remember { mutableStateOf(false) }
    var showAddEntryDialog by remember { mutableStateOf(false) }
    var entryToEdit by remember { mutableStateOf<PersonalCalendarEntry?>(null) }
    var holidaySearchQuery by remember { mutableStateOf("") }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(context, "Notification permission granted for calendar reminders.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Notification permission denied. Reminders may not sound.", Toast.LENGTH_LONG).show()
        }
    }

    fun checkAndRequestNotificationPermission(onGranted: () -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                onGranted()
            } else {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            onGranted()
        }
    }

    val selectedHijriDate = remember(selectedDate, hijriOffset) {
        IslamicCalendarData.getEstimatedHijriDate(selectedDate, hijriOffset)
    }

    val selectedDateEvents = remember(selectedHijriDate) {
        IslamicCalendarData.getEventsForHijriDay(selectedHijriDate.monthNumber, selectedHijriDate.day)
    }

    val selectedDateEntries = remember(personalEntries, selectedDate) {
        val iso = selectedDate.toString()
        personalEntries.filter { it.dateIso == iso }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBg)
    ) {
        // Top Bar
        Surface(
            color = EmeraldDark,
            tonalElevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
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
                            modifier = Modifier.testTag("calendar_back_btn")
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Column {
                            Text(
                                text = "Islamic & Gregorian Calendar",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = "${selectedHijriDate.formatted()} • ${selectedDate.format(DateTimeFormatter.ofPattern("d MMM yyyy"))}",
                                style = MaterialTheme.typography.bodySmall.copy(color = EmeraldPale)
                            )
                        }
                    }

                    // Sighting / Convention Settings Button
                    IconButton(
                        onClick = { showConventionDialog = true },
                        modifier = Modifier.testTag("calendar_convention_btn")
                    ) {
                        Icon(Icons.Default.Tune, contentDescription = "Hijri Convention & Moon Sighting Adjustment", tint = GoldLight)
                    }
                }
            }
        }

        // Top Navigation Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            contentColor = EmeraldDark
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Dual Calendar", fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal) },
                icon = { Icon(Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(18.dp)) },
                selectedContentColor = EmeraldDark,
                unselectedContentColor = TextMuted,
                modifier = Modifier.testTag("calendar_tab_dual")
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Sacred Days", fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal) },
                icon = { Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(18.dp)) },
                selectedContentColor = EmeraldDark,
                unselectedContentColor = TextMuted,
                modifier = Modifier.testTag("calendar_tab_holidays")
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("Personal (${personalEntries.size})", fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal) },
                icon = { Icon(Icons.Default.Bookmark, contentDescription = null, modifier = Modifier.size(18.dp)) },
                selectedContentColor = EmeraldDark,
                unselectedContentColor = TextMuted,
                modifier = Modifier.testTag("calendar_tab_tracking")
            )
        }

        when (selectedTab) {
            // TAB 0: DUAL MONTHLY CALENDAR GRID
            0 -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Month Navigation & Today Button
                    item {
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = { currentYearMonth = currentYearMonth.minusMonths(1) },
                                        modifier = Modifier.testTag("calendar_prev_month")
                                    ) {
                                        Icon(Icons.Default.ChevronLeft, contentDescription = "Previous Month", tint = EmeraldDark)
                                    }

                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = currentYearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = EmeraldDark
                                            )
                                        )
                                        // Corresponding Hijri month approximation
                                        val midMonthDate = currentYearMonth.atDay(15)
                                        val midHijri = IslamicCalendarData.getEstimatedHijriDate(midMonthDate, hijriOffset)
                                        Text(
                                            text = "${midHijri.monthName} ${midHijri.year} AH",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = GoldAccent,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        )
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        // Return to Today Button
                                        TextButton(
                                            onClick = {
                                                currentYearMonth = YearMonth.from(today)
                                                selectedDate = today
                                            },
                                            modifier = Modifier.testTag("calendar_today_btn")
                                        ) {
                                            Icon(Icons.Default.Today, contentDescription = null, modifier = Modifier.size(16.dp), tint = EmeraldPrimary)
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Today", color = EmeraldPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        }

                                        IconButton(
                                            onClick = { currentYearMonth = currentYearMonth.plusMonths(1) },
                                            modifier = Modifier.testTag("calendar_next_month")
                                        ) {
                                            Icon(Icons.Default.ChevronRight, contentDescription = "Next Month", tint = EmeraldDark)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Days of Week Header
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { dayName ->
                                        Text(
                                            text = dayName,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = if (dayName == "Fri") EmeraldPrimary else TextMuted
                                            ),
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                HorizontalDivider(color = CreamBorder, thickness = 0.5.dp)
                                Spacer(modifier = Modifier.height(8.dp))

                                // Monthly Grid Days
                                val firstDayOfMonth = currentYearMonth.atDay(1)
                                val daysInMonth = currentYearMonth.lengthOfMonth()
                                // Day of week (1 for Monday, 7 for Sunday; convert to 0 for Sunday..6 for Saturday)
                                val dayOfWeekSundayIndexed = (firstDayOfMonth.dayOfWeek.value % 7)

                                val totalCells = ((dayOfWeekSundayIndexed + daysInMonth + 6) / 7) * 7
                                val weeks = totalCells / 7

                                for (w in 0 until weeks) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 2.dp),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        for (d in 0..6) {
                                            val cellIndex = w * 7 + d
                                            val dayNumber = cellIndex - dayOfWeekSundayIndexed + 1

                                            if (dayNumber in 1..daysInMonth) {
                                                val cellDate = currentYearMonth.atDay(dayNumber)
                                                val isSelected = cellDate == selectedDate
                                                val isToday = cellDate == today
                                                val cellHijri = IslamicCalendarData.getEstimatedHijriDate(cellDate, hijriOffset)
                                                val hasIslamicEvent = IslamicCalendarData.getEventsForHijriDay(cellHijri.monthNumber, cellHijri.day).isNotEmpty()
                                                val hasPersonalNote = personalEntries.any { it.dateIso == cellDate.toString() }

                                                Box(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .height(52.dp)
                                                        .clip(RoundedCornerShape(10.dp))
                                                        .background(
                                                            when {
                                                                isSelected -> EmeraldDark
                                                                isToday -> EmeraldPale
                                                                else -> Color.Transparent
                                                            }
                                                        )
                                                        .border(
                                                            width = if (isToday && !isSelected) 1.5.dp else 0.dp,
                                                            color = if (isToday && !isSelected) EmeraldPrimary else Color.Transparent,
                                                            shape = RoundedCornerShape(10.dp)
                                                        )
                                                        .clickable { selectedDate = cellDate }
                                                        .testTag("cal_day_$dayNumber"),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                        Text(
                                                            text = "$dayNumber",
                                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                                fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Medium,
                                                                color = if (isSelected) Color.White else TextDark,
                                                                fontSize = 14.sp
                                                            )
                                                        )
                                                        Text(
                                                            text = "${cellHijri.day}",
                                                            style = MaterialTheme.typography.labelSmall.copy(
                                                                fontSize = 10.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                color = when {
                                                                    isSelected -> GoldLight
                                                                    hasIslamicEvent -> GoldAccent
                                                                    else -> TextMuted
                                                                }
                                                            )
                                                        )
                                                        // Dots indicators
                                                        Row(
                                                            horizontalArrangement = Arrangement.Center,
                                                            verticalAlignment = Alignment.CenterVertically,
                                                            modifier = Modifier.padding(top = 1.dp)
                                                        ) {
                                                            if (hasIslamicEvent) {
                                                                Box(
                                                                    modifier = Modifier
                                                                        .size(4.dp)
                                                                        .clip(CircleShape)
                                                                        .background(if (isSelected) GoldLight else GoldAccent)
                                                                )
                                                            }
                                                            if (hasPersonalNote) {
                                                                Spacer(modifier = Modifier.width(2.dp))
                                                                Box(
                                                                    modifier = Modifier
                                                                        .size(4.dp)
                                                                        .clip(CircleShape)
                                                                        .background(if (isSelected) Color.White else EmeraldPrimary)
                                                                )
                                                            }
                                                        }
                                                    }
                                                }
                                            } else {
                                                // Empty cell for preceding/succeeding months
                                                Spacer(modifier = Modifier.weight(1f))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Selected Date Detail Banner
                    item {
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = EmeraldDark),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(color = GoldAccent, shape = RoundedCornerShape(50)) {
                                        Text(
                                            text = if (selectedDate == today) "TODAY'S DATE" else "SELECTED DATE",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                letterSpacing = 1.sp
                                            ),
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                        )
                                    }

                                    FilledTonalButton(
                                        onClick = {
                                            entryToEdit = null
                                            showAddEntryDialog = true
                                        },
                                        colors = ButtonDefaults.filledTonalButtonColors(
                                            containerColor = GoldAccent,
                                            contentColor = Color.White
                                        ),
                                        shape = RoundedCornerShape(50)
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Add Note / Goal", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = selectedHijriDate.formatted(),
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                )
                                Text(
                                    text = selectedDate.format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy")),
                                    style = MaterialTheme.typography.bodyMedium.copy(color = EmeraldPale)
                                )

                                if (selectedHijriDate.isWhiteDay) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Surface(
                                        color = GoldAccent.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(Icons.Default.WbSunny, contentDescription = null, tint = GoldLight, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "Ayyam al-Beed (White Day) • Sunnah Fasting Recommended",
                                                style = MaterialTheme.typography.labelSmall.copy(color = GoldLight, fontWeight = FontWeight.Bold)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Islamic Events on this day
                    if (selectedDateEvents.isNotEmpty()) {
                        item {
                            SectionHeader(
                                title = "Sacred Observances on this Date",
                                subtitle = "Significance according to Islamic tradition"
                            )
                        }

                        items(selectedDateEvents) { event ->
                            IslamicEventCard(
                                event = event,
                                onSetReminder = {
                                    checkAndRequestNotificationPermission {
                                        ReminderScheduler.scheduleCalendarEventReminder(
                                            context = context,
                                            entryId = "holiday_${event.monthNumber}_${event.dayNumber}",
                                            title = event.title,
                                            note = event.description,
                                            year = selectedDate.year,
                                            month = selectedDate.monthValue,
                                            day = selectedDate.dayOfMonth,
                                            hour = 8,
                                            minute = 0
                                        )
                                        Toast.makeText(context, "Reminder set for ${event.title} on ${selectedDate.format(DateTimeFormatter.ofPattern("d MMM yyyy"))}!", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )
                        }
                    }

                    // Personal notes & worship tracking on this day
                    item {
                        SectionHeader(
                            title = "Personal Goals & Notes",
                            subtitle = if (selectedDateEntries.isEmpty()) "No personal entries on this date" else "${selectedDateEntries.size} saved entry on this date",
                            actionLabel = if (selectedDateEntries.isEmpty()) "Add Note" else null,
                            onActionClick = {
                                entryToEdit = null
                                showAddEntryDialog = true
                            }
                        )
                    }

                    if (selectedDateEntries.isEmpty()) {
                        item {
                            Surface(
                                color = Color.White,
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        entryToEdit = null
                                        showAddEntryDialog = true
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.NoteAdd, contentDescription = null, tint = EmeraldPrimary)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "Tap to track a worship goal or note",
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, color = TextDark)
                                        )
                                        Text(
                                            text = "Qur'an khatm milestones, fasting intentions, family events & reminders",
                                            style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        items(selectedDateEntries, key = { it.id }) { entry ->
                            PersonalEntryCard(
                                entry = entry,
                                onEdit = {
                                    entryToEdit = entry
                                    showAddEntryDialog = true
                                },
                                onDelete = {
                                    ReminderScheduler.cancelCalendarEventReminder(context, entry.id)
                                    repository.deleteCalendarEntry(entry.id)
                                    Toast.makeText(context, "Entry deleted.", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }

                    // Sighting notice
                    item {
                        Surface(
                            color = CreamSurface,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = EmeraldDark, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Current Convention: ${hijriConvention.displayName} (${if (hijriOffset >= 0) "+$hijriOffset" else "$hijriOffset"} day offset). Tap the tune icon at top to adjust for local moon sightings.",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextDark, lineHeight = 18.sp)
                                )
                            }
                        }
                    }
                }
            }

            // TAB 1: ALL ISLAMIC SPECIAL DAYS & HOLIDAYS
            1 -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    OutlinedTextField(
                        value = holidaySearchQuery,
                        onValueChange = { holidaySearchQuery = it },
                        placeholder = { Text("Search sacred days (Ramadan, Eid, Ashura, Arafah...)") },
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = EmeraldPrimary) },
                        trailingIcon = {
                            if (holidaySearchQuery.isNotEmpty()) {
                                IconButton(onClick = { holidaySearchQuery = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear", tint = TextMuted)
                                }
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = EmeraldPrimary,
                            unfocusedBorderColor = CreamBorder
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    val filteredEvents = remember(holidaySearchQuery) {
                        if (holidaySearchQuery.isBlank()) {
                            IslamicCalendarData.MAJOR_EVENTS
                        } else {
                            val q = holidaySearchQuery.trim().lowercase()
                            IslamicCalendarData.MAJOR_EVENTS.filter {
                                it.title.lowercase().contains(q) ||
                                        it.hijriDate.lowercase().contains(q) ||
                                        it.description.lowercase().contains(q)
                            }
                        }
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 90.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredEvents) { event ->
                            IslamicEventCard(
                                event = event,
                                onSetReminder = {
                                    checkAndRequestNotificationPermission {
                                        Toast.makeText(context, "Reminder registered for ${event.title}!", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )
                        }
                    }
                }
            }

            // TAB 2: MY TRACKED PERSONAL DATES & NOTES
            2 -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Personal Dates & Worship Goals",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = EmeraldDark)
                            )
                            Text(
                                text = "Private to your device • Never shared",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                            )
                        }

                        Button(
                            onClick = {
                                entryToEdit = null
                                showAddEntryDialog = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldDark),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.testTag("add_personal_entry_btn")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("New Entry", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (personalEntries.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(CircleShape)
                                        .background(EmeraldPale),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.EditCalendar, contentDescription = null, tint = EmeraldDark, modifier = Modifier.size(32.dp))
                                }
                                Spacer(modifier = Modifier.height(14.dp))
                                Text(
                                    text = "No personal entries yet",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextDark)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Add Qur'an milestones, fasting targets, family dates, and personal spiritual goals.",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextMuted, textAlign = TextAlign.Center)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                FilledTonalButton(
                                    onClick = {
                                        entryToEdit = null
                                        showAddEntryDialog = true
                                    },
                                    colors = ButtonDefaults.filledTonalButtonColors(containerColor = EmeraldPrimary, contentColor = Color.White)
                                ) {
                                    Text("Add Your First Entry")
                                }
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 90.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(personalEntries, key = { it.id }) { entry ->
                                PersonalEntryCard(
                                    entry = entry,
                                    onEdit = {
                                        entryToEdit = entry
                                        showAddEntryDialog = true
                                    },
                                    onDelete = {
                                        ReminderScheduler.cancelCalendarEventReminder(context, entry.id)
                                        repository.deleteCalendarEntry(entry.id)
                                        Toast.makeText(context, "Entry removed.", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // ==========================================
    // DIALOG: HIJRI CONVENTION & SIGHTING OFFSET
    // ==========================================
    if (showConventionDialog) {
        AlertDialog(
            onDismissRequest = { showConventionDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Tune, contentDescription = null, tint = EmeraldPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Hijri Convention & Offset", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Islamic dates rely on crescent moon sighting by local authorities. Adjust this setting if your local authority declared the month start 1 day earlier or later.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextDark, lineHeight = 18.sp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Calendar Convention Authority:",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = EmeraldDark)
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    HijriConvention.entries.forEach { conv ->
                        val isSelected = hijriConvention == conv
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { repository.setHijriConvention(conv) }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { repository.setHijriConvention(conv) },
                                colors = RadioButtonDefaults.colors(selectedColor = EmeraldPrimary)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = conv.displayName,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) EmeraldDark else TextDark
                                    )
                                )
                                Text(
                                    text = conv.description,
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Manual Lunar Day Offset:",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = EmeraldDark)
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        listOf(-2, -1, 0, 1, 2).forEach { offsetVal ->
                            val isSelected = hijriOffset == offsetVal
                            FilterChip(
                                selected = isSelected,
                                onClick = { repository.setHijriOffsetDays(offsetVal) },
                                label = {
                                    Text(
                                        text = if (offsetVal > 0) "+$offsetVal d" else if (offsetVal < 0) "$offsetVal d" else "0 (Std)",
                                        fontSize = 11.sp
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = EmeraldPrimary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showConventionDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldDark)
                ) {
                    Text("Done")
                }
            }
        )
    }

    // ==========================================
    // DIALOG: ADD / EDIT PERSONAL CALENDAR ENTRY
    // ==========================================
    if (showAddEntryDialog) {
        var entryTitle by remember { mutableStateOf(entryToEdit?.title ?: "") }
        var entryNote by remember { mutableStateOf(entryToEdit?.note ?: "") }
        var entryCategory by remember { mutableStateOf(entryToEdit?.category ?: CalendarEntryCategory.WORSHIP_GOAL) }
        var entryDateIso by remember { mutableStateOf(entryToEdit?.dateIso ?: selectedDate.toString()) }
        var enableReminder by remember { mutableStateOf(entryToEdit?.hasReminder ?: false) }
        var reminderHour by remember { mutableStateOf(entryToEdit?.reminderHour ?: 8) }
        var reminderMinute by remember { mutableStateOf(entryToEdit?.reminderMinute ?: 0) }

        AlertDialog(
            onDismissRequest = { showAddEntryDialog = false },
            title = {
                Text(
                    text = if (entryToEdit == null) "Track Date / Goal" else "Edit Entry",
                    fontWeight = FontWeight.Bold,
                    color = EmeraldDark
                )
            },
            text = {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    item {
                        Text(
                            text = "Date: $entryDateIso",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = GoldAccent)
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = entryTitle,
                            onValueChange = { entryTitle = it },
                            label = { Text("Title / Goal Name") },
                            placeholder = { Text("e.g. Read Surah Al-Kahf, Fasting Day") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text("Category:", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                        Spacer(modifier = Modifier.height(6.dp))

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(CalendarEntryCategory.entries) { cat ->
                                val isSelected = entryCategory == cat
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { entryCategory = cat },
                                    label = { Text(cat.displayName, fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = EmeraldPrimary,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = entryNote,
                            onValueChange = { entryNote = it },
                            label = { Text("Personal Notes & Du'a") },
                            placeholder = { Text("Add specific intentions or targets...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Notifications, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text("Set Notification Reminder", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                                    Text("Alerts on morning of this date", style = MaterialTheme.typography.bodySmall.copy(color = TextMuted))
                                }
                            }
                            Switch(
                                checked = enableReminder,
                                onCheckedChange = { enableReminder = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = EmeraldPrimary)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (entryTitle.isBlank()) {
                            Toast.makeText(context, "Please enter a title for your goal.", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        val parsedDate = try {
                            LocalDate.parse(entryDateIso)
                        } catch (e: Exception) {
                            selectedDate
                        }

                        val newEntry = PersonalCalendarEntry(
                            id = entryToEdit?.id ?: java.util.UUID.randomUUID().toString(),
                            dateIso = parsedDate.toString(),
                            title = entryTitle.trim(),
                            note = entryNote.trim(),
                            category = entryCategory,
                            hasReminder = enableReminder,
                            reminderHour = reminderHour,
                            reminderMinute = reminderMinute
                        )

                        repository.addCalendarEntry(newEntry)

                        if (enableReminder) {
                            checkAndRequestNotificationPermission {
                                ReminderScheduler.scheduleCalendarEventReminder(
                                    context = context,
                                    entryId = newEntry.id,
                                    title = newEntry.title,
                                    note = newEntry.note,
                                    year = parsedDate.year,
                                    month = parsedDate.monthValue,
                                    day = parsedDate.dayOfMonth,
                                    hour = reminderHour,
                                    minute = reminderMinute
                                )
                            }
                        } else {
                            ReminderScheduler.cancelCalendarEventReminder(context, newEntry.id)
                        }

                        Toast.makeText(context, "Goal saved successfully!", Toast.LENGTH_SHORT).show()
                        showAddEntryDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldDark)
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddEntryDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun IslamicEventCard(
    event: IslamicEvent,
    onSetReminder: () -> Unit
) {
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
                    text = event.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = EmeraldDark
                    ),
                    modifier = Modifier.weight(1f)
                )

                if (event.isFastingRecommended) {
                    Surface(
                        color = GoldAccent.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = "Sunnah Fasting",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = GoldAccent,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CalendarToday, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${event.hijriDate} • ${event.estimatedGregorianDate}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = GoldAccent,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = event.description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextDark,
                    lineHeight = 20.sp
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onSetReminder) {
                    Icon(Icons.Default.NotificationsActive, contentDescription = null, modifier = Modifier.size(16.dp), tint = EmeraldPrimary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Remind Me", color = EmeraldPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun PersonalEntryCard(
    entry: PersonalCalendarEntry,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Surface(
                        color = EmeraldPale,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = when (entry.category) {
                                    CalendarEntryCategory.WORSHIP_GOAL -> Icons.Default.Favorite
                                    CalendarEntryCategory.QURAN_MILESTONE -> Icons.Default.MenuBook
                                    CalendarEntryCategory.SUNNAH_FAST -> Icons.Default.WbSunny
                                    CalendarEntryCategory.FAMILY_DUA -> Icons.Default.Groups
                                    CalendarEntryCategory.PERSONAL_NOTE -> Icons.Default.Edit
                                },
                                contentDescription = null,
                                tint = EmeraldDark,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = entry.title,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextDark)
                        )
                        Text(
                            text = "${entry.category.displayName} • ${entry.dateIso}",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                        )
                    }
                }

                Row {
                    IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = TextMuted, modifier = Modifier.size(16.dp))
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFDC2626), modifier = Modifier.size(16.dp))
                    }
                }
            }

            if (entry.note.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = entry.note,
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextDark, lineHeight = 20.sp)
                )
            }

            if (entry.hasReminder) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Alarm, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Reminder set for ${String.format("%02d:%02d", entry.reminderHour, entry.reminderMinute)}",
                        style = MaterialTheme.typography.labelSmall.copy(color = EmeraldPrimary, fontWeight = FontWeight.SemiBold)
                    )
                }
            }
        }
    }
}

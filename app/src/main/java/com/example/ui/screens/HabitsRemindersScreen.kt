package com.example.ui.screens

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.data.habits.HabitData
import com.example.data.habits.HabitDefinition
import com.example.data.repository.ZikrPreferencesRepository
import com.example.ui.components.SectionHeader
import com.example.ui.components.ZikrTopBar
import com.example.ui.theme.*

@Composable
fun HabitsRemindersScreen(
    repository: ZikrPreferencesRepository
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) } // 0: Habits, 1: Reminders

    val todayHabits by repository.todayHabits.collectAsState()
    val remindersMap by repository.prayerReminders.collectAsState()

    var hasNotificationPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasNotificationPermission = isGranted
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBg)
    ) {
        ZikrTopBar(
            title = "Habits & Reminders",
            subtitle = "Consistency in worship • Notifications"
        )

        // Sub Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = EmeraldDark,
            contentColor = Color.White,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = GoldLight,
                    height = 3.dp
                )
            }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Text(
                        "Daily Habits (${todayHabits.size}/${HabitData.HABITS.size})",
                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == 0) GoldLight else Color.White
                    )
                },
                modifier = Modifier.testTag("habits_tab_btn")
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Text(
                        "Islamic Reminders",
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == 1) GoldLight else Color.White
                    )
                },
                modifier = Modifier.testTag("reminders_tab_btn")
            )
        }

        if (selectedTab == 0) {
            HabitsChecklistView(
                todayHabits = todayHabits,
                onToggleHabit = { repository.toggleHabit(it) }
            )
        } else {
            RemindersSettingsView(
                remindersMap = remindersMap,
                hasPermission = hasNotificationPermission,
                onRequestPermission = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                },
                onToggleReminder = { key, enabled ->
                    repository.toggleReminder(key, enabled)
                    if (key == "MorningAdhkar") {
                        if (enabled) {
                            com.example.data.reminder.ReminderScheduler.scheduleMorningReminder(context)
                        } else {
                            com.example.data.reminder.ReminderScheduler.cancelReminder(
                                context,
                                com.example.data.reminder.ReminderScheduler.REQUEST_CODE_MORNING
                            )
                        }
                    } else if (key == "EveningAdhkar") {
                        if (enabled) {
                            com.example.data.reminder.ReminderScheduler.scheduleEveningReminder(context)
                        } else {
                            com.example.data.reminder.ReminderScheduler.cancelReminder(
                                context,
                                com.example.data.reminder.ReminderScheduler.REQUEST_CODE_EVENING
                            )
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun HabitsChecklistView(
    todayHabits: Set<String>,
    onToggleHabit: (String) -> Unit
) {
    val totalCount = HabitData.HABITS.size
    val completedCount = todayHabits.size
    val progressRatio = completedCount.toFloat() / totalCount

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Progress Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
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
                        Column {
                            Text(
                                text = "Today's Consistency",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldDark
                                )
                            )
                            Text(
                                text = "$completedCount of $totalCount worship habits completed",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(if (progressRatio >= 1.0f) EmeraldMedium else EmeraldPale),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${(progressRatio * 100).toInt()}%",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (progressRatio >= 1.0f) Color.White else EmeraldDark
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LinearProgressIndicator(
                        progress = { progressRatio },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        color = EmeraldPrimary,
                        trackColor = EmeraldContainer
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        color = CreamSurface,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                tint = GoldAccent,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "“The deeds most loved by Allah are those done regularly, even if small.” — Sahih al-Bukhari",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextDark)
                            )
                        }
                    }
                }
            }
        }

        // Checklist Items
        items(HabitData.HABITS, key = { it.id }) { habit ->
            val isDone = todayHabits.contains(habit.id)

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDone) EmeraldPale.copy(alpha = 0.5f) else Color.White
                ),
                border = if (isDone) androidx.compose.foundation.BorderStroke(1.dp, EmeraldLight) else null,
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleHabit(habit.id) }
                    .testTag("habit_card_${habit.id}")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isDone,
                        onCheckedChange = { onToggleHabit(habit.id) },
                        colors = CheckboxDefaults.colors(
                            checkedColor = EmeraldPrimary,
                            checkmarkColor = Color.White
                        ),
                        modifier = Modifier.testTag("habit_check_${habit.id}")
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = habit.title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDone) EmeraldDark else TextDark
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = GoldContainer.copy(alpha = 0.6f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = habit.category,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = GoldMuted,
                                        fontSize = 10.sp
                                    ),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = habit.subtitle,
                            style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = habit.hadithInspiration,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = EmeraldPrimary,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RemindersSettingsView(
    remindersMap: Map<String, Boolean>,
    hasPermission: Boolean,
    onRequestPermission: () -> Unit,
    onToggleReminder: (String, Boolean) -> Unit
) {
    val context = LocalContext.current
    var testSentMessage by remember { mutableStateOf<String?>(null) }

    fun sendTestNotification() {
        try {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val channelId = "zikr_reminders_channel"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    "ZIKR LIFE Reminders",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Prayer times and remembrance reminders"
                }
                notificationManager.createNotificationChannel(channel)
            }

            val notification = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setContentTitle("ZIKR LIFE • Prayer Reminder")
                .setContentText("It is time to remember Allah. Offer your prayer with tranquility.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build()

            notificationManager.notify(1001, notification)
            testSentMessage = "Test reminder sent to your notification bar!"
        } catch (e: Exception) {
            testSentMessage = "Error sending reminder: ${e.localizedMessage}"
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Permission Status
        item {
            if (!hasPermission) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = GoldContainer),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = GoldMuted)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Enable Notifications",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = GoldMuted
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "To receive timely prayer and adhkar notifications when the app is in the background, please grant notification permissions.",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextDark)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = onRequestPermission,
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                            modifier = Modifier.testTag("enable_notifications_btn")
                        ) {
                            Text("Grant Permission")
                        }
                    }
                }
            } else {
                Card(
                    colors = CardDefaults.cardColors(containerColor = EmeraldPale),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldMedium)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Notifications are active on Android",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldDark
                                )
                            )
                        }

                        Button(
                            onClick = { sendTestNotification() },
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("send_test_notification_btn")
                        ) {
                            Text("Test")
                        }
                    }
                }
            }
        }

        if (testSentMessage != null) {
            item {
                Text(
                    text = testSentMessage!!,
                    style = MaterialTheme.typography.bodySmall.copy(color = EmeraldDark, fontWeight = FontWeight.SemiBold)
                )
            }
        }

        item {
            SectionHeader(
                title = "Prayer Notifications",
                subtitle = "Fajr, Dhuhr, Asr, Maghrib, and Isha"
            )
        }

        val prayerReminders = listOf("Fajr", "Dhuhr", "Asr", "Maghrib", "Isha")
        items(prayerReminders) { prayer ->
            val isEnabled = remindersMap[prayer] ?: true
            ReminderToggleRow(
                title = "$prayer Prayer",
                subtitle = "At prayer entrance time",
                enabled = isEnabled,
                onToggle = { onToggleReminder(prayer, it) }
            )
        }

        item {
            SectionHeader(
                title = "Special Islamic Reminders",
                subtitle = "Adhkar, Sunnah Fasts, and Friday Surah Al-Kahf"
            )
        }

        val specialReminders = listOf(
            Triple("MorningAdhkar", "Morning Adhkar", "After Fajr / Sunrise reminder"),
            Triple("EveningAdhkar", "Evening Adhkar", "After Asr / Before sunset reminder"),
            Triple("FridayKahf", "Friday Surah Al-Kahf & Salawat", "Every Friday morning"),
            Triple("MondaysThursdaysFasting", "Mondays & Thursdays Fasting", "Remind evening before Sunnah fast"),
            Triple("SuhoorIftar", "Ramadan Suhoor & Iftar Alerts", "Pre-dawn alert & Maghrib alert")
        )

        items(specialReminders) { (key, title, sub) ->
            val isEnabled = remindersMap[key] ?: true
            ReminderToggleRow(
                title = title,
                subtitle = sub,
                enabled = isEnabled,
                onToggle = { onToggleReminder(key, it) }
            )
        }
    }
}

@Composable
fun ReminderToggleRow(
    title: String,
    subtitle: String,
    enabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = TextDark
                    )
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                )
            }
            Switch(
                checked = enabled,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = EmeraldPrimary
                ),
                modifier = Modifier.testTag("reminder_switch_${title.replace(" ", "_").lowercase()}")
            )
        }
    }
}

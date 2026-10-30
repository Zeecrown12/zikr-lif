package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.AudioPlayerManager
import com.example.data.repository.ZikrPreferencesRepository
import com.example.ui.screens.*
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.ZikrLifeTheme

enum class MainNavTab(val title: String, val icon: ImageVector) {
    HOME("Home", Icons.Default.Home),
    QURAN("Quran", Icons.Default.MenuBook),
    DHIKR("Dhikr", Icons.Default.TouchApp),
    REMINDERS("Reminders", Icons.Default.Notifications),
    COMMUNITY("Community", Icons.Default.Groups)
}

enum class SubScreen {
    RAMADAN,
    ALLAH_NAMES,
    WOMEN,
    CALENDAR,
    QIBLA,
    SETTINGS,
    HISNUL_MUSLIM,
    APP_UPDATE
}

class MainActivity : ComponentActivity() {

    private lateinit var repository: ZikrPreferencesRepository
    private lateinit var audioManager: AudioPlayerManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        repository = ZikrPreferencesRepository(applicationContext)
        audioManager = AudioPlayerManager(applicationContext)

        setContent {
            ZikrLifeTheme {
                ZikrLifeApp(
                    repository = repository,
                    audioManager = audioManager
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        audioManager.release()
    }
}

@Composable
fun ZikrLifeApp(
    repository: ZikrPreferencesRepository,
    audioManager: AudioPlayerManager
) {
    var currentTab by remember { mutableStateOf(MainNavTab.HOME) }
    var dhikrInitialTab by remember { mutableStateOf(0) }
    var activeSubScreen by remember { mutableStateOf<SubScreen?>(null) }
    var hisnulChapterId by remember { mutableStateOf<Int?>(null) }

    // Intercept back button when in a subscreen or non-home tab
    BackHandler(enabled = activeSubScreen != null || currentTab != MainNavTab.HOME) {
        if (activeSubScreen != null) {
            activeSubScreen = null
            hisnulChapterId = null
        } else {
            currentTab = MainNavTab.HOME
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (activeSubScreen == null) {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 8.dp
                ) {
                    MainNavTab.values().forEach { tab ->
                        val isSelected = currentTab == tab
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                currentTab = tab
                                if (tab == MainNavTab.DHIKR) {
                                    dhikrInitialTab = 0
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = tab.title,
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = tab.title,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 11.sp
                                    )
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = EmeraldDark,
                                selectedTextColor = EmeraldDark,
                                indicatorColor = com.example.ui.theme.EmeraldContainer,
                                unselectedIconColor = com.example.ui.theme.TextMuted,
                                unselectedTextColor = com.example.ui.theme.TextMuted
                            ),
                            modifier = Modifier.testTag("nav_${tab.title.lowercase()}")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (activeSubScreen != null) {
                when (activeSubScreen) {
                    SubScreen.RAMADAN -> RamadanScreen(
                        repository = repository,
                        onBack = { activeSubScreen = null }
                    )
                    SubScreen.ALLAH_NAMES -> AllahNamesScreen(
                        onBack = { activeSubScreen = null }
                    )
                    SubScreen.WOMEN -> WomenScreen(
                        repository = repository,
                        onBack = { activeSubScreen = null }
                    )
                    SubScreen.CALENDAR -> CalendarScreen(
                        repository = repository,
                        onBack = { activeSubScreen = null }
                    )
                    SubScreen.QIBLA -> QiblaScreen(
                        repository = repository,
                        onBack = { activeSubScreen = null }
                    )
                    SubScreen.SETTINGS -> SettingsScreen(
                        repository = repository,
                        onNavigateToUpdate = { activeSubScreen = SubScreen.APP_UPDATE },
                        onBack = { activeSubScreen = null }
                    )
                    SubScreen.HISNUL_MUSLIM -> HisnulMuslimScreen(
                        repository = repository,
                        audioManager = audioManager,
                        initialChapterId = hisnulChapterId,
                        onBack = {
                            hisnulChapterId = null
                            activeSubScreen = null
                        }
                    )
                    SubScreen.APP_UPDATE -> UpdateScreen(
                        onBack = { activeSubScreen = null }
                    )
                    null -> {}
                }
            } else {
                AnimatedContent(
                    targetState = currentTab,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "tab_animation"
                ) { tab ->
                    when (tab) {
                        MainNavTab.HOME -> HomeScreen(
                            repository = repository,
                            onNavigateToQuran = { currentTab = MainNavTab.QURAN },
                            onNavigateToDhikrTab = { tabIndex ->
                                dhikrInitialTab = tabIndex
                                currentTab = MainNavTab.DHIKR
                            },
                            onNavigateToHabits = { currentTab = MainNavTab.REMINDERS },
                            onNavigateToCommunity = { currentTab = MainNavTab.COMMUNITY },
                            onNavigateToRamadan = { activeSubScreen = SubScreen.RAMADAN },
                            onNavigateToAllahNames = { activeSubScreen = SubScreen.ALLAH_NAMES },
                            onNavigateToQibla = { activeSubScreen = SubScreen.QIBLA },
                            onNavigateToSettings = { activeSubScreen = SubScreen.SETTINGS },
                            onNavigateToCalendar = { activeSubScreen = SubScreen.CALENDAR },
                            onNavigateToHisnulMuslim = { chapterId ->
                                hisnulChapterId = chapterId
                                activeSubScreen = SubScreen.HISNUL_MUSLIM
                            }
                        )
                        MainNavTab.QURAN -> QuranScreen(
                            repository = repository,
                            audioManager = audioManager
                        )
                        MainNavTab.DHIKR -> DhikrScreen(
                            repository = repository,
                            audioManager = audioManager,
                            initialTab = dhikrInitialTab
                        )
                        MainNavTab.REMINDERS -> HabitsRemindersScreen(
                            repository = repository
                        )
                        MainNavTab.COMMUNITY -> CommunityScreen(
                            repository = repository,
                            onNavigateToRamadan = { activeSubScreen = SubScreen.RAMADAN },
                            onNavigateToAllahNames = { activeSubScreen = SubScreen.ALLAH_NAMES },
                            onNavigateToWomen = { activeSubScreen = SubScreen.WOMEN },
                            onNavigateToCalendar = { activeSubScreen = SubScreen.CALENDAR },
                            onNavigateToQibla = { activeSubScreen = SubScreen.QIBLA },
                            onNavigateToSettings = { activeSubScreen = SubScreen.SETTINGS }
                        )
                    }
                }
            }
        }
    }
}

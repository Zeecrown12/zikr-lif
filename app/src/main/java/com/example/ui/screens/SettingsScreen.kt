package com.example.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.data.prayer.CalculationMethod
import com.example.data.prayer.CityLocation
import com.example.data.prayer.JuristicMethod
import com.example.data.prayer.PrayerCalculator
import com.example.data.repository.ZikrPreferencesRepository
import com.example.ui.components.SectionHeader
import com.example.ui.components.TypographySettingsDialog
import com.example.ui.components.ZikrTopBar
import com.example.ui.theme.*
import com.google.android.gms.location.LocationServices

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    repository: ZikrPreferencesRepository,
    onNavigateToUpdate: () -> Unit = {},
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val currentCity by repository.currentCity.collectAsState()
    val calcMethod by repository.calculationMethod.collectAsState()
    val juristicMethod by repository.juristicMethod.collectAsState()
    val isHapticEnabled by repository.hapticEnabled.collectAsState()
    val quranFontSize by repository.quranFontSize.collectAsState()
    val translationFontSize by repository.translationFontSize.collectAsState()
    val arabicFont by repository.arabicFont.collectAsState()
    val englishFont by repository.englishFont.collectAsState()

    var showCityDialog by remember { mutableStateOf(false) }
    var showTypographyDialog by remember { mutableStateOf(false) }
    var locationStatusMessage by remember { mutableStateOf<String?>(null) }

    if (showTypographyDialog) {
        TypographySettingsDialog(
            repository = repository,
            onDismiss = { showTypographyDialog = false }
        )
    }

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { loc: Location? ->
                    if (loc != null) {
                        val tzOffset = (java.util.TimeZone.getDefault().rawOffset / 3600000.0)
                        val gpsCity = CityLocation("Current GPS Location", "Local Device", loc.latitude, loc.longitude, tzOffset)
                        repository.setCity(gpsCity)
                        locationStatusMessage = "Location updated to GPS coordinates!"
                    } else {
                        locationStatusMessage = "GPS signal acquired; select city if indoors."
                    }
                }
            } catch (e: SecurityException) {
                locationStatusMessage = "Location permission error."
            }
        } else {
            locationStatusMessage = "Location permission denied. You can select your city manually below."
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBg)
    ) {
        ZikrTopBar(
            title = "App Settings",
            subtitle = "Calculation methods, Location & Privacy",
            showBack = true,
            onBackClick = onBack
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Location & Prayer Time Settings
            item {
                SectionHeader(
                    title = "Location & Prayer Calculations",
                    subtitle = "Used to determine accurate Fajr, Dhuhr, Asr, Maghrib & Isha"
                )
            }

            // Current City Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showCityDialog = true }
                        .testTag("settings_city_card")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = EmeraldPrimary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "City / Coordinates",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                                )
                                Text(
                                    text = "${currentCity.name}, ${currentCity.country}",
                                    style = MaterialTheme.typography.bodySmall.copy(color = EmeraldDark, fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                        TextButton(onClick = { showCityDialog = true }) {
                            Text("Change")
                        }
                    }
                }
            }

            // GPS Auto-detect Button
            item {
                OutlinedButton(
                    onClick = {
                        val hasCoarse = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED

                        if (hasCoarse) {
                            try {
                                fusedLocationClient.lastLocation.addOnSuccessListener { loc: Location? ->
                                    if (loc != null) {
                                        val tzOffset = (java.util.TimeZone.getDefault().rawOffset / 3600000.0)
                                        val gpsCity = CityLocation("Current Location", "Device GPS", loc.latitude, loc.longitude, tzOffset)
                                        repository.setCity(gpsCity)
                                        locationStatusMessage = "Detected location: %.2f°, %.2f°".format(loc.latitude, loc.longitude)
                                    } else {
                                        locationStatusMessage = "Location not immediately available. Please choose from city presets."
                                    }
                                }
                            } catch (_: SecurityException) {}
                        } else {
                            locationPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("gps_auto_detect_btn"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.MyLocation, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Auto-Detect Location (GPS)")
                }

                if (locationStatusMessage != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = locationStatusMessage!!,
                        style = MaterialTheme.typography.bodySmall.copy(color = EmeraldDark)
                    )
                }
            }

            // Calculation Method
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Calculation Method",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = EmeraldDark)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        for (method in CalculationMethod.values()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { repository.setCalculationMethod(method) }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = calcMethod == method,
                                    onClick = { repository.setCalculationMethod(method) },
                                    colors = RadioButtonDefaults.colors(selectedColor = EmeraldPrimary)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = method.displayName,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = if (calcMethod == method) FontWeight.Bold else FontWeight.Normal
                                        )
                                    )
                                    Text(
                                        text = "Fajr: ${method.fajrAngle}° • Isha: ${if (method.ishaMinutes != null) "${method.ishaMinutes}m" else "${method.ishaAngle}°"}",
                                        style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Asr Juristic School
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Asr Juristic Method",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = EmeraldDark)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        for (method in JuristicMethod.values()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { repository.setJuristicMethod(method) }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = juristicMethod == method,
                                    onClick = { repository.setJuristicMethod(method) },
                                    colors = RadioButtonDefaults.colors(selectedColor = EmeraldPrimary)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = method.displayName,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = if (juristicMethod == method) FontWeight.Bold else FontWeight.Normal
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // General & Accessibility Settings
            item {
                SectionHeader(
                    title = "Preferences & Feedback",
                    subtitle = "Haptics, font size, and display"
                )
            }

            // Haptic Feedback
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
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
                                text = "Vibration & Haptic Feedback",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                            Text(
                                text = "Tactile vibration when tapping Digital Tasbih beads",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                            )
                        }
                        Switch(
                            checked = isHapticEnabled,
                            onCheckedChange = { repository.setHapticEnabled(it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = EmeraldPrimary)
                        )
                    }
                }
            }

            // Qur'an & Reading Typography
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showTypographyDialog = true }
                        .testTag("settings_typography_card")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.FormatSize, contentDescription = null, tint = EmeraldPrimary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Reading & Qur'an Typography",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                                )
                                Text(
                                    text = "Arabic: ${arabicFont.displayName} • English: ${englishFont.displayName}",
                                    style = MaterialTheme.typography.bodySmall.copy(color = EmeraldDark, fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "Arabic: ${quranFontSize}sp • Meaning: ${translationFontSize}sp",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                                )
                            }
                        }
                        TextButton(onClick = { showTypographyDialog = true }) {
                            Text("Change")
                        }
                    }
                }
            }

            // Quran Font Size
            item {
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
                                text = "Quran Arabic Font Size",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                            Text(
                                text = "${quranFontSize}sp",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = EmeraldDark)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Slider(
                            value = quranFontSize.toFloat(),
                            onValueChange = { repository.setQuranFontSize(it.toInt()) },
                            valueRange = 18f..38f,
                            steps = 9,
                            colors = SliderDefaults.colors(
                                thumbColor = EmeraldPrimary,
                                activeTrackColor = EmeraldPrimary
                            )
                        )
                    }
                }
            }

            // App Updates & Distribution Channel Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CreamBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToUpdate() }
                        .testTag("settings_updates_btn")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldPale),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SystemUpdate,
                                    contentDescription = null,
                                    tint = EmeraldDark,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "App Updates & Distribution",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextDark
                                    )
                                )
                                Text(
                                    text = "Check version, direct APK updates & Play Store",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = TextMuted
                        )
                    }
                }
            }

            // Privacy & Architecture Pledge
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CreamSurface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = EmeraldDark)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Privacy & Offline Architecture",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = EmeraldDark)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "• No user account or registration required for core features.\n• All worship habits, tasbih totals, bookmarks, and notes remain on your phone.\n• Zero tracking or selling of private spiritual activity.\n• Free tools and services prioritized.",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextDark, lineHeight = 20.sp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "ZIKR LIFE • “Remember Allah. Grow Together.”",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                        )
                    }
                }
            }
        }
    }

    // City Selection Dialog
    if (showCityDialog) {
        AlertDialog(
            onDismissRequest = { showCityDialog = false },
            title = { Text("Select City") },
            text = {
                LazyColumn(modifier = Modifier.height(300.dp)) {
                    items(PrayerCalculator.DEFAULT_CITIES) { city ->
                        val isSelected = currentCity.name == city.name
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    repository.setCity(city)
                                    showCityDialog = false
                                }
                                .padding(vertical = 10.dp, horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = city.name,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) EmeraldDark else TextDark
                                    )
                                )
                                Text(
                                    text = city.country,
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                                )
                            }
                            if (isSelected) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = EmeraldMedium)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCityDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

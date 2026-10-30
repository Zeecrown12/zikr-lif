package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.update.ReleaseUpdateInfo
import com.example.data.update.UpdateCheckResult
import com.example.data.update.UpdateManager
import com.example.ui.components.ZikrTopBar
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun UpdateScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var checkResult by remember { mutableStateOf<UpdateCheckResult>(UpdateCheckResult.Idle) }
    var isChecking by remember { mutableStateOf(false) }
    var showPermissionGuide by remember { mutableStateOf(false) }

    val currentVersionName = remember { UpdateManager.getCurrentVersionName() }
    val currentVersionCode = remember { UpdateManager.getCurrentVersionCode() }
    val isPlayStore = remember { UpdateManager.isInstalledFromPlayStore(context) }

    // Automatic check on first load
    LaunchedEffect(Unit) {
        isChecking = true
        checkResult = UpdateCheckResult.Checking
        val result = UpdateManager.checkForUpdates(forceOnline = true)
        checkResult = result
        isChecking = false
    }

    BackHandler {
        onBack()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBg)
    ) {
        ZikrTopBar(
            title = "App Updates & Distribution",
            subtitle = "Official Releases, Versioning & Data Safety",
            showBack = true,
            onBackClick = onBack
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Version Header Card
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(EmeraldPale),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SystemUpdate,
                                contentDescription = null,
                                tint = EmeraldDark,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "ZIKR LIFE",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = EmeraldDark
                            )
                        )

                        Text(
                            text = "Installed Version: $currentVersionName (Build $currentVersionCode)",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextMedium,
                                fontWeight = FontWeight.Medium
                            )
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Distribution Channel Badge
                        Surface(
                            color = if (isPlayStore) EmeraldContainer else GoldContainer,
                            shape = RoundedCornerShape(50)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = if (isPlayStore) Icons.Default.Shop else Icons.Default.Download,
                                    contentDescription = null,
                                    tint = if (isPlayStore) EmeraldDark else GoldMuted,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isPlayStore) "Installed via Google Play Store" else "Direct APK / File Share Installation",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (isPlayStore) EmeraldDark else GoldMuted,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Live Check Button
                        OutlinedButton(
                            onClick = {
                                coroutineScope.launch {
                                    isChecking = true
                                    checkResult = UpdateCheckResult.Checking
                                    val res = UpdateManager.checkForUpdates(forceOnline = true)
                                    checkResult = res
                                    isChecking = false
                                }
                            },
                            enabled = !isChecking,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = EmeraldDark),
                            border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.testTag("check_updates_btn")
                        ) {
                            if (isChecking) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = EmeraldDark,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Checking official release...")
                            } else {
                                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Check for Updates Now")
                            }
                        }
                    }
                }
            }

            // Update Status Card
            item {
                when (val result = checkResult) {
                    is UpdateCheckResult.Checking -> {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(color = EmeraldPrimary, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = "Connecting to official release channel...",
                                    style = MaterialTheme.typography.bodyMedium.copy(color = TextDark)
                                )
                            }
                        }
                    }

                    is UpdateCheckResult.UpToDate -> {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = EmeraldPale),
                            border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldLight),
                            modifier = Modifier.fillMaxWidth().testTag("latest_version_card")
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = EmeraldMedium,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "You're Using the Latest Version",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = EmeraldDark
                                            )
                                        )
                                        Text(
                                            text = "Version ${result.currentVersion} is up to date.",
                                            style = MaterialTheme.typography.bodySmall.copy(color = TextMedium)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    is UpdateCheckResult.UpdateAvailable -> {
                        UpdateAvailableCard(
                            update = result.update,
                            currentVersion = result.currentVersion,
                            isPlayStore = isPlayStore,
                            onUpdateNow = {
                                if (isPlayStore) {
                                    UpdateManager.openGooglePlay(context)
                                } else {
                                    UpdateManager.openDownloadPage(context, result.update.apkDownloadUrl)
                                }
                            },
                            onRemindLater = onBack
                        )
                    }

                    is UpdateCheckResult.Error -> {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Update Check Notice",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        color = Color(0xFF991B1B),
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = result.message,
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF7F1D1D))
                                )
                            }
                        }
                    }

                    is UpdateCheckResult.Idle -> {}
                }
            }

            // Direct Sharing via WhatsApp & Messaging
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CreamBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Share, contentDescription = null, tint = EmeraldDark)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Share Zikr Life with Others",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldDark
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Send the official APK or direct download link to friends and family via WhatsApp, Telegram, or other apps.",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextDark, lineHeight = 19.sp)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Button(
                                onClick = { UpdateManager.shareAppViaWhatsApp(context) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                                shape = RoundedCornerShape(50),
                                modifier = Modifier.weight(1f).testTag("share_whatsapp_btn")
                            ) {
                                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Share via WhatsApp", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                            OutlinedButton(
                                onClick = { UpdateManager.openDownloadPage(context, UpdateManager.OFFICIAL_RELEASE_PAGE) },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = EmeraldDark),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CreamBorder),
                                shape = RoundedCornerShape(50)
                            ) {
                                Text("Official Releases")
                            }
                        }
                    }
                }
            }

            // Data Preservation Guarantee
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CreamSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CreamBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = EmeraldDark)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Safe Update Guarantee: Zero Data Loss",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldDark
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "• Never uninstall the existing app to update! Simply install the new APK directly over your current version.\n• Android will automatically preserve all your Tasbih counts, bookmarks, notes, font settings, and calendar entries.\n• Both versions share the exact same package ID (${UpdateManager.getApplicationId()}) and official signing key.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextDark,
                                lineHeight = 20.sp
                            )
                        )
                    }
                }
            }

            // Android Permission Explanation for APK Sideloading
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CreamBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showPermissionGuide = !showPermissionGuide },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.HelpOutline, contentDescription = null, tint = GoldAccent)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Android Installation Guide",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextDark
                                    )
                                )
                            }
                            Icon(
                                imageVector = if (showPermissionGuide) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = TextMuted
                            )
                        }

                        AnimatedVisibility(visible = showPermissionGuide) {
                            Column(modifier = Modifier.padding(top = 12.dp)) {
                                Text(
                                    text = "When installing an update received through WhatsApp or downloaded from the browser:\n\n1. Android may show a prompt: “For your security, your phone is not allowed to install unknown apps from this source”.\n2. Tap Settings on that prompt.\n3. Turn on “Allow from this source” for WhatsApp or your browser.\n4. Tap Back and tap Install.\n5. Android will upgrade Zikr Life seamlessly without resetting your data.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextDark,
                                        lineHeight = 20.sp
                                    )
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                TextButton(
                                    onClick = { UpdateManager.openUnknownAppSourcesSettings(context) }
                                ) {
                                    Text(
                                        text = "Open Unknown App Sources Settings",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            color = EmeraldPrimary,
                                            fontWeight = FontWeight.Bold
                                        )
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

@Composable
fun UpdateAvailableCard(
    update: ReleaseUpdateInfo,
    currentVersion: String,
    isPlayStore: Boolean,
    onUpdateNow: () -> Unit,
    onRemindLater: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, EmeraldPrimary),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth().testTag("update_available_card")
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // New version badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    color = EmeraldContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "NEW UPDATE AVAILABLE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = EmeraldDark,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Text(
                    text = update.releaseDate,
                    style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Zikr Life v${update.versionName}",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = EmeraldDark
                )
            )

            Text(
                text = update.releaseTitle,
                style = MaterialTheme.typography.titleSmall.copy(
                    color = GoldAccent,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Text(
                text = "Current: v$currentVersion → New: v${update.versionName}",
                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
            )

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = CreamBorder, thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(12.dp))

            // What's New Section
            Text(
                text = "What's New in this Release:",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                update.changelog.forEach { item ->
                    Row(
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = EmeraldMedium,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = item,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextDark,
                                lineHeight = 19.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Action Buttons: Update Now & Remind Me Later
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onUpdateNow,
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.weight(1f).testTag("update_now_btn")
                ) {
                    Icon(
                        imageVector = if (isPlayStore) Icons.Default.Shop else Icons.Default.Download,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isPlayStore) "Update on Google Play" else "Update Now",
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = onRemindLater,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextDark),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CreamBorder),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.testTag("remind_later_btn")
                ) {
                    Text("Remind Later")
                }
            }
        }
    }
}

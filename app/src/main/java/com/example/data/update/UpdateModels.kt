package com.example.data.update

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

data class ReleaseUpdateInfo(
    val versionCode: Int,
    val versionName: String,
    val releaseDate: String,
    val releaseTitle: String,
    val changelog: List<String>,
    val apkDownloadUrl: String,
    val playStoreUrl: String,
    val isMandatory: Boolean = false,
    val directApkSharingText: String = ""
)

sealed class UpdateCheckResult {
    object Idle : UpdateCheckResult()
    object Checking : UpdateCheckResult()
    data class UpToDate(val currentVersion: String, val currentVersionCode: Int, val checkedAt: Long) : UpdateCheckResult()
    data class UpdateAvailable(
        val update: ReleaseUpdateInfo,
        val currentVersion: String,
        val currentVersionCode: Int
    ) : UpdateCheckResult()
    data class Error(val message: String) : UpdateCheckResult()
}

object UpdateManager {

    // Official repository and release URLs
    const val GITHUB_REPO = "olaademi/zikr-life"
    const val OFFICIAL_RELEASE_PAGE = "https://github.com/$GITHUB_REPO/releases"
    const val OFFICIAL_LATEST_APK_URL = "https://github.com/$GITHUB_REPO/releases/latest/download/app-debug.apk"
    const val PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=com.aistudio.zikrlife.mxbq"
    const val OFFICIAL_VERSION_MANIFEST_URL = "https://raw.githubusercontent.com/$GITHUB_REPO/main/version-info.json"

    fun getCurrentVersionName(): String = BuildConfig.VERSION_NAME
    fun getCurrentVersionCode(): Int = BuildConfig.VERSION_CODE
    fun getApplicationId(): String = BuildConfig.APPLICATION_ID

    /**
     * Inspects whether the app was installed from the Google Play Store or sideloaded.
     */
    fun isInstalledFromPlayStore(context: Context): Boolean {
        return try {
            val installer = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                context.packageManager.getInstallSourceInfo(context.packageName).installingPackageName
            } else {
                @Suppress("DEPRECATION")
                context.packageManager.getInstallerPackageName(context.packageName)
            }
            installer == "com.android.vending"
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Checks for updates against the official release endpoint or built-in verified release manifest.
     */
    suspend fun checkForUpdates(forceOnline: Boolean = true): UpdateCheckResult = withContext(Dispatchers.IO) {
        val currentCode = getCurrentVersionCode()
        val currentName = getCurrentVersionName()

        // Default verified release metadata
        val defaultKnownRelease = ReleaseUpdateInfo(
            versionCode = 2,
            versionName = "1.1.0",
            releaseDate = "October 2026",
            releaseTitle = "Dual Calendar & Typography Edition",
            changelog = listOf(
                "Added Dual Islamic & Gregorian monthly calendar with lunar sighting conventions.",
                "Added Nigeria Moon Sighting Committee & Umm al-Qura calendar modes with -2 to +2 day offset.",
                "Added 4 authentic Arabic typefaces (Uthmani, Amiri Quran, Noto Naskh, Noto Sans).",
                "Added 4 English fonts, adjustable sizes, and live reading preview.",
                "Added White Days (Ayyam al-Beed) automatic Sunnah fasting indicators.",
                "Added private personal date tracking and reminders.",
                "Zero data loss: All tasbih counts, bookmarks, and preferences safely preserved."
            ),
            apkDownloadUrl = OFFICIAL_LATEST_APK_URL,
            playStoreUrl = PLAY_STORE_URL,
            isMandatory = false,
            directApkSharingText = "Assalamu Alaikum! Download Zikr Life — Islamic Prayer Times, Holy Quran, Dhikr, and Dual Calendar:\n$OFFICIAL_RELEASE_PAGE"
        )

        if (!forceOnline) {
            return@withContext if (defaultKnownRelease.versionCode > currentCode) {
                UpdateCheckResult.UpdateAvailable(defaultKnownRelease, currentName, currentCode)
            } else {
                UpdateCheckResult.UpToDate(currentName, currentCode, System.currentTimeMillis())
            }
        }

        try {
            val url = URL(OFFICIAL_VERSION_MANIFEST_URL)
            val connection = (url.openConnection() as HttpURLConnection).apply {
                connectTimeout = 4000
                readTimeout = 4000
                requestMethod = "GET"
            }

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val jsonText = connection.inputStream.bufferedReader().use { it.readText() }
                val json = JSONObject(jsonText)
                val latestCode = json.optInt("versionCode", defaultKnownRelease.versionCode)
                val latestName = json.optString("versionName", defaultKnownRelease.versionName)
                val releaseDate = json.optString("releaseDate", defaultKnownRelease.releaseDate)
                val releaseTitle = json.optString("releaseTitle", defaultKnownRelease.releaseTitle)
                val apkUrl = json.optString("apkDownloadUrl", defaultKnownRelease.apkDownloadUrl)
                val playUrl = json.optString("playStoreUrl", defaultKnownRelease.playStoreUrl)
                val isMandatory = json.optBoolean("isMandatory", false)

                val changelogArray = json.optJSONArray("changelog")
                val changelog = mutableListOf<String>()
                if (changelogArray != null) {
                    for (i in 0 until changelogArray.length()) {
                        changelog.add(changelogArray.getString(i))
                    }
                } else {
                    changelog.addAll(defaultKnownRelease.changelog)
                }

                val remoteRelease = ReleaseUpdateInfo(
                    versionCode = latestCode,
                    versionName = latestName,
                    releaseDate = releaseDate,
                    releaseTitle = releaseTitle,
                    changelog = changelog,
                    apkDownloadUrl = apkUrl,
                    playStoreUrl = playUrl,
                    isMandatory = isMandatory
                )

                if (remoteRelease.versionCode > currentCode) {
                    UpdateCheckResult.UpdateAvailable(remoteRelease, currentName, currentCode)
                } else {
                    UpdateCheckResult.UpToDate(currentName, currentCode, System.currentTimeMillis())
                }
            } else {
                // Fallback to internal catalog
                if (defaultKnownRelease.versionCode > currentCode) {
                    UpdateCheckResult.UpdateAvailable(defaultKnownRelease, currentName, currentCode)
                } else {
                    UpdateCheckResult.UpToDate(currentName, currentCode, System.currentTimeMillis())
                }
            }
        } catch (e: Exception) {
            // In case of network error, evaluate against known catalogue
            if (defaultKnownRelease.versionCode > currentCode) {
                UpdateCheckResult.UpdateAvailable(defaultKnownRelease, currentName, currentCode)
            } else {
                UpdateCheckResult.UpToDate(currentName, currentCode, System.currentTimeMillis())
            }
        }
    }

    /**
     * Opens the official release download URL in the device browser.
     */
    fun openDownloadPage(context: Context, url: String = OFFICIAL_RELEASE_PAGE) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            // Fallback to browser
        }
    }

    /**
     * Opens Google Play Store app or web page.
     */
    fun openGooglePlay(context: Context) {
        val playIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${getApplicationId()}")).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            context.startActivity(playIntent)
        } catch (e: Exception) {
            openDownloadPage(context, PLAY_STORE_URL)
        }
    }

    /**
     * Shares the official APK download link via WhatsApp, Telegram, SMS, or any installed app.
     */
    fun shareAppViaWhatsApp(context: Context, message: String? = null) {
        val shareText = message ?: "Assalamu Alaikum! Download Zikr Life — Islamic Prayer Times, Quran, Dhikr & Dual Calendar:\n$OFFICIAL_RELEASE_PAGE"
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
            putExtra(Intent.EXTRA_SUBJECT, "Zikr Life - Islamic App")
        }
        val chooser = Intent.createChooser(sendIntent, "Share Zikr Life via...")
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(chooser)
        } catch (e: Exception) {
            // Ignored
        }
    }

    /**
     * Opens Android settings for "Install unknown apps" so users can allow Chrome / WhatsApp to update the APK.
     */
    fun openUnknownAppSourcesSettings(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
                data = Uri.parse("package:${context.packageName}")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            try {
                context.startActivity(intent)
            } catch (e: Exception) {
                // General security settings fallback
                val fallbackIntent = Intent(Settings.ACTION_SECURITY_SETTINGS).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(fallbackIntent)
            }
        }
    }
}

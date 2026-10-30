package com.example.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.R

// Arabic Font Families
val ScheherazadeNewFontFamily = FontFamily(
    Font(R.font.scheherazade_new, FontWeight.Normal)
)

val AmiriQuranFontFamily = FontFamily(
    Font(R.font.amiri_quran, FontWeight.Normal)
)

val NotoNaskhArabicFontFamily = FontFamily(
    Font(R.font.noto_naskh_arabic, FontWeight.Normal)
)

val NotoSansArabicFontFamily = FontFamily(
    Font(R.font.noto_sans_arabic, FontWeight.Normal)
)

// English Translation Font Families
val NotoSansFontFamily = FontFamily(
    Font(R.font.noto_sans, FontWeight.Normal)
)

val RobotoFontFamily = FontFamily(
    Font(R.font.roboto, FontWeight.Normal)
)

val LoraFontFamily = FontFamily(
    Font(R.font.lora, FontWeight.Normal)
)

val MerriweatherFontFamily = FontFamily(
    Font(R.font.merriweather, FontWeight.Normal)
)

enum class ArabicFontOption(
    val id: String,
    val displayName: String,
    val subtitle: String,
    val fontFamily: FontFamily
) {
    UTHMANI("uthmani", "Uthmani Style", "Verified Qur'anic Mushaf Script (Scheherazade)", ScheherazadeNewFontFamily),
    AMIRI_QURAN("amiri_quran", "Amiri Quran", "Classic Cairo Classical Quranic Typeface", AmiriQuranFontFamily),
    NOTO_NASKH("noto_naskh", "Noto Naskh Arabic", "Clear, high-legibility traditional Naskh", NotoNaskhArabicFontFamily),
    NOTO_SANS("noto_sans", "Noto Sans Arabic", "Modern, crisp & clean Arabic sans-serif", NotoSansArabicFontFamily);

    companion object {
        fun fromId(id: String): ArabicFontOption =
            entries.find { it.id.equals(id, ignoreCase = true) } ?: UTHMANI
    }
}

enum class EnglishFontOption(
    val id: String,
    val displayName: String,
    val subtitle: String,
    val fontFamily: FontFamily
) {
    NOTO_SANS("noto_sans", "Noto Sans", "Modern, neutral sans-serif designed for digital screens", NotoSansFontFamily),
    ROBOTO("roboto", "Roboto", "Clear Android standard typeface with balanced proportions", RobotoFontFamily),
    LORA("lora", "Lora", "Contemporary, elegant literary serif with warm curves", LoraFontFamily),
    MERRIWEATHER("merriweather", "Merriweather", "Editorial serif engineered for effortless long-form reading", MerriweatherFontFamily);

    companion object {
        fun fromId(id: String): EnglishFontOption =
            entries.find { it.id.equals(id, ignoreCase = true) } ?: NOTO_SANS
    }
}

enum class LineSpacingOption(
    val id: String,
    val displayName: String,
    val multiplier: Float
) {
    COMPACT("compact", "Compact (1.5x)", 1.5f),
    NORMAL("normal", "Standard (1.8x)", 1.8f),
    RELAXED("relaxed", "Relaxed (2.2x)", 2.2f);

    companion object {
        fun fromId(id: String): LineSpacingOption =
            entries.find { it.id.equals(id, ignoreCase = true) } ?: NORMAL
    }
}

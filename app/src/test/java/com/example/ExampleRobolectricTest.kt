package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.calendar.HijriConvention
import com.example.data.repository.ZikrPreferencesRepository
import com.example.ui.theme.ArabicFontOption
import com.example.ui.theme.EnglishFontOption
import com.example.ui.theme.LineSpacingOption
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("ZIKR LIFE", appName)
  }

  @Test
  fun `test typography and calendar preferences persistence`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repository = ZikrPreferencesRepository(context)

    // Set custom typography choices
    repository.setArabicFont(ArabicFontOption.AMIRI_QURAN)
    repository.setEnglishFont(EnglishFontOption.LORA)
    repository.setLineSpacing(LineSpacingOption.RELAXED)
    repository.setQuranFontSize(28)
    repository.setTranslationFontSize(18)

    assertEquals(ArabicFontOption.AMIRI_QURAN, repository.arabicFont.value)
    assertEquals(EnglishFontOption.LORA, repository.englishFont.value)
    assertEquals(LineSpacingOption.RELAXED, repository.lineSpacing.value)
    assertEquals(28, repository.arabicFontSize.value)
    assertEquals(18, repository.translationFontSize.value)

    // Set calendar convention and offset
    repository.setHijriOffsetDays(1)
    repository.setHijriConvention(HijriConvention.NIGERIA_MOONSIGHTING)

    assertEquals(1, repository.hijriOffsetDays.value)
    assertEquals(HijriConvention.NIGERIA_MOONSIGHTING, repository.hijriConvention.value)
  }
}

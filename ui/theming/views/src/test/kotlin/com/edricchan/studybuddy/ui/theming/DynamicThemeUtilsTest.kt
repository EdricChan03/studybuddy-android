package com.edricchan.studybuddy.ui.theming

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.google.android.material.color.DynamicColors
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class DynamicThemeUtilsTest {
    @Test
    fun isDynamicColorAvailable_callsCorrect() {
        assertEquals(isDynamicColorAvailable, DynamicColors.isDynamicColorAvailable())
    }

    @Test
    fun prefDynamicTheme_returnsValue() {
        val context = mockk<Context>(relaxed = true)
        val sharedPrefs = mockk<SharedPreferences>()
        mockkStatic(PreferenceManager::class)

        every { PreferenceManager.getDefaultSharedPreferences(any()) } returns sharedPrefs

        val value = false
        every { sharedPrefs.getBoolean(PREF_DYNAMIC_THEME, isDynamicColorAvailable) } returns value

        val dynamicTheme = context.prefDynamicTheme

        assertEquals(dynamicTheme, value)

        verify { sharedPrefs.getBoolean(PREF_DYNAMIC_THEME, isDynamicColorAvailable) }
    }

    @Test
    fun prefDynamicTheme_setsValue() {
        val context = mockk<Context>(relaxed = true)
        val sharedPrefs = mockk<SharedPreferences>()
        val sharedPrefsEditor = mockk<SharedPreferences.Editor>()
        mockkStatic(PreferenceManager::class)

        every { PreferenceManager.getDefaultSharedPreferences(any()) } returns sharedPrefs
        every { sharedPrefs.edit() } returns sharedPrefsEditor

        val newValue = true
        every {
            sharedPrefsEditor.putBoolean(
                PREF_DYNAMIC_THEME,
                newValue
            )
        } answers { sharedPrefsEditor }
        justRun { sharedPrefsEditor.apply() }

        context.prefDynamicTheme = newValue

        verify { sharedPrefsEditor.putBoolean(PREF_DYNAMIC_THEME, newValue) }
    }
}

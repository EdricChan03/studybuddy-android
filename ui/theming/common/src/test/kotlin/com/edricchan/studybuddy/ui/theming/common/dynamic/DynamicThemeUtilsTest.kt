package com.edricchan.studybuddy.ui.theming.common.dynamic

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.edricchan.studybuddy.core.settings.appearance.keyPrefDynamicTheme
import com.google.android.material.color.DynamicColors
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify

class DynamicThemeUtilsTest : DescribeSpec({
    describe("isDynamicColorAvailable") {
        it("should return the appropriate value") {
            isDynamicColorAvailable shouldBeEqual DynamicColors.isDynamicColorAvailable()
        }
    }

    describe("prefDynamicTheme") {
        lateinit var context: Context
        lateinit var sharedPrefs: SharedPreferences

        beforeTest {
            context = mockk<Context>(relaxed = true)
            sharedPrefs = mockk<SharedPreferences>()
            mockkStatic(PreferenceManager::class)
        }

        it("should return the appropriate value") {
            every { PreferenceManager.getDefaultSharedPreferences(any()) } returns sharedPrefs

            val value = false
            every {
                sharedPrefs.getBoolean(
                    keyPrefDynamicTheme,
                    isDynamicColorAvailable
                )
            } returns value

            val dynamicTheme = context.prefDynamicTheme

            dynamicTheme shouldBeEqual value

            verify { sharedPrefs.getBoolean(keyPrefDynamicTheme, isDynamicColorAvailable) }
        }

        it("should set the appropriate value") {
            val sharedPrefsEditor = mockk<SharedPreferences.Editor>()

            every { PreferenceManager.getDefaultSharedPreferences(any()) } returns sharedPrefs
            every { sharedPrefs.edit() } returns sharedPrefsEditor

            val newValue = true
            every {
                sharedPrefsEditor.putBoolean(
                    keyPrefDynamicTheme,
                    newValue
                )
            } answers { sharedPrefsEditor }
            justRun { sharedPrefsEditor.apply() }

            context.prefDynamicTheme = newValue

            verify { sharedPrefsEditor.putBoolean(keyPrefDynamicTheme, newValue) }
        }
    }
})

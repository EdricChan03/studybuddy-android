package com.edricchan.studybuddy.ui.theming

import androidx.appcompat.app.AppCompatDelegate
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(Parameterized::class)
class NightModeEnumTest(
    @AppCompatDelegate.NightMode private val input: Int,
    private val expected: NightMode?
) {
    @Test
    fun nightModeFromValue_returnsCorrect() {
        assertEquals(NightMode.fromMode(input), expected)
    }

    companion object {
        @Parameters(name = "{index}: fromMode({0})={1}")
        @JvmStatic
        fun data() = listOf(
            arrayOf(AppCompatDelegate.MODE_NIGHT_YES, NightMode.Yes),
            arrayOf(AppCompatDelegate.MODE_NIGHT_NO, NightMode.No),
            arrayOf(AppCompatDelegate.MODE_NIGHT_AUTO_TIME, NightMode.AutoTime),
            arrayOf(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY, NightMode.AutoBattery),
            arrayOf(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, NightMode.FollowSystem),
            arrayOf(AppCompatDelegate.MODE_NIGHT_UNSPECIFIED, NightMode.Unspecified),
            // Invalid value
            arrayOf(Int.MAX_VALUE, null)
        )
    }
}

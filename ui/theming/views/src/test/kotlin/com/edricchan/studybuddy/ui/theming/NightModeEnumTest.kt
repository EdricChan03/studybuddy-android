package com.edricchan.studybuddy.ui.theming

import androidx.appcompat.app.AppCompatDelegate
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldNotBeNull

@Suppress("DEPRECATION")
class NightModeEnumTest : DescribeSpec({
    describe("NightMode.fromMode") {
        withData(
            ts = listOf(
                AppCompatDelegate::MODE_NIGHT_YES to NightMode.Yes,
                AppCompatDelegate::MODE_NIGHT_NO to NightMode.No,
                AppCompatDelegate::MODE_NIGHT_AUTO_TIME to NightMode.AutoTime,
                AppCompatDelegate::MODE_NIGHT_AUTO_BATTERY to NightMode.AutoBattery,
                AppCompatDelegate::MODE_NIGHT_FOLLOW_SYSTEM to NightMode.FollowSystem,
                AppCompatDelegate::MODE_NIGHT_UNSPECIFIED to NightMode.Unspecified
            ),
            nameFn = { (input, expected) ->
                "fromMode(${input.name}) should equal $expected"
            }
        ) { (input, expected) ->
            NightMode.fromMode(input.get()) shouldNotBeNull {
                this shouldBeEqual expected
            }
        }
    }
})

package com.edricchan.studybuddy.ui.theming.compose

import com.edricchan.studybuddy.ui.theming.compose.theme.compat.StudyBuddyCompatDarkColors
import com.edricchan.studybuddy.ui.theming.compose.theme.compat.StudyBuddyCompatLightColors
import com.edricchan.studybuddy.ui.theming.compose.theme.m3.StudyBuddyM3DarkColors
import com.edricchan.studybuddy.ui.theming.compose.theme.m3.StudyBuddyM3LightColors
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual

private val lightColorSchemesMap = mapOf(
    lightColorScheme(useM3Colors = true) to StudyBuddyM3LightColors,
    lightColorScheme(useM3Colors = false) to StudyBuddyCompatLightColors,
)

private val darkColorSchemesMap = mapOf(
    darkColorScheme(useM3Colors = true) to StudyBuddyM3DarkColors,
    darkColorScheme(useM3Colors = false) to StudyBuddyCompatDarkColors,
)

private val colorSchemesMap = mapOf(
    "light" to lightColorSchemesMap,
    "dark" to darkColorSchemesMap
)

class StudyBuddyThemeUnitTest : FunSpec({
    colorSchemesMap.forEach { (name, schemeMap) ->
        test("$name colour scheme should use the appropriate M3 colours") {
            schemeMap.forEach { (expected, actual) ->
                expected shouldBeEqual actual
            }
        }
    }
})

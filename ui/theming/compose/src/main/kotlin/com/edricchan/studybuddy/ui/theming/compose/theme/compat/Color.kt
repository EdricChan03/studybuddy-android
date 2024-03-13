package com.edricchan.studybuddy.ui.theming.compose.theme.compat

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

@Deprecated(
    "Use the M3 variant (StudyBuddyM3LightColors) where applicable",
    ReplaceWith(
        "StudyBuddyM3LightColors",
        "com.edricchan.studybuddy.ui.theming.compose.theme.m3.StudyBuddyM3LightColors"
    )
)
val StudyBuddyCompatLightColors = lightColorScheme(
    // TODO: Add colours
    primary = Color(0xff3948ab),
    primaryContainer = Color(0xff00227b),
    secondary = Color(0xffff4081)
)

@Deprecated(
    "Use the M3 variant (StudyBuddyM3DarkColors) where applicable",
    ReplaceWith(
        "StudyBuddyM3DarkColors",
        "com.edricchan.studybuddy.ui.theming.compose.theme.m3.StudyBuddyM3DarkColors"
    )
)
val StudyBuddyCompatDarkColors = darkColorScheme(
    primary = Color(0xff8e24aa),
    primaryContainer = Color(0xff5c007a),
    secondary = Color(0xfffdd835)
)

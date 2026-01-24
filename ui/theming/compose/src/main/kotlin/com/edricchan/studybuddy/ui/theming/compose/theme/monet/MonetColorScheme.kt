package com.edricchan.studybuddy.ui.theming.compose.theme.monet

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import com.edricchan.studybuddy.ui.theming.compose.theme.AppColorScheme

@RequiresApi(Build.VERSION_CODES.S)
class MonetColorScheme(context: Context) : AppColorScheme() {
    override val lightScheme: ColorScheme = dynamicLightColorScheme(context)
    override val darkScheme: ColorScheme = dynamicDarkColorScheme(context)
}

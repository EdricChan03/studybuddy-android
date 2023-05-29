package com.edricchan.studybuddy.ui.theming.compose.theme

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

/** Whether dynamic colour theming is supported. */
@get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
val supportsDynamicColor get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

package com.edricchan.studybuddy.annotations

import androidx.annotation.StringDef
import com.edricchan.studybuddy.interfaces.Visibility

@StringDef(
		Visibility.PRIVATE,
		Visibility.PUBLIC,
		Visibility.UNLISTED
)
annotation class VisibilityAnnotation
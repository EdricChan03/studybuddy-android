package com.edricchan.studybuddy.core.di.qualifiers.prefs

import javax.inject.Qualifier

/** [Qualifier] for the [android.content.SharedPreferences] used by AndroidX Preference. */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultSharedPreferences

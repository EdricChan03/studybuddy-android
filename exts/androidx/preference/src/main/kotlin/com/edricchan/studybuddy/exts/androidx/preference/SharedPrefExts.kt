package com.edricchan.studybuddy.exts.androidx.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

/** Retrieves the default shared preference file. */
inline val Context.defaultSharedPreferences: SharedPreferences
    get() = PreferenceManager.getDefaultSharedPreferences(this)

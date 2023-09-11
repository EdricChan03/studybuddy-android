package com.edricchan.studybuddy.exts.android

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.BundleCompat
import java.io.Serializable

@Suppress("DEPRECATION")
inline fun <reified T : Serializable> Bundle.getSerializableCompat(key: String?) =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) getSerializable(key, T::class.java)
    else getSerializable(key) as? T

inline fun <reified T : Parcelable> Bundle.getParcelableCompat(key: String?) =
    BundleCompat.getParcelable(this, key, T::class.java)

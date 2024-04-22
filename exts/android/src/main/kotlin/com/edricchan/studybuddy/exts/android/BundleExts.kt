package com.edricchan.studybuddy.exts.android

import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.BundleCompat
import java.io.Serializable

inline fun <reified T : Serializable> Bundle.getSerializableCompat(key: String?) =
    BundleCompat.getSerializable(this, key, T::class.java)

inline fun <reified T : Parcelable> Bundle.getParcelableCompat(key: String?) =
    BundleCompat.getParcelable(this, key, T::class.java)

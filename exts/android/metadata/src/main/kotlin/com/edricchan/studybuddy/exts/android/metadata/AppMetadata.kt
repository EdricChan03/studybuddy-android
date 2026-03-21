package com.edricchan.studybuddy.exts.android.metadata

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.content.pm.PackageInfoCompat

/** Retrieves the application's icon as a [Drawable]. */
inline val Context.appIcon: Drawable
    get() = packageManager.getApplicationIcon(applicationInfo)

/** Retrieve the application's label. */
inline val Context.appLabel: CharSequence
    get() = packageManager.getApplicationLabel(applicationInfo)

/** Retrieves the application's package information. */
// Needed until https://issuetracker.google.com/issues/246845196 is resolved
inline val Context.packageInfo: PackageInfo
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0)) else
        packageManager.getPackageInfo(packageName, 0)

// BuildConfig.VERSION_NAME can't be used in library modules
/** Retrieves the application's version name. */
inline val Context.versionName: String?
    get() = packageInfo.versionName

/** Retrieves the application's version code. */
inline val Context.versionCode: Long
    get() = PackageInfoCompat.getLongVersionCode(packageInfo)

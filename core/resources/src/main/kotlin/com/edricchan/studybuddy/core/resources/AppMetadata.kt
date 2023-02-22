package com.edricchan.studybuddy.core.resources

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.pm.PackageInfoCompat

/** Retrieves the application's icon as a [android.graphics.drawable.Drawable]. */
val Context.appIcon
    get() = packageManager.getApplicationIcon(applicationInfo)

/** Retrieve the application's label. */
val Context.appLabel
    get() = packageManager.getApplicationLabel(applicationInfo)

/** Retrieves the application's package information. */
// Needed until https://issuetracker.google.com/issues/246845196 is resolved
@Suppress("DEPRECATION")
val Context.packageInfo
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0)) else
        packageManager.getPackageInfo(packageName, 0)

// BuildConfig.VERSION_NAME can't be used in library modules
/** Retrieves the application's version name. */
val Context.versionName
    get() = packageInfo.versionName

/** Retrieves the application's version code. */
val Context.versionCode
    get() = PackageInfoCompat.getLongVersionCode(packageInfo)

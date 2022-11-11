package com.edricchan.studybuddy.features.help

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.view.LayoutInflater
import com.edricchan.studybuddy.exts.material.dialog.showMaterialAlertDialog
import com.edricchan.studybuddy.features.help.databinding.VersionDialogBinding

internal val Context.appIcon
    get() = packageManager.getApplicationIcon(applicationInfo)

internal val Context.appLabel
    get() = packageManager.getApplicationLabel(applicationInfo)

// BuildConfig.VERSION_NAME can't be used in library modules
internal val Context.versionName
    get() = (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0)) else
        packageManager.getPackageInfo(packageName, 0)).versionName

/**
 * Shows a version dialog.
 */
fun Context.showVersionDialog() = showMaterialAlertDialog {
    setView(
        VersionDialogBinding.inflate(LayoutInflater.from(context)).apply {
            appIconImageView.setImageDrawable(appIcon)
            appNameTextView.text = appLabel
            appVersionTextView.text = versionName
        }.root
    )
}

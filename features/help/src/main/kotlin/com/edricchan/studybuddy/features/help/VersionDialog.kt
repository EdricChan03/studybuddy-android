package com.edricchan.studybuddy.features.help

import android.content.Context
import android.view.LayoutInflater
import com.edricchan.studybuddy.exts.android.metadata.appIcon
import com.edricchan.studybuddy.exts.android.metadata.appLabel
import com.edricchan.studybuddy.exts.android.metadata.versionCode
import com.edricchan.studybuddy.exts.android.metadata.versionName
import com.edricchan.studybuddy.exts.material.dialog.showMaterialAlertDialog
import com.edricchan.studybuddy.features.help.databinding.VersionDialogBinding

/**
 * Shows a version dialog.
 */
fun Context.showVersionDialog() = showMaterialAlertDialog {
    setView(
        VersionDialogBinding.inflate(LayoutInflater.from(context)).apply {
            appIconImageView.setImageDrawable(appIcon)
            appNameTextView.text = appLabel
            appVersionTextView.text =
                getString(R.string.version_dialog_version_formatted, versionName, versionCode)
        }.root
    )
}

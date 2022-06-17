package com.edricchan.studybuddy.ui.preference

import android.content.Context
import android.content.DialogInterface
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.preference.PreferenceDialogFragmentCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder

internal fun PreferenceDialogFragmentCompat.createDialogBuilder(
    listener: DialogInterface.OnClickListener,
    onCreateDialogView: (Context) -> View?,
    onBindDialogView: (View) -> Unit,
    onPrepareDialogBuilder: (AlertDialog.Builder) -> Unit
) =
    MaterialAlertDialogBuilder(requireContext()).apply {
        setTitle(preference.dialogTitle)
        setIcon(preference.dialogIcon)
        setPositiveButton(preference.positiveButtonText, listener)
        setNegativeButton(preference.negativeButtonText, listener)
        val contentView = onCreateDialogView(requireActivity())
        if (contentView != null) {
            onBindDialogView(contentView)
            setView(contentView)
        } else {
            setMessage(preference.dialogMessage)
        }
        onPrepareDialogBuilder(this)
    }.create()

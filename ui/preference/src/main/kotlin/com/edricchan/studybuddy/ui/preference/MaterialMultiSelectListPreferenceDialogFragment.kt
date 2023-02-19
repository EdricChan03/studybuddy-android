package com.edricchan.studybuddy.ui.preference

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.preference.MultiSelectListPreference
import androidx.preference.MultiSelectListPreferenceDialogFragmentCompat
import androidx.preference.PreferenceFragmentCompat

// Code from https://stackoverflow.com/a/61424867
class MaterialMultiSelectListPreferenceDialogFragment :
    MultiSelectListPreferenceDialogFragmentCompat() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mWhichButtonClicked = DialogInterface.BUTTON_NEGATIVE
        return createDialogBuilder(
            this,
            this::onCreateDialogView, this::onBindDialogView, this::onPrepareDialogBuilder
        )
    }

    /* Override the methods that access mWhichButtonClicked (because we cannot set it properly here) */

    /** Which button was clicked.  */
    private var mWhichButtonClicked = 0

    override fun onClick(dialog: DialogInterface, which: Int) {
        mWhichButtonClicked = which
    }

    override fun onDismiss(dialog: DialogInterface) {
        onDialogClosedWasCalledFromOnDismiss = true
        super.onDismiss(dialog)
    }

    private var onDialogClosedWasCalledFromOnDismiss = false

    override fun onDialogClosed(positiveResult: Boolean) {
        if (onDialogClosedWasCalledFromOnDismiss) {
            onDialogClosedWasCalledFromOnDismiss = false
            // this means the positiveResult needs to be calculated from our mWhichButtonClicked
            super.onDialogClosed(mWhichButtonClicked == DialogInterface.BUTTON_POSITIVE)
        } else {
            super.onDialogClosed(positiveResult)
        }
    }
}

@Suppress("DEPRECATION") // See https://issuetracker.google.com/issues/212905758
fun PreferenceFragmentCompat.showMultiSelectListPreferenceDialog(
    preference: MultiSelectListPreference
) {
    val dialogFragment = MaterialMultiSelectListPreferenceDialogFragment().apply {
        arguments = bundleOf("key" to preference.key)
    }
    dialogFragment.apply {
        setTargetFragment(this@showMultiSelectListPreferenceDialog, 0)
        show(
            this@showMultiSelectListPreferenceDialog.parentFragmentManager,
            "androidx.preference.PreferenceFragment.DIALOG"
        )
    }
}

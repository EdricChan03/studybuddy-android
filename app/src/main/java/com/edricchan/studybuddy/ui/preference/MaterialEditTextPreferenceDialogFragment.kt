package com.edricchan.studybuddy.ui.preference

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.preference.EditTextPreference
import androidx.preference.EditTextPreferenceDialogFragmentCompat
import androidx.preference.PreferenceFragmentCompat

// Code from https://stackoverflow.com/a/61424867
class MaterialEditTextPreferenceDialogFragment : EditTextPreferenceDialogFragmentCompat() {
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

fun PreferenceFragmentCompat.showEditTextPreferenceDialog(preference: EditTextPreference) {
    val dialogFragment = MaterialEditTextPreferenceDialogFragment().apply {
        arguments = bundleOf("key" to preference.key)
    }
    dialogFragment.apply {
        setTargetFragment(this@showEditTextPreferenceDialog, 0)
        show(
            this@showEditTextPreferenceDialog.parentFragmentManager,
            "androidx.preference.PreferenceFragment.DIALOG"
        )
    }
}

package com.edricchan.studybuddy.ui.preference.compose.dialog

import androidx.compose.runtime.Immutable

/** Scope which all dialog-based preferences inherit from. */
@Immutable
interface DialogPreferenceScope {
    /** Requests for the preference's dialog to be dismissed. */
    fun requestDismiss()
}

internal class DialogPreferenceScopeImpl(private val onDismissRequest: () -> Unit) :
    DialogPreferenceScope {
    override fun requestDismiss() {
        onDismissRequest()
    }
}

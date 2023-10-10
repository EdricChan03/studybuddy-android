package com.edricchan.studybuddy.exts.material.textfield

import android.text.Editable
import com.google.android.material.textfield.TextInputLayout

/** The [TextInputLayout]'s [android.widget.EditText] value as an [Editable]. */
var TextInputLayout.editTextValue: Editable?
    get() = this.editText?.text
    set(value) {
        this.editText?.text = value
    }

/** The [TextInputLayout]'s [android.widget.EditText] value as a [String]. */
var TextInputLayout.editTextStrValue: String
    // TODO: Remove orEmpty() when usages have migrated to nullable String
    get() = this.editText?.text?.toString().orEmpty()
    set(value) {
        this.editText?.setText(value)
    }

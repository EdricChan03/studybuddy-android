package com.edricchan.studybuddy.extensions

import android.text.Editable
import android.text.SpannableStringBuilder
import com.google.android.material.textfield.TextInputLayout

/**
 * Getter/setter for the [TextInputLayout]'s [EditText] value
 * Returns/requires an [Editable]
 */
var TextInputLayout.editTextValue: Editable?
    get() = this.editText?.text
    set(value) {
        this.editText?.text = value
    }

/**
 * Getter/setter for the [TextInputLayout]'s [EdiText] value
 * Returns/requires a [String]
 */
var TextInputLayout.editTextStrValue: String
    get() = this.editText?.text.toString()
    set(value) {
        this.editText?.text = SpannableStringBuilder(value)
    }

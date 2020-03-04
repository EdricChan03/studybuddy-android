package com.edricchan.studybuddy.extensions

import android.text.Editable
import android.text.SpannableStringBuilder
import com.google.android.material.textfield.TextInputEditText

/**
 * Getter/setter for a [TextInputEditText]'s value
 * Returns/requires a [String]
 */
var TextInputEditText.strValue: String?
    get() = this.text.toString()
    set(value) {
        this.text = SpannableStringBuilder(value)
    }

/**
 * Getter/setter for a [TextInputEditText]'s value
 * Returns/requires an [Editable]
 */
var TextInputEditText.value: Editable?
    get() = this.text
    set(value) {
        this.text = value
    }
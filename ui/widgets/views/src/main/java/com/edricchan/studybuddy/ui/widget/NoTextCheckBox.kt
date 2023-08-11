package com.edricchan.studybuddy.ui.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.R as AppCompatR

/**
 * An implementation of [AppCompatCheckBox], but without text
 *
 * See: [StackOverflow answer](https://stackoverflow.com/a/20374661/6782707)
 */
class NoTextCheckBox @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = AppCompatR.attr.checkboxStyle
) : AppCompatCheckBox(context, attrs, defStyleAttr) {
    override fun getSuggestedMinimumWidth() = compoundPaddingLeft + compoundPaddingRight
}

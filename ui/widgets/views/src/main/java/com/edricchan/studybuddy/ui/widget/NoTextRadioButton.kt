package com.edricchan.studybuddy.ui.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.appcompat.R as AppCompatR

/**
 * An implementation of [AppCompatRadioButton], but without text
 *
 * See: [StackOverflow answer](https://stackoverflow.com/a/20374661/6782707)
 */
class NoTextRadioButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = AppCompatR.attr.radioButtonStyle
) : AppCompatRadioButton(context, attrs, defStyleAttr) {
    override fun getSuggestedMinimumWidth() = compoundPaddingLeft + compoundPaddingRight
}

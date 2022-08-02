package com.edricchan.studybuddy.ui.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton

/**
 * An implementation of [AppCompatRadioButton], but without text
 *
 * See: [StackOverflow answer](https://stackoverflow.com/a/20374661/6782707)
 */
class NoTextRadioButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.radioButtonStyle
) : AppCompatRadioButton(context, attrs, defStyleAttr) {
    override fun getSuggestedMinimumWidth() = compoundPaddingLeft + compoundPaddingRight
}

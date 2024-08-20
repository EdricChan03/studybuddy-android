package com.edricchan.studybuddy.exts.android.widget

import android.graphics.Paint
import android.widget.TextView

/**
 * Paints a strike-through on the receiver [TextView].
 * @param shouldStrike Whether strike-through should be applied.
 * @see Paint.STRIKE_THRU_TEXT_FLAG
 */
fun TextView.paintStrikeThrough(shouldStrike: Boolean = true) {
    paintFlags = if (shouldStrike) paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    else paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
}

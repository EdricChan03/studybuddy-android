package com.edricchan.studybuddy.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatCheckBox

/**
 * An implementation of [AppCompatCheckBox], but without text
 *
 * See: [StackOverflow answer](https://stackoverflow.com/a/20374661/6782707)
 */
class NoTextCheckBox : AppCompatCheckBox {
	private var checkBoxButtonDrawable: Drawable? = null

	constructor(context: Context?) : super(context)
	constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

	@SuppressLint("ObsoleteSdkInt")
	override fun getSuggestedMinimumWidth(): Int {
		return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			compoundPaddingLeft + compoundPaddingRight
		} else {
			if (checkBoxButtonDrawable != null) checkBoxButtonDrawable!!.intrinsicWidth else 0
		}
	}

	override fun setButtonDrawable(drawable: Drawable?) {
		super.setButtonDrawable(drawable)
		checkBoxButtonDrawable = drawable
	}
}
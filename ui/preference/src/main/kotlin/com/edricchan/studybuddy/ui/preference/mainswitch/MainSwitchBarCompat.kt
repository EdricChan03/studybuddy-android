/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.edricchan.studybuddy.ui.preference.mainswitch

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.CompoundButton
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import com.edricchan.studybuddy.ui.preference.R
import com.edricchan.studybuddy.ui.preference.databinding.PrefM3MainSwitchBarBinding
import kotlinx.parcelize.Parcelize
import androidx.appcompat.R as AppCompatR
import com.google.android.material.R as MaterialR

/**
 * MainSwitchBar is a View with a customized Switch.
 * This component is used as the main switch of the page
 * to enable or disable the preferences on the page.
 */
class MainSwitchBarCompat @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes),
    CompoundButton.OnCheckedChangeListener {
    private val mSwitchChangeListeners = mutableListOf<OnMainSwitchChangeListener>()

    @ColorInt
    private var mBackgroundColor = 0

    @ColorInt
    private var mBackgroundActivatedColor = 0

    private var mBackgroundOn: Drawable? = null
    private var mBackgroundOff: Drawable? = null
    private var mBackgroundDisabled: Drawable? = null

    private val binding: PrefM3MainSwitchBarBinding

    /**
     * Retrieves/sets the status of the switch.
     */
    var isChecked: Boolean
        get() = binding.switchWidget.isChecked
        set(checked) {
            binding.switchWidget.isChecked = checked
            setBackground(checked)
        }

    init {
        binding = PrefM3MainSwitchBarBinding.inflate(LayoutInflater.from(context), this, true)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            context.withStyledAttributes(attrs = intArrayOf(MaterialR.attr.colorAccent)) {
                mBackgroundActivatedColor = getColor(0, 0)
                mBackgroundColor =
                    ContextCompat.getColor(context, AppCompatR.color.material_grey_600)
            }
        }
        isFocusable = true
        isClickable = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            mBackgroundOn = AppCompatResources.getDrawable(context, R.drawable.m3_switch_bar_bg_on)
            mBackgroundOff =
                AppCompatResources.getDrawable(context, R.drawable.m3_switch_bar_bg_off)
            mBackgroundDisabled = AppCompatResources.getDrawable(
                context,
                R.drawable.m3_switch_bar_bg_disabled
            )
        }
        addOnSwitchChangeListener { _, isChecked ->
            this.isChecked = isChecked
        }
        isChecked = binding.switchWidget.isChecked

        // Enable saving of state
        isSaveEnabled = true
    }

    /** The main switch bar's title. */
    var title by binding.switchText::text

    // This is a separate init block from the first such that the title can be set correctly
    init {
        if (attrs != null) {
            context.withStyledAttributes(attrs, R.styleable.MainSwitchBar) {
                title = getText(
                    R.styleable.MainSwitchBar_title
                ) ?: getText(R.styleable.MainSwitchBar_android_title)
            }
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        propagateChecked(isChecked)
    }

    override fun performClick(): Boolean {
        return binding.switchWidget.performClick()
    }

    /**
     * Show the MainSwitchBar
     */
    fun show() {
        isVisible = true
        binding.switchWidget.setOnCheckedChangeListener(this)
    }

    /**
     * Hide the MainSwitchBar
     */
    fun hide() {
        if (isVisible) {
            isVisible = false
            binding.switchWidget.setOnCheckedChangeListener(null)
        }
    }

    /**
     * Adds a listener for switch changes
     */
    fun addOnSwitchChangeListener(listener: OnMainSwitchChangeListener) {
        if (listener !in mSwitchChangeListeners) {
            mSwitchChangeListeners.add(listener)
        }
    }

    /**
     * Remove a listener for switch changes
     */
    fun removeOnSwitchChangeListener(listener: OnMainSwitchChangeListener) {
        mSwitchChangeListeners.remove(listener)
    }

    /**
     * Enable or disable the text and switch.
     */
    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        binding.apply {
            switchWidget.isEnabled = enabled
            switchText.isEnabled = enabled
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                frame.background = if (enabled) if (isChecked) mBackgroundOn else mBackgroundOff
                else mBackgroundDisabled
            }
        }
    }

    private fun propagateChecked(isChecked: Boolean) {
        setBackground(isChecked)
        mSwitchChangeListeners.forEach { listener ->
            listener.onSwitchChanged(binding.switchWidget, isChecked)
        }
    }

    private fun setBackground(isChecked: Boolean) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            setBackgroundColor(if (isChecked) mBackgroundActivatedColor else mBackgroundColor)
        } else {
            binding.frame.background = if (isChecked) mBackgroundOn else mBackgroundOff
        }
    }

    @Parcelize
    class SavedState(
        @Suppress("CanBeParameter") private val state: Parcelable,
        val isChecked: Boolean,
        val isVisible: Boolean
    ) :
        BaseSavedState(state)

    override fun onSaveInstanceState() =
        super.onSaveInstanceState()?.let { SavedState(it, isChecked, isVisible) }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }

        super.onRestoreInstanceState(state.superState)
        isChecked = state.isChecked
        isVisible = state.isVisible
        binding.switchWidget.setOnCheckedChangeListener(if (state.isVisible) this else null)
        requestLayout()
    }
}
